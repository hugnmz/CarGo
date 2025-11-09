<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="vi_VN" />

<c:set var="c" value="${sessionScope.c}" />
<c:set var="userType" value="${sessionScope.userType}" />
<c:set var="isCustomer" value="${userType == 'CUSTOMER'}" />

<!-- Lấy tất cả thông tin từ customer object -->
<c:set var="username" value="${c.username}" />
<c:set var="fullName" value="${c.fullName}" />
<c:set var="email" value="${c.email}" />
<c:set var="phone" value="${c.phone}" />
<c:set var="avatar" value="${not empty sessionScope.avatar ? sessionScope.avatar : 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'}" />
<c:set var="isLoggedIn" value="${not empty c}" />
<c:set var="listContract" value="${requestScope.listContract}" />

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Thông tin cá nhân - CarGo</title>

  <!-- Bootstrap & Icons để đồng bộ với header/footer -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

  <!-- CSS riêng của trang -->
  <link href="${pageContext.request.contextPath}/css/customer/profile.css" rel="stylesheet">

  <style>
    html, body { height: 100%; }
    body {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      padding-top: 88px; /* tránh navbar fixed-top che nội dung; chỉnh theo chiều cao header thực tế */
      background-color: #f8f9fa;
    }
    main { flex: 1 0 auto; }
  </style>
