package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import service.CustomerService;
import util.MessageUtil;
import util.di.DIContainer;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
    
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
        // Check if user came from forgot password flow
        String username = (String) request.getSession().getAttribute("resetUsername");
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/ForgotPasswordServlet");
            return;
        }
        request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String code = request.getParameter("code");
        String newPassword = request.getParameter("newPassword");
        
        // Xóa tất cả check - đã chuyển vào service
        
        try {
            boolean success = customerService.resetPasswordWithCode(
                username != null ? username.trim() : null, 
                code != null ? code.trim() : null, 
                newPassword
            );
            
            if (success) {
                // Clear session
                request.getSession().removeAttribute("resetUsername");
                response.sendRedirect(request.getContextPath() + "/auth/login.jsp?reset=success");
            } else {
                request.setAttribute("error", MessageUtil.getError("error.reset.password.code.expired"));
                request.setAttribute("username", username);
                request.setAttribute("code", code);
                request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            }
        } catch (ValidationException | BusinessException | DataAccessException e) {
            e.printStackTrace();
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.setAttribute("username", username);
            request.setAttribute("code", code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", MessageUtil.getError("error.reset.password.system.error"));
            request.setAttribute("username", username);
            request.setAttribute("code", code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
        }
    }
}
