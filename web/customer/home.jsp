<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="c" value="${sessionScope.c}" />
<c:set var="username" value="${c != null ? c.username : sessionScope.username}" />
<c:set var="avatar" value="${not empty sessionScope.avatar ? sessionScope.avatar : 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'}" />

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>CarGo - Thuê xe tự lái cao cấp</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/customer/home.css" rel="stylesheet">
    </head>
    <body>
        <!-- Modern Navbar -->
        <nav class="navbar navbar-expand-lg navbar-modern fixed-top">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                    <i class="fas fa-car"></i> CarGo
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto align-items-center">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/cars">Xe cho thuê</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/customer/contact.jsp">Chúng Tôi</a></li>

                        <c:choose>
                            <c:when test="${not empty username}">
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/my-contracts">Hợp đồng</a></li>
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ViewCartDetail"><i class="fas fa-shopping-cart"></i></a></li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                        <img src="${avatar}" alt="Avatar" style="width:35px; height:35px; border-radius:50%; margin-right:8px;">
                                        ${username}
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/CustomerServlet"><i class="fas fa-user me-2"></i>Thông tin cá nhân</a></li>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutServlet"><i class="fas fa-sign-out-alt me-2"></i>Đăng xuất</a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item">
                                    <a class="btn btn-primary-custom" href="${pageContext.request.contextPath}/auth/login.jsp">Đăng nhập</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Hero Section -->
        <div class="hero-modern">
            <div class="container text-center">
                <h1>Thuê xe tự lái cao cấp<br>Trải nghiệm đẳng cấp</h1>
                <p>Hơn 1000+ xe sang trên toàn quốc - Giá tốt nhất - Dịch vụ 5 sao</p>
                <div class="d-flex justify-content-center gap-3 flex-wrap">
                    <div class="d-flex align-items-center text-white">
                        <i class="fas fa-shield-alt fa-2x me-2"></i>
                        <div class="text-start">
                            <small>Bảo hiểm</small><br>
                            <strong>Toàn diện</strong>
                        </div>
                    </div>
                    <div class="d-flex align-items-center text-white">
                        <i class="fas fa-clock fa-2x me-2"></i>
                        <div class="text-start">
                            <small>Hỗ trợ</small><br>
                            <strong>24/7</strong>
                        </div>
                    </div>
                    <div class="d-flex align-items-center text-white">
                        <i class="fas fa-star fa-2x me-2"></i>
                        <div class="text-start">
                            <small>Đánh giá</small><br>
                            <strong>5.0/5.0</strong>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <!-- Modern Search Box -->
            <div class="search-modern">
                <form class="row g-4" action="${pageContext.request.contextPath}/searchcar" method="post">
                    <!-- Địa điểm -->
                    <div class="col-md-6 col-lg-3">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-map-marker-alt text-primary me-2"></i>Địa điểm
                        </label>
                        <select class="form-select" name="location">
                            <option selected disabled value="0">Chọn địa điểm...</option>
                            <c:if test="${not empty allLocations}">
                                <c:forEach var="l" items="${allLocations}">
                                    <option value="${l.locationId}">${l.city}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>

                    <!-- Tên xe -->
                    <div class="col-md-6 col-lg-3">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-car-side text-success me-2"></i>Tên xe
                        </label>
                        <input name="carName" type="text" class="form-control" placeholder="Nhập tên xe...">
                    </div>

                    <!-- Loại xe -->
                    <div class="col-md-6 col-lg-2">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-car text-warning me-2"></i>Loại xe
                        </label>
                        <select name="category" class="form-select">
                            <option selected disabled value="0">Chọn loại xe...</option>
                            <c:if test="${not empty allCategories}">
                                <c:forEach var="c" items="${allCategories}">
                                    <option value="${c.categoryId}">${c.categoryName}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>

                    <!-- Giá tiền -->
                    <div class="col-md-6 col-lg-2">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-money-bill-wave text-danger me-2"></i>Giá tiền (VND)
                        </label>
                        <input name="price" type="number" class="form-control" step="10000" min="0" placeholder="Nhập giá tối đa...">
                    </div>

                    <!-- Nút tìm kiếm -->
                    <div class="col-12 col-lg-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-search w-100">
                            <i class="fas fa-search me-2"></i>Tìm xe
                        </button>
                    </div>
                </form>
            </div>


            <!-- Chọn xe theo hãng -->
            <h3 class="fw-bold mt-5 mb-3 text-center">Chọn xe theo hãng</h3>
            <div class="d-flex flex-wrap justify-content-center gap-3">
                <div class="brand-logo text-center">
                    <img src="https://cdnlogo.com/logos/f/41/ford.svg">
                    <p>Ford</p>
                </div>
                <div class="brand-logo text-center">
                    <img src="https://cdnlogo.com/logos/m/48/mercedes.svg">
                    <p>Mercedes</p>
                </div>
                <div class="brand-logo text-center">
                    <img src="https://cdnlogo.com/logos/a/56/audi.svg">
                    <p>Audi</p>
                </div>
                <div class="brand-logo text-center">
                    <img src="https://cdnlogo.com/logos/p/84/peugeot.svg">
                    <p>Peugeot</p>
                </div>
                <div class="brand-logo text-center">
                    <img src="https://cdnlogo.com/logos/s/32/subaru.svg">
                    <p>Subaru</p>
                </div>
                <div class="brand-logo text-center">
                    <img src="https://cdnlogo.com/logos/b/16/byd.svg">
                    <p>BYD</p>
                </div>
            </div>

            <!-- Địa điểm nổi bật -->
            <h3 class="fw-bold mt-5 mb-3 text-center">Địa điểm nổi bật</h3>
            <div class="row g-4">
                <div class="col-md-3">
                    <div class="card card-location shadow">
                        <img src="https://upload.wikimedia.org/wikipedia/commons/4/4c/Bitexco_Financial_Tower_2016.jpg" class="card-img-top" alt="">
                        <div class="card-body">
                            <h5 class="card-title">Hồ Chí Minh</h5>
                            <p class="text-success"><i class="fa fa-car"></i> 500+ xe</p>
                            <a href="#" class="btn btn-outline-success w-100">TÌM XE</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card card-location shadow">
                        <img src="https://statics.vinpearl.com/cau-vang-da-nang-2_1665731772.jpg" class="card-img-top" alt="">
                        <div class="card-body">
                            <h5 class="card-title">Đà Nẵng</h5>
                            <p class="text-success"><i class="fa fa-car"></i> 100+ xe</p>
                            <a href="#" class="btn btn-outline-success w-100">TÌM XE</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card card-location shadow">
                        <img src="https://statics.vinpearl.com/ho-hoan-kiem-ha-noi-2_1684468738.jpg" class="card-img-top" alt="">
                        <div class="card-body">
                            <h5 class="card-title">Hà Nội</h5>
                            <p class="text-success"><i class="fa fa-car"></i> 150+ xe</p>
                            <a href="#" class="btn btn-outline-success w-100">TÌM XE</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card card-location shadow">
                        <img src="https://static.vinwonders.com/production/binh-duong-1.jpg" class="card-img-top" alt="">
                        <div class="card-body">
                            <h5 class="card-title">Bình Dương</h5>
                            <p class="text-success"><i class="fa fa-car"></i> 150+ xe</p>
                            <a href="#" class="btn btn-outline-success w-100">TÌM XE</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Stats Section -->
            <div class="stats-modern">
                <div class="row">
                    <div class="col-6 col-md-3">
                        <div class="stat-item">
                            <span class="stat-number">1000+</span>
                            <span class="stat-label">Xe cao cấp</span>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="stat-item">
                            <span class="stat-number">50K+</span>
                            <span class="stat-label">Khách hàng</span>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="stat-item">
                            <span class="stat-number">63</span>
                            <span class="stat-label">Tỉnh thành</span>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="stat-item">
                            <span class="stat-number">5.0</span>
                            <span class="stat-label">Đánh giá TB</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Featured Cars -->
            <h2 class="section-title">Xe nổi bật</h2>
            <div class="row g-4">
                <c:choose>
                    <c:when test="${not empty allCars}">
                        <c:forEach var="car" items="${allCars}" varStatus="status" end="5">
                            <div class="col-md-6 col-lg-4">
                                <div class="card car-card-modern car-card-clickable" onclick="window.location.href = '${pageContext.request.contextPath}/car-detail?carId=${car.carId}'">
                                    <div class="position-relative overflow-hidden">
                                        <c:choose>
                                            <c:when test="${not empty car.image}">
                                                <img src="${car.image}" class="card-img-top car-image" alt="${car.name}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="https://via.placeholder.com/400x250?text=No+Image" class="card-img-top car-image" alt="No Image">
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Icon đặc biệt (góc trái trên) -->
                                        <div class="special-icon">
                                            <i class="fa fa-bolt text-warning"></i>
                                        </div>

                                        <!-- Badge giảm giá (góc phải dưới) - chỉ hiển thị nếu có giá -->
                                        <c:if test="${not empty car.dailyPrice}">
                                            <div class="discount-badge">
                                                <span class="badge bg-warning text-dark">Giảm 18%</span>
                                            </div>
                                        </c:if>
                                    </div>

                                    <div class="card-body p-3">
                                        <div class="d-flex justify-content-between align-items-start mb-2">
                                            <span class="badge badge-modern"><i class="fas fa-shield-alt me-1"></i>Miễn thế chấp</span>
                                            <span class="badge bg-warning text-dark"><i class="fas fa-bolt me-1"></i>Nổi bật</span>
                                        </div>
                                        <h5 class="fw-bold mb-2">${car.name.toUpperCase()} ${car.year}</h5>
                                        <div class="d-flex gap-3 mb-3 text-muted small">
                                            <span><i class="fas fa-cog me-1"></i>Tự động</span>
                                            <span><i class="fas fa-users me-1"></i>${car.seatingType != null ? car.seatingType : 'N/A'} chỗ</span>
                                            <span><i class="fas fa-gas-pump me-1"></i>${car.fuelType != null ? car.fuelType : 'N/A'}</span>
                                        </div>
                                        <div class="d-flex align-items-center mb-3">
                                            <i class="fas fa-map-marker-alt text-danger me-2"></i>
                                            <small class="text-muted">${car.locationCity != null ? car.locationCity : 'N/A'}</small>
                                            <span class="ms-auto">
                                                <i class="fas fa-star text-warning"></i>
                                                <small class="fw-semibold">5.0</small>
                                            </span>
                                        </div>
                                        <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                                            <c:choose>
                                                <c:when test="${not empty car.dailyPrice}">
                                                    <div>
                                                        <c:set var="currentPrice" value="${car.dailyPrice}" />
                                                        <c:set var="originalPrice" value="${currentPrice * 1.18}" />
                                                        <small class="price-old d-block"><fmt:formatNumber value="${originalPrice}" pattern="#,###" /> VNĐ</small>
                                                        <span class="price-modern"><fmt:formatNumber value="${currentPrice}" pattern="#,###" /> VNĐ<small class="text-muted fw-normal">/ngày</small></span>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Liên hệ</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <button class="btn btn-sm btn-primary-custom">Đặt xe</button>
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

            <div class="text-center mt-5">
                <a href="${pageContext.request.contextPath}/cars" class="btn btn-primary-custom btn-lg">
                    <i class="fas fa-th me-2"></i>Xem tất cả xe
                </a>
            </div>

            <!-- Testimonials -->
            <h2 class="section-title mt-5">Khách hàng nói gì về chúng tôi</h2>
            <div class="row g-4 mb-5">
                <div class="col-md-4">
                    <div class="testimonial-card">
                        <div class="text-warning mb-3">
                            <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>
                            <i class="fas fa-star"></i><i class="fas fa-star"></i>
                        </div>
                        <p class="mb-3">"Xe mới, sạch sẽ và giao nhanh. Rất hài lòng với dịch vụ! Đội ngũ nhân viên chuyên nghiệp."</p>
                        <div class="d-flex align-items-center">
                            <img src="https://i.pravatar.cc/150?img=12" class="rounded-circle me-3" width="50" height="50">
                            <div>
                                <strong>Nguyễn Minh Đức</strong><br>
                                <small class="text-muted">TP.HCM</small>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="testimonial-card">
                        <div class="text-warning mb-3">
                            <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>
                            <i class="fas fa-star"></i><i class="fas fa-star"></i>
                        </div>
                        <p class="mb-3">"Thủ tục nhanh gọn, giá hợp lý. Xe hoạt động tốt suốt chuyến đi. Sẽ giới thiệu cho bạn bè."</p>
                        <div class="d-flex align-items-center">
                            <img src="https://i.pravatar.cc/150?img=47" class="rounded-circle me-3" width="50" height="50">
                            <div>
                                <strong>Trần Thu Hà</strong><br>
                                <small class="text-muted">Hà Nội</small>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="testimonial-card">
                        <div class="text-warning mb-3">
                            <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>
                            <i class="fas fa-star"></i><i class="fas fa-star"></i>
                        </div>
                        <p class="mb-3">"Nhân viên hỗ trợ nhiệt tình, xe giao đúng giờ. Trải nghiệm tuyệt vời. Chắc chắn sẽ thuê lại!"</p>
                        <div class="d-flex align-items-center">
                            <img src="https://i.pravatar.cc/150?img=33" class="rounded-circle me-3" width="50" height="50">
                            <div>
                                <strong>Phạm Quang Anh</strong><br>
                                <small class="text-muted">Đà Nẵng</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <footer class="footer-modern">
            <div class="container">
                <div class="row">
                    <div class="col-md-4 mb-4">
                        <h5 class="fw-bold mb-3"><i class="fas fa-car me-2"></i>CarGo</h5>
                        <p class="text-white-50">Dịch vụ cho thuê xe tự lái cao cấp hàng đầu Việt Nam. Cam kết mang đến trải nghiệm tốt nhất cho khách hàng.</p>
                    </div>
                    <div class="col-md-2 mb-4">
                        <h6 class="fw-bold mb-3">Dịch vụ</h6>
                        <ul class="list-unstyled">
                            <li class="mb-2"><a href="#">Thuê xe tự lái</a></li>
                            <li class="mb-2"><a href="#">Thuê xe có tài</a></li>
                            <li class="mb-2"><a href="#">Xe dài hạn</a></li>
                        </ul>
                    </div>
                    <div class="col-md-2 mb-4">
                        <h6 class="fw-bold mb-3">Về chúng tôi</h6>
                        <ul class="list-unstyled">
                            <li class="mb-2"><a href="#">Giới thiệu</a></li>
                            <li class="mb-2"><a href="#">Liên hệ</a></li>
                            <li class="mb-2"><a href="#">Tuyển dụng</a></li>
                        </ul>
                    </div>
                    <div class="col-md-4 mb-4">
                        <h6 class="fw-bold mb-3">Liên hệ</h6>
                        <p class="text-white-50">
                            <i class="fas fa-phone me-2"></i>1900 1234<br>
                            <i class="fas fa-envelope me-2"></i>support@cargo.vn<br>
                            <i class="fas fa-map-marker-alt me-2"></i>TP. Hồ Chí Minh
                        </p>
                    </div>
                </div>
                <hr style="border-color: rgba(255,255,255,0.1)">
                <div class="text-center text-white-50">
                    <p class="mb-0">© 2025 CarGo. All rights reserved.</p>
                </div>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>