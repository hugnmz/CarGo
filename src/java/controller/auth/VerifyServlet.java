/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import util.di.DIContainer;
import util.MessageUtil;
import service.CusService;

/**
 *
 * @author admin
 */
@WebServlet("/VerifyServlet")
public class VerifyServlet extends HttpServlet {
    
    private CusService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

        try {
            customerService = DIContainer.get(CusService.class);
        } catch (Exception e) {
            e.printStackTrace();
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
        
        HttpSession session = req.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("pendingUser") : null;
        String inputCode = req.getParameter("code");
        if (inputCode != null) {
            inputCode = inputCode.trim();
        }
        String email = (session != null) ? (String) session.getAttribute("pendingEmail") : null;
        
        
        if (username != null && inputCode != null) {
            
            boolean isValid = customerService.verifyAccount(username, inputCode);            
            if (isValid) {
                // Xóa session sau khi xác minh thành công
                if (session != null) {
                    session.removeAttribute("pendingUser");
                    session.removeAttribute("pendingEmail");
                }
                
                req.setAttribute("successMessage", MessageUtil.getMessage("verification.success.login"));
                req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
                return;
            } else {
                req.setAttribute("errorMessage", MessageUtil.getError("error.verification.code.wrong"));
            }
        } else {
            req.setAttribute("errorMessage", MessageUtil.getError("error.verification.invalid.info"));
        }
        
        req.getRequestDispatcher("/auth/verify.jsp").forward(req, resp);
    }
    
}
