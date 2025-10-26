package controller.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name="LogoutAdmin", urlPatterns={"/LogoutAdmin"})
public class LogoutAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy session hiện tại, nếu có
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // huỷ session
        }
        // Redirect về trang login admin
        response.sendRedirect("LoginAdmin");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Gọi doGet để logout cũng được
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Logout admin servlet";
    }
}
