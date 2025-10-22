package controller.auth;

import dto.CustomerDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import service.CustomerService;
import service.UserService;
import service.impl.CustomerServiceImpl;
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

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            // Đã đăng nhập thì về home
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher("login.jsp").forward(request, response);
        /*
        // Chi lay session neu da ton tai
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("customerId") != null) {
            // Da login
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Chua login -> check cookie
        String rememberUsername = null;

        // Doc cookie tu request
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            // Duyet tat ca cac cookie hien co de tim dung ten cookie
            for (Cookie c : cookies) {
                if ("rememberMeUser".equals(c.getName())) { // SUA: rememberUsername -> rememberMeUser
                    // Lay gia tri username da luu
                    rememberUsername = c.getValue();
                    break; // Thoat khoi vong lap khi tim thay
                }
            }

            // Neu co username tu cookie
            if (rememberUsername != null) {
                // Tim user trong cookie theo username
                Optional<CustomerDTO> customerOpt = customerService.getCustomerByUsername(rememberUsername);

                if (customerOpt.isPresent()) {
                    CustomerDTO customer = customerOpt.get();

                    // Tao session moi neu chua co
                    session = request.getSession(true);
                    // Gan cac thuoc tinh phien cho nguoi dung
                    setSessionAttributes(session, customer);
                    response.sendRedirect(request.getContextPath() + "/home");
                    return;
                }
            }
        }

        // Ko co cookie -> ve login 
        response.sendRedirect("login.jsp");
         */
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lay tham so tu form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        // Check du lieu dau vao
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            // 🔹 1. Đăng nhập với vai trò Customer
            Optional<CustomerDTO> customerOpt = customerService.loginCustomer(username, password);
            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();
                HttpSession session = request.getSession(true);
                setCustomerSession(session, customer);
                handleRememberMe(response, username, request.getContextPath(), rememberMe);
                redirectByRole(response, request, "CUSTOMER");
                return;
            }

            // 🔹 2. Đăng nhập với vai trò Staff / Manager
            Optional<UserDTO> userOpt = userService.loginUser(username, password);
            if (userOpt.isPresent()) {
                UserDTO user = userOpt.get();

                HttpSession session = request.getSession(true);
                setUserSession(session, user);
                handleRememberMe(response, username, request.getContextPath(), rememberMe);
                redirectByRole(response, request, user.getRoleName());
                return;
            }

            // 🔹 3. Sai thông tin đăng nhập
            request.setAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu.");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi đăng nhập.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // Set attribute cho session
    private void setCustomerSession(HttpSession session, CustomerDTO customer) {
        session.setAttribute("userType", "CUSTOMER");
        session.setAttribute("customerId", customer.getCustomerId());
        session.setAttribute("username", customer.getUsername());
        session.setAttribute("fullName", customer.getFullName());
        session.setAttribute("email", customer.getEmail());
        session.setAttribute("phone", customer.getPhone());
        session.setAttribute("city", customer.getCity());
        session.setMaxInactiveInterval(60 * 60);
    }

    private void setUserSession(HttpSession session, UserDTO user) {
        session.setAttribute("userType", user.getRoleName());
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("roleName", user.getRoleName());
        session.setMaxInactiveInterval(60 * 60);
    }
    
    private void handleRememberMe(HttpServletResponse response, String username, String contextPath, String rememberMe) {
        if ("on".equals(rememberMe)) {
            Cookie cookie = new Cookie("rememberMeUser", username);
            cookie.setMaxAge(30 * 24 * 60 * 60);
            cookie.setHttpOnly(true);
            cookie.setPath((contextPath == null || contextPath.isEmpty()) ? "/" : contextPath);
            response.addCookie(cookie);
        }
    }

    private void redirectByRole(HttpServletResponse response, HttpServletRequest request, String role)
            throws IOException, ServletException {
        String path = request.getContextPath();

        switch (role.toUpperCase()) {
            case "MANAGER":
                response.sendRedirect(path + "/home");
                break;
            case "STAFF":
                response.sendRedirect(path + "/staff/home");
                break;
            case "CUSTOMER":
                response.sendRedirect(path + "/home");
                break;
            default:
                response.sendRedirect(path + "/error403.jsp");
        }
    }
    private boolean isSafeInternalPath(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        if (!url.startsWith("/")) {
            return false;
        }
        // chặn chuỗi có "://" hoặc mã độc kiểu CR/LF
        if (url.contains("://") || url.contains("\r") || url.contains("\n")) {
            return false;
        }
        return true;
    }
}