</head>
<body>
  <%@ include file="/include/header.jsp" %>

  <main>
    <!-- Check authorization -->
    <c:if test="${!isCustomer}">
      <div class="container mt-4">
        <div class="alert alert-warning text-center">
          <h4><i class="fa fa-exclamation-triangle"></i> Trang không phù hợp!</h4>
          <p>Trang này chỉ dành cho khách hàng.</p>
          <c:choose>
            <c:when test="${userType == 'MANAGER'}">
              <a href="${pageContext.request.contextPath}/manager/manager_home.jsp" class="btn btn-primary">Về trang quản lý</a>
            </c:when>
            <c:when test="${userType == 'STAFF'}">
              <a href="${pageContext.request.contextPath}/staff/staff.jsp" class="btn btn-primary">Về trang nhân viên</a>
            </c:when>
            <c:when test="${userType == 'ADMIN'}">
              <a href="${pageContext.request.contextPath}/admin/adminhome.jsp" class="btn btn-primary">Về trang admin</a>
            </c:when>
            <c:otherwise>
              <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Về trang chủ</a>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </c:if>

    <c:if test="${isCustomer}">
      <!-- Error Messages -->
      <c:if test="${not empty error}">
        <div class="container mt-4">
          <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fa fa-exclamation-triangle"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        </div>
      </c:if>

      <!-- Success Messages -->
      <c:if test="${not empty success}">
        <div class="container mt-4">
          <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fa fa-check-circle"></i> ${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        </div>
      </c:if>

      <!-- Main Content -->
      <div class="container mt-3">
        <div class="row">
          <!-- Sidebar -->
          <div class="col-md-3">
            <div class="profile-sidebar">
              <div class="profile-card text-center">
                <div class="profile-avatar">
                  <img src="${avatar}" alt="Avatar" class="avatar-img">
                  <button class="btn btn-sm btn-outline-primary avatar-edit-btn" data-bs-toggle="modal" data-bs-target="#avatarModal">
                    <i class="fa fa-camera"></i>
                  </button>
                </div>
                <h5 class="mt-3 mb-1">${fullName}</h5>
                <p class="text-muted mb-3">@${username}</p>
                <div class="profile-stats">
                  <div class="stat-item">
                    <span class="stat-number">12</span>
                    <span class="stat-label">Chuyến đi</span>
                  </div>
                  <div class="stat-item">
                    <span class="stat-number">4.8</span>
                    <span class="stat-label">Đánh giá</span>
                  </div>
                </div>
              </div>

              <!-- Navigation Menu -->
              <div class="profile-menu">
                <ul class="nav flex-column">
                  <li class="nav-item">
                    <a class="nav-link active" href="#profile-info" data-bs-toggle="tab">
                      <i class="fa fa-user"></i> Thông tin cá nhân
                    </a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#booking-history" data-bs-toggle="tab">
                      <i class="fa fa-history"></i> Lịch sử đặt xe
                    </a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#favorites" data-bs-toggle="tab">
                      <i class="fa fa-heart"></i> Xe yêu thích
                    </a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#settings" data-bs-toggle="tab">
                      <i class="fa fa-cog"></i> Cài đặt
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <!-- Main Content Area -->
          <div class="col-md-9">
            <div class="tab-content">
              <!-- Profile Information Tab -->
              <div class="tab-pane fade show active" id="profile-info">
                <div class="profile-content">
                  <div class="content-header">
                    <h4><i class="fa fa-user text-primary"></i> Thông tin cá nhân</h4>
                    <small class="text-muted">Chỉnh sửa thông tin và nhấn "Lưu thay đổi" để cập nhật</small>
                  </div>

                  <!-- Success message from URL parameter -->
                  <c:if test="${not empty param.success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                      <i class="fa fa-check-circle"></i>
                      <c:choose>
                        <c:when test="${param.success == '1'}">
                          Cập nhật thông tin thành công!
                        </c:when>
                        <c:otherwise>
                          Thao tác thành công!
                        </c:otherwise>
                      </c:choose>
                      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                  </c:if>

                  <!-- Error list -->
                  <c:if test="${not empty errors}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                      <i class="fa fa-exclamation-circle"></i>
                      <strong>Có lỗi xảy ra:</strong>
                      <ul class="mb-0 mt-2">
                        <c:forEach var="error" items="${errors}">
                          <li>${error}</li>
                        </c:forEach>
                      </ul>
                      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                  </c:if>

                  <form action="${pageContext.request.contextPath}/CustomerServlet" method="POST" id="profileForm">
                    <input type="hidden" name="_back" value="/customer/profile.jsp" />
                    <input type="hidden" name="isVerified" value="${not empty c.isVerified ? c.isVerified : 'true'}">

                    <div class="profile-details">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="info-item">
                            <label for="fullName"><i class="fa fa-user text-muted"></i> Họ và tên</label>
                            <input type="text" class="form-control" id="fullName" name="fullName"
                                   value="${c.fullName}" placeholder="Nhập họ và tên" required>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="info-item">
                            <label for="email"><i class="fa fa-envelope text-muted"></i> Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="${c.email}" placeholder="Nhập email" required>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="info-item">
                            <label for="phone"><i class="fa fa-phone text-muted"></i> Số điện thoại</label>
                            <input type="tel" class="form-control" id="phone" name="phone"
                                   value="${c.phone}" placeholder="Nhập số điện thoại" required>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="info-item">
                            <label for="joinDate"><i class="fa fa-calendar text-muted"></i> Ngày tham gia</label>
                            <input type="date" class="form-control" id="joinDate" name="joinDate"
                                   value="${c.createAt.toLocalDate()}" readonly>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="info-item">
                            <label for="city"><i class="fa fa-map-marker-alt text-muted"></i> Thành phố</label>
                            <select class="form-control" id="city" name="city" required>
                              <option value="">-- Chọn thành phố --</option>
                              <c:forEach var="location" items="${applicationScope.locations}">
                                <option value="${location.city}" ${c.city == location.city ? 'selected' : ''}>
                                  ${location.city}
                                </option>
                              </c:forEach>
                            </select>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="info-item">
                            <label for="dateOfBirth"><i class="fa fa-birthday-cake text-muted"></i> Ngày sinh</label>
                            <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth"
                                   value="${c.dateOfBirth}">
                          </div>
                        </div>
                      </div>

                      <!-- Hidden fields for additional customer data -->
                      <input type="hidden" name="customerId" value="${c.customerId}">
                      <input type="hidden" name="username" value="${c.username}">

                      <!-- Form buttons -->
                      <div class="form-actions mt-4">
                        <button type="submit" class="btn btn-success">
                          <i class="fa fa-save"></i> Lưu thay đổi
                        </button>
                      </div>
                    </div>
                  </form>

                  <!-- Recent Activity -->
                  <div class="recent-activity mt-4">
                    <h5><i class="fa fa-clock text-primary"></i> Hoạt động gần đây</h5>
                    <div class="activity-list">
                      <div class="activity-item">
                        <div class="activity-icon"><i class="fa fa-car text-success"></i></div>
                        <div class="activity-content">
                          <p class="mb-1">Đặt xe Toyota Camry 2024</p>
                          <small class="text-muted">2 ngày trước</small>
                        </div>
                      </div>
                      <div class="activity-item">
                        <div class="activity-icon"><i class="fa fa-star text-warning"></i></div>
                        <div class="activity-content">
                          <p class="mb-1">Đánh giá chuyến đi</p>
                          <small class="text-muted">1 tuần trước</small>
                        </div>
                      </div>
                      <div class="activity-item">
                        <div class="activity-icon"><i class="fa fa-heart text-danger"></i></div>
                        <div class="activity-content">
                          <p class="mb-1">Thêm xe vào yêu thích</p>
                          <small class="text-muted">2 tuần trước</small>
                        </div>
                      </div>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Booking History Tab -->
              <div class="tab-pane fade" id="booking-history">
                <div class="profile-content">
                  <div class="content-header">
                    <h4><i class="fa fa-history text-primary"></i> Lịch sử đặt xe</h4>
                  </div>

                  <div class="booking-list">
                    <form>
                      <c:choose>
                        <c:when test="${not empty listContract}">
                          <c:forEach var="contract" items="${listContract}">
                            <div class="booking-item">
                              <div class="booking-image">
                                <img src="https://via.placeholder.com/100x80?text=Car" alt="Car">
                              </div>
                              <div class="booking-details">
                                <h6>Hợp đồng #${contract.contractId}</h6>
                                <p class="text-muted mb-1">Từ ${contract.startDate} đến ${contract.endDate}</p>
                                <p class="text-muted mb-1">Tạo lúc: ${contract.createAt}</p>
                                <span class="badge ${contract.status == 'ACCEPTED' ? 'bg-success' : contract.status == 'PENDING' ? 'bg-warning' : 'bg-danger'}">
                                  ${contract.status == 'ACCEPTED' ? 'ACCEPTED' : contract.status == 'PENDING' ? 'PENDING' : 'REJECTED'}
                                </span>
                              </div>
                              <div class="booking-price">
                                <h6 class="text-success"><fmt:formatNumber value="${contract.totalAmount}" pattern="#,###" /> VNĐ</h6>
                                <p class="text-muted small">Cọc: <fmt:formatNumber value="${contract.depositAmount}" pattern="#,###" /> VNĐ</p>
                                <a href="${pageContext.request.contextPath}/view-contract?contractId=${contract.contractId}" class="btn btn-sm btn-outline-primary">Chi tiết</a>
                              </div>
                            </div>
                          </c:forEach>
                        </c:when>
                        <c:otherwise>
                          <div class="text-center py-4">
                            <i class="fa fa-car fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">Chưa có lịch sử đặt xe</h5>
                            <p class="text-muted">Bạn chưa có hợp đồng thuê xe nào</p>
                            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                              <i class="fa fa-search"></i> Tìm xe ngay
                            </a>
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </form>
                  </div>
                </div>
              </div>

              <!-- Favorites Tab -->
              <div class="tab-pane fade" id="favorites">
                <div class="profile-content">
                  <div class="content-header">
                    <h4><i class="fa fa-heart text-primary"></i> Xe yêu thích</h4>
                  </div>

                  <div class="row">
                    <div class="col-md-6 mb-3">
                      <div class="favorite-card">
                        <img src="https://via.placeholder.com/300x200?text=BMW+X5" alt="Car">
                        <div class="favorite-content">
                          <h6>BMW X5 2024</h6>
                          <p class="text-muted">SUV - 7 chỗ</p>
                          <div class="d-flex justify-content-between align-items-center">
                            <span class="text-success fw-bold">2.500.000 VNĐ/ngày</span>
                            <button class="btn btn-sm btn-outline-danger"><i class="fa fa-heart"></i></button>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-6 mb-3">
                      <div class="favorite-card">
                        <img src="https://via.placeholder.com/300x200?text=Mercedes+C-Class" alt="Car">
                        <div class="favorite-content">
                          <h6>Mercedes C-Class 2024</h6>
                          <p class="text-muted">Sedan - 5 chỗ</p>
                          <div class="d-flex justify-content-between align-items-center">
                            <span class="text-success fw-bold">1.800.000 VNĐ/ngày</span>
                            <button class="btn btn-sm btn-outline-danger"><i class="fa fa-heart"></i></button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Settings Tab -->
              <div class="tab-pane fade" id="settings">
                <div class="profile-content">
                  <div class="content-header">
                    <h4><i class="fa fa-cog text-primary"></i> Cài đặt tài khoản</h4>
                  </div>

                  <!-- Kết quả đổi password -->
                  <c:if test="${not empty ok}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                      <i class="fa fa-check-circle"></i> ${ok}
                      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                  </c:if>

                  <c:if test="${not empty errorMess}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                      <i class="fa fa-exclamation-circle"></i> ${errorMess}
                      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                  </c:if>

                  <div class="settings-section">
                    <h6>Bảo mật</h6>
                    <form method="POST" action="${pageContext.request.contextPath}/ChangePasswordServlet">
                      <input type="hidden" name="_back" value="/customer/profile.jsp" />
                      <div class="setting-item">
                        <div class="row">
                          <div class="col-md-4">
                            <div class="mb-3">
                              <label for="oldPassword" class="form-label">Mật khẩu cũ</label>
                              <input type="password" class="form-control" id="oldPassword" name="old" placeholder="Nhập mật khẩu cũ" required>
                            </div>
                          </div>
                          <div class="col-md-4">
                            <div class="mb-3">
                              <label for="newPassword" class="form-label">Mật khẩu mới</label>
                              <input type="password" class="form-control" id="newPassword" name="new" placeholder="Nhập mật khẩu mới" required>
                            </div>
                          </div>
                          <div class="col-md-4">
                            <div class="mb-3">
                              <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
                              <input type="password" class="form-control" id="confirmPassword" name="confirm" placeholder="Nhập lại mật khẩu mới" required>
                            </div>
                          </div>
                        </div>

                        <!-- Hidden field -->
                        <input type="hidden" name="customerId" value="${c.customerId}">

                        <div class="mb-3">
                          <p class="text-muted">Cập nhật mật khẩu để bảo vệ tài khoản</p>
                        </div>

                        <button type="submit" class="btn btn-outline-primary">
                          <i class="fa fa-key"></i> Đổi mật khẩu
                        </button>
                      </div>
                    </form>

                    <div class="setting-item">
                      <div>
                        <h6>Xác thực 2 bước</h6>
                        <p class="text-muted">Thêm lớp bảo mật cho tài khoản</p>
                      </div>
                      <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" id="twoFactor">
                        <label class="form-check-label" for="twoFactor"></label>
                      </div>
                    </div>
                  </div>

                  <div class="settings-section">
                    <h6>Thông báo</h6>
                    <div class="setting-item">
                      <div>
                        <h6>Email thông báo</h6>
                        <p class="text-muted">Nhận thông báo qua email</p>
                      </div>
                      <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" id="emailNotif" checked>
                        <label class="form-check-label" for="emailNotif"></label>
                      </div>
                    </div>

                    <div class="setting-item">
                      <div>
                        <h6>SMS thông báo</h6>
                        <p class="text-muted">Nhận thông báo qua SMS</p>
                      </div>
                      <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" id="smsNotif">
                        <label class="form-check-label" for="smsNotif"></label>
                      </div>
                    </div>
                  </div>

                  <div class="settings-section">
                    <h6>Quyền riêng tư</h6>
                    <div class="setting-item">
                      <div>
                        <h6>Hiển thị thông tin công khai</h6>
                        <p class="text-muted">Cho phép người khác xem thông tin cơ bản</p>
                      </div>
                      <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" id="publicInfo">
                        <label class="form-check-label" for="publicInfo"></label>
                      </div>
                    </div>
                  </div>

                </div>
              </div>
            </div><!-- /.tab-content -->
          </div><!-- /.col-md-9 -->
        </div><!-- /.row -->
      </div><!-- /.container -->
    </c:if>
  </main>

  <%@ include file="/include/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>