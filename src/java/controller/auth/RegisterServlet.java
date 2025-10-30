package controller.auth;

import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import util.MessageUtil;
import util.EmailUtil;
import util.di.DIContainer;
import service.CusService;

// servlet xu ly dang ky tai khoan
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private CusService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        // khoi tao customerservice tu di container
        try {
            customerService = DIContainer.get(CusService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // redirect get requests to register page
        response.sendRedirect(request.getContextPath() + "/auth/register.jsp");
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
        
        // Tạo list để thu thập lỗi
        List<String> errors = new ArrayList<>();
        
        // xac thuc du lieu
        try {
            // Kiểm tra các trường bắt buộc
            if (fullname == null || fullname.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.fullname.required"));
            }
            
            if (phone == null || phone.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.phone.required"));
            }
            
            if (email == null || email.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.email.required"));
            }
            
            if (username == null || username.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.username.required"));
            }
            
            if (password == null || password.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.password.required"));
            }
            
            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.confirm.password.required"));
            }

            // Kiểm tra mật khẩu khớp
            if (password != null && confirmPassword != null && !confirmPassword.equals(password)) {
                errors.add(MessageUtil.getError("error.password.mismatch"));
            }

            // Nếu có lỗi validation, hiển thị tất cả lỗi
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
                return;
            }

            // kiem tra trung lap
            if (customerService.isEmailExists(email)) {
                errors.add(MessageUtil.getError("error.email.exists"));
            }

            if (customerService.isPhoneExists(phone)) {
                errors.add(MessageUtil.getError("error.phone.exists"));
            }

            if (customerService.isUsernameExists(username)) {
                errors.add(MessageUtil.getError("error.username.exists"));
            }

            // Nếu có lỗi trùng lặp, hiển thị tất cả lỗi
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
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

                    request.getRequestDispatcher("/auth/verify.jsp").forward(request, response);
                    return;
                }
            } else {
                errors.add(MessageUtil.getError("error.register.failed"));
                request.setAttribute("errors", errors);
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(MessageUtil.getError("error.system.register"));
            request.setAttribute("errors", errors);
            setFormData(request, fullname, phone, email, city, username);
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
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
