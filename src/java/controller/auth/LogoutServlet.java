package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// servlet xu ly dang xuat
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get cung xu ly nhu post
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // lay session hien tai
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            String username = (String) session.getAttribute("username");
            // huy session
            session.invalidate();
        }
        
        // xoa remember me cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberMeUser".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        
        // chuyen huong ve trang chu voi thong bao logout thanh cong
        response.sendRedirect(request.getContextPath() + "/home?logout=success");
    }
}