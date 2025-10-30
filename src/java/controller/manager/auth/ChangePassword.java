/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.auth;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.RoleService;
import util.di.DIContainer;
import service.UseService;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/changepass"})
public class ChangePassword extends HttpServlet {

    private UseService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = DIContainer.get(UseService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển hướng về trang profile hoặc trang đổi mật khẩu
        request.getRequestDispatcher("/manager/user_profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        Integer userId = Integer.valueOf(request.getParameter("userId"));

        if (userService.changeUserPassword(userId, oldPass, newPass)) {
            request.setAttribute("ok", "password.change.success");
        } else {
            request.setAttribute("errorMess", "error.password.change.failed");
        }

        // Sau khi đổi xong, trở lại trang profile
        request.getRequestDispatcher("/manager/user_profile.jsp").forward(request, response);
    }

}
