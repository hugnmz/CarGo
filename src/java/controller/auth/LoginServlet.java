package controller.auth;

import dto.CustomerDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import service.CustomerService;
import service.UserService;
import util.MessageUtil;
import util.di.DIContainer;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private CustomerService customerService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CustomerService.class);
            userService = DIContainer.get(UserService.class);
        } catch (Exception e) {
            throw new RuntimeException("Dependency injection error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Chỉ lấy session nếu đã tồn tại
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            // Đã login
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Chưa login -> vào trang login của bản 1
        request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy tham số từ form
        String username   = request.getParameter("username");
        String password   = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        // Thu thập lỗi theo style bản 1
        List<String> errors = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            errors.add(MessageUtil.getError("error.username.required"));
        }
        if (password == null || password.trim().isEmpty()) {
            errors.add(MessageUtil.getError("error.password.required"));
        }

        // Nếu có lỗi validation
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
            return;
        }

        try {
            // 1) Đăng nhập với vai trò CUSTOMER (giữ logic bản 1)
            Optional<CustomerDTO> customerOpt = customerService.loginCustomer(username, password);
            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();

                // Hủy session cũ (nếu có) và giữ redirectAfterLogin
                HttpSession old = request.getSession(false);
                String redirectURL = null;
                if (old != null) {
                    redirectURL = (String) old.getAttribute("redirectAfterLogin");
                    old.invalidate();
                }

                // Tạo session mới
                HttpSession session = request.getSession(true);
                setCustomerSession(session, customer);       // set theo bản 2 + bổ sung trường của bản 1
                if (redirectURL != null) {
                    session.setAttribute("redirectAfterLogin", redirectURL);
                }
                session.removeAttribute("redirectURL");

                // Remember-me (giữ từ bản 2)
                handleRememberMe(response, username, request.getContextPath(), rememberMe);

                // Nếu có redirectAfterLogin hợp lệ thì về đó, còn không thì về /home
                String currURL = (String) session.getAttribute("redirectAfterLogin");
                String targetPath = (isSafeInternalPath(currURL) ? currURL : "/home?login=success");
                response.sendRedirect(request.getContextPath() + targetPath);
                return;
            }

            // 2) Nếu không phải Customer -> thử STAFF / MANAGER (thêm từ bản 2)
            Optional<UserDTO> userOpt = userService.loginUser(username, password);
            if (userOpt.isPresent()) {
                UserDTO user = userOpt.get();

                // Hủy session cũ (nếu có) và giữ redirectAfterLogin cho đồng nhất hành vi
                HttpSession old = request.getSession(false);
                String redirectURL = null;
                if (old != null) {
                    redirectURL = (String) old.getAttribute("redirectAfterLogin");
                    old.invalidate();
                }

                HttpSession session = request.getSession(true);
                setUserSession(session, user);
                if (redirectURL != null) {
                    session.setAttribute("redirectAfterLogin", redirectURL);
                }
                session.removeAttribute("redirectURL");

                handleRememberMe(response, username, request.getContextPath(), rememberMe);

                // Ưu tiên redirectAfterLogin nếu có và hợp lệ,
                // nếu không thì điều hướng theo role (bản 2)
                String currURL = (String) session.getAttribute("redirectAfterLogin");
                if (isSafeInternalPath(currURL)) {
                    response.sendRedirect(request.getContextPath() + currURL);
                } else {
                    redirectByRole(response, request, user.getRoleName());
                }
                return;
            }

            // 3) Sai thông tin đăng nhập
            errors.add(MessageUtil.getError("error.login.invalid"));
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            errors.add(MessageUtil.getError("error.system.login"));
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
        }
    }

    /* ===== Helpers ===== */

    // Session cho CUSTOMER (kết hợp trường của bản 1)
    private void setCustomerSession(HttpSession session, CustomerDTO customer) {
        session.setAttribute("userType", "CUSTOMER");
        session.setAttribute("customerId", customer.getCustomerId());
        session.setAttribute("username", customer.getUsername());
        session.setAttribute("fullName", customer.getFullName());
        session.setAttribute("email", customer.getEmail());
        session.setAttribute("phone", customer.getPhone());
        session.setAttribute("city", customer.getCity());
        session.setAttribute("dateOfBirth", customer.getDateOfBirth()); // từ bản 1
        session.setAttribute("loginTime", System.currentTimeMillis());   // từ bản 1
        session.setMaxInactiveInterval(60 * 60); // 60 phút
    }

    // Session cho STAFF/MANAGER (từ bản 2)
    private void setUserSession(HttpSession session, UserDTO user) {
        session.setAttribute("userType", user.getRoleName());
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("roleName", user.getRoleName());
        session.setAttribute("loginTime", System.currentTimeMillis());   // cho đồng nhất
        session.setMaxInactiveInterval(60 * 60);
    }

    private void handleRememberMe(HttpServletResponse response, String username, String contextPath, String rememberMe) {
        if ("on".equals(rememberMe)) {
            Cookie cookie = new Cookie("rememberMeUser", username);
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
            cookie.setHttpOnly(true);
            cookie.setPath((contextPath == null || contextPath.isEmpty()) ? "/" : contextPath);
            response.addCookie(cookie);
        }
    }

    private void redirectByRole(HttpServletResponse response, HttpServletRequest request, String role)
            throws IOException {
        String path = request.getContextPath();
        switch (role == null ? "" : role.toUpperCase()) {
            case "MANAGER":
                // NOTE: đường "/homemange" trong code gốc có vẻ là typo,
                // nếu route thực tế của bạn là "/homeManager" thì đổi lại ở đây.
                response.sendRedirect(path + "/homemange");
                break;
            case "STAFF":
                response.sendRedirect(path + "/home");
                break;
            case "CUSTOMER":
                response.sendRedirect(path + "/home");
                break;
            default:
                response.sendRedirect(path + "/error403.jsp");
        }
    }

    // Chặn open-redirect
    private boolean isSafeInternalPath(String url) {
        if (url == null || url.isEmpty()) return false;
        if (!url.startsWith("/")) return false;
        if (url.contains("://") || url.contains("\r") || url.contains("\n")) return false;
        return true;
    }
}
