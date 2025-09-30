
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

        <meta charset="UTF-8">
        <title>Car Rental Login</title>
        <!-- Link Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            /* Nền gradient toàn màn hình */
            body {
                background: linear-gradient(135deg, #74ebd5 0%, #ACB6E5 100%);
                min-height: 100vh;
            }
            /* Hiệu ứng fadeIn */
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            .fade-in {
                animation: fadeIn 1s ease-in-out;
            }
            /* Style riêng cho nút Facebook */
            .btn-facebook {
                background-color: #3b5998;
                color: white;
            }
            .btn-facebook:hover {
                background-color: #2d4373;
                color: white;
            }
            /* Style riêng cho nút Google */
            .btn-google {
                background-color: #db4437;
                color: white;
            }
            .btn-google:hover {
                background-color: #a33224;
                color: white;
            }
        </style>
    </head> 
    <body class="d-flex justify-content-center align-items-center">

        <!-- Card login -->
        <div class="card shadow-lg p-4 text-center fade-in" style="max-width: 400px; width: 100%; border-radius: 12px;">
            <!-- Logo -->
            <img src="https://img.freepik.com/vector-cao-cap/hinh-minh-hoa-vector-logo-xe-hoi-nen-trang_917213-258381.jpg" 
                 alt="Car Logo" class="mx-auto d-block rounded-circle mb-3" style="width:150px; height:150px; object-fit:cover;">

            <!-- Tiêu đề -->
            <h2 class="mb-4 text-dark">Car Rental Login</h2>

            <!-- Form đăng nhập -->
            <form action="LoginServlet" method="post">
                <!-- Username -->
                <div class="mb-3">
                    <input type="text" name="username" class="form-control" placeholder="Tên đăng nhập" required>
                </div>
                <!-- Password -->
                <div class="mb-3">
                    <input type="password" name="password" class="form-control" placeholder="Mật khẩu" required>
                </div>
                <!-- Submit -->
                <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
            </form>

            <!-- Đăng nhập bằng MXH -->
            <div class="mt-3">
                <p class="text-muted">Hoặc đăng nhập bằng</p>
                <div class="d-flex gap-2">
                    <a href="#" class="btn btn-outline-primary w-50">
                        <i class="fab fa-facebook-f"></i> Facebook
                    </a>           
                    <a href="#" class="btn btn-outline-danger w-50">
                        <i class="fab fa-google"></i> Google
                    </a>            
                </div>
            </div>

            <!-- Liên kết -->
            <div class="d-flex justify-content-between mt-3">
                <a href="#">Quên mật khẩu?</a>
                <a href="register.jsp">Đăng ký tài khoản</a>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

