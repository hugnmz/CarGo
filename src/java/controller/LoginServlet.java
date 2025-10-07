package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        // Lấy danh sách accounts từ application scope
        Map<String, String> accounts = (Map<String, String>) getServletContext().getAttribute("accounts");

        if (accounts != null && accounts.containsKey(username) && accounts.get(username).equals(password)) {
            // Đăng nhập thành công
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInUser", username);

            // Chuyển hướng về home.jsp
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            // Đăng nhập thất bại
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
