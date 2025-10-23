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

        // chua login -> check cookie
        String rememberUsername = null;

        // doc cookie tu request
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            // duyet tat ca cac cookie hien co de tim dung ten cookie
            for (Cookie c : cookies) {
                if ("rememberMeUser".equals(c.getName())) {
                    // lay gia tri username da luu
                    rememberUsername = c.getValue();
                    break; // thoat khoi vong lap khi tim thay
                }
            }

            // neu co username tu cookie
            if (rememberUsername != null) {
                // tim user trong cookie theo username
                Optional<CustomerDTO> customerOpt = customerService.getCustomerByUsername(rememberUsername);

                if (customerOpt.isPresent()) {
                    CustomerDTO customer = customerOpt.get();

                    // tao session moi neu chua co
                    session = request.getSession(true);
                    // gan cac thuoc tinh phien cho nguoi dung
                    setSessionAttributes(session, customer);
                    response.sendRedirect(request.getContextPath() + "/home");
                    return;
                }
            }
        }

        // ko co cookie -> ve login 
        response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // lay tham so tu form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        // check du lieu dau vao
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "vui long nhap day du thong tin");
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

                // neu nguoi dung tick on
                // tao cookie luu username
                if ("on".equals(rememberMe)) {
                    // tao cookie
                    Cookie userCookie = new Cookie("rememberMeUser", customer.getUsername());

                    // han luu cookie la 30 ngay
                    userCookie.setMaxAge(30 * 24 * 60 * 60);

                    // chi cho server doc
                    userCookie.setHttpOnly(true);

                    String ctxPath = request.getContextPath();
                    userCookie.setPath((ctxPath == null || ctxPath.isEmpty()) ? "/" : ctxPath);

                    response.addCookie(userCookie);
                } // neu nguoi dung ko tick chon
                else {
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie c : cookies) {
                            if ("rememberMeUser".equals(c.getName())) {
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
                // chi cho phep path noi bo de tranh open-redirect
                String targetPath = (isSafeInternalPath(currURL) ? currURL : "/home?login=success");
                response.sendRedirect(request.getContextPath() + targetPath);
                return;
            } else {
                request.setAttribute("errorMessage", "tai khoan hoac mat khau khong dung");
                request.getRequestDispatcher("auth/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "co loi xay ra khi dang nhap");
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
        // chan chuoi co "://" hoac ma doc kieu cr/lf
        if (url.contains("://") || url.contains("\r") || url.contains("\n")) {
            return false;
        }
        return true;
    }
}
