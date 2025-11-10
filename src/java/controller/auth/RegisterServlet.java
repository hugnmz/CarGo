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
import java.util.Arrays;
import util.MessageUtil;
import service.LocationService;
import dto.LocationDTO;
import java.time.LocalDate;
import util.EmailUtil;
import util.di.DIContainer;
import util.exception.ApplicationException;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import service.CustomersService;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    // service xu ly thong tin khach hang
    private CustomersService customerService;
    private LocationService locationService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao customer service tu di container
            customerService = DIContainer.get(CustomersService.class);
            // khoi tao location service tu di container
            locationService = DIContainer.get(LocationService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system"), e);
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
        } catch (ApplicationException e) {
            request.setAttribute("errors", Arrays.asList(MessageUtil.getErrorFromException(e)));
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errors", Arrays.asList(MessageUtil.getError("error.system.register")));
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
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
        String dateOfBirth = request.getParameter("dateOfBirth");

        // tao danh sach de luu cac loi validation
        List<String> errors = new ArrayList<>();

        try {
            // tao doi tuong customer dto de luu thong tin dang ky
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setUsername(username.trim());
            customerDTO.setFullName(fullname.trim());
            customerDTO.setPhone(phone.trim());
            customerDTO.setEmail(email.trim());
            customerDTO.setCity(city);
            customerDTO.setDateOfBirth(LocalDate.parse(dateOfBirth));
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
        } catch (ValidationException | BusinessException | DataAccessException e) {
            errors.add(MessageUtil.getErrorFromException(e));
            request.setAttribute("errors", errors);
            setFormData(request, fullname, phone, email, city, username);
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
        } catch (Exception e) {
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
