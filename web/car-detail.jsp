<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.*, jakarta.servlet.*" %>
<%
    String username = (String) session.getAttribute("username");
    String avatar = (String) session.getAttribute("avatar");
    if (avatar == null || avatar.isEmpty()) {
        avatar = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết xe - CarRental</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/car-detail.css" rel="stylesheet">
    </head>
    <body>
        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg fixed-top">
            <div class="container">
                <a class="navbar-brand fw-bold text-success" href="home">🚗 CarRental</a>
                <div class="collapse navbar-collapse">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item"><a class="nav-link" href="home">Trang chủ</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Về chúng tôi</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Liên hệ</a></li>
                            <% if (username != null && !username.isEmpty()) { %>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                <img src="<%= avatar %>" alt="Avatar" style="width:40px; height:40px; border-radius:50%;">
                                <span class="ms-1"><%= username %></span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                <li><a class="dropdown-item" href="user.jsp">Thông tin cá nhân</a></li>
                                <li><a class="dropdown-item" href="LogoutServlet">Đăng xuất</a></li>
                            </ul>
                        </li>
                        <% } else { %>
                        <li class="nav-item">
                            <a class="nav-link" href="login.jsp">Đăng nhập</a>
                        </li>
                        <% } %>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Car Header -->
        <div class="car-header">
            <div class="container">
                <div class="row align-items-center">
                    <div class="col-md-4">
                        <c:choose>
                            <c:when test="${not empty car.image}">
                                <img src="${car.image}" class="img-fluid car-image" alt="${car.name}">
                            </c:when>
                            <c:otherwise>
                                <img src="https://via.placeholder.com/400x300?text=No+Image" class="img-fluid car-image" alt="No Image">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="col-md-8">
                        <h1 class="display-4 fw-bold mb-3">${car.name}</h1>
                        <p class="lead mb-4">${car.description}</p>

                        <!-- Thông tin chi tiết -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h5 class="text-white mb-3"><i class="fa fa-info-circle me-2"></i>Thông tin cơ bản</h5>
                                <ul class="list-unstyled text-white">
                                    <li><i class="fa fa-calendar me-2"></i><strong>Năm sản xuất:</strong> ${car.year}</li>
                                    <li><i class="fa fa-tags me-2"></i><strong>Loại xe:</strong> ${car.categoryName}</li>
                                    <li><i class="fa fa-users me-2"></i><strong>Số chỗ ngồi:</strong> ${car.seatingType} chỗ</li>
                                    <li><i class="fa fa-gas-pump me-2"></i><strong>Nhiên liệu:</strong> ${car.fuelType}</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h5 class="text-white mb-3"><i class="fa fa-star me-2"></i>Đặc điểm nổi bật</h5>
                                <ul class="list-unstyled text-white">
                                    <li><i class="fa fa-shield-alt me-2"></i>Bảo hiểm toàn diện</li>
                                    <li><i class="fa fa-key me-2"></i>Giao xe tận nơi</li>
                                    <li><i class="fa fa-phone me-2"></i>Hỗ trợ 24/7</li>
                                    <li><i class="fa fa-credit-card me-2"></i>Thanh toán linh hoạt</li>
                                </ul>
                            </div>
                        </div>

                        <!-- Giá và nút đặt xe -->
                        <div class="d-flex align-items-center">
                            <div class="me-4">
                                <div class="d-flex align-items-baseline">
                                    <span class="h2 text-warning fw-bold">${car.dailyPrice}K</span>
                                    <span class="text-white ms-2">/ngày</span>
                                </div>
                                <small class="text-white-50 d-block">
                                    <i class="fa fa-info-circle me-1"></i>Giá từ ${car.dailyPrice}K/ngày
                                </small>
                            </div>
                            <a href="#vehicles-section" class="btn btn-warning btn-lg">
                                <i class="fa fa-car me-2"></i>Chọn xe ngay
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Main Content -->
        <div class="container my-5">
            <!-- Back Button -->
            <div class="mb-4">
                <a href="home" class="back-btn">
                    <i class="fa fa-arrow-left me-2"></i>Quay lại trang chủ
                </a>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fa fa-exclamation-triangle me-2"></i>${error}
                </div>
            </c:if>

            <c:if test="${param.add == 'true'}">
                <div class="alert alert-success">
                    <i class="fa fa-check-circle me-2"></i>Đã thêm xe vào giỏ hàng thành công!
                </div>
            </c:if>

            <c:if test="${param.error == 'add_failed'}">
                <div class="alert alert-danger">
                    <i class="fa fa-exclamation-triangle me-2"></i>Không thể thêm xe vào giỏ hàng. Vui lòng thử lại!
                </div>
            </c:if>


            <!-- Thông tin chi tiết -->
            <div class="row mb-5">
                <div class="col-md-8">
                    <!-- Mô tả chi tiết -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0"><i class="fa fa-info-circle me-2"></i>Mô tả chi tiết</h5>
                        </div>
                        <div class="card-body">
                            <p class="mb-3">${car.description}</p>
                            <p>Xe ${car.name} là lựa chọn hoàn hảo cho những chuyến đi dài ngắn khác nhau. Với thiết kế hiện đại, tiết kiệm nhiên liệu và an toàn tuyệt đối.</p>
                        </div>
                    </div>

                    <!-- Thông số kỹ thuật -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0"><i class="fa fa-cogs me-2"></i>Thông số kỹ thuật</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <table class="table table-borderless">
                                        <tr>
                                            <td><strong>Loại xe:</strong></td>
                                            <td>${car.categoryName}</td>
                                        </tr>
                                        <tr>
                                            <td><strong>Năm sản xuất:</strong></td>
                                            <td>${car.year}</td>
                                        </tr>
                                        <tr>
                                            <td><strong>Số chỗ ngồi:</strong></td>
                                            <td>${car.seatingType} chỗ</td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="col-md-6">
                                    <table class="table table-borderless">
                                        <tr>
                                            <td><strong>Nhiên liệu:</strong></td>
                                            <td>${car.fuelType}</td>
                                        </tr>
                                        <tr>
                                            <td><strong>Hộp số:</strong></td>
                                            <td>Tự động</td>
                                        </tr>
                                        <tr>
                                            <td><strong>Điều hòa:</strong></td>
                                            <td>Có</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <!-- Thông tin đặt xe -->
                    <div class="card sticky-top">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0"><i class="fa fa-calendar me-2"></i>Đặt xe ngay</h5>
                        </div>
                        <div class="card-body">
                            <form action="book-car" method="post">
                                <input type="hidden" name="carId" value="${car.carId}">

                                <div class="mb-3">
                                    <label class="form-label fw-bold">Ngày nhận xe</label>
                                    <input type="date" name="pickupDate" class="form-control" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label fw-bold">Ngày trả xe</label>
                                    <input type="date" name="returnDate" class="form-control" required>
                                </div>

                                <!-- Chọn xe cụ thể -->
                                <div class="mb-3">
                                    <label class="form-label fw-bold">
                                        <i class="fa fa-car me-1"></i>Chọn xe cụ thể
                                    </label>
                                    <c:choose>
                                        <c:when test="${not empty vehicles}">
                                            <select name="vehicleId" class="form-select" required>
                                                <option value="">-- Chọn biển số xe --</option>
                                                <c:forEach var="vehicle" items="${vehicles}">
                                                    <c:if test="${vehicle.isActive}">
                                                        <option value="${vehicle.vehicleId}">
                                                            ${vehicle.plateNumber} - ${vehicle.city}
                                                            <c:if test="${not empty car.dailyPrice}">
                                                                (${car.dailyPrice}K/ngày)
                                                            </c:if>
                                                        </option>
                                                    </c:if>
                                                </c:forEach>
                                            </select>
                                            <small class="form-text text-muted">
                                                <i class="fa fa-info-circle me-1"></i>
                                                Chọn biển số xe bạn muốn thuê
                                            </small>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="alert alert-warning">
                                                <i class="fa fa-exclamation-triangle me-2"></i>
                                                Không có xe nào có sẵn
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label fw-bold">Địa điểm nhận xe</label>
                                    <select name="pickupLocation" class="form-select" required>
                                        <option value="">-- Chọn địa điểm --</option>
                                        <option value="1">Hà Nội - Sân bay Nội Bài</option>
                                        <option value="2">TP.HCM - Sân bay Tân Sơn Nhất</option>
                                        <option value="3">Đà Nẵng - Sân bay Đà Nẵng</option>
                                        <option value="4">Bình Dương - Trung tâm thành phố</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label fw-bold">Địa điểm trả xe</label>
                                    <select name="returnLocation" class="form-select" required>
                                        <option value="">-- Chọn địa điểm --</option>
                                        <option value="1">Hà Nội - Sân bay Nội Bài</option>
                                        <option value="2">TP.HCM - Sân bay Tân Sơn Nhất</option>
                                        <option value="3">Đà Nẵng - Sân bay Đà Nẵng</option>
                                        <option value="4">Bình Dương - Trung tâm thành phố</option>
                                    </select>
                                </div>

                                <button type="submit" class="btn btn-success w-100" 
                                        <c:if test="${empty vehicles}">disabled</c:if>>
                                            <i class="fa fa-check me-2"></i>Đặt xe ngay
                                        </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Vehicles Section -->
                <div id="vehicles-section">
                <c:choose>
                    <c:when test="${not empty vehicles}">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h2 class="fw-bold mb-0">
                                <i class="fa fa-car me-2"></i>Xe có sẵn (${vehicles.size()} chiếc)
                            </h2>

                            <!-- Tổng quan giá và địa điểm -->
                            <div class="text-end">
                                <c:set var="hasPrice" value="false" />
                                <c:set var="minPrice" value="999999" />
                                <c:set var="maxPrice" value="0" />
                                <c:set var="locations" value="" />
                                <c:forEach var="vehicle" items="${vehicles}">
                                    <c:if test="${not empty vehicle.currentPrice}">
                                        <c:set var="hasPrice" value="true" />
                                        <c:if test="${vehicle.currentPrice < minPrice}">
                                            <c:set var="minPrice" value="${vehicle.currentPrice}" />
                                        </c:if>
                                        <c:if test="${vehicle.currentPrice > maxPrice}">
                                            <c:set var="maxPrice" value="${vehicle.currentPrice}" />
                                        </c:if>
                                    </c:if>
                                    <c:if test="${not empty vehicle.city}">
                                        <c:if test="${empty locations}">
                                            <c:set var="locations" value="${vehicle.city}" />
                                        </c:if>
                                        <c:if test="${locations != vehicle.city}">
                                            <c:set var="locations" value="${locations}, ${vehicle.city}" />
                                        </c:if>
                                    </c:if>
                                </c:forEach>

                                <div class="d-flex flex-column align-items-end">
                                    <c:choose>
                                        <c:when test="${hasPrice}">
                                            <c:choose>
                                                <c:when test="${minPrice == maxPrice}">
                                                    <span class="badge bg-success fs-6 mb-1">
                                                        <i class="fa fa-money-bill-wave me-1"></i>${minPrice}K/ngày
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-success fs-6 mb-1">
                                                        <i class="fa fa-money-bill-wave me-1"></i>${minPrice}K - ${maxPrice}K/ngày
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                    </c:choose>

                                </div>
                            </div>
                        </div>

                        <div class="row g-4">
                            <c:forEach var="vehicle" items="${vehicles}">
                                <div class="col-lg-6 col-xl-4">
                                    <div class="card vehicle-card h-100">
                                        <!-- Vehicle Image -->
                                        <div class="position-relative">
                                            <c:choose>
                                                <c:when test="${not empty vehicle.image}">
                                                    <img src="${vehicle.image}" class="card-img-top vehicle-image" alt="${vehicle.carName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="https://via.placeholder.com/400x200?text=No+Image" class="card-img-top vehicle-image" alt="No Image">
                                                </c:otherwise>
                                            </c:choose>

                                            <!-- Status Badge -->
                                            <div class="position-absolute top-0 end-0 m-3">
                                                <c:choose>
                                                    <c:when test="${vehicle.isActive}">
                                                        <span class="badge bg-success">
                                                            <i class="fa fa-check-circle me-1"></i>Có sẵn
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger">
                                                            <i class="fa fa-times-circle me-1"></i>Không có sẵn
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>

                                        <div class="card-body d-flex flex-column">
                                            <!-- Vehicle Info -->
                                            <div class="mb-3">
                                                <h5 class="card-title fw-bold text-primary">${vehicle.carName} ${vehicle.year}</h5>
                                                <div class="d-flex align-items-center mb-2">
                                                    <span class="plate-badge me-2">
                                                        <i class="fa fa-id-card me-1"></i>${vehicle.plateNumber}
                                                    </span>
                                                </div>

                                                <div class="location-badge d-inline-block mb-2">
                                                    <i class="fa fa-map-marker-alt me-1"></i>
                                                    ${vehicle.city}
                                                </div>
                                            </div>

                                            <!-- Price Section -->
                                            <div class="mt-auto">
                                                <!-- Giá chính -->
                                                <div class="mb-3">
                                                    <div class="d-flex justify-content-between align-items-center">
                                                        <div>
                                                            <span class="h5 text-success fw-bold">
                                                                <i class="fa fa-money-bill-wave me-1"></i>
                                                                ${car.dailyPrice}K/ngày
                                                            </span>
                                                        </div>
                                                        <c:if test="${not empty vehicle.depositAmount}">
                                                            <small class="text-muted">
                                                                <i class="fa fa-shield-alt me-1"></i>Cọc: ${vehicle.depositAmount}K
                                                            </small>
                                                        </c:if>
                                                    </div>
                                                </div>

                                                <!-- Thông tin địa điểm và giá -->
                                                <div class="bg-light p-3 rounded mb-3">
                                                    <div class="row">
                                                        <div class="col-6">
                                                            <small class="text-muted d-block">
                                                                <i class="fa fa-map-marker-alt me-1"></i>
                                                                <strong>Địa điểm:</strong>
                                                            </small>
                                                            <span class="text-primary fw-bold">${vehicle.city}</span>
                                                        </div>
                                                        <div class="col-6">
                                                            <small class="text-muted d-block">
                                                                <i class="fa fa-info-circle me-1"></i>
                                                                <strong>Bao gồm:</strong>
                                                            </small>
                                                            <span class="text-success">Bảo hiểm + Thuế</span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <!-- Add to Cart Button -->
                                                <c:choose>
                                                    <c:when test="${vehicle.isActive}">
                                                        <form action="${pageContext.request.contextPath}/Cart" method="post" class="d-grid">
                                                            <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}">
                                                            <input type="hidden" name="carId" value="${vehicle.carId}">
                                                            <button type="submit" class="btn add-to-cart-btn w-100">
                                                                <i class="fa fa-shopping-cart me-2"></i>Thêm vào giỏ hàng
                                                            </button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button class="btn btn-secondary w-100" disabled>
                                                            <i class="fa fa-ban me-2"></i>Không có sẵn
                                                        </button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="fa fa-car fa-3x text-muted mb-3"></i>
                            <h3 class="text-muted">Không có xe nào có sẵn</h3>
                            <p class="text-muted">Hiện tại không có xe ${car.name} nào có sẵn để thuê.</p>
                            <a href="home" class="btn btn-primary">
                                <i class="fa fa-arrow-left me-2"></i>Quay lại trang chủ
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>




        <!-- Footer -->
        <footer class="bg-dark text-white text-center py-4 mt-5">
            <p class="mb-0">© 2025 CarRental. Mọi quyền được bảo lưu.</p>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>