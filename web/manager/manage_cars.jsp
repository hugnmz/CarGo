<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %> <!-- 🔹 Thêm dòng này để dùng fmt:formatNumber -->

<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires", 0);

    String role = (String) session.getAttribute("roleName");
    if (role == null || !"MANAGER".equalsIgnoreCase(role)) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý xe - Manager</title>
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
            .car-img {
                width: 100px;
                height: 60px;
                object-fit: cover;
                border-radius: 6px;
                border: 1px solid #ddd;
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
            <a href="manage_customers.jsp"><i class="fa-solid fa-users"></i> Quản lý khách hàng</a>
            <a href="manage_cars.jsp"
               class="<%= request.getRequestURI().contains("manage_cars.jsp") ? "active" : "" %>">
                <i class="fa-solid fa-car-side"></i> Quản lý xe
            </a>
            <a href="#"><i class="fa-solid fa-file-contract"></i> Hợp đồng</a>
            <a href="${pageContext.request.contextPath}/LogoutServlet"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
        </div>

        <!-- Main Content -->
        <div class="content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="fa-solid fa-car-side"></i> Danh sách xe</h2>
                <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#addCarForm">
                    + Thêm Car
                </button>
            </div>

            <!-- Thông báo -->
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <!-- Form thêm xe -->
            <div class="collapse mb-4 mt-3" id="addCarForm">
                <div class="card shadow-lg border-primary">
                    <div class="card-header bg-primary text-white text-center fs-5">
                        Thêm xe mới
                    </div>
                    <div class="card-body">
                        <form action="controllerinformationcar" method="post" class="row g-3">
                            <input type="hidden" name="action" value="create">
                            <!-- Tên xe -->
                            <div class="col-md-6">
                                <label for="name" class="form-label">Tên xe</label>
                                <input type="text" class="form-control" id="name" name="name" required>
                            </div>
                            <!-- Năm sản xuất -->
                            <div class="col-md-6">
                                <label for="year" class="form-label">Năm sản xuất</label>
                                <input type="number" class="form-control" id="year" name="year" min="1990" max="2025" required>
                            </div>
                            <!-- Mô tả xe -->
                            <div class="col-12">
                                <label for="description" class="form-label">Mô tả xe</label>
                                <textarea class="form-control" id="description" name="description" rows="3" placeholder="Nhập mô tả chi tiết về xe..."></textarea>
                            </div>
                            <!-- Ảnh xe -->
                            <!--                            <div class="col-md-6">
                                                            <label for="image" class="form-label">Hình ảnh xe</label>
                                                            <input type="text" class="form-control" id="image" name="image" accept="image/*" required>
                                                        </div>-->
                            <div class="col-md-6">
                                <label for="image" class="form-label">Hình ảnh xe (Link)</label>
                                <input type="text" class="form-control" id="image" name="image" placeholder="Nhập link ảnh trực tuyến" required>
                            </div>

                            <!-- Danh mục -->
                            <div class="col-md-6">
                                <label for="categoryId" class="form-label">Danh mục</label>
                                <select id="categoryId" name="categoryId" class="form-select" required>
                                    <option value="" hidden>-- Chọn danh mục --</option>
                                    <c:forEach var="cat" items="${allCategories}"> <!-- 🔹 sửa từ ${categories} -->
                                        <option value="${cat.categoryId}">${cat.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Loại nhiên liệu -->
                            <div class="col-md-6">
                                <label for="fuelId" class="form-label">Loại nhiên liệu</label>
                                <select id="fuelId" name="fuelId" class="form-select" required>
                                    <option value="" hidden>-- Chọn loại nhiên liệu --</option>
                                    <c:forEach var="f" items="${allFuels}"> <!-- 🔹 sửa từ ${fuels} -->
                                        <option value="${f.fuelId}">${f.fuelType}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Số chỗ ngồi -->
                            <div class="col-md-6">
                                <label for="seatingId" class="form-label">Số chỗ ngồi</label>
                                <select id="seatingId" name="seatingId" class="form-select" required>
                                    <option value="" hidden>-- Chọn số chỗ ngồi --</option>
                                    <c:forEach var="s" items="${allSeatings}"> <!-- 🔹 sửa từ ${seatings} -->
                                        <option value="${s.seatingId}">${s.seatingType}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Giá xe -->
                            <div class="col-md-6">
                                <label for="price" class="form-label">Giá xe (VNĐ)</label>
                                <input type="number" class="form-control" id="price" name="price" min="0" step="100000" required>
                            </div>

                            <!-- Tiền đặt cọc -->
                            <div class="col-md-6">
                                <label for="deposit" class="form-label">Tiền đặt cọc (VNĐ)</label>
                                <input type="number" class="form-control" id="deposit" name="deposit" min="0" step="100000" required>
                            </div>

                            <!-- Nút submit -->
                            <div class="col-12 text-end mt-3">
                                <button type="submit" class="btn btn-success px-4">Thêm</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Bảng danh sách xe -->
            <div class="card shadow-sm">
                <div class="card-body">
                    <table class="table table-bordered align-middle text-center">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Hình ảnh</th>
                                <th>Tên xe</th>
                                <th>Giá thuê/ngày</th>
                                <th>Hãng xe</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="car" items="${allCars}">
                                <tr>
                                    <td><c:out value="${car.carId}"/></td>
                                    <!-- Hình ảnh -->
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty car.image}">
                                                <img src="${car.image}" alt="Car Image" class="car-img"> <!-- 🔹 thêm contextPath -->
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/images/default_car.png" alt="Default Car" class="car-img">
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${car.name}"/></td>
                                    <td><fmt:formatNumber value="${car.dailyPrice}" type="currency" currencySymbol="₫"/> /ngày</td> <!-- 🔹 sửa hiển thị giá -->
                                    <td><c:out value="${car.categoryName}"/></td>
                                    <td>
                                        <form action="controllerinformationcar" method="post" 
                                              style="display:inline;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="carId" value="${car.carId}">
                                            <button type="submit" class="btn btn-danger btn-sm" 
                                                    onclick="return confirm('Bạn có chắc muốn xóa xe này không?')">
                                                <i class="fa-solid fa-trash"></i> Xóa
                                            </button>
                                        </form>
                                        <a href="controllerinformationcar?action=edit&carId=${car.carId}" class="btn btn-warning btn-sm">
                                            <i class="fa-solid fa-pen"></i> Sửa
                                        </a>

                                        <a href="controllerinformationcar?action=detail&carId=${car.carId}" class="btn btn-info btn-sm text-white">
                                            <i class="fa-solid fa-circle-info"></i> Chi tiết
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty allCars}">
                                <tr>
                                    <td colspan="7" class="text-center text-muted">Chưa có xe nào trong hệ thống</td>
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
