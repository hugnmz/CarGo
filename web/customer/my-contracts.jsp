<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Hợp đồng của tôi - CarGo</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
  <style>
    :root {
      --primary: #10b981;
      --primary-dark: #059669;
      --dark: #1f2937;
      --gray: #6b7280;
      --light-gray: #f3f4f6;
    }
    body { font-family: 'Inter', sans-serif; background: var(--light-gray); color: var(--dark); }
    
    .page-header {
      background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
      color: white;
      padding: 3rem 0 2rem;
      margin-bottom: 2rem;
    }
    .page-header h1 {
      font-size: 2rem;
      font-weight: 800;
      margin-bottom: 0.5rem;
    }
    
    .contract-card {
      background: white;
      border-radius: 16px;
      padding: 1.5rem;
      margin-bottom: 1.5rem;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
      transition: all 0.3s;
      border: 2px solid transparent;
    }
    .contract-card:hover {
      box-shadow: 0 8px 24px rgba(0,0,0,0.12);
      border-color: var(--primary);
      transform: translateY(-2px);
    }
    
    .contract-id {
      font-size: 1.25rem;
      font-weight: 700;
      color: var(--dark);
    }
    
    .status-badge {
      padding: 0.4rem 1rem;
      border-radius: 20px;
      font-size: 0.875rem;
      font-weight: 600;
      display: inline-block;
    }
    .status-pending { background: #fef3c7; color: #92400e; }
    .status-accepted { background: #dbeafe; color: #1e40af; }
    .status-in-progress { background: #e0e7ff; color: #4338ca; }
    .status-completed { background: #d1fae5; color: #065f46; }
    .status-cancelled { background: #fee2e2; color: #991b1b; }
    
    .contract-date {
      display: flex;
      align-items: center;
      color: var(--gray);
      font-size: 0.95rem;
      margin-bottom: 0.5rem;
    }
    .contract-date i {
      margin-right: 0.5rem;
      color: var(--primary);
    }
    
    .contract-amount {
      font-size: 1.5rem;
      font-weight: 700;
      color: var(--primary);
    }
    
    .btn-view {
      background: var(--primary);
      color: white;
      padding: 0.6rem 1.5rem;
      border-radius: 10px;
      font-weight: 600;
      border: none;
      transition: all 0.3s;
      text-decoration: none;
      display: inline-block;
    }
    .btn-view:hover {
      background: var(--primary-dark);
      color: white;
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(16,185,129,0.3);
    }
    
    .empty-state {
      text-align: center;
      padding: 4rem 2rem;
      background: white;
      border-radius: 16px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    }
    .empty-state i {
      font-size: 4rem;
      color: var(--gray);
      margin-bottom: 1.5rem;
    }
    .empty-state h3 {
      color: var(--dark);
      margin-bottom: 1rem;
    }
    .empty-state p {
      color: var(--gray);
      margin-bottom: 2rem;
    }
    
    @media (max-width: 768px) {
      .contract-card { padding: 1rem; }
      .page-header h1 { font-size: 1.5rem; }
    }
  </style>
</head>
<body>
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
                        <c:set var="statusClass" value="${c.status == 'PENDING' ? 'status-pending' : c.status == 'ACCEPTED' ? 'status-accepted' : c.status == 'IN_PROGRESS' ? 'status-in-progress' : c.status == 'COMPLETED' ? 'status-completed' : 'status-cancelled'}" />
                        <span class="status-badge ${statusClass} ms-2">${c.status}</span>
                      </div>
                    </div>
                    <div class="contract-date">
                      <i class="far fa-calendar-check"></i>
                      <span>${c.startDate}</span>
                    </div>
                    <div class="contract-date">
                      <i class="far fa-calendar-times"></i>
                      <span>${c.endDate}</span>
                    </div>
                  </div>
                  <div class="col-lg-3 mb-3 mb-lg-0">
                    <small class="text-muted d-block">Tổng tiền</small>
                    <div class="contract-amount">${c.totalAmount} VNĐ</div>
                    <small class="text-muted">Đặt cọc: ${c.depositAmount} VNĐ</small>
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

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
