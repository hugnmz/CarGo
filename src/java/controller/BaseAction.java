package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// lop co so cho cac action servlet
public abstract class BaseAction extends HttpServlet {

    // lay session tu request
    protected HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    // dat thong bao vao request
    protected void setMessage(HttpServletRequest request, String key, String message) {
        request.setAttribute(key, message);
    }

    // chuyen huong den url
    protected void redirect(HttpServletResponse response, String url) throws java.io.IOException {
        response.sendRedirect(url);
    }

    // forward den url
    protected void forward(HttpServletRequest request, HttpServletResponse response, String url) 
            throws jakarta.servlet.ServletException, java.io.IOException {
        request.getRequestDispatcher(url).forward(request, response);
    }
}
