<%-- Document : staff Created on : 23 thg 10, 2025, 11:31:55 Author : HOANGNAM
--%> <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Staff Dashboard - CarGo</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
      rel="stylesheet"
    />
    <link
      href="${pageContext.request.contextPath}/css/staff/staff.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark">
      <div class="container">
        <a class="navbar-brand" href="#">
          <i class="fas fa-car"></i> CarGo Staff Dashboard
        </a>
        <div class="navbar-nav ms-auto">
          <span class="navbar-text me-3">
            <i class="fas fa-user"></i> Staff ID: 001
          </span>
          <a class="btn btn-outline-light" href="#">
            <i class="fas fa-sign-out-alt"></i> Logout
          </a>
        </div>
      </div>
    </nav>

    <div class="container mt-4">
      <!-- Header -->
      <div class="staff-header text-center">
        <h1><i class="fas fa-tachometer-alt"></i> Staff Dashboard</h1>
        <p class="mb-0">Xem và xử lý hợp đồng từ khách hàng</p>
      </div>

      <!-- Statistics -->
      <div class="row mb-4">
        <div class="col-md-3">
          <div class="stats-card">
            <i class="fas fa-file-contract fa-2x text-primary mb-2"></i>
            <div class="stats-number">5</div>
            <div class="text-muted">Tổng hợp đồng</div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="stats-card">
            <i class="fas fa-clock fa-2x text-warning mb-2"></i>
            <div class="stats-number">2</div>
            <div class="text-muted">Chờ xử lý</div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="stats-card">
            <i class="fas fa-check-circle fa-2x text-success mb-2"></i>
            <div class="stats-number">3</div>
            <div class="text-muted">Đã chấp nhận</div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="stats-card">
            <i class="fas fa-dollar-sign fa-2x text-info mb-2"></i>
            <div class="stats-number">$15,000</div>
            <div class="text-muted">Tổng giá trị</div>
          </div>
        </div>
      </div>

      <!-- Filter Tabs -->
      <div class="filter-tabs">
        <h5 class="mb-3"><i class="fas fa-filter"></i> Lọc hợp đồng</h5>
        <button class="filter-tab active">
          <i class="fas fa-list"></i> Tất cả
        </button>
        <button class="filter-tab">
          <i class="fas fa-clock"></i> Chờ xử lý
        </button>
        <button class="filter-tab">
          <i class="fas fa-check-circle"></i> Đã chấp nhận
        </button>
        <button class="filter-tab">
          <i class="fas fa-times-circle"></i> Đã từ chối
        </button>
      </div>

      <!-- Contract List -->
      <div class="row">
        <div class="col-12">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h3>
              <i class="fas fa-list"></i> Danh sách hợp đồng từ khách hàng
            </h3>
            <button class="btn btn-primary btn-action">
              <i class="fas fa-sync-alt"></i> Làm mới
            </button>
          </div>
        </div>
      </div>

      <!-- Sample Contract 1 -->
      <div class="contract-card">
        <div class="card-body">
          <div class="row align-items-center">
            <div class="col-md-2">
              <div class="text-center">
                <i class="fas fa-file-contract fa-3x text-primary mb-2"></i>
                <div class="fw-bold">#001</div>
              </div>
            </div>
            <div class="col-md-3">
              <h6 class="mb-1">Khách hàng</h6>
              <div class="fw-bold">Nguyễn Văn A</div>
              <small class="text-muted">
                <i class="fas fa-phone"></i> 0123456789
              </small>
            </div>
            <div class="col-md-2">
              <h6 class="mb-1">Thời gian</h6>
              <div class="small">
                <i class="fas fa-calendar-start"></i> 01/01/2025 08:00<br />
                <i class="fas fa-calendar-end"></i> 05/01/2025 18:00
              </div>
            </div>
            <div class="col-md-2">
              <h6 class="mb-1">Số tiền</h6>
              <div class="fw-bold text-success">
                <i class="fas fa-dollar-sign"></i> $500
              </div>
              <small class="text-muted">Cọc: $100</small>
            </div>
            <div class="col-md-1">
              <h6 class="mb-1">Trạng thái</h6>
              <span class="status-badge status-pending">Chờ xử lý</span>
            </div>
            <div class="col-md-2">
              <div class="btn-group-vertical w-100" role="group">
                <button
                  type="button"
                  class="btn btn-view btn-action btn-sm mb-1"
                >
                  <i class="fas fa-eye"></i> Xem chi tiết
                </button>
                <div class="btn-group" role="group">
                  <button type="button" class="btn btn-success btn-sm">
                    <i class="fas fa-check"></i>
                  </button>
                  <button type="button" class="btn btn-danger btn-sm">
                    <i class="fas fa-times"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Sample Contract 2 -->
      <div class="contract-card">
        <div class="card-body">
          <div class="row align-items-center">
            <div class="col-md-2">
              <div class="text-center">
                <i class="fas fa-file-contract fa-3x text-primary mb-2"></i>
                <div class="fw-bold">#002</div>
              </div>
            </div>
            <div class="col-md-3">
              <h6 class="mb-1">Khách hàng</h6>
              <div class="fw-bold">Trần Thị B</div>
              <small class="text-muted">
                <i class="fas fa-phone"></i> 0987654321
              </small>
            </div>
            <div class="col-md-2">
              <h6 class="mb-1">Thời gian</h6>
              <div class="small">
                <i class="fas fa-calendar-start"></i> 10/01/2025 09:00<br />
                <i class="fas fa-calendar-end"></i> 15/01/2025 17:00
              </div>
            </div>
            <div class="col-md-2">
              <h6 class="mb-1">Số tiền</h6>
              <div class="fw-bold text-success">
                <i class="fas fa-dollar-sign"></i> $800
              </div>
              <small class="text-muted">Cọc: $160</small>
            </div>
            <div class="col-md-1">
              <h6 class="mb-1">Trạng thái</h6>
              <span class="status-badge status-accepted">Đã chấp nhận</span>
            </div>
            <div class="col-md-2">
              <div class="btn-group-vertical w-100" role="group">
                <button
                  type="button"
                  class="btn btn-view btn-action btn-sm mb-1"
                >
                  <i class="fas fa-eye"></i> Xem chi tiết
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Sample Contract 3 -->
      <div class="contract-card">
        <div class="card-body">
          <div class="row align-items-center">
            <div class="col-md-2">
              <div class="text-center">
                <i class="fas fa-file-contract fa-3x text-primary mb-2"></i>
                <div class="fw-bold">#003</div>
              </div>
            </div>
            <div class="col-md-3">
              <h6 class="mb-1">Khách hàng</h6>
              <div class="fw-bold">Lê Văn C</div>
              <small class="text-muted">
                <i class="fas fa-phone"></i> 0369852147
              </small>
            </div>
            <div class="col-md-2">
              <h6 class="mb-1">Thời gian</h6>
              <div class="small">
                <i class="fas fa-calendar-start"></i> 20/01/2025 10:00<br />
                <i class="fas fa-calendar-end"></i> 25/01/2025 16:00
              </div>
            </div>
            <div class="col-md-2">
              <h6 class="mb-1">Số tiền</h6>
              <div class="fw-bold text-success">
                <i class="fas fa-dollar-sign"></i> $1200
              </div>
              <small class="text-muted">Cọc: $240</small>
            </div>
            <div class="col-md-1">
              <h6 class="mb-1">Trạng thái</h6>
              <span class="status-badge status-pending">Chờ xử lý</span>
            </div>
            <div class="col-md-2">
              <div class="btn-group-vertical w-100" role="group">
                <button
                  type="button"
                  class="btn btn-view btn-action btn-sm mb-1"
                >
                  <i class="fas fa-eye"></i> Xem chi tiết
                </button>
                <div class="btn-group" role="group">
                  <button type="button" class="btn btn-success btn-sm">
                    <i class="fas fa-check"></i>
                  </button>
                  <button type="button" class="btn btn-danger btn-sm">
                    <i class="fas fa-times"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
