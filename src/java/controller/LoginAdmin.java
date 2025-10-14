/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dto.UserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import model.Users;
import service.UserService;
import util.di.DIContainer;

/**
 *
 * @author DELL
 */
@WebServlet(name = "LoginAdmin", urlPatterns = {"/LoginAdmin"})
public class LoginAdmin extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = DIContainer.get(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("admin_login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //Kiểm tra đầu vào
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("admin_login.jsp").forward(request, response);
            return;
        }

        try {
            //Gọi service xử lý đăng nhập admin
            Optional<UserDTO> adminOpt = userService.loginUser(username, password);

            if (adminOpt.isPresent()) {
                UserDTO admin = adminOpt.get();

                // Tạo session
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", admin.getUserId());
                session.setAttribute("username", admin.getUsername());
                session.setAttribute("role", "ADMIN");
                session.setMaxInactiveInterval(60 * 60); // 60 phút

                //Điều hướng tới dashboard admin
                //response.sendRedirect(response.encodeRedirectURL("admin/dashboard.jsp"));
                response.sendRedirect("home.jsp");
            } else {
                request.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng hoặc không có quyền quản trị");
                request.getRequestDispatcher("admin_login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi đăng nhập");
            request.getRequestDispatcher("admin_login.jsp").forward(request, response);
        }
    }

}
