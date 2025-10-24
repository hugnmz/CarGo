<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Hợp Đồng</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Bootstrap & Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
  <link rel="stylesheet" href="CSS/listcontract.css">
</head>
<body>

  <!-- Sidebar -->
  <aside class="sidebar">
    <a href="#" class="brand">
      <i class="fa-solid fa-car-side fs-4"></i>
      <span>Car Rental</span>
    </a>
    <nav class="nav flex-column px-2">
      <a class="nav-link" href="staff_returncar.jsp"><i class="fa-solid fa-house"></i> Trang chủ</a>
      <a class="nav-link" href="#"><i class="fa-solid fa-users"></i> Quản lý khách hàng</a>
      <a class="nav-link" href="#"><i class="fa-solid fa-car-side"></i> Quản lý xe</a>
      <a class="nav-link active" href="contract_list.jsp"><i class="fa-solid fa-file-contract"></i> Hợp đồng</a>
      <a class="nav-link" href="${pageContext.request.contextPath}/LogoutServlet"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
    </nav>
  </aside>

  <!-- Main -->
  <main class="content">
    <div class="d-flex align-items-center justify-content-between mb-3">
      <h1 class="h3 mb-0">Danh Sách Hợp Đồng</h1>
      <a class="btn btn-primary d-none d-sm-inline-flex" href="#">
        <i class="fa-solid fa-plus me-2"></i> Tạo hợp đồng
      </a>
    </div>

    <!-- Card -->
    <div class="card">
      <div class="card-header">
        <form class="row g-2 align-items-center">
          <div class="col-12 col-md-6">
            <div class="input-group">
              <span class="input-group-text bg-white"><i class="fa-solid fa-magnifying-glass"></i></span>
              <input type="text" name="search" value="${param.search}" class="form-control" placeholder="Tìm mã hợp đồng">
            </div>
          </div>
          <div class="col-6 col-md-3">
            <select class="form-select" name="status">
              <option value="">Tất cả trạng thái</option>
              <option ${param.status=='pending' ? 'selected' : ''} value="pending">Chờ duyệt</option>
              <option ${param.status=='accepted' ? 'selected' : ''} value="accepted">Đã duyệt</option>
              <option ${param.status=='rejected' ? 'selected' : ''} value="rejected">Từ chối</option>
            </select>
          </div>
          <div class="col-6 col-md-3 d-grid d-md-flex gap-2">
            <button type="submit" class="btn btn-primary"><i class="fa-solid fa-search me-2"></i>Tìm kiếm</button>
            <a href="contract_list.jsp" class="btn btn-outline-secondary"><i class="fa-solid fa-rotate-left me-2"></i>Reset</a>
          </div>
        </form>
      </div>

      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover table-striped mb-0 align-middle">
            <thead>
              <tr>
                <th class="text-center" style="width:70px">#</th>
                <th>Thời gian tạo</th>
                <th>Mã HĐ</th>
                <th>Loại thanh toán</th>
                <th>Khách hàng</th>
                <th>Xe thuê</th>
                <th>Biển số</th>
                <th>Ngày bắt đầu</th>
                <th>Ngày kết thúc</th>
                <th class="text-center">Trạng thái</th>
                <th class="text-end" style="width:120px">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <c:if test="${empty list}">
                <tr>
                  <td colspan="11" class="text-center py-5 text-muted">
                    <i class="fa-regular fa-folder-open me-2"></i>Không có hợp đồng nào.
                  </td>
                </tr>
              </c:if>

              <!-- Lặp dữ liệu -->
              <c:forEach var="item" items="${list}" varStatus="st">
                <tr>
                  <td class="text-center">${st.index + 1}</td>
                  <td><span class="text-muted"><i class="fa-regular fa-clock me-1"></i>${item.createAt}</span></td>
                  <td><span class="fw-semibold">${item.contractId}</span></td>
                  <td>${item.payments != null && !item.payments.isEmpty() ? item.payments.get(0).method : '-'}</td>
                  <td>
                    <div class="d-flex align-items-center gap-2">
                      <i class="fa-solid fa-user text-secondary"></i>
                      <div>
                        <div class="fw-semibold">${item.customerName}</div>
                        <small class="text-muted">${item.customerPhone}</small>
                      </div>
                    </div>
                  </td>
                  <td>${item.contractDetails != null && !item.contractDetails.isEmpty() ? item.contractDetails.get(0).carName : '-'}</td>
                  <td>${item.contractDetails != null && !item.contractDetails.isEmpty() ? item.contractDetails.get(0).plateNumber : '-'}</td>
                  <td>${item.startDate}</td>
                  <td>${item.endDate}</td>
                  <td class="text-center">
                    <c:choose>
                      <c:when test="${item.status == 'accepted'}">
                        <span class="badge badge-status status-accepted">Đã duyệt</span>
                      </c:when>
                      <c:when test="${item.status == 'rejected'}">
                        <span class="badge badge-status status-rejected">Từ chối</span>
                      </c:when>
                      <c:otherwise>
                        <span class="badge badge-status status-pending">Chờ duyệt</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td class="text-end">
                    <div class="btn-group">
                      <a class="btn btn-sm btn-outline-primary" href="contract_detail.jsp?id=${item.contractId}">
                        <i class="fa-regular fa-eye"></i>
                      </a>
                      <a class="btn btn-sm btn-outline-secondary" href="contract_edit.jsp?id=${item.contractId}">
                        <i class="fa-regular fa-pen-to-square"></i>
                      </a>
                      <a class="btn btn-sm btn-outline-danger" href="ContractDeleteServlet?id=${item.contractId}" onclick="return confirm('Xoá hợp đồng này?');">
                        <i class="fa-regular fa-trash-can"></i>
                      </a>
                    </div>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card-footer d-flex flex-wrap justify-content-between align-items-center gap-2">
        <div class="text-muted">
          <i class="fa-regular fa-circle-check me-1"></i>
          Tổng: <strong>${list != null ? list.size() : 0}</strong> hợp đồng
        </div>
        
      </div>
    </div>
  </main>

  <!-- JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
