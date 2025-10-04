/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;

import service.CustomerService;
import util.di.DIContainer;

/**
 *
 * @author admin
 */
@WebServlet("/VerifyServlet")
public class VerifyServlet extends HttpServlet {
    
    private CustomerService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

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
                
                req.setAttribute("successMessage", "Xác minh thành công! Bạn có thể đăng nhập ngay bây giờ.");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            } else {
                req.setAttribute("errorMessage", "Mã xác minh không đúng hoặc đã hết hạn. Vui lòng thử lại.");
            }
        } else {
            req.setAttribute("errorMessage", "Thông tin xác minh không hợp lệ. Vui lòng đăng ký trước.");
        }
        
        req.getRequestDispatcher("verify.jsp").forward(req, resp);
    }
    
}
