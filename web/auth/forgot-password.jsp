<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <meta charset="UTF-8" />
    <title>Quên mật khẩu - CarGo</title>
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
    <!-- Card forgot password -->
    <div class="card shadow-lg p-4 text-center fade-in login-card">
      <!-- Logo -->
      <img
        src="https://img.freepik.com/vector-cao-cap/hinh-minh-hoa-vector-logo-xe-hoi-nen-trang_917213-258381.jpg"
        alt="Car Logo"
        class="mx-auto d-block rounded-circle mb-3 login-logo"
      />

      <!-- Tiêu đề -->
      <h2 class="mb-4 text-dark">Quên mật khẩu</h2>
      <p class="text-muted mb-4">
        Vui lòng nhập tên đăng nhập và email để nhận mã đặt lại mật khẩu
      </p>

      <%@ taglib prefix="c" uri="jakarta.tags.core" %>

      <!-- Hiển thị lỗi từ Filter (errors - List) -->
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

      <!-- Hiển thị lỗi từ Servlet (error - String) -->
      <c:if test="${not empty error}">
        <div class="alert alert-danger">
          <i class="fas fa-exclamation-triangle me-2"></i>
          ${error}
        </div>
      </c:if>

      <c:if test="${not empty success}">
        <div class="alert alert-success">
          <i class="fas fa-check-circle me-2"></i>
          ${success}
        </div>
      </c:if>

      <!-- Form quên mật khẩu -->
      <form
        action="${pageContext.request.contextPath}/ForgotPasswordServlet"
        method="post"
      >
        <!-- Hidden field để filter biết trang quay lại -->
        <input type="hidden" name="_back" value="/auth/forgot-password.jsp" />
        <!-- Username -->
        <div class="mb-3">
          <div class="input-group">
            <span class="input-group-text">
              <i class="fas fa-user"></i>
            </span>
            <input
              type="text"
              name="username"
              class="form-control"
              placeholder="Tên đăng nhập"
              value="${username != null ? username : ''}"
              required
            />
          </div>
        </div>

        <!-- Email -->
        <div class="mb-3">
          <div class="input-group">
            <span class="input-group-text">
              <i class="fas fa-envelope"></i>
            </span>
            <input
              type="email"
              name="email"
              class="form-control"
              placeholder="Email"
              value="${email != null ? email : ''}"
              required
            />
          </div>
        </div>

        <!-- Submit -->
        <button type="submit" class="btn btn-primary w-100 mb-3">
          <i class="fas fa-paper-plane me-2"></i>Gửi mã xác nhận
        </button>
      </form>

      <!-- Liên kết -->
      <div class="text-center mt-3">
        <a
          href="${pageContext.request.contextPath}/auth/login.jsp"
          class="text-decoration-none"
        >
          <i class="fas fa-arrow-left me-1"></i>Quay lại đăng nhập
        </a>
      </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
