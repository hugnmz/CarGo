<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires", 0);
    
    String role = (String) session.getAttribute("roleName");
    if (role == null) {
        response.sendRedirect("LoginAdmin");
        return;
    }
    if (!"ADMIN".equalsIgnoreCase(role)) {
        request.setAttribute("error", "Bạn không có quyền truy cập trang này!");
        request.getRequestDispatcher("error.jsp").forward(request, response);
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Trang quản trị - CarGo</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/admin/admin_login.css" rel="stylesheet">
    </head>
    <body class="bg-light">

        <!-- Thanh điều hướng -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid d-flex justify-content-between align-items-center">
                <!-- Left: Logo -->
                <a class="navbar-brand" href="HomeAdmin">🚗 Admin</a>

                <!-- Right: Nút thêm và logout -->
                <div>
                    <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#addUserForm">
                        + Thêm User
                    </button>
                    <a href="LogoutAdmin" class="btn btn-outline-light">Đăng xuất</a>
                </div>
            </div>
        </nav>

        <div class="container-fluid mt-4">
            <h3 class="mb-3 text-primary text-center">Quản lý người dùng</h3>

            <!-- Thông báo -->
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>


            <!-- Hiển thị lỗi từ filter -->
            <c:if test="${not empty errors}">
                <div class="alert alert-danger mt-2">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <strong>Có lỗi xảy ra:</strong>
                    <ul class="mb-0 mt-2">
                        <c:forEach var="error" items="${errors}">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </div>
            </c:if>

            <!-- Form thêm người dùng (ẩn lúc đầu) -->
            <div class="collapse mb-4" id="addUserForm">
                <div class="card shadow-lg border-primary">
                    <div class="card-header bg-primary text-white text-center fs-5">
                        Thêm người dùng mới
                    </div>
                    <div class="card-body">
                        <form action="ControllerAdmin" method="post" class="row g-3">
                            <input type="hidden" name="_back" value="/HomeAdmin"/>
                            <input type="hidden" name="action" value="create">

                            <div class="col-md-6">
                                <label class="form-label">Tên đăng nhập:</label>
                                <input type="text" name="username" class="form-control" 
                                       placeholder="Nhập tên đăng nhập" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Họ tên:</label>
                                <input type="text" name="fullname" class="form-control" 
                                       placeholder="Nhập họ tên" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Email:</label>
                                <input type="email" name="email" class="form-control" 
                                       placeholder="Nhập email">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Số điện thoại:</label>
                                <input type="text" name="phone" class="form-control" 
                                       placeholder="Nhập số điện thoại">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Vai trò:</label>
                                <select name="roleid" class="form-control" required>
                                    <option value="">-- Chọn vai trò --</option>
                                    <c:forEach var="r" items="${roles}">
                                        <option value="${r.roleId}">${r.roleName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Thành phố:</label>
                                <select name="locationid" class="form-control">
                                    <option value="">-- Chọn thành phố --</option>
                                    <c:forEach var="l" items="${locations}">
                                        <option value="${l.locationId}">${l.city}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Mật khẩu:</label>
                                <input type="password" name="password" class="form-control" 
                                       placeholder="Nhập mật khẩu" required>
                            </div>

                            <div class="col-12 d-flex justify-content-end mt-3">
                                <button type="submit" class="btn btn-primary px-5"
                                        onclick="return confirm('Bạn có chắc chắn muốn tạo tài khoản này?')"
                                        >Lưu</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Danh sách người dùng -->
            <div class="card shadow-sm" style="margin-bottom: 2%">
                <div class="card-header bg-secondary text-white text-center">Danh sách người dùng</div>
                <div class="card-body p-0">
                    <table class="table table-striped mb-0 text-center align-middle table-bordered">
                        <thead class="thead-dark">
                            <tr>
                                <th>ID</th>
                                <th>Tên đăng nhập</th>
                                <th>Họ tên</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Thành phố</th>
                                <th>Vai trò</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td>${user.userId}</td>
                                    <td>${user.username}</td>
                                    <td>${user.fullName}</td>
                                    <td>${user.email}</td>
                                    <td>${user.phone}</td>
                                    <td>${user.city}</td>
                                    <td>
                                        <span class="badge bg-info">${user.roleName}</span>
                                    </td>
                                    <td>
                                        <!-- Ẩn nút Xóa/Sửa nếu người dùng là ADMIN -->
                                        <c:if test="${user.roleName ne 'ADMIN'}">
                                            <form action="ControllerAdmin" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="userId" value="${user.userId}">
                                                <button type="submit" class="btn btn-danger btn-sm"
                                                        onclick="return confirm('Bạn có chắc chắn muốn xóa tài khoản này?')">
                                                    Xóa
                                                </button>
                                            </form>
                                            <a href="ControllerAdmin?action=edit&userId=${user.userId}" 
                                               class="btn btn-warning btn-sm"

                                               >Sửa</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>