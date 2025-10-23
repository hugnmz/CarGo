<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ page
import="javax.servlet.http.*, javax.servlet.*" %> <% String username = (String)
session.getAttribute("useranme"); String fullName = (String)
session.getAttribute("fullName"); String email = (String)
session.getAttribute("email"); String phone = (String)
session.getAttribute("phone"); String city = (String)
session.getAttribute("city"); String avatar = (String)
session.getAttribute("avatar"); if (username == null) {
response.sendRedirect("auth/login.jsp"); return; } %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <title>Thông tin cá nhân - CarRental</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      rel="stylesheet"
    />
    <link
      href="${pageContext.request.contextPath}/css/admin/user.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="profile-card">
      <img src="<%= avatar != null ? avatar :
      "https://cdn-icons-png.flaticon.com/512/3135/3135715.png" %>"
      alt="Avatar">
      <h2>Thông tin cá nhân</h2>

      <div class="profile-info">
        <p><strong>Tên đăng nhập:</strong> <%= username %></p>
        <p>
          <strong>Họ và tên:</strong> <%= fullName != null ? fullName : "Chưa
          cập nhật" %>
        </p>
        <p>
          <strong>Email:</strong> <%= email != null ? email : "Chưa cập nhật" %>
        </p>
        <p>
          <strong>Số điện thoại:</strong> <%= phone != null ? phone : "Chưa cập
          nhật" %>
        </p>
        <p>
          <strong>Thành phố:</strong> <%= city != null ? city : "Chưa cập nhật"
          %>
        </p>
      </div>

      <a href="#" class="btn-edit">
        <i class="fas fa-edit"></i> Chỉnh sửa thông tin
      </a>
    </div>
  </body>
</html>
