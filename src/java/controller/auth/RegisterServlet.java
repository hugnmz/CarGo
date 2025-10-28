package controller.auth;

import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import util.MessageUtil;
import service.CustomerService;
import service.LocationService;
import dto.LocationDTO;
import util.EmailUtil;
import util.di.DIContainer;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    // service xu ly thong tin khach hang
    private CustomerService customerService;
    private LocationService locationService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao customer service tu di container
            customerService = DIContainer.get(CustomerService.class);
            // khoi tao location service tu di container
            locationService = DIContainer.get(LocationService.class);
        } catch (Exception e) {
            // nem loi neu khoi tao service that bai
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // lay danh sach locations tu database
            List<LocationDTO> locations = locationService.getAllLocations();
            request.setAttribute("locations", locations);

            // chuyen huong den trang dang ky
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // neu co loi thi chuyen huong den trang dang ky khong co locations
            response.sendRedirect(request.getContextPath() + "/auth/register.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // lay du lieu tu form dang ky
        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // tao danh sach de luu cac loi validation
        List<String> errors = new ArrayList<>();

        try {
            // kiem tra cac truong bat buoc
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

            if (city == null || city.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.city.required"));
            }

            // kiem tra mat khau khop
            if (password != null && confirmPassword != null && !confirmPassword.equals(password)) {
                errors.add(MessageUtil.getError("error.password.mismatch"));
            }

            // kiem tra dinh dang email
            if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.add(MessageUtil.getError("error.email.invalid"));
            }

            // kiem tra dinh dang so dien thoai
            if (phone != null && !phone.matches("^[0-9]{10,11}$")) {
                errors.add(MessageUtil.getError("error.phone.invalid"));
            }

            // neu co loi validation thi hien thi tat ca loi
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
                return;
            }

            // kiem tra trung lap email
            if (customerService.isEmailExists(email)) {
                errors.add(MessageUtil.getError("error.email.exists"));
            }

            // kiem tra trung lap so dien thoai
            if (customerService.isPhoneExists(phone)) {
                errors.add(MessageUtil.getError("error.phone.exists"));
            }

            // kiem tra trung lap username
            if (customerService.isUsernameExists(username)) {
                errors.add(MessageUtil.getError("error.username.exists"));
            }

            // neu co loi trung lap thi hien thi tat ca loi
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
                return;
            }

            // tao doi tuong customer dto de luu thong tin dang ky
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setUsername(username.trim());
            customerDTO.setFullName(fullname.trim());
            customerDTO.setPhone(phone.trim());
            customerDTO.setEmail(email.trim());
            customerDTO.setCity(city);
            customerDTO.setCreateAt(LocalDateTime.now());
            // thuc hien dang ky tai khoan customer
            boolean success = customerService.registerCustomer(customerDTO, password);

            if (success) {
                // tao va luu ma xac thuc
                Optional<String> otpCode = customerService.generateAndStoreVerificationCode(customerDTO.getUsername());
                if (otpCode.isPresent()) {
                    String code = otpCode.get();

                    // tao url xac thuc
                    String baseUrl = request.getScheme() + "://" + request.getServerName()
                            + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort())
                            + request.getContextPath();

                    String link = baseUrl + "/VerifyServlet?u=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                            + "&code=" + code;

                    // gui email xac thuc
                    EmailUtil.send(email,
                            "[CarGo] Ma xac minh tai khoan",
                            "<p>Mã xác minh: <b>" + code + "</b> (hết hạn 10 phút).</p>"
                            + "<p><a href='" + link + "'>Xác minh ngay</a></p>");

                    // luu session de verify servlet biet user nao dang cho xac minh
                    HttpSession session = request.getSession(true);
                    session.setAttribute("pendingUser", username);
                    session.setAttribute("pendingEmail", email);

                    // chuyen huong den trang xac thuc
                    request.getRequestDispatcher("/auth/verify.jsp").forward(request, response);
                    return;
                }
            } else {
                // neu dang ky that bai thi them loi vao danh sach
                errors.add(MessageUtil.getError("error.register.failed"));
                request.setAttribute("errors", errors);
                setFormData(request, fullname, phone, email, city, username);
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // log loi ra console
            e.printStackTrace();
            // them loi he thong vao danh sach
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
        List<LocationDTO> locations = locationService.getAllLocations();
        request.setAttribute("locations", locations);

    }
}
