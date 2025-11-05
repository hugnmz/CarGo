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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lay tham so tu form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

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
                redirectByRole(response, request, "CUSTOMER");
                return;
            }

            //2. Đăng nhập với vai trò Staff / Manager
            Optional<UserDTO> userOpt = userService.loginUser(username, password);
            if (userOpt.isPresent()) {
                UserDTO user = userOpt.get();

                HttpSession session = request.getSession(true);
                setUserSession(session, user);
                redirectByRole(response, request, user.getRoleName());
                return;
            }

            //3. Sai thông tin đăng nhập
            request.setAttribute("username", username);
            request.setAttribute("errors", "Sai tên đăng nhập hoặc mật khẩu.");
            request.getRequestDispatcher("auth/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errors", "Đã xảy ra lỗi khi đăng nhập.");
            request.getRequestDispatcher("auth/login.jsp").forward(request, response);
        }
    }

    private void setCustomerSession(HttpSession session, CustomerDTO customer) {
        session.setAttribute("userType", "CUSTOMER");
        session.setAttribute("c", customer);
        session.setAttribute("loginTime", System.currentTimeMillis());
        session.setMaxInactiveInterval(2 * 60 * 60); // tang len 2 gio
    }

     private void setUserSession(HttpSession session, UserDTO user) {
        session.setAttribute("userType", user.getRoleName());
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("usernameu", user.getUsername());
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("roleName", user.getRoleName());
        session.setMaxInactiveInterval(60 * 60);
    }
    

        private void redirectByRole(HttpServletResponse response, HttpServletRequest request, String role)
            throws IOException, ServletException {
        String path = request.getContextPath();

        switch (role.toUpperCase()) {
            case "MANAGER":
                response.sendRedirect(path + "/homemange");
                break;
            case "STAFF":
                response.sendRedirect(path + "/staff");
                break;
            case "CUSTOMER":
                response.sendRedirect(path + "/home");
                break;
            case "ADMIN":
                response.sendRedirect(request.getContextPath() + "/HomeAdmin");
                break;
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
