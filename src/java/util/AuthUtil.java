/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author admin
 */
public class AuthUtil {

    public static boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("customerId") == null) {
            // Lưu URL hiện tại (loại bỏ context path)
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            String currentURL = requestURI.substring(contextPath.length());

            String queryString = request.getQueryString();
            if (queryString != null) {
                currentURL += "?" + queryString;
            }

            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", currentURL);
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return false;
        }

        return true;
    }

    public static Integer getCustomerId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Integer) session.getAttribute("customerId");
        }
        return null;
    }
}
