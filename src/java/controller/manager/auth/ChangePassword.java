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
import service.UserService;
import util.di.DIContainer;
import util.MessageUtil;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/changepass"})
public class ChangePassword extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = DIContainer.get(UserService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/manager/user_profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("reNewPassword");
        Integer userId = Integer.valueOf(request.getParameter("userId"));

        // Kiểm tra xác nhận mật khẩu
        if (!newPass.equals(confirmPass)) {
            request.setAttribute("errorMess", MessageUtil.getError("error.password.mismatch"));
            request.getRequestDispatcher("/manager/user_profile.jsp").forward(request, response);
            return;
        }

        if (userService.changeUserPassword(userId, oldPass, newPass)) {
            request.setAttribute("ok", MessageUtil.getMessage("password.change.success"));
        } else {
            request.setAttribute("errorMess", MessageUtil.getError("error.password.change.failed"));
        }

        // Sau khi đổi xong, trở lại trang profile
        request.getRequestDispatcher("/manager/user_profile.jsp").forward(request, response);
    }

}
