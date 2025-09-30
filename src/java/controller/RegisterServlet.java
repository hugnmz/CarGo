
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static Map<String, String> accounts = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Kiểm tra trùng username (email hoặc phone có thể dùng làm username)
        if (accounts.containsKey(phone)) {
            request.setAttribute("errorMessage", "Tài khoản đã tồn tại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Lưu tài khoản
        accounts.put(phone, password);

        // Chuyển sang login sau khi đăng ký thành công
        request.setAttribute("successMessage", "Đăng ký thành công, vui lòng đăng nhập!");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
