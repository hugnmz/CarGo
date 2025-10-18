<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.Cars, model.Vehicles" %>
<%
    // --- LẤY DỮ LIỆU TỪ SESSION ---
    String username = (String) session.getAttribute("username"); // Sửa đúng tên
    String avatar = (String) session.getAttribute("avatar");
    if (avatar == null || avatar.isEmpty()) {
        avatar = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";
    }

    // Nếu chưa đăng nhập thì chuyển về login
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // --- LẤY DỮ LIỆU XE TỪ SERVLET ---
    List<Cars> cars = (List<Cars>) request.getAttribute("cars");
    List<Vehicles> vehicles = (List<Vehicles>) request.getAttribute("vehicles");
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
                background: url('https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif') center/cover no-repeat;
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
            .car-card img {
                height: 180px;
                object-fit: cover;
                border-radius: 12px 12px 0 0;
            }
            .car-card:hover {
                transform: scale(1.03);
                transition: 0.2s;
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
                        <li class="nav-item"><a class="nav-link" href="#">Trang chủ</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Về chúng tôi</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Liên hệ</a></li>
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

            <!-- Danh sách xe từ Servlet -->
            <h3 class="fw-bold mt-5 mb-3 text-center">Danh sách xe khả dụng</h3>
            <div class="row g-4">
                <%
                    if (vehicles != null && !vehicles.isEmpty()) {
                        for (Vehicles v : vehicles) {
                %>
                <div class="col-md-4">
                    <div class="card car-card shadow">
                        <img src="<%= v.getImageUrl() != null ? v.getImageUrl() : "https://via.placeholder.com/400x200" %>" alt="">
                        <div class="card-body">
                            <h5 class="card-title"><%= v.getCarName() %></h5>
                            <p class="text-muted"><%= v.getLicensePlate() %></p>
                            <p class="text-success fw-bold"><%= v.getPricePerDay() %>đ/ngày</p>
                            <a href="rent.jsp?id=<%= v.getVehicleId() %>" class="btn btn-success w-100">Đặt xe ngay</a>
                        </div>
                    </div>
                </div>
                <%
                        }
                    } else {
                %>
                <p class="text-center text-muted">Hiện chưa có xe khả dụng.</p>
                <%
                    }
                %>
            </div>
        </div>

        <footer>
            <p>© 2025 CarRental. Mọi quyền được bảo lưu.</p>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
