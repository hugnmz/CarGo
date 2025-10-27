package controller.admin;

import dto.UserDTO;
import java.io.IOException;
import java.util.Optional;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.UserService;
import util.di.DIContainer;
import util.MessageUtil;

@WebServlet(name = "LoginAdmin", urlPatterns = {"/LoginAdmin"})
public class LoginAdmin extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Nếu đã đăng nhập và là ADMIN thì chuyển đến HomeAdmin
        HttpSession session = req.getSession(false);
        if (session != null && "ADMIN".equalsIgnoreCase((String) session.getAttribute("roleName"))) {
            resp.sendRedirect("HomeAdmin");
            return;
        }

        // Nếu chưa đăng nhập → ở lại trang đăng nhập
        req.getRequestDispatcher("admin/admin_login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", MessageUtil.getError("error.admin.login.required"));
            request.getRequestDispatcher("admin/admin_login.jsp").forward(request, response);
            return;
        }

        try {
            Optional<UserDTO> adminOpt = userService.loginUser(username, password);

            if (adminOpt.isPresent()) {
                UserDTO admin = adminOpt.get();

                // Kiểm tra quyền ADMIN
                if (!"ADMIN".equalsIgnoreCase(admin.getRoleName())) {
                    request.setAttribute("error", MessageUtil.getError("error.admin.no.permission"));
                    request.getRequestDispatcher("admin/admin_login.jsp").forward(request, response);
                    return;
                }

                // Tạo session mới
                HttpSession sessionadmin = request.getSession(true);
                setSessionAttributes(sessionadmin, admin);

                response.sendRedirect("HomeAdmin");
            } else {
                request.setAttribute("error", MessageUtil.getError("error.admin.login.invalid"));
                request.getRequestDispatcher("admin/admin_login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", MessageUtil.getError("error.admin.login.error"));
            request.getRequestDispatcher("admin/admin_login.jsp").forward(request, response);
        }
    }

    private void setSessionAttributes(HttpSession session, UserDTO admin) {
        session.setAttribute("userId", admin.getUserId());
        session.setAttribute("usernamead", admin.getUsername());
        session.setAttribute("fullName", admin.getFullName());
        session.setAttribute("email", admin.getEmail());
        session.setAttribute("roleName", admin.getRoleName());
        session.setAttribute("loginTime", System.currentTimeMillis());
        session.setMaxInactiveInterval(60 * 60); // 60 phút
    }
}
