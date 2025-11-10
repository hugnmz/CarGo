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
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import service.CustomersService;

/**
 *
 * @author admin
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/ChangePasswordServlet"})
public class ChangePasswordServlet extends HttpServlet {

    private CustomersService customerService;

    @Override
    public void init() throws ServletException {
        super.init(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        try {
            customerService = DIContainer.get(CustomersService.class);

        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
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
        try {
            String oldPass = request.getParameter("old");
            String newPass = request.getParameter("new");
            String confirmPass = request.getParameter("confirm");
            Integer customerId = Integer.valueOf(request.getParameter("customerId"));

            // Kiểm tra xác nhận mật khẩu
            if (!newPass.equals(confirmPass)) {
                throw new ValidationException("error.password.mismatch");
            }

            if (customerService.changeCustomerPassword(customerId, oldPass, newPass)) {
                request.setAttribute("ok", MessageUtil.getMessage("password.change.success"));
            } else {
                throw new BusinessException("error.password.change.failed");
            }

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("errorMess", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.setAttribute("errorMess", MessageUtil.getError("error.system.change.password"));
        }

        request.getRequestDispatcher("/CustomerServlet").forward(request, response);
    }
}
