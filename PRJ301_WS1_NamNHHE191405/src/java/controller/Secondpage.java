package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Secondpage extends HttpServlet {

    private List<String> codes;

    @Override
    public void init() throws ServletException {
        String codeStr = getServletConfig().getInitParameter("codes");
        codes = Arrays.asList(codeStr.split(","));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");

        if (name == null || name.trim().isEmpty() || !codes.contains(code)) {
            response.sendRedirect("index.html");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<h2>Hello " + name + ", your validation code is " + code + "</h2>");
        out.println("<form action='NamNHHE191405SecondPage' method='POST'>");
        out.println("Email: <input type='text' name='email'><br><br>");
        out.println("Salary: <input type='text' name='salary'><br><br>");
        out.println("Begin date: <input type='date' name='begin'><br><br>");
        out.println("End date: <input type='date' name='end'><br><br>");
        out.println("English Level: ");
        out.println("<input type='radio' name='level' value='A'>A ");
        out.println("<input type='radio' name='level' value='B'>B ");
        out.println("<input type='radio' name='level' value='C'>C ");
        out.println("<input type='radio' name='level' value='D'>D <br><br>");

        out.println("Spoken Languages: ");
        out.println("<input type='checkbox' name='lang' value='English'>English ");
        out.println("<input type='checkbox' name='lang' value='French'>French ");
        out.println("<input type='checkbox' name='lang' value='Spanish'>Spanish ");
        out.println("<input type='checkbox' name='lang' value='German'>German <br><br>");

        out.println("Department: <select name='dept'>");
        out.println("<option>IT</option>");
        out.println("<option>HR</option>");
        out.println("<option>Finance</option>");
        out.println("</select><br><br>");

        out.println("<input type='hidden' name='name' value='" + name + "'>");
        out.println("<input type='hidden' name='code' value='" + code + "'>");
        out.println("<input type='submit' name='action' value='Submit'>");
        out.println("<input type='submit' name='action' value='Logout'>");
        out.println("</form>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("Logout".equals(action)) {
            response.sendRedirect("index.html");
            return;
        }

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String email = request.getParameter("email");
        String salary = request.getParameter("salary");
        String begin = request.getParameter("begin");
        String end = request.getParameter("end");
        String level = request.getParameter("level");
        String[] langs = request.getParameterValues("lang");
        String dept = request.getParameter("dept");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Validate salary
        try {
            Float.parseFloat(salary);
        } catch (NumberFormatException e) {
            out.println("<h3>Error: Salary must be a float number!</h3>");
            return;
        }

        // Validate email
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            out.println("<h3>Error: Invalid email format!</h3>");
            return;
        }

        out.println("<h2>Hello " + name + ", your validation code is " + code + "</h2>");
        out.println("<p>You have given the following information:</p>");
        out.println("<ul>");
        out.println("<li>Email: " + email + "</li>");
        out.println("<li>Salary: " + salary + "</li>");
        out.println("<li>Begin date: " + begin + "</li>");
        out.println("<li>End date: " + end + "</li>");
        out.println("<li>English level: " + level + "</li>");
        out.println("<li>Spoken languages:</li><ul>");
        if (langs != null) {
            for (String l : langs) {
                out.println("<li>" + l + "</li>");
            }
        }
        out.println("</ul>");
        out.println("<li>Department: " + dept + "</li>");
        out.println("</ul>");
        out.println("<a href='mailto:" + email + "'>" + email + "</a>");
    }
}
