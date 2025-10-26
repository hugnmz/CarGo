package controller.auth;

import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import util.MessageUtil;
import service.CustomerService;
import service.impl.CustomerServiceImpl;
import util.di.DIContainer;

// servlet xu ly dang nhap
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        // khoi tao customerservice tu di container
        try {
            customerService = DIContainer.get(CustomerService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // chi lay session neu da ton tai
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("customerId") != null) {
            // da login
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // lay tham so tu form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Tạo list để thu thập lỗi
        List<String> errors = new ArrayList<>();

        // check du lieu dau vao
            if (username == null || username.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.username.required"));
            }

            if (password == null || password.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.password.required"));
            }

        // Nếu có lỗi validation, hiển thị tất cả lỗi
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("username", username); // Giữ lại username
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
            return;
        }

        try {
            // thuc hien dang nhap
            // xac thuc dang nhap qua service va tra ve optional (tranh null)
            Optional<CustomerDTO> customerOpt = customerService.loginCustomer(username, password);

            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();

                // huy session cu
                HttpSession old = request.getSession(false);
                String redirectURL = null;
                if (old != null) {
                    redirectURL = (String) old.getAttribute("redirectAfterLogin");
                    old.invalidate();
                }

                // tao session moi va gan thuoc tinh vao
                HttpSession session = request.getSession(true);
                setSessionAttributes(session, customer);
                if (redirectURL != null) {
                    session.setAttribute("redirectAfterLogin", redirectURL);
                }

                session.removeAttribute("redirectURL");

                String currURL = (String) session.getAttribute("redirectAfterLogin");
                // chi cho phep path noi bo de tranh open-redirect
                String targetPath = (isSafeInternalPath(currURL) ? currURL : "/home?login=success");
                response.sendRedirect(request.getContextPath() + targetPath);
                return;
            } else {
                errors.add(MessageUtil.getError("error.login.invalid"));
                request.setAttribute("errors", errors);
                request.setAttribute("username", username); // Giữ lại username
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
                errors.add(MessageUtil.getError("error.system.login"));
            request.setAttribute("errors", errors);
            request.setAttribute("username", username); // Giữ lại username
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
        }
    }

    // set attribute cho session
    private void setSessionAttributes(HttpSession session, CustomerDTO customer) {
        session.setAttribute("customerId", customer.getCustomerId());
        session.setAttribute("username", customer.getUsername());
        session.setAttribute("fullName", customer.getFullName());
        session.setAttribute("email", customer.getEmail());
        session.setAttribute("phone", customer.getPhone());
        session.setAttribute("city", customer.getCity());
        session.setAttribute("dateOfBirth", customer.getDateOfBirth());
        session.setAttribute("loginTime", System.currentTimeMillis());

        session.setMaxInactiveInterval(60 * 60); // 60 phut
    }

    private boolean isSafeInternalPath(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        if (!url.startsWith("/")) {
            return false;
        }
        // chan chuoi co "://" hoac ma doc kieu cr/lf
        if (url.contains("://") || url.contains("\r") || url.contains("\n")) {
            return false;
        }
        return true;
    }
}
