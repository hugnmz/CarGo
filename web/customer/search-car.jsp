<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="c" value="${sessionScope.c}" />
<c:set var="username" value="${c != null ? c.username : sessionScope.username}" />
<c:set var="avatar" value="${not empty sessionScope.avatar ? sessionScope.avatar : 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'}" />
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Tìm xe - CarRental</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/customer/search-car.css" rel="stylesheet">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg fixed-top">
            <div class="container">
                <a class="navbar-brand fw-bold text-success" href="${pageContext.request.contextPath}/home">🚗 CarRental</a>
                <div class="collapse navbar-collapse">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/customer/contact.jsp">Liên hệ</a></li>
                            <c:choose>
                                <c:when test="${not empty username}">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                        <img src="${avatar}" alt="Avatar" style="width:40px; height:40px; border-radius:50%;">
                                        <span class="ms-1">${username}</span>
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/CustomerServlet">Thông tin cá nhân</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutServlet">Đăng xuất</a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/auth/login.jsp">Đăng nhập</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container mt-5 pt-5">

            <!-- Modern Search Box -->
            <div class="search-modern ">

                <h3 class="fw-bold mb-4 text-center">Tìm xe bạn muốn thuê </h3>

                <form class="row g-4" action="${pageContext.request.contextPath}/searchcar" method="post">
                    <!-- Địa điểm -->
                    <div class="col-md-6 col-lg-3">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-map-marker-alt text-primary me-2"></i>Địa điểm
                        </label>
                        <select class="form-select" name="location">
                            <option selected disabled >Chọn địa điểm...</option>
                            <c:if test="${not empty allLocations}">
                                <c:forEach var="l" items="${allLocations}">
                                    <option value="${l.locationId}">${l.city}</option>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty allLocations}">
                                <c:redirect url="/searchcar"/>
                            </c:if>

                        </select>
                    </div>

                    <!-- Tên xe -->
                    <div class="col-md-6 col-lg-3">
                        <label class="form-label fw-semibold" >
                            <i class="fas fa-car-side text-success me-2"></i>Tên xe
                        </label>
                        <input type="text" class="form-control" placeholder="Nhập tên xe..." name="carName">
                    </div>

                    <!-- Loại xe -->
                    <div class="col-md-6 col-lg-2">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-car text-warning me-2"></i>Loại xe
                        </label>
                        <select class="form-select" name="category">
                            <option selected disabled >Chọn loại xe...</option>
                            <c:if test="${not empty allCategories}">
                                <c:forEach var="c" items="${allCategories}">
                                    <option value="${c.categoryId}">${c.categoryName}</option>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty allCategories}">
                                <c:redirect url="/searchcar"/>
                            </c:if>

                        </select>
                    </div>

                    <!-- Giá tiền -->
                    <div class="col-md-6 col-lg-2">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-money-bill-wave text-danger me-2"></i>Giá tiền (VND)
                        </label>
                        <input type="number" class="form-control" step="10000" min="0" placeholder="Nhập giá tối đa..." name="price">
                    </div>

                    <!-- Nút tìm kiếm -->
                    <div class="col-12 col-lg-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-search w-100">
                            <i class="fas fa-search me-2"></i>Tìm xe
                        </button>
                    </div>
                </form>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger"><i class="fa fa-exclamation-triangle me-2"></i>${error}</div>
                </c:if>

            <div class="row g-4">
                <c:choose>
                    <c:when test="${not empty searchCars}">
                        <c:forEach var="car" items="${searchCars}">
                            <div class="col-md-4">
                                <div class="card car-card shadow-sm car-card-clickable" onclick="window.location.href = '${pageContext.request.contextPath}/car-detail?carId=${car.carId}'">
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
                            <div class="alert alert-info mt-5">
                                <i class="fa fa-info-circle"></i>
                                Hiện tại chưa tìm thấy xe nào trong hệ thống.
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <footer class="footer">
            <p>© 2025 CarRental. Mọi quyền được bảo lưu.</p>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
