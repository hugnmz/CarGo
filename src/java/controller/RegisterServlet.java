package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullname = request.getParameter("fullname").trim();
        String phone = request.getParameter("phone").trim();
        String email = request.getParameter("email").trim();
        String city = request.getParameter("city").trim();
        String password = request.getParameter("password").trim();
        String confirmPassword = request.getParameter("confirmPassword").trim();

        // Lấy accounts từ application scope
        Map<String, String> accounts = (Map<String, String>) getServletContext().getAttribute("accounts");
        if (accounts == null) {
            accounts = new HashMap<>();
            getServletContext().setAttribute("accounts", accounts);
        }

        // Kiểm tra password xác nhận
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra fullname đã tồn tại chưa
        if (accounts.containsKey(fullname)) {
            request.setAttribute("error", "Tài khoản này đã tồn tại.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Lưu fullname và password
        accounts.put(fullname, password);

        // Chuyển hướng về login.jsp
        response.sendRedirect("login.jsp");
    }
}
