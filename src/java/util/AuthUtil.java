package util;

import dto.CustomerDTO;
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

        // kiem tra session va customer object
        CustomerDTO customer = (session != null) ? (CustomerDTO) session.getAttribute("c") : null;
        if (session == null || customer == null || customer.getCustomerId() == null) {
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

    // method lay customer id tu customer object trong session
    public static Integer getCustomerId(HttpServletRequest request) {
        // lay session hien tai, neu khong co thi null
        HttpSession session = request.getSession(false);
        if (session != null) {
            CustomerDTO customer = (CustomerDTO) session.getAttribute("c");
            // tra ve customer id tu customer object
            return (customer != null) ? customer.getCustomerId() : null;
        }
        return null;
    }
    
    // method lay customer object tu session
    public static CustomerDTO getCustomer(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (CustomerDTO) session.getAttribute("c");
        }
        return null;
    }
}
