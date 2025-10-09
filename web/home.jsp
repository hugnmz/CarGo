<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%
    String username = (String) session.getAttribute("loggedInUser");
    String avatar = (String) session.getAttribute("userAvatar"); // có thể null, dùng mặc định
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thuê xe tự lái - Car Rental</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f9f9f9;
            }
            .navbar {
                background-color: #fff;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }
            .hero {
                background: url('https://img.freepik.com/free-photo/red-sport-car-road_1232-4143.jpg') center/cover no-repeat;
                color: white;
                text-align: center;
                padding: 120px 20px;
                border-radius: 15px;
                margin-top: 20px;
            }
            .search-box {
                background-color: white;
                padding: 25px;
                border-radius: 15px;
                margin-top: -60px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            .brand-logo img {
                width: 60px;
                height: 60px;
                object-fit: contain;
            }
            .brand-logo {
                border: 1px solid #eee;
                border-radius: 12px;
                background: #fff;
                padding: 20px;
                transition: 0.2s;
            }
            .brand-logo:hover {
                transform: scale(1.05);
                border-color: #4CAF50;
            }
            .card-location img {
                height: 180px;
                object-fit: cover;
            }
            .card-location {
                border-radius: 12px;
                overflow: hidden;
                transition: 0.2s;
            }
            .card-location:hover {
                transform: scale(1.03);
            }
            .car-card img {
                height: 180px;
                object-fit: cover;
                border-radius: 12px 12px 0 0;
            }
            .car-card:hover {
                transform: scale(1.03);
                transition: 0.2s;
            }
            .step-card img {
                width: 90px;
                height: 90px;
                border-radius: 50%;
                border: 4px solid #4CAF50;
            }
            .review-card {
                background: #fff;
                border-radius: 12px;
                padding: 20px;
                box-shadow: 0 0 10px rgba(0,0,0,0.05);
            }
            footer {
                margin-top: 80px;
                padding: 30px 0;
                background-color: #fff;
                text-align: center;
                border-top: 1px solid #ddd;
            }
        </style>
    </head>
    <body>

        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg fixed-top">
            <div class="container">
                <a class="navbar-brand fw-bold text-success" href="#">🚗 CarRental</a>
                <div class="collapse navbar-collapse">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item"><a class="nav-link" href="home">Trang chủ</a></li>
                        <li class="nav-item"><a class="nav-link" href="cart">Giỏ hàng</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Về chúng tôi</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Liên hệ</a></li>

                        <% if (username != null) { %>
<li class="nav-item dropdown">
    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown" role="button"
       data-bs-toggle="dropdown" aria-expanded="false">
        <img src="<%= avatar != null ? avatar : "https://cdn-icons-png.flaticon.com/512/3135/3135715.png" %>" 
             alt="Avatar" style="width:40px; height:40px; border-radius:50%;">
        <span class="ms-2"><%= username %></span>
    </a>
    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
        <li><a class="dropdown-item" href="user.jsp">Thông tin cá nhân</a></li>
        <li><a class="dropdown-item" href="LogoutServlet">Đăng xuất</a></li>
    </ul>
</li>
<% } else { %>
<li class="nav-item"><a class="btn btn-outline-success" href="login.jsp">Đăng nhập</a></li>
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

            <!-- Form tìm kiếm -->
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

            <!-- Hiển thị thông báo lỗi nếu có -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Hiển thị thông báo thành công nếu có -->
            <c:if test="${not empty param.message}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle"></i> ${param.message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Hiển thị thông báo lỗi từ parameter nếu có -->
            <c:if test="${not empty param.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i> ${param.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Danh sách xe nổi bật -->
            <h3 class="fw-bold mt-5 mb-3 text-center">Xe nổi bật</h3>
            
            <!-- Kiểm tra có xe không -->
            <c:choose>
                <c:when test="${empty cars}">
                    <!-- Không có xe -->
                    <div class="text-center py-5">
                        <i class="fas fa-car fa-3x text-muted mb-3"></i>
                        <h5 class="text-muted">Hiện tại chưa có xe nào</h5>
                        <p class="text-muted">Vui lòng liên hệ admin để thêm xe vào hệ thống</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- Hiển thị danh sách xe từ database -->
                    <div class="row g-4">
                        <c:forEach var="car" items="${cars}" varStatus="status">
                            <div class="col-md-4">
                                <div class="card car-card shadow">
                                    <!-- Hình ảnh xe -->
                                    <img src="${not empty car.image ? car.image : 'https://via.placeholder.com/400x250/4CAF50/white?text=' += car.name}" 
                                         alt="${car.name}" 
                                         style="height: 200px; object-fit: cover;">
                                    
                                    <div class="card-body">
                                        <!-- Tên xe -->
                                        <h5 class="card-title">
                                            <i class="fas fa-car"></i> ${car.name}
                                        </h5>
                                        
                                        <!-- Thông tin xe -->
                                        <p class="text-muted">
                                            <i class="fas fa-calendar"></i> Năm ${car.year}
                                            <br>
                                            <i class="fas fa-users"></i> 
                                            <c:choose>
                                                <c:when test="${car.seatingId == 1}">4 chỗ</c:when>
                                                <c:when test="${car.seatingId == 2}">5 chỗ</c:when>
                                                <c:when test="${car.seatingId == 3}">7 chỗ</c:when>
                                                <c:otherwise>${car.seatingId} chỗ</c:otherwise>
                                            </c:choose>
                                        </p>
                                        
                                        <!-- Mô tả xe -->
                                        <c:if test="${not empty car.description}">
                                            <p class="text-muted small">
                                                <i class="fas fa-info-circle"></i> ${car.description}
                                            </p>
                                        </c:if>
                                        
                                        <!-- Giá thuê -->
                                        <c:choose>
                                            <c:when test="${not empty car.carPrices and not empty car.carPrices[0].dailyPrice}">
                                                <p class="text-success fw-bold">
                                                    <i class="fas fa-dollar-sign"></i>
                                                    <fmt:formatNumber value="${car.carPrices[0].dailyPrice}" type="currency" currencyCode="VND"/>/ngày
                                                </p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="text-warning fw-bold">
                                                    <i class="fas fa-exclamation-triangle"></i> Liên hệ để biết giá
                                                </p>
                                            </c:otherwise>
                                        </c:choose>
                                        
                                        <!-- Form đặt xe -->
                                        <form action="add-to-cart" method="post" class="add-to-cart-form">
                                            <!-- Hidden field chứa ID của xe thực tế -->
                                            <c:set var="availableVehicles" value="" />
                                            <c:forEach var="vehicle" items="${vehicles}">
                                                <c:if test="${vehicle.carId == car.carId}">
                                                    <c:set var="availableVehicles" value="${availableVehicles},${vehicle.vehicleId}" />
                                                </c:if>
                                            </c:forEach>
                                            
                                            <c:choose>
                                                <c:when test="${not empty availableVehicles}">
                                                    <!-- Có xe thực tế - hiển thị form đặt xe -->
                                                    <input type="hidden" name="vehicleId" value="${availableVehicles.split(',')[1]}">
                                                    
                                                    <!-- Ngày bắt đầu thuê -->
                                                    <div class="mb-2">
                                                        <label class="form-label small">
                                                            <i class="fas fa-calendar-alt"></i> Ngày bắt đầu:
                                                        </label>
                                                        <input type="datetime-local" 
                                                               name="rentStartDate" 
                                                               class="form-control form-control-sm" 
                                                               required
                                                               min="<%= java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) %>"
                                                               title="Chọn ngày bắt đầu thuê xe">
                                                    </div>
                                                    
                                                    <!-- Ngày kết thúc thuê -->
                                                    <div class="mb-2">
                                                        <label class="form-label small">
                                                            <i class="fas fa-calendar-alt"></i> Ngày kết thúc:
                                                        </label>
                                                        <input type="datetime-local" 
                                                               name="rentEndDate" 
                                                               class="form-control form-control-sm" 
                                                               required
                                                               title="Chọn ngày kết thúc thuê xe">
                                                    </div>
                                                    
                                                    <!-- Nút thêm vào giỏ hàng -->
                                                    <button type="submit" class="btn btn-success w-100 btn-sm">
                                                        <i class="fas fa-shopping-cart"></i> Thêm vào giỏ
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <!-- Không có xe thực tế -->
                                                    <button type="button" class="btn btn-secondary w-100 btn-sm" disabled>
                                                        <i class="fas fa-times"></i> Tạm hết xe
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

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