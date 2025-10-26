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
import util.JdbcTemplateUtil;
import util.di.DIContainer;
import util.MessageUtil;

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
        req.getRequestDispatcher("/admin/admin_login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //Kiểm tra đầu vào
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", MessageUtil.getError("error.admin.login.required"));
            request.getRequestDispatcher("/admin/admin_login.jsp").forward(request, response);
            return;
        }

        try {
            //Gọi service xử lý đăng nhập admin
            Optional<UserDTO> adminOpt = userService.loginUser(username, password);

            if (adminOpt.isPresent()) {
                UserDTO admin = adminOpt.get();

                // Truy vấn role của user từ DB
                class RoleRow { @util.di.annotation.Column(name = "roleName") public String roleName; }
                var roleRows = JdbcTemplateUtil.query(
                        "SELECT r.roleName FROM Roles r JOIN UserRoles ur ON ur.roleId=r.roleId WHERE ur.userId=?",
                        RoleRow.class,
                        admin.getUserId()
                );

                java.util.List<String> roles = new java.util.ArrayList<>();
                for (RoleRow rr : roleRows) { if (rr.roleName != null) roles.add(rr.roleName); }

                if (roles.isEmpty()) {
                    request.setAttribute("error", MessageUtil.getError("error.admin.no.permission"));
                    request.getRequestDispatcher("/admin/admin_login.jsp").forward(request, response);
                    return;
                }

                // Ưu tiên role: ADMIN > MANAGER > STAFF
                String primaryRole = roles.contains("ADMIN") ? "ADMIN"
                        : roles.contains("MANAGER") ? "MANAGER"
                        : roles.contains("STAFF") ? "STAFF" : roles.get(0);

                // Tạo session
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", admin.getUserId());
                session.setAttribute("username", admin.getUsername());
                session.setAttribute("role", primaryRole);
                session.setAttribute("roles", roles);
                session.setMaxInactiveInterval(60 * 60); // 60 phút

                // Điều hướng theo role
                if ("ADMIN".equals(primaryRole)) {
                    response.sendRedirect(request.getContextPath() + "/admin/user.jsp");
                } else if ("MANAGER".equals(primaryRole) || "STAFF".equals(primaryRole)) {
                    // TODO: tạo dashboard riêng cho MANAGER/STAFF nếu cần
                    response.sendRedirect(request.getContextPath() + "/home");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                request.setAttribute("error", MessageUtil.getError("error.admin.login.invalid"));
                request.getRequestDispatcher("/admin/admin_login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", MessageUtil.getError("error.admin.system"));
            request.getRequestDispatcher("/admin/admin_login.jsp").forward(request, response);
        }
    }

}
