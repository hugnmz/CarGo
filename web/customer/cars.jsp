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
    <title>Tất cả xe - CarRental</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/customer/home.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg fixed-top">
    <div class="container">
        <a class="navbar-brand fw-bold text-success" href="${pageContext.request.contextPath}/home">🚗 CarRental</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/customer/contact.jsp">Liên hệ</a></li>
                <% if (username != null && !username.isEmpty()) { %>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <img src="<%= avatar %>" alt="Avatar" style="width:40px; height:40px; border-radius:50%;">
                        <span class="ms-1"><%= username %></span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/user.jsp">Thông tin cá nhân</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutServlet">Đăng xuất</a></li>
                    </ul>
                </li>
                <% } else { %>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/auth/login.jsp">Đăng nhập</a>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>

<div class="container mt-5 pt-5">
    <h3 class="fw-bold mb-4 text-center">Tất cả xe</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger"><i class="fa fa-exclamation-triangle me-2"></i>${error}</div>
    </c:if>

    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty allCars}">
                <c:forEach var="car" items="${allCars}">
                    <div class="col-md-4">
                        <div class="card car-card shadow-sm" onclick="window.location.href='${pageContext.request.contextPath}/car-detail?carId=${car.carId}'" style="cursor: pointer;">
                            <div class="car-image-container position-relative">
                                <c:choose>
                                    <c:when test="${not empty car.image}">
                                        <img src="${car.image}" class="card-img-top car-image" alt="${car.name}">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://via.placeholder.com/400x250?text=No+Image" class="card-img-top car-image" alt="No Image">
                                    </c:otherwise>
                                </c:choose>
                                <div class="special-icon">
                                    <i class="fa fa-bolt text-warning"></i>
                                </div>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title fw-bold text-dark mb-2">${car.name.toUpperCase()} ${car.year}</h5>
                                <div class="car-specs mb-3">
                                    <div class="row text-center">
                                        <div class="col-4">
                                            <div class="spec-item">
                                                <i class="fa fa-cog text-muted"></i>
                                                <small class="d-block text-muted">Số tự động</small>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="spec-item">
                                                <i class="fa fa-users text-muted"></i>
                                                <small class="d-block text-muted">${car.seatingType != null ? car.seatingType : 'N/A'} chỗ</small>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="spec-item">
                                                <i class="fa fa-gas-pump text-muted"></i>
                                                <small class="d-block text-muted">${car.fuelType != null ? car.fuelType : 'N/A'}</small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="location-info mb-2">
                                    <i class="fa fa-map-marker-alt text-danger me-1"></i>
                                    <small class="text-muted">${car.locationCity != null ? car.locationCity : 'N/A'}</small>
                                </div>
                                <div class="price-section">
                                    <c:choose>
                                        <c:when test="${not empty car.dailyPrice}">
                                            <c:set var="currentPrice" value="${car.dailyPrice}" />
                                            <c:set var="originalPrice" value="${currentPrice * 1.18}" />
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div>
                                                    <span class="text-decoration-line-through text-muted small">${originalPrice}K</span>
                                                    <span class="h5 text-success fw-bold ms-2">${currentPrice}K/ngày</span>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <span class="text-muted">Liên hệ</span>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12 text-center">
                    <div class="alert alert-info">
                        <i class="fa fa-info-circle"></i>
                        Hiện tại chưa có xe nào trong hệ thống.
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<footer>
    <p>© 2025 CarRental. Mọi quyền được bảo lưu.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
