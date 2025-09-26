package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Logincontroller extends HttpServlet {

    private List<String> codes;

    @Override
    public void init() throws ServletException {
        String codeStr = getServletConfig().getInitParameter("codes");
        codes = Arrays.asList(codeStr.split(","));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (name == null || name.trim().isEmpty()) {
            out.println("<h3>Error: Name must not be empty!</h3>");
            request.getRequestDispatcher("index.html").include(request, response);
        } else if (!codes.contains(code)) {
            out.println("<h3>Error: Validation code is not correct!</h3>");
            request.getRequestDispatcher("index.html").include(request, response);
        } else {
            // Redirect sang SecondPage, truyền dữ liệu qua query string
            response.sendRedirect(request.getContextPath()
                    + "/NamNHHE191405SecondPage?name=" + name + "&code=" + code);
        }
    }
}
