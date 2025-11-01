package controller.auth;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import service.CustomerService;
import util.EmailUtil;
import util.MessageUtil;
import util.di.DIContainer;
import dto.CustomerDTO;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    
    private CustomerService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CustomerService.class);
        } catch (Exception e) {
            throw new RuntimeException("Dependency injection error", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        
        try {
            // Verify username và email match
            Optional<CustomerDTO> customerOpt = customerService.getCustomerByUsernameAndEmail(username.trim(), email.trim());
            if (!customerOpt.isPresent()) {
                request.setAttribute("error", MessageUtil.getError("error.forgot.password.credentials.mismatch"));
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
                return;
            }
            
            CustomerDTO customer = customerOpt.get();
            
            // Generate reset code
            Optional<String> codeOpt = customerService.generateAndStoreResetCode(username.trim());
            if (!codeOpt.isPresent()) {
                request.setAttribute("error", MessageUtil.getError("error.forgot.password.code.generation.failed"));
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
                return;
            }
            
            String code = codeOpt.get();
            
            // Send email
            String emailSubject = MessageUtil.getMessage("forgot.password.email.subject");
            String emailBodyTemplate = MessageUtil.getMessage("forgot.password.email.body");
            String emailBody = emailBodyTemplate
                    .replace("{0}", customer.getFullName())
                    .replace("{1}", username.trim())
                    .replace("{2}", code);
            
            EmailUtil.send(email.trim(), emailSubject, emailBody);
            
            // Store username in session for reset password page
            request.getSession().setAttribute("resetUsername", username.trim());
            
            String successMsg = MessageUtil.getMessage("forgot.password.code.sent").replace("{0}", email.trim());
            request.setAttribute("success", successMsg);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            
        } catch (MessagingException e) {
            e.printStackTrace();
            request.setAttribute("error", MessageUtil.getError("error.forgot.password.email.send.failed"));
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", MessageUtil.getError("error.forgot.password.system.error"));
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/auth/forgot-password.jsp").forward(request, response);
        }
    }
}
