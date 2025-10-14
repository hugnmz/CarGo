<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Admin Login</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <style>
      body {
        background: linear-gradient(135deg, #2b5876, #4e4376);
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .login-card {
        background: #fff;
        border-radius: 12px;
        padding: 40px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
        width: 100%;
        max-width: 400px;
      }
    </style>
  </head>
  <body>
    <div class="login-card text-center">
      <h3 class="mb-4 text-dark">Admin Login</h3>

      <%@ taglib prefix="c" uri="jakarta.tags.core" %>
      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>

      <form
        action="${pageContext.request.contextPath}/LoginAdmin"
        method="post"
      >
        <div class="mb-3">
          <input
            type="text"
            name="username"
            class="form-control"
            placeholder="Tên đăng nhập"
            required
          />
        </div>

        <div class="mb-3">
          <input
            type="password"
            name="password"
            class="form-control"
            placeholder="Mật khẩu"
            required
          />
        </div>

        <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
      </form>

      <hr class="my-4" />
      <p class="text-muted mb-0">Dành riêng cho quản trị viên</p>
    </div>
  </body>
</html>
