<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires", 0);

    String role = (String) session.getAttribute("roleName");
    if (role == null || !"MANAGER".equalsIgnoreCase(role)) {
        response.sendRedirect("auth/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý hợp đồng - Manager</title>
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
            <a href="managecus"><i class="fa-solid fa-users"></i> Quản lý khách hàng</a>
            <a href="managecar"><i class="fa-solid fa-car-side"></i> Quản lý xe</a>
            <a href="listcontract" class="active"><i class="fa-solid fa-file-contract"></i> Hợp đồng</a>
            <a href="${pageContext.request.contextPath}/LogoutServlet"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
        </div>

        <!-- Main Content -->
        <div class="content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="fa-solid fa-users"></i> Danh sách hợp đồng</h2>
            </div>

            <!-- Thông báo -->
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <!-- Thông báo thành công -->
            <c:if test="${not empty sessionScope.flash_message}">
                <div class="alert alert-success" role="alert">
                    ${sessionScope.flash_message}
                </div>
                <c:remove var="flash_message" scope="session"/>
            </c:if>

            <!-- Thông báo lỗi -->
            <c:if test="${not empty sessionScope.flash_error}">
                <div class="alert alert-danger" role="alert">
                    ${sessionScope.flash_error}
                </div>
                <c:remove var="flash_error" scope="session"/>
            </c:if>


            <!-- Bảng danh sách khách hàng -->
            <div class="card shadow-sm">
                <div class="card-body">
                    <table class="table table-bordered align-middle text-center">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Thời gian tạo</th>
                                <th>Nhân viên xử lí</th>
                                <th>Khách hàng</th>
                                <th>Số điện thoại</th>
                                <th>Ngày bắt đầu</th>
                                <th>Ngày kết thúc</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${allContracts}">
                                <tr>
                                    <td>${c.contractId}</td>
                                    <td>${c.createAtToString}</td>
                                    <td>${not empty c.staffName? c.staffName:'Trống'}</td>
                                    <td>${c.customerName}</td>
                                    <td>${c.customerPhone}</td>
                                    <td>${c.startDateToString}</td>
                                    <td>${c.endDateToString}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${c.status == 'ACCEPTED'}">
                                                Chấp nhận
                                            </c:when>
                                            <c:when test="${c.status == 'PENDING'}">
                                                Chờ xử lý
                                            </c:when>
                                            <c:when test="${c.status == 'REJECTED'}">
                                                Từ chối
                                            </c:when>
                                                 <c:when test="${c.status == 'RETURNED'}">
                                                Đã trả xe
                                            </c:when>
                                            <c:otherwise>
                                                Không xác định
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form method="POST" action="listcontract">
                                            <a href="contractdetail?contractId=${c.contractId}" class="btn btn-info btn-sm text-white">
                                                <i class="fa-solid fa-circle-info"></i> Chi tiết
                                            </a>
                                            <input type="hidden" name="contractId" value="${c.contractId}">
                                            <input type="submit" value="Xóa"
                                                   class="btn btn-danger btn-sm"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa hợp đồng này không?');"/>
                                        </form>

                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty allContracts}">
                                <tr>
                                    <td colspan="9" class="text-center text-muted">Chưa có hợp đồng nào</td>
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
