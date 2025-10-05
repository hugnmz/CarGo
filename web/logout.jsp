<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="jakarta.servlet.http.HttpSession" %> <% // Xóa session HttpSession
session = request.getSession(); session.invalidate(); // Redirect về trang login
response.sendRedirect("login.jsp"); %>
