package controller.auth;

import dto.CustomerDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import service.UserService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import service.CustomersService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    // service xu ly thong tin khach hang
    private CustomersService customerService;
    // service xu ly thong tin user
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao customer service tu di container
            customerService = DIContainer.get(CustomersService.class);
            // khoi tao user service tu di container
            userService = DIContainer.get(UserService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system"), e);
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

        try {
            //1. Đăng nhập với vai trò Customer
            try {
                Optional<CustomerDTO> customerOpt = customerService.loginCustomer(username, password);
                if (customerOpt.isPresent()) {
                    CustomerDTO customer = customerOpt.get();
                    HttpSession session = request.getSession(true);
                    setCustomerSession(session, customer);
                    redirectAfterLogin(response, request, "CUSTOMER");
                    return;
                }
            } catch (ValidationException | BusinessException | DataAccessException e) {
                // Customer login thất bại, tiếp tục thử login user
                // Không làm gì, để tiếp tục thử login user
            }

            //2. Đăng nhập với vai trò Staff / Manager / Admin
            try {
                Optional<UserDTO> userOpt = userService.loginUser(username, password);
                if (userOpt.isPresent()) {
                    UserDTO user = userOpt.get();

                    HttpSession session = request.getSession(true);
                    setUserSession(session, user);
                    redirectAfterLogin(response, request, user.getRoleName());
                    return;
                }
            } catch (ValidationException | BusinessException | DataAccessException e) {
                // User login cũng thất bại, sẽ hiển thị lỗi ở dưới
            }

            //3. Cả customer và user đều login thất bại
            request.setAttribute("username", username);
            List<String> errorList = new ArrayList<>();
            errorList.add(MessageUtil.getError("error.login.invalid"));
            request.setAttribute("errors", errorList);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("username", username);
            List<String> errorList = new ArrayList<>();
            errorList.add(MessageUtil.getError("error.system.login"));
            request.setAttribute("errors", errorList);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
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
    

    /**
     * Redirect sau khi đăng nhập thành công
     * Ưu tiên redirect về trang trước đó (nếu có), nếu không thì redirect theo role
     */
    private void redirectAfterLogin(HttpServletResponse response, HttpServletRequest request, String role)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String redirectUrl = null;
        
        // Kiểm tra xem có URL cần redirect sau khi login không
        if (session != null) {
            redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if (redirectUrl != null) {
                // Xóa redirectAfterLogin khỏi session sau khi sử dụng
                session.removeAttribute("redirectAfterLogin");
                // Redirect về trang trước đó
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return;
            }
        }
        
        // Nếu không có redirectAfterLogin, redirect theo role như cũ
        redirectByRole(response, request, role);
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
}
