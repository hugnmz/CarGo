<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Car Rental Register</title>
    <!-- Link Bootstrap để dùng class có sẵn -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <!-- Link Font Awesome để lấy icon Google, Facebook -->
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link
<<<<<<< HEAD:web/auth/register.jsp
      href="${pageContext.request.contextPath}/css/auth/register.css"
=======
      href="${pageContext.request.contextPath}/CSS/register.css"
>>>>>>> origin/main:web/register.jsp
      rel="stylesheet"
    />
  </head>
  <body class="d-flex justify-content-center align-items-center">
    <!-- Card chính chứa form đăng ký -->
    <div
      class="card shadow-lg p-4"
      style="max-width: 500px; width: 100%; border-radius: 12px"
    >
      <!-- Logo đặt ở trên cùng -->
      <div class="text-center mb-3">
        <img
          src="https://img.freepik.com/vector-cao-cap/hinh-minh-hoa-vector-logo-xe-hoi-nen-trang_917213-258381.jpg"
          alt="Car Logo"
          class="mx-auto d-block mb-2"
          style="width: 80px; height: 80px"
        />
      </div>

      <!-- Tiêu đề -->
      <h4 class="text-center mb-4">Đăng ký tài khoản</h4>

      <%@ taglib prefix="c" uri="jakarta.tags.core" %>

      <c:if test="${not empty errors}">
        <div class="alert alert-danger">
          <i class="fas fa-exclamation-triangle me-2"></i>
          <strong>Có lỗi xảy ra:</strong>
          <ul class="mb-0 mt-2">
            <c:forEach var="error" items="${errors}">
              <li>${error}</li>
            </c:forEach>
          </ul>
        </div>
      </c:if>
      <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
      </c:if>

      <!-- Form đăng ký, gửi dữ liệu đến RegisterServlet -->
      <form
        action="${pageContext.request.contextPath}/RegisterServlet"
        method="post"
      >
        <!-- Nhập họ và tên -->
        <div class="mb-3">
          <input
            type="text"
            name="fullname"
            class="form-control"
            placeholder="Nhập họ và tên *"
            value="${fullname != null ? fullname : ''}"
            required
          />
        </div>

        <!-- Nhập số điện thoại -->
        <div class="mb-3">
          <input
            type="text"
            name="phone"
            class="form-control"
            placeholder="Nhập số điện thoại *"
            value="${phone != null ? phone : ''}"
            required
          />
        </div>

        <!-- Nhập email -->
        <div class="mb-3">
          <input
            type="email"
            name="email"
            class="form-control"
            placeholder="Nhập email*)"
            value="${email != null ? email : ''}"
          />
        </div>

        <!-- Chọn tỉnh/thành phố -->
        <div class="mb-3">
          <select name="city" class="form-select" required>
            <option value="" disabled ${city == null ? 'selected' : ''}>Tỉnh/ thành phố (*)</option>
            <option value="Hà Nội" ${city == 'Hà Nội' ? 'selected' : ''}>Hà Nội</option>
            <option value="Hồ Chí Minh" ${city == 'Hồ Chí Minh' ? 'selected' : ''}>Hồ Chí Minh</option>
            <option value="Đà Nẵng" ${city == 'Đà Nẵng' ? 'selected' : ''}>Đà Nẵng</option>
            <option value="Khác..." ${city == 'Khác...' ? 'selected' : ''}>Khác...</option>
          </select>
        </div>

        <!-- Username -->
        <div class="mb-3">
          <input
            type="text"
            name="username"
            class="form-control"
            placeholder="Tên đăng nhập *"
            value="${username != null ? username : ''}"
            required
          />
        </div>
        <!-- Nhập mật khẩu -->
        <div class="mb-3">
          <input
            type="password"
            name="password"
            class="form-control"
            placeholder="Nhập mật khẩu *"
            required
          />
        </div>

        <!-- Nhập lại mật khẩu để xác nhận -->
        <div class="mb-3">
          <input
            type="password"
            name="confirmPassword"
            class="form-control"
            placeholder="Nhập lại mật khẩu *"
            required
          />
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
        <hr class="flex-grow-1" />
        <span class="px-2 text-muted">Hoặc</span>
        <hr class="flex-grow-1" />
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
