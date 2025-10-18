<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>
<%
    String username = (String) session.getAttribute("useranme");
    String fullName = (String) session.getAttribute("fullName");
    String email = (String) session.getAttribute("email");
    String phone = (String) session.getAttribute("phone");
    String city = (String) session.getAttribute("city");
    String avatar = (String) session.getAttribute("avatar");

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
            margin: 60px auto;
            background: #fff;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            text-align: center;
        }
        .profile-card img {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            margin-bottom: 20px;
            border: 4px solid #4CAF50;
        }
        .profile-info {
            text-align: left;
            margin-top: 20px;
        }
        .profile-info p {
            font-size: 1.1rem;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="profile-card">
        <img src="<%= avatar != null ? avatar : "https://cdn-icons-png.flaticon.com/512/3135/3135715.png" %>" alt="Avatar">
        <h3 class="fw-bold"><%= fullName != null ? fullName : username %></h3>
        <p class="text-muted">Chào mừng bạn đến với CarRental!</p>

        <div class="profile-info">
            <p><strong>Username:</strong> <%= username %></p>
            <p><strong>Email:</strong> <%= email %></p>
            <p><strong>Số điện thoại:</strong> <%= phone %></p>
            <p><strong>Thành phố:</strong> <%= city %></p>
            <p><strong>ID khách hàng:</strong> <%= session.getAttribute("customerId") %></p>
        </div>

        <div class="d-flex justify-content-center gap-3 mt-4">
            <a href="home.jsp" class="btn btn-outline-success"><i class="fa fa-home"></i> Trang chủ</a>
            <a href="LogoutServlet" class="btn btn-success"><i class="fa fa-sign-out-alt"></i> Đăng xuất</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
