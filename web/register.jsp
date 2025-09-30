
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Car Rental Register</title>
    <!-- Link Bootstrap để dùng class có sẵn -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Link Font Awesome để lấy icon Google, Facebook -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        /* Toàn bộ nền của trang sẽ là màu gradient xanh */
        body {
            background: linear-gradient(135deg, #74ebd5 0%, #ACB6E5 100%);
            min-height: 100vh; /* cao bằng toàn màn hình */
        }
    </style>
</head>
<body class="d-flex justify-content-center align-items-center">
    <!-- Card chính chứa form đăng ký -->
    <div class="card shadow-lg p-4" style="max-width: 500px; width: 100%; border-radius: 12px;">

        <!-- Logo đặt ở trên cùng -->
        <div class="text-center mb-3">
            <img src="https://img.freepik.com/vector-cao-cap/hinh-minh-hoa-vector-logo-xe-hoi-nen-trang_917213-258381.jpg"
                 alt="Car Logo" class="mx-auto d-block mb-2" style="width:80px; height:80px;">
        </div>

        <!-- Tiêu đề -->
        <h4 class="text-center mb-4">Đăng ký tài khoản</h4>

        <!-- Form đăng ký, gửi dữ liệu đến RegisterServlet -->
        <form action="RegisterServlet" method="post">

            <!-- Nhập họ và tên -->
            <div class="mb-3">
                <input type="text" name="fullname" class="form-control" placeholder="Nhập họ và tên *" required>
            </div>

            <!-- Nhập số điện thoại -->
            <div class="mb-3">
                <input type="text" name="phone" class="form-control" placeholder="Nhập số điện thoại *" required>
            </div>

            <!-- Nhập email -->
            <div class="mb-3">
                <input type="email" name="email" class="form-control" placeholder="Nhập email*)">
            </div>

            <!-- Chọn tỉnh/thành phố -->
            <div class="mb-3">
                <select name="city" class="form-select" required>
                    <option value="" disabled selected>Tỉnh/ thành phố (*)</option>
                    <option>Hà Nội</option>
                    <option>Hồ Chí Minh</option>
                    <option>Đà Nẵng</option>
                    <option>Khác...</option>
                </select>
            </div>

            <!-- Nhập mật khẩu -->
            <div class="mb-3">
                <input type="password" name="password" class="form-control" placeholder="Nhập mật khẩu *" required>
            </div>

            <!-- Nhập lại mật khẩu để xác nhận -->
            <div class="mb-3">
                <input type="password" name="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu *" required>
            </div>

            <!-- Thông báo về chính sách -->
            <p class="small text-muted text-center">
                Bằng việc đăng kí này, bạn đã chấp nhận các chính sách của CarGo
            </p>

            <!-- Nút đăng ký -->
            <button type="submit" class="btn btn-primary w-100">Đăng ký</button>
        </form>

        <!-- Thanh ngang chia form với phần đăng nhập MXH -->
        <div class="d-flex align-items-center my-3">
            <hr class="flex-grow-1">
            <span class="px-2 text-muted">Hoặc</span>
            <hr class="flex-grow-1">
        </div>

        <!-- Các nút đăng nhập MXH -->
        <div class="d-flex gap-2 mb-3">
            <!-- Google -->
            <a href="#" class="btn btn-outline-danger w-50">
                <i class="fab fa-google"></i> Google
            </a>
            <!-- Facebook -->
            <a href="#" class="btn btn-outline-primary w-50">
                <i class="fab fa-facebook-f"></i> Facebook
            </a>
        </div>

        <!-- Link chuyển sang đăng nhập -->
        <div class="text-center">
            <span>Bạn đã có tài khoản? </span>
            <a href="login.jsp" class="text-danger">Đăng nhập ngay</a>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

