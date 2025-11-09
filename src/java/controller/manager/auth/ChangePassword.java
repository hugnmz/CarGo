package controller.manager.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.UserService;
import util.di.DIContainer;

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
        // Có thể điều hướng về staffmanage nếu cần
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        Integer userId = (Integer) session.getAttribute("userId");
        request.setAttribute("user", userService.getUserById(userId));
        request.setAttribute("locations", userService.getAllLocation());
        request.getRequestDispatcher("/manager/staffmanage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer sessionUserId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        Integer userId = sessionUserId != null ? sessionUserId : Integer.valueOf(request.getParameter("userId"));

        if (userService.changeUserPassword(userId, oldPass, newPass)) {
            request.setAttribute("ok", "Đổi mật khẩu thành công");
        } else {
            request.setAttribute("errorMess", "Đổi mật khẩu thất bại");
        }

        // Nạp lại dữ liệu và forward về staffmanage
        request.setAttribute("user", userService.getUserById(userId));
        request.setAttribute("locations", userService.getAllLocation());
        request.getRequestDispatcher("/manager/staffmanage.jsp").forward(request, response);
    }
}