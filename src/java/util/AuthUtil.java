package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// class xu ly xac thuc dang nhap
public class AuthUtil {

    // method kiem tra dang nhap, neu chua dang nhap thi chuyen den trang login
    public static boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // lay session hien tai, neu khong co thi null
        HttpSession session = request.getSession(false);

        // kiem tra session va customer id
        if (session == null || session.getAttribute("customerId") == null) {
            // luu url hien tai de chuyen huong sau khi dang nhap
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            String currentURL = requestURI.substring(contextPath.length());

            // them query string neu co
            String queryString = request.getQueryString();
            if (queryString != null) {
                currentURL += "?" + queryString;
            }

            // tao session moi va luu url chuyen huong
            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", currentURL);
            // chuyen den trang dang nhap
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return false;
        }

        return true;
    }

    // method lay customer id tu session
    public static Integer getCustomerId(HttpServletRequest request) {
        // lay session hien tai, neu khong co thi null
        HttpSession session = request.getSession(false);
        if (session != null) {
            // tra ve customer id tu session
            return (Integer) session.getAttribute("customerId");
        }
        return null;
    }
}
