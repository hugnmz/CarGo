<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />

    <meta charset="UTF-8" />
    <title>Car Rental Login</title>
    <!-- Link Bootstrap -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="${pageContext.request.contextPath}/css/auth/login.css"
      rel="stylesheet"
    />
  </head>
  <body class="d-flex justify-content-center align-items-center">
    <!-- Card login -->
    <div class="card shadow-lg p-4 text-center fade-in login-card">
      <!-- Logo -->
      <img
        src="https://img.freepik.com/vector-cao-cap/hinh-minh-hoa-vector-logo-xe-hoi-nen-trang_917213-258381.jpg"
        alt="Car Logo"
        class="mx-auto d-block rounded-circle mb-3 login-logo"
      />

      <!-- Tiêu đề -->
      <h2 class="mb-4 text-dark">Car Rental Login</h2>

      <%@ taglib prefix="c" uri="jakarta.tags.core" %> <%@ taglib prefix="fmt"
      uri="jakarta.tags.fmt" %>

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

      <!-- Form đăng nhập -->
      <form
        action="${pageContext.request.contextPath}/LoginServlet"
        method="post"
      >
        <!-- Hidden field để filter biết trang quay lại -->
        <input type="hidden" name="_back" value="/auth/login.jsp" />
        <!-- Username -->
        <div class="mb-3">
          <input
            type="text"
            name="username"
            class="form-control"
            placeholder="Tên đăng nhập"
            value="${username != null ? username : ''}"
            required
          />
        </div>
        <!-- Password -->
        <div class="mb-3">
          <input
            type="password"
            name="password"
            class="form-control"
            placeholder="Mật khẩu"
            required
          />
        </div>

        <!-- Remember Me -->
        <div class="mb-3 form-check text-start">
          <input
            type="checkbox"
            name="rememberMe"
            class="form-check-input"
            id="rememberMe"
          />
          <label class="form-check-label" for="rememberMe">
            Ghi nhớ đăng nhập
          </label>
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
        <a href="${pageContext.request.contextPath}/ForgotPasswordServlet"
          >Quên mật khẩu?</a
        >
        <a href="${pageContext.request.contextPath}/RegisterServlet"
          >Đăng ký tài khoản</a
        >
      </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
