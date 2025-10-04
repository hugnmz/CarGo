package controller;

import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import service.CustomerService;
import util.EmailUtil;
import util.di.DIContainer;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        try {
            customerService = DIContainer.get(CustomerService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to register page
        response.sendRedirect("register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // doc du lieu tu form 
        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // xac thuc du lieu
        try {

            // kiem tra trung lap
            if (customerService.isEmailExists(email)) {
                request.setAttribute("errorMessage", "Email đã tồn tại.");
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            if (customerService.isPhoneExists(phone)) {
                request.setAttribute("errorMessage", "số này đã tồn tại");
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            if (customerService.isUsernameExists(username)) {
                request.setAttribute("errorMessage", "tên đăng nhập này đã tồn tại");
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            if(!confirmPassword.equals(password)){
                request.setAttribute("errorMessage", "nhập lại mk");
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // tao dto
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setUsername(username.trim());
            customerDTO.setFullName(fullname.trim());
            customerDTO.setPhone(phone.trim());
            customerDTO.setEmail(email.trim());
            customerDTO.setCity(city);

            // thuc hien dang ki
            boolean success = customerService.registerCustomer(customerDTO, password);

            if (success) {
                Optional<String> otpCode = customerService.generateAndStoreVerificationCode(customerDTO.getUsername());
                if (otpCode.isPresent()) {
                    String code = otpCode.get();

                    String baseUrl = request.getScheme() + "://" + request.getServerName()
                            + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort())
                            + request.getContextPath();

                    String link = baseUrl + "/VerifyServlet?u=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                            + "&code=" + code;

                    EmailUtil.send(email,
                            "[CarGo] Mã xác minh tài khoản",
                            "<p>Mã xác minh: <b>" + code + "</b> (hết hạn 10 phút).</p>"
                            + "<p><a href='" + link + "'>Xác minh ngay</a></p>");

                    // Lưu session để VerifyServlet biết user nào đang chờ xác minh
                    HttpSession session = request.getSession(true);
                    session.setAttribute("pendingUser", username);
                    session.setAttribute("pendingEmail", email);

                    request.getRequestDispatcher("verify.jsp").forward(request, response);
                    return;
                }
            } else {
                request.setAttribute("errorMessage", "dki that bai");
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra trong quá trình đăng ký. Vui lòng thử lại!");
            setFormData(request, fullname, phone, email, city, username);
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    public void setFormData(HttpServletRequest request, String fullname, String phone,
            String email, String city, String username) {
        request.setAttribute("fullname", fullname);
        request.setAttribute("phone", phone);
        request.setAttribute("email", email);
        request.setAttribute("city", city);
        request.setAttribute("username", username);
    }
}
