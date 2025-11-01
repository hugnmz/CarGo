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
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation - Check all fields
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", MessageUtil.getError("error.reset.password.username.required"));
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (code == null || code.trim().isEmpty()) {
            request.setAttribute("error", MessageUtil.getError("error.reset.password.code.required"));
            request.setAttribute("username", username);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (code.trim().length() != 6 || !code.matches("[0-9]+")) {
            request.setAttribute("error", MessageUtil.getError("error.reset.password.code.invalid"));
            request.setAttribute("username", username);
            request.setAttribute("code", code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("error", MessageUtil.getError("error.reset.password.new.required"));
            request.setAttribute("username", username);
            request.setAttribute("code", code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", MessageUtil.getError("error.reset.password.confirm.required"));
            request.setAttribute("username", username);
            request.setAttribute("code", code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        // Check password length
        if (newPassword.length() < 6) {
            request.setAttribute("error", MessageUtil.getError("error.reset.password.length.min"));
            request.setAttribute("username", username);
            request.setAttribute("code", code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        // Check password match
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", MessageUtil.getError("error.password.mismatch"));
            request.setAttribute("username", username);
            request.setAttribute("code", code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        try {
            boolean success = customerService.resetPasswordWithCode(username.trim(), code.trim(), newPassword);
            
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
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", MessageUtil.getError("error.reset.password.system.error"));
            request.setAttribute("username", username);
            request.setAttribute("code", code);
            request.getRequestDispatcher("/auth/reset-password.jsp").forward(request, response);
        }
    }
}
