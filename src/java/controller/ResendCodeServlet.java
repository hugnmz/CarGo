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
import util.EmailUtil;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.*;
import service.CustomersService;

@WebServlet("/resend-code")
public class ResendCodeServlet extends HttpServlet {

    private CustomersService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CustomersService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Redirect GET requests to verify page
        req.getRequestDispatcher("/auth/verify.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
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
                        throw new BusinessException("error.email.send.failed");
                    }

                    req.setAttribute("infoMessage", MessageUtil.getMessage("resend.code.success"));
                } else {
                    throw new BusinessException("error.resend.code.failed");
                }
            } else {
                throw new ValidationException("error.session.expired.verify");
            }
            
        } catch (ValidationException | BusinessException | DataAccessException e) {
            req.setAttribute("errorMessage", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            req.setAttribute("errorMessage", MessageUtil.getError("error.system.resend.code"));
        }
        
        req.getRequestDispatcher("/auth/verify.jsp").forward(req, resp);
    }
}
