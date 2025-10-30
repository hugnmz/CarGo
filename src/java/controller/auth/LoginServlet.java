
package controller.auth;

import dto.CustomerDTO;
import dto.UseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import service.impl.CusServiceImpl;
import util.di.DIContainer;
import service.CusService;
import service.UseService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private CusService customerService;
    private UseService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CusService.class);
            userService = DIContainer.get(UseService.class);
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

        request.getRequestDispatcher("auth/login.jsp").forward(request, response);
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
            request.setAttribute("errors", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("auth/login.jsp").forward(request, response);
            return;
        }

        try {
            //1. Đăng nhập với vai trò Customer
            Optional<CustomerDTO> customerOpt = customerService.loginCustomer(username, password);
            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();
                HttpSession session = request.getSession(true);
                setCustomerSession(session, customer);
                handleRememberMe(response, username, request.getContextPath(), rememberMe);
                redirectByRole(response, request, "CUSTOMER");
                return;
            }

            //2. Đăng nhập với vai trò Staff / Manager
            Optional<UseDTO> userOpt = userService.loginUser(username, password);
            if (userOpt.isPresent()) {
                UseDTO user = userOpt.get();

                HttpSession session = request.getSession(true);
                setUserSession(session, user);
                handleRememberMe(response, username, request.getContextPath(), rememberMe);
                redirectByRole(response, request, user.getRoleName());
                return;
            }

            //3. Sai thông tin đăng nhập
            request.setAttribute("errors", "Sai tên đăng nhập hoặc mật khẩu.");
            request.getRequestDispatcher("auth/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errors", "Đã xảy ra lỗi khi đăng nhập.");
            request.getRequestDispatcher("auth/login.jsp").forward(request, response);
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

    private void setUserSession(HttpSession session, UseDTO user) {
        session.setAttribute("userType", user.getRoleName());
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("usernameu", user.getUsername());
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
                response.sendRedirect(path + "/homemange");
                break;
            case "STAFF":
                response.sendRedirect(path + "/home");
                break;
            case "CUSTOMER":
                response.sendRedirect(path + "/home");
                break;
            case "ADMIN":
                response.sendRedirect(request.getContextPath() + "/HomeAdmin");
                break;
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
