package controller.auth;

import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import service.CustomerService;
import service.impl.CustomerServiceImpl;
import util.di.DIContainer;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CustomerService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
            // Thuc hien dang nhap
            // Xac thuc dang nhap qua service va tra ve Optional (tranh null)
            Optional<CustomerDTO> customerOpt = customerService.loginCustomer(username, password);

            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();

                // Huy session cu
                HttpSession old = request.getSession(false);
                String redirectURL = null;
                if (old != null) {
                    redirectURL = (String) old.getAttribute("redirectAfterLogin");
                    old.invalidate();
                }

                // Tao session moi va gan thuoc tinh vao
                HttpSession session = request.getSession(true);
                setSessionAttributes(session, customer);
                if (redirectURL != null) {
                    session.setAttribute("redirectAfterLogin", redirectURL);
                }

                // Neu nguoi dung tick on
                // Tao cookie luu username
                if ("on".equals(rememberMe)) {
                    // Tao cookie
                    Cookie userCookie = new Cookie("rememberMeUser", customer.getUsername());

                    // Han luu cookie la 30 ngay
                    userCookie.setMaxAge(30 * 24 * 60 * 60);

                    // Chi cho server doc
                    userCookie.setHttpOnly(true);

                    String ctxPath = request.getContextPath();
                    userCookie.setPath((ctxPath == null || ctxPath.isEmpty()) ? "/" : ctxPath);

                    response.addCookie(userCookie);
                } // Neu nguoi dung ko tick chon
                else {
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie c : cookies) {
                            if ("rememberMeUser".equals(c.getName())) { // SUA: so sanh ten cookie, khong phai username
                                c.setMaxAge(0);
                                c.setPath("/");
                                response.addCookie(c);
                                break;
                            }
                        }
                    }
                }

                session.removeAttribute("redirectURL");

                String currURL = (String) session.getAttribute("redirectAfterLogin");
                // Chỉ cho phép path nội bộ để tránh open-redirect
                String targetPath = (isSafeInternalPath(currURL) ? currURL : "/home?login=success");
                response.sendRedirect(request.getContextPath() + targetPath);
                return;
            } else {
                request.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi đăng nhập");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // Set attribute cho session
    private void setSessionAttributes(HttpSession session, CustomerDTO customer) {
        session.setAttribute("customerId", customer.getCustomerId());
        session.setAttribute("username", customer.getUsername());
        session.setAttribute("fullName", customer.getFullName());
        session.setAttribute("email", customer.getEmail());
        session.setAttribute("phone", customer.getPhone());
        session.setAttribute("city", customer.getCity());
        session.setAttribute("loginTime", Long.valueOf(System.currentTimeMillis()));

        session.setMaxInactiveInterval(60 * 60); // 60 phut
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
