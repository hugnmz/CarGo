<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="vi_VN" />
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Hợp đồng của tôi - CarGo</title>

  <!-- Bootstrap & Icons để đồng bộ với header/footer -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet" />
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet" />

  <!-- CSS riêng của trang -->
  <link href="${pageContext.request.contextPath}/css/customer/my-contracts.css" rel="stylesheet" />

  <style>
    html, body { height: 100%; }
    body {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      padding-top: 88px; /* tránh navbar fixed-top che nội dung; chỉnh theo chiều cao header thực tế */
      background-color: #f8f9fa;
      font-family: 'Inter', system-ui, -apple-system, Segoe UI, Roboto, 'Helvetica Neue', Arial, 'Noto Sans', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
    }
    main { flex: 1 0 auto; }
  </style>
</head>
<body>
  <%@ include file="/include/header.jsp" %>

  <main>
    <div class="page-header">
      <div class="container">
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <h1><i class="fas fa-file-contract me-2"></i>Hợp đồng của tôi</h1>
            <p class="mb-0 opacity-75">Quản lý tất cả các hợp đồng thuê xe</p>
          </div>
          <a class="btn btn-light" href="${pageContext.request.contextPath}/home">
            <i class="fas fa-home me-2"></i>Trang chủ
          </a>
        </div>
      </div>
    </div>

    <div class="container pb-5">
      <c:choose>
        <c:when test="${empty contracts}">
          <div class="empty-state">
            <i class="fas fa-inbox"></i>
            <h3>Chưa có hợp đồng nào</h3>
            <p>Bạn chưa tạo hợp đồng thuê xe nào. Hãy bắt đầu đặt xe ngay!</p>
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-view btn-lg">
              <i class="fas fa-car me-2"></i>Xem xe có sẵn
            </a>
          </div>
        </c:when>
        <c:otherwise>
          <div class="row">
            <c:forEach var="c" items="${contracts}">
              <div class="col-12">
                <div class="contract-card">
                  <div class="row align-items-center">
                    <div class="col-lg-6 mb-3 mb-lg-0">
                      <div class="d-flex align-items-start justify-content-between mb-2">
                        <div>
                          <span class="contract-id">#${c.contractId}</span>
                          <c:set var="statusClass"
                                 value="${c.status == 'PENDING' ? 'status-pending'
                                      : c.status == 'ACCEPTED' ? 'status-accepted'
                                      : c.status == 'IN_PROGRESS' ? 'status-in-progress'
                                      : c.status == 'COMPLETED' ? 'status-completed'
                                      : 'status-cancelled'}" />
                          <span class="status-badge ${statusClass} ms-2">${c.status}</span>
                        </div>
                      </div>
                      <div class="contract-date">
                        <i class="far fa-calendar-check"></i>
                        <span>${c.startDate.toLocalDate()}</span>
                      </div>
                      <div class="contract-date">
                        <i class="far fa-calendar-times"></i>
                        <span>${c.endDate.toLocalDate()}</span>
                      </div>
                    </div>
                    <div class="col-lg-3 mb-3 mb-lg-0">
                      <small class="text-muted d-block">Tổng tiền</small>
                      <div class="contract-amount">
                        <fmt:formatNumber value="${c.totalAmount}" pattern="#,###" /> VNĐ
                      </div>
                      <small class="text-muted">
                        Đặt cọc:
                        <fmt:formatNumber value="${c.depositAmount}" pattern="#,###" /> VNĐ
                      </small>
                    </div>
                    <div class="col-lg-3 text-lg-end">
                      <a class="btn-view" href="${pageContext.request.contextPath}/view-contract?contractId=${c.contractId}">
                        <i class="fas fa-eye me-2"></i>Xem chi tiết
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </main>

  <%@ include file="/include/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>