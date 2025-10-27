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
import service.CustomerService;
import util.di.DIContainer;
import util.MessageUtil;

/**
 *
 * @author admin
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/ChangePasswordServlet"})
public class ChangePasswordServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/CustomerServlet").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oldPass = request.getParameter("old");
        String newPass = request.getParameter("new");
        String confirmPass = request.getParameter("confirm");
        Integer customerId = Integer.valueOf(request.getParameter("customerId"));

        // Kiểm tra xác nhận mật khẩu
        if (!newPass.equals(confirmPass)) {
            request.setAttribute("errorMess", MessageUtil.getError("error.password.mismatch"));
            request.getRequestDispatcher("/CustomerServlet").forward(request, response);
            return;
        }

        if (customerService.changeCustomerPassword(customerId, oldPass, newPass)) {
            request.setAttribute("ok", MessageUtil.getMessage("password.change.success"));
        } else {
            request.setAttribute("errorMess", MessageUtil.getError("error.password.change.failed"));
        }

        request.getRequestDispatcher("/CustomerServlet").forward(request, response);
    }
}
