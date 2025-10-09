<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>
<%
    String username = (String) session.getAttribute("loggedInUser");
    Integer avatarId = (Integer) session.getAttribute("userAvatar"); 
    String avatar = avatarId != null ? avatarId.toString() : null;

    // lấy các thông tin khác
    String fullName = (String) session.getAttribute("fullName");
    String email = (String) session.getAttribute("email");
    String phone = (String) session.getAttribute("phone");
    String city = (String) session.getAttribute("city");

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông tin cá nhân - CarRental</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body { background-color: #f9f9f9; }
        .profile-card {
            max-width: 600px;
            margin: 80px auto;
            background: #fff;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .profile-card img {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            margin-bottom: 20px;
            border: 4px solid #4CAF50;
        }
        .profile-info p {
            font-size: 16px;
            margin-bottom: 8px;
        }
    </style>
</head>
<body>
    <div class="profile-card text-center">
        <img src="<%= avatar != null ? avatar : "https://cdn-icons-png.flaticon.com/512/3135/3135715.png" %>" alt="Avatar">
        <h3 class="fw-bold"><%= fullName != null ? fullName : username %></h3>
        <p>@<%= username %></p>

        <div class="profile-info text-start mt-4 px-4">
            <p><strong>Email:</strong> <%= email != null ? email : "Chưa cập nhật" %></p>
            <p><strong>Điện thoại:</strong> <%= phone != null ? phone : "Chưa cập nhật" %></p>
            <p><strong>Thành phố:</strong> <%= city != null ? city : "Chưa cập nhật" %></p>
        </div>

        <div class="d-flex justify-content-center gap-3 mt-4">
            <a href="home.jsp" class="btn btn-outline-success"><i class="fa fa-home"></i> Trang chủ</a>
            <a href="LogoutServlet" class="btn btn-success"><i class="fa fa-sign-out-alt"></i> Đăng xuất</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
