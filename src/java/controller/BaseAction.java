package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public abstract class BaseAction extends HttpServlet {

    protected HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    protected void setMessage(HttpServletRequest request, String key, String message) {
        request.setAttribute(key, message);
    }

    protected void redirect(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
