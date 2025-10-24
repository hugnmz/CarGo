<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý hệ thống - Staff</title>
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
        .card {
            border-radius: 12px;
        }
        .table th {
            background-color: #0d6efd;
            color: white;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h4 class="text-center text-white mb-4"><i class="fa-solid fa-car"></i> Car Rental</h4>
        <a href="staff_returncar.jsp"><i class="fa-solid fa-house"></i> Trang chủ</a>
        <a href="#"><i class="fa-solid fa-users"></i> Quản lý khách hàng</a>
        <a href="#"><i class="fa-solid fa-car-side"></i> Quản lý xe</a>
        <a href="contract_list.jsp"><i class="fa-solid fa-file-contract"></i> Hợp đồng</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
    </div>

    <!-- Main Content -->
    <div class="content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>👋 Xin chào, <c:out value="${sessionScope.fullName}"/></h2>
            <span class="badge bg-primary">Vai trò: Staff</span>
        </div>

        <!-- Dashboard Cards -->
        <div class="row mb-4">
            <div class="col-md-6">
                <div class="card shadow-sm p-3">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5>Quản lý khách hàng</h5>
                            <p class="text-muted">Xem, thêm, cập nhật hoặc xóa thông tin khách hàng</p>
                        </div>
                        <a href="manage_customers.jsp" class="btn btn-outline-primary">
                            <i class="fa-solid fa-users"></i> Truy cập
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card shadow-sm p-3">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5>Quản lý xe</h5>
                            <p class="text-muted">Thêm xe mới, chỉnh sửa thông tin hoặc xóa xe</p>
                        </div>
                        <a href="manage_cars.jsp" class="btn btn-outline-success">
                            <i class="fa-solid fa-car"></i> Truy cập
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Thống kê nhanh -->
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <i class="fa-solid fa-chart-line"></i> Thống kê nhanh
            </div>
            <div class="card-body">
                <div class="row text-center">
                    <div class="col-md-4">
                        <h4 class="text-primary"><c:out value="${totalCustomers != null ? totalCustomers : 0}"/></h4>
                        <p>Khách hàng</p>
                    </div>
                    <div class="col-md-4">
                        <h4 class="text-success"><c:out value="${totalCars != null ? totalCars : 0}"/></h4>
                        <p>Xe đang có</p>
                    </div>
                    <div class="col-md-4">
                        <h4 class="text-danger"><c:out value="${totalContracts != null ? totalContracts : 0}"/></h4>
                        <p>Hợp đồng đang hoạt động</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
