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

    // service xu ly thong tin khach hang
    private CustomerService customerService;
    // service xu ly thong tin user
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao customer service tu di container
            customerService = DIContainer.get(CustomerService.class);
            // khoi tao user service tu di container
            userService = DIContainer.get(UserService.class);
        } catch (Exception e) {
            // nem loi neu khoi tao service that bai
            throw new RuntimeException("Dependency injection error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // lay session hien tai, neu khong co thi null
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            // neu da dang nhap thi chuyen den trang chu
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // neu chua dang nhap thi chuyen den trang dang nhap
        request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
    }

    /**
     * method xu ly dang nhap
     * - lay tham so tu form dang nhap
     * - validate thong tin dang nhap
     * - dang nhap voi vai tro customer hoac user
     * - tao session va chuyen huong den trang phu hop
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // lay tham so tu form dang nhap
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        // tao danh sach de luu cac loi validation
        List<String> errors = new ArrayList<>();

        // kiem tra username co rong khong
        if (username == null || username.trim().isEmpty()) {
            errors.add(MessageUtil.getError("error.username.required"));
        }
        // kiem tra password co rong khong
        if (password == null || password.trim().isEmpty()) {
            errors.add(MessageUtil.getError("error.password.required"));
        }

        // neu co loi validation thi hien thi lai trang dang nhap
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
            return;
        }

        try {
            // dang nhap voi vai tro customer truoc
            Optional<CustomerDTO> customerOpt = customerService.loginCustomer(username, password);
            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();

                // huy session cu neu co va luu lai redirect url
                HttpSession old = request.getSession(false);
                String redirectURL = null;
                if (old != null) {
                    redirectURL = (String) old.getAttribute("redirectAfterLogin");
                    old.invalidate();
                }

                // tao session moi cho customer
                HttpSession session = request.getSession(true);
                setCustomerSession(session, customer);
                if (redirectURL != null) {
                    session.setAttribute("redirectAfterLogin", redirectURL);
                }
                session.removeAttribute("redirectURL");

                // xu ly remember me neu duoc chon
                handleRememberMe(response, username, request.getContextPath(), rememberMe);

                // neu co redirect after login hop le thi chuyen den do, neu khong thi ve trang chu
                String currURL = (String) session.getAttribute("redirectAfterLogin");
                String targetPath = (isSafeInternalPath(currURL) ? currURL : "/home?login=success");
                response.sendRedirect(request.getContextPath() + targetPath);
                return;
            }

            // neu khong phai customer thi thu dang nhap voi vai tro staff/manager
            Optional<UserDTO> userOpt = userService.loginUser(username, password);
            if (userOpt.isPresent()) {
                UserDTO user = userOpt.get();

                // huy session cu neu co va luu lai redirect url
                HttpSession old = request.getSession(false);
                String redirectURL = null;
                if (old != null) {
                    redirectURL = (String) old.getAttribute("redirectAfterLogin");
                    old.invalidate();
                }

                // tao session moi cho user
                HttpSession session = request.getSession(true);
                setUserSession(session, user);
                if (redirectURL != null) {
                    session.setAttribute("redirectAfterLogin", redirectURL);
                }
                session.removeAttribute("redirectURL");

                // xu ly remember me neu duoc chon
                handleRememberMe(response, username, request.getContextPath(), rememberMe);

                // uu tien redirect after login neu co va hop le, neu khong thi chuyen huong theo role
                String currURL = (String) session.getAttribute("redirectAfterLogin");
                if (isSafeInternalPath(currURL)) {
                    response.sendRedirect(request.getContextPath() + currURL);
                } else {
                    redirectByRole(response, request, user.getRoleName());
                }
                return;
            }

            // neu sai thong tin dang nhap thi them loi vao danh sach
            errors.add(MessageUtil.getError("error.login.invalid"));
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);

        } catch (Exception e) {
            // log loi ra console
            e.printStackTrace();
            // them loi he thong vao danh sach
            errors.add(MessageUtil.getError("error.system.login"));
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
        }
    }
    private void setCustomerSession(HttpSession session, CustomerDTO customer) {
        session.setAttribute("userType", "CUSTOMER");
        session.setAttribute("customerId", customer.getCustomerId());
        session.setAttribute("username", customer.getUsername());
        session.setAttribute("fullName", customer.getFullName());
        session.setAttribute("loginTime", System.currentTimeMillis());
        session.setMaxInactiveInterval(2 * 60 * 60); // tang len 2 gio
    }


    private void setUserSession(HttpSession session, UserDTO user) {
        session.setAttribute("userType", user.getRoleName());
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("roleName", user.getRoleName());
        session.setAttribute("loginTime", System.currentTimeMillis());   // cho dong nhat
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
                response.sendRedirect(path + "/staff/staff.jsp");
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
        if (url == null || url.isEmpty()) {
            return false;
        }
        if (!url.startsWith("/")) {
            return false;
        }
        if (url.contains("://") || url.contains("\r") || url.contains("\n")) {
            return false;
        }
        return true;
    }
}
