package controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.CustomerService;
import util.EmailUtil;
import util.di.DIContainer;

@WebServlet("/resend-code")
public class ResendCodeServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CustomerService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Redirect GET requests to verify page
        req.getRequestDispatcher("verify.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String username = (String) session.getAttribute("pendingUser");
        String email = (session != null) ? (String) session.getAttribute("pendingEmail") : null;

        if (username != null && email != null) {
            Optional<String> ocode = customerService.generateAndStoreVerificationCode(username);
            if (ocode.isPresent()) {
                String code = ocode.get();

                String baseUrl = req.getScheme() + "://" + req.getServerName()
                        + (req.getServerPort() == 80 ? "" : ":" + req.getServerPort())
                        + req.getContextPath();

                String link = baseUrl + "/VerifyServlet?u=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                        + "&code=" + code;

                try {
                    EmailUtil.send(email,
                            "[CarGo] Mã xác minh mới",
                            "<p>Mã mới: <b>" + code + "</b> (hết hạn 10 phút).</p>"
                                    + "<p><a href='" + link + "'>Xác minh ngay</a></p>");
                } catch (MessagingException ex) {
                    Logger.getLogger(ResendCodeServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                req.setAttribute("infoMessage", "Đã gửi lại mã, vui lòng kiểm tra email.");
            } else {
                req.setAttribute("errorMessage", "Không thể gửi lại mã.");
            }
        } else {
            req.setAttribute("errorMessage", "Phiên làm việc đã hết hạn. Vui lòng đăng ký lại.");
        }
        
        req.getRequestDispatcher("verify.jsp").forward(req, resp);
    }
}
