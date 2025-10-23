<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đặt xe thành công - CarGo</title>
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
        
        .success-header {
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
            color: white;
            padding: 3rem 0 2rem;
            text-align: center;
            margin-bottom: 2rem;
        }
        .success-icon {
            width: 80px;
            height: 80px;
            background: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1.5rem;
            font-size: 2.5rem;
            color: var(--primary);
            animation: successPulse 1.5s ease-in-out;
        }
        @keyframes successPulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.1); }
        }
        
        .invoice-card {
            background: white;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            margin-bottom: 1.5rem;
        }
        .invoice-header {
            border-bottom: 3px solid var(--primary);
            padding-bottom: 1.5rem;
            margin-bottom: 1.5rem;
        }
        .invoice-id {
            font-size: 1.5rem;
            font-weight: 800;
            color: var(--dark);
        }
        .invoice-date {
            color: var(--gray);
            font-size: 0.95rem;
        }
        
        .detail-table {
            width: 100%;
            margin: 1.5rem 0;
        }
        .detail-table th {
            background: var(--light-gray);
            padding: 0.75rem 1rem;
            font-weight: 600;
            border-bottom: 2px solid #e5e7eb;
        }
        .detail-table td {
            padding: 1rem;
            border-bottom: 1px solid #e5e7eb;
        }
        
        .total-section {
            background: var(--light-gray);
            padding: 1.5rem;
            border-radius: 12px;
            margin-top: 1.5rem;
        }
        .total-amount {
            font-size: 2rem;
            font-weight: 800;
            color: var(--primary);
        }
        
        .action-buttons {
            display: flex;
            gap: 1rem;
            margin-top: 2rem;
            flex-wrap: wrap;
        }
        .btn-view-detail {
            flex: 1;
            background: var(--primary);
            color: white;
            padding: 0.75rem 1.5rem;
            border-radius: 10px;
            font-weight: 600;
            border: none;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        .btn-view-detail:hover {
            background: var(--primary-dark);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(16,185,129,0.3);
        }
        .btn-home {
            flex: 1;
            background: white;
            color: var(--gray);
            padding: 0.75rem 1.5rem;
            border-radius: 10px;
            font-weight: 600;
            border: 2px solid #e5e7eb;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        .btn-home:hover {
            border-color: var(--primary);
            color: var(--primary);
        }
        
        @media (max-width: 768px) {
            .action-buttons { flex-direction: column; }
            .success-icon { width: 60px; height: 60px; font-size: 2rem; }
        }
    </style>
</head>
<body>
    <div class="success-header">
        <div class="container">
            <div class="success-icon">
                <i class="fas fa-check"></i>
            </div>
            <h1 class="h2 mb-2">Đặt xe thành công!</h1>
            <p class="mb-0 opacity-75">Hợp đồng thuê xe của bạn đã được tạo thành công</p>
        </div>
    </div>

    <div class="container pb-5">
        <c:choose>
            <c:when test="${empty created}">
                <div class="invoice-card text-center">
                    <i class="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
                    <h4>Không tạo được hợp đồng</h4>
                    <p class="text-muted mb-4">Có lỗi xảy ra trong quá trình tạo hợp đồng. Vui lòng thử lại.</p>
                    <a class="btn btn-view-detail" href="${pageContext.request.contextPath}/ViewCartDetail">
                        <i class="fas fa-arrow-left me-2"></i>Quay lại giỏ hàng
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="c" items="${created}">
                    <div class="invoice-card">
                        <div class="invoice-header">
                            <div class="d-flex justify-content-between align-items-center flex-wrap gap-3">
                                <div>
                                    <div class="invoice-id">Hợp đồng #${c.contractId}</div>
                                    <div class="invoice-date">
                                        <i class="fas fa-user me-2"></i><strong>Khách hàng:</strong> ${c.customerName}
                                    </div>
                                    <div class="invoice-date">
                                        <i class="far fa-calendar me-2"></i>${c.start} → ${c.end}
                                    </div>
                                </div>
                                <div>
                                    <span class="badge bg-warning text-dark px-3 py-2" style="font-size: 1rem; border-radius: 8px;">
                                        <i class="fas fa-clock me-1"></i>Đang chờ xử lý
                                    </span>
                                </div>
                            </div>
                        </div>

                        <h5 class="mb-3"><i class="fas fa-car me-2 text-primary"></i>Danh sách xe thuê</h5>
                        <table class="detail-table">
                            <thead>
                                <tr>
                                    <th>Mã xe</th>
                                    <th>Ngày nhận</th>
                                    <th>Ngày trả</th>
                                    <th class="text-end">Giá thuê</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="d" items="${c.details}">
                                    <tr>
                                        <td><span class="badge bg-dark">#${d.vehicleId}</span></td>
                                        <td>${d.rentStartDate}</td>
                                        <td>${d.rentEndDate}</td>
                                        <td class="text-end"><strong>${d.price}</strong> VNĐ</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="total-section">
                            <div class="row align-items-center">
                                <div class="col-md-6">
                                    <div class="mb-2">
                                        <i class="fas fa-money-bill-wave me-2 text-primary"></i>
                                        <strong>Tổng tiền thuê:</strong>
                                        <span class="ms-2">${c.total} VNĐ</span>
                                    </div>
                                    <div class="mb-2">
                                        <i class="fas fa-wallet me-2 text-warning"></i>
                                        <strong>Tiền đặt cọc:</strong>
                                        <span class="ms-2 text-warning"><strong>${c.deposit} VNĐ</strong></span>
                                    </div>
                                    <div class="text-muted" style="font-size: 0.85rem;">
                                        <i class="fas fa-info-circle me-1"></i>
                                        Đặt cọc khi nhận xe tại địa điểm
                                    </div>
                                </div>
                                <div class="col-md-6 text-end">
                                    <div class="text-muted mb-1" style="font-size: 0.9rem;">Tổng thanh toán</div>
                                    <div class="total-amount">${c.total} VNĐ</div>
                                </div>
                            </div>
                        </div>

                        <div class="action-buttons">
                            <a class="btn-view-detail" href="${pageContext.request.contextPath}/view-contract?contractId=${c.contractId}">
                                <i class="fas fa-file-invoice me-2"></i>Xem chi tiết hợp đồng
                            </a>
                        </div>
                    </div>
                </c:forEach>

                <div class="text-center mt-4">
                    <div class="action-buttons" style="max-width: 600px; margin: 0 auto;">
                        <a class="btn-view-detail" href="${pageContext.request.contextPath}/my-contracts">
                            <i class="fas fa-list me-2"></i>Xem tất cả hợp đồng
                        </a>
                        <a class="btn-home" href="${pageContext.request.contextPath}/home">
                            <i class="fas fa-home me-2"></i>Về trang chủ
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
