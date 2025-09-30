
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    // Giả lập cơ sở dữ liệu bằng Map
    private static Map<String, String> accounts = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        // Thêm 1 tài khoản mặc định
        accounts.put("admin", "123456");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String storedPassword = accounts.get(username);

        if (storedPassword != null && storedPassword.equals(password)) {
            // Lưu thông tin user vào session
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            // Chuyển hướng sang home.jsp
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } else {
            // Sai thông tin đăng nhập
            request.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
