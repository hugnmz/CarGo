<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.*, jakarta.servlet.*" %>
<%
    String username = (String) session.getAttribute("username"); // dùng đúng tên từ Servlet
    String avatar = (String) session.getAttribute("avatar");
    if (avatar == null || avatar.isEmpty()) {
        avatar = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";
    }
    

%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thuê xe tự lái - Car Rental</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
         <link href="${pageContext.request.contextPath}/css/customer/home.css" rel="stylesheet">
    </head>
    <body>
        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg fixed-top">
            <div class="container">
                <a class="navbar-brand fw-bold text-success" href="#">🚗 CarRental</a>
                <div class="collapse navbar-collapse">
                    <ul class="navbar-nav ms-auto">
                         <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/HomeServlet">Trang chủ</a></li>
                         <li class="nav-item"><a class="nav-link" href="#">Về chúng tôi</a></li>
                         <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/customer/contact.jsp">Liên hệ</a></li>
                            <% if (username != null && !username.isEmpty()) { %>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                <img src="<%= avatar %>" alt="Avatar" style="width:40px; height:40px; border-radius:50%;">
                                <span class="ms-1"><%= username %></span>
                            </a>
                             <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                 <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/user.jsp">Thông tin cá nhân</a></li>
                                 <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutServlet">Đăng xuất</a></li>
                             </ul>
                        </li>
                        <%  }else { %>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth/login.jsp">Đăng nhập</a>
                        </li>
                        <% } %>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Banner -->
        <div class="container mt-5 pt-5">
            <div class="hero rounded">
                <h1 class="fw-bold">Thuê xe tự lái dễ dàng và nhanh chóng</h1>
                <p>Hơn 1000+ xe trên toàn quốc với giá ưu đãi nhất</p>
            </div>

            <!-- Form tìm kiếm (giữ nguyên) -->
            <div class="search-box shadow mt-4">
                <form class="row g-3">
                    <div class="col-md-3">
                        <label class="form-label">Địa điểm nhận xe</label>
                        <input type="text" class="form-control" placeholder="Nhập địa điểm...">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Địa điểm trả xe</label>
                        <input type="text" class="form-control" placeholder="Nhập địa điểm...">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Ngày nhận</label>
                        <input type="date" class="form-control">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Ngày trả</label>
                        <input type="date" class="form-control">
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-success w-100"><i class="fa fa-search"></i> Tìm xe</button>
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

            <!-- Danh sách xe nổi bật -->
            <h3 class="fw-bold mt-5 mb-3 text-center">Xe nổi bật</h3>


            <div class="row g-4">
                <c:choose>
                    <c:when test="${not empty allCars}">
                        <c:forEach var="car" items="${allCars}" varStatus="status" end="5">
                            <div class="col-md-4">
                                 <div class="card car-card shadow-sm" onclick="window.location.href = '${pageContext.request.contextPath}/car-detail?carId=${car.carId}'" style="cursor: pointer;">                                    <!-- Hình ảnh xe với overlay -->
                                    <div class="car-image-container position-relative">
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

                                    <div class="card-body">
                                        <!-- Badge miễn thế chấp -->
                                        <div class="mb-2">
                                            <span class="badge bg-success">
                                                <i class="fa fa-shield-alt me-1"></i>Miễn thế chấp
                                            </span>
                                        </div>

                                        <!-- Tên xe và năm -->
                                        <h5 class="card-title fw-bold text-dark mb-2">${car.name.toUpperCase()} ${car.year}</h5>

                                        <!-- Thông tin kỹ thuật -->
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

                                        <!-- Địa điểm -->
                                        <div class="location-info mb-2">
                                            <i class="fa fa-map-marker-alt text-danger me-1"></i>
                                            <small class="text-muted">${car.locationCity != null ? car.locationCity : 'N/A'}</small>
                                        </div>

                                        <!-- Đánh giá (không có số chuyến) -->
                                        <div class="rating-info mb-3">
                                            <span>
                                                <i class="fa fa-star text-warning"></i>
                                                <small class="text-muted">5.0</small>
                                            </span>
                                        </div>

                                        <!-- Giá -->
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

             <!-- Nút xem thêm xe -->
             <div class="text-center mt-4">
                 <a href="${pageContext.request.contextPath}/customer/cars.jsp" class="btn btn-outline-success btn-lg">
                     <i class="fa fa-car"></i> Xem tất cả xe
                 </a>
             </div>

            <!-- 3 bước thuê xe -->
            <h3 class="fw-bold mt-5 mb-4 text-center">3 bước thuê xe dễ dàng</h3>
            <div class="row text-center">
                <div class="col-md-4">
                    <div class="step-card">
                        <img src="https://cdn-icons-png.flaticon.com/512/2910/2910768.png" alt="">
                        <p class="mt-3 fw-semibold">1. Chọn xe phù hợp với nhu cầu</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="step-card">
                        <img src="https://cdn-icons-png.flaticon.com/512/2436/2436639.png" alt="">
                        <p class="mt-3 fw-semibold">2. Đặt xe và xác nhận lịch trình</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="step-card">
                        <img src="https://cdn-icons-png.flaticon.com/512/684/684908.png" alt="">
                        <p class="mt-3 fw-semibold">3. Nhận xe và tận hưởng hành trình</p>
                    </div>
                </div>
            </div>

            <!-- Đánh giá khách hàng -->
            <h3 class="fw-bold mt-5 mb-4 text-center">Đánh giá khách hàng</h3>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="review-card">
                        <p class="text-warning mb-2">
                            <i class="fa fa-star"></i><i class="fa fa-star"></i><i class="fa fa-star"></i>
                            <i class="fa fa-star"></i><i class="fa fa-star"></i>
                        </p>
                        <p>"Xe mới, sạch sẽ và giao nhanh. Rất hài lòng với dịch vụ!"</p>
                        <strong>Nguyễn Minh Đức</strong><br><small>TP.HCM</small>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="review-card">
                        <p class="text-warning mb-2">
                            <i class="fa fa-star"></i><i class="fa fa-star"></i><i class="fa fa-star"></i>
                            <i class="fa fa-star"></i><i class="fa fa-star-half-alt"></i>
                        </p>
                        <p>"Thủ tục nhanh gọn, giá hợp lý, xe hoạt động tốt suốt chuyến đi."</p>
                        <strong>Trần Thu Hà</strong><br><small>Hà Nội</small>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="review-card">
                        <p class="text-warning mb-2">
                            <i class="fa fa-star"></i><i class="fa fa-star"></i><i class="fa fa-star"></i>
                            <i class="fa fa-star"></i><i class="fa fa-star"></i>
                        </p>
                        <p>"Nhân viên hỗ trợ nhiệt tình, xe giao đúng giờ. Sẽ thuê lại!"</p>
                        <strong>Phạm Quang Anh</strong><br><small>Đà Nẵng</small>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <footer>
            <p>© 2025 CarRental. Mọi quyền được bảo lưu.</p>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>