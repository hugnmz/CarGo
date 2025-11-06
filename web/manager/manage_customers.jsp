
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires", 0);

    String role = (String) session.getAttribute("roleName");
    if (role == null || !"MANAGER".equalsIgnoreCase(role)) {
        response.sendRedirect("LoginServlet");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý khách hàng - Manager</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .sidebar {
                height: 100vh;
                background-color: #212529;
                color: white;
                position: fixed;
                width: 240px;
                top: 0;
                left: 0;
                padding-top: 20px;
            }
            .sidebar a {
                color: #adb5bd;
                text-decoration: none;
                display: block;
                padding: 10px 20px;
            }
            .sidebar a:hover {
                background-color: #343a40;
                color: #fff;
            }
            .content {
                margin-left: 250px;
                padding: 20px;
            }
            .table th {
                background-color: #0d6efd;
                color: white;
            }
            .sidebar a.active {
                background-color: #0d6efd;
                color: #fff !important;
            }
        </style>
    </head>
    <body>
        <!-- Sidebar -->
        <div class="sidebar">
            <h4 class="text-center text-white mb-4"><i class="fa-solid fa-car"></i> Car Rental</h4>
            <a href="homemange"><i class="fa-solid fa-house"></i> Trang chủ</a>
            <a href="profile"><i class="fa-solid fa-user-gear"></i> Thông tin cá nhân</a>
            <a href="managecus" class="active"><i class="fa-solid fa-users"></i> Quản lý khách hàng</a>
            <a href="managecar"><i class="fa-solid fa-car-side"></i> Quản lý xe</a>
            <a href="listcontract"><i class="fa-solid fa-file-contract"></i> Hợp đồng</a>
            <a href="${pageContext.request.contextPath}/LogoutServlet"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
        </div>

        <!-- Main Content -->
        <div class="content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="fa-solid fa-users"></i> Danh sách khách hàng</h2>
            </div>

            <!-- Thông báo -->
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <!-- Bảng danh sách khách hàng -->
            <div class="card shadow-sm">
                <div class="card-body">
                    <table class="table table-bordered align-middle text-center">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên đăng nhập</th>
                                <th>Họ và tên</th>
                                <th>Email</th>
                                <th>Điện thoại</th>
                                <th>Thành phố</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cus" items="${allCustomers}">
                                <tr>
                                    <td>${cus.customerId}</td>
                                    <td>${cus.username}</td>
                                    <td>${cus.fullName}</td>
                                    <td>${cus.email}</td>
                                    <td>${cus.phone}</td>
                                    <td>${cus.city}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${cus.isVerified}">
                                                Hoạt động
                                            </c:when>
                                            <c:otherwise>
                                                Không hoạt động
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="controllerinformationcustomer?action=edit&customerId=${cus.customerId}" class="btn btn-warning btn-sm">
                                            <i class="fa-solid fa-pen"></i> Sửa
                                        </a>
                                        <a href="controllerinformationcustomer?action=detail&customerId=${cus.customerId}" class="btn btn-info btn-sm text-white">
                                            <i class="fa-solid fa-circle-info"></i> Chi tiết
                                        </a>
                                        <a href="controllerinformationcustomer?action=delete&customerId=${cus.customerId}" 
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Bạn có chắc chắn muốn xóa khách hàng này không?');">
                                            Xóa
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty allCustomers}">
                                <tr>
                                    <td colspan="9" class="text-center text-muted">Chưa có khách hàng nào</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
