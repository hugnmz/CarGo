<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Hồ sơ Staff - CarGo</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/staff">🚗 Staff</a>
    <div>
      <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn btn-outline-light">Đăng xuất</a>
    </div>
  </div>
</nav>

<div class="container py-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">Hồ sơ cá nhân</h3>
    <span class="badge bg-primary">Vai trò: ${sessionScope.roleName}</span>
  </div>

  <c:if test="${not empty message}">
    <div class="alert alert-success">${message}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>
  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">${errorMessage}</div>
  </c:if>

  <div class="card shadow-sm p-3 mb-3">
    <div class="row">
      <div class="col-md-6">
        <div class="mb-2">
          <strong>Mã người dùng:</strong> ${user.userId}
        </div>
        <div class="mb-2">
          <strong>Username:</strong> ${user.username}
        </div>
        <div class="mb-2">
          <strong>Họ và tên:</strong> ${user.fullName}
        </div>
      </div>
      <div class="col-md-6">
        <div class="mb-2">
          <strong>Email:</strong> ${user.email}
        </div>
        <div class="mb-2">
          <strong>Số điện thoại:</strong> ${user.phone}
        </div>
        <div class="mb-2">
          <strong>Thành phố:</strong>
          <c:forEach var="loc" items="${locations}">
            <c:if test="${loc.locationId == user.locationId}">${loc.city}</c:if>
          </c:forEach>
        </div>
      </div>
    </div>
  </div>

  <div class="d-flex gap-2 mb-3">
    <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#editProfile">Chỉnh sửa thông tin</button>
    <button class="btn btn-warning" type="button" data-bs-toggle="modal" data-bs-target="#changePasswordModal">Đổi mật khẩu</button>
  </div>

  <div class="collapse" id="editProfile">
    <div class="card shadow-sm p-3">
      <form action="${pageContext.request.contextPath}/updateinfor" method="post">
        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label">Họ và tên</label>
            <input type="text" class="form-control" name="fullName" value="${user.fullName}" required>
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">Email</label>
            <input type="email" class="form-control" name="email" value="${user.email}">
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">Số điện thoại</label>
            <input type="text" class="form-control" name="phone" value="${user.phone}">
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">Thành phố</label>
            <select class="form-select" name="locationId">
              <option value="">-- Chọn thành phố --</option>
              <c:forEach var="l" items="${locations}">
                <option value="${l.locationId}" ${l.locationId == user.locationId ? 'selected' : ''}>${l.city}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <input type="hidden" name="userId" value="${user.userId}">
        <div class="text-end">
          <button class="btn btn-primary">Lưu thay đổi</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Modal đổi mật khẩu -->
<div class="modal fade" id="changePasswordModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <form class="modal-content" action="${pageContext.request.contextPath}/changepass" method="post">
      <div class="modal-header">
        <h5 class="modal-title">Đổi mật khẩu</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <c:if test="${not empty ok}">
          <div class="alert alert-success">${ok}</div>
        </c:if>
        <c:if test="${not empty errorMess}">
          <div class="alert alert-danger">${errorMess}</div>
        </c:if>

        <div class="mb-2">
          <label class="form-label">Mật khẩu cũ</label>
          <input type="password" class="form-control" name="oldPassword" required>
        </div>
        <div class="mb-2">
          <label class="form-label">Mật khẩu mới</label>
          <input type="password" class="form-control" name="newPassword" required>
        </div>
        <div class="mb-2">
          <label class="form-label">Nhập lại mật khẩu</label>
          <input type="password" class="form-control" name="reNewPassword" required>
        </div>
        <input type="hidden" name="userId" value="${user.userId}">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
        <button class="btn btn-primary">Đổi mật khẩu</button>
      </div>
    </form>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>