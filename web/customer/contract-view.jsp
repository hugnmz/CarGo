<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chi tiết hợp đồng #${contract.contractId} - CarGo</title>
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
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        
        .info-card {
            background: white;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            margin-bottom: 1.5rem;
        }
        
        .info-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem 0;
            border-bottom: 1px solid #e5e7eb;
        }
        .info-row:last-child { border-bottom: none; }
        .info-label {
            color: var(--gray);
            font-weight: 500;
            display: flex;
            align-items: center;
        }
        .info-label i {
            margin-right: 0.5rem;
            color: var(--primary);
        }
        .info-value {
            font-weight: 600;
            color: var(--dark);
        }
        
        .status-badge {
            padding: 0.5rem 1.2rem;
            border-radius: 25px;
            font-size: 1rem;
            font-weight: 700;
            display: inline-block;
        }
        .status-pending { background: #fef3c7; color: #92400e; }
        .status-accepted { background: #dbeafe; color: #1e40af; }
        .status-in-progress { background: #e0e7ff; color: #4338ca; }
        .status-completed { background: #d1fae5; color: #065f46; }
        .status-cancelled { background: #fee2e2; color: #991b1b; }
        
        .total-amount {
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
            color: white;
            padding: 1.5rem;
            border-radius: 12px;
            text-align: center;
        }
        .total-amount h2 {
            font-size: 2.5rem;
            font-weight: 800;
            margin-bottom: 0.5rem;
        }
        
        .detail-card {
            background: white;
            border-radius: 12px;
            padding: 1.5rem;
            margin-bottom: 1rem;
            box-shadow: 0 2px 6px rgba(0,0,0,0.06);
            transition: all 0.3s;
        }
        .detail-card:hover {
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
            transform: translateX(4px);
        }
        
        .vehicle-icon {
            width: 60px;
            height: 60px;
            background: var(--light-gray);
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            color: var(--primary);
        }
        
        .btn-back {
            background: white;
            color: var(--primary);
            padding: 0.6rem 1.5rem;
            border-radius: 10px;
            font-weight: 600;
            border: 2px solid white;
            transition: all 0.3s;
        }
        .btn-back:hover {
            background: transparent;
            color: white;
        }
        
        @media (max-width: 768px) {
            .info-row { flex-direction: column; align-items: flex-start; gap: 0.5rem; }
            .total-amount h2 { font-size: 1.8rem; }
        }
    </style>
</head>
<body>
    <div class="page-header">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center flex-wrap">
                <div>
                    <h1 class="h2 mb-2"><i class="fas fa-file-contract me-2"></i>Hợp đồng #${contract.contractId}</h1>
                    <p class="mb-0 opacity-75">Chi tiết đầy đủ về hợp đồng thuê xe</p>
                </div>
                <div class="d-flex gap-2 mt-3 mt-md-0">
                    <a class="btn btn-back" href="${pageContext.request.contextPath}/my-contracts">
                        <i class="fas fa-arrow-left me-2"></i>Danh sách HĐ
                    </a>
                    <a class="btn btn-back" href="${pageContext.request.contextPath}/home">
                        <i class="fas fa-home me-2"></i>Trang chủ
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="container pb-5">
        <div class="row">
            <!-- Contract Info -->
            <div class="col-lg-4 mb-4">
                <div class="info-card">
                    <h5 class="fw-bold mb-4">Thông tin hợp đồng</h5>
                    <div class="info-row">
                        <span class="info-label"><i class="fas fa-hashtag"></i>Mã hợp đồng</span>
                        <span class="info-value">#${contract.contractId}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="fas fa-user"></i>Khách hàng</span>
                        <span class="info-value"><strong>${contract.customerName}</strong></span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="fas fa-info-circle"></i>Trạng thái</span>
                        <c:set var="statusClass" value="${contract.status == 'PENDING' ? 'status-pending' : contract.status == 'ACCEPTED' ? 'status-accepted' : contract.status == 'IN_PROGRESS' ? 'status-in-progress' : contract.status == 'COMPLETED' ? 'status-completed' : 'status-cancelled'}" />
                        <span class="status-badge ${statusClass}">${contract.status}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="far fa-calendar-check"></i>Ngày bắt đầu</span>
                        <span class="info-value">${contract.startDate}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="far fa-calendar-times"></i>Ngày kết thúc</span>
                        <span class="info-value">${contract.endDate}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label"><i class="fas fa-piggy-bank"></i>Tiền đặt cọc</span>
                        <span class="info-value">${contract.depositAmount} VNĐ</span>
                    </div>
                </div>
                
                <div class="total-amount">
                    <small class="d-block opacity-75">Tổng giá trị hợp đồng</small>
                    <h2>${contract.totalAmount}</h2>
                    <small class="opacity-75">VNĐ</small>
                </div>
            </div>
            
            <!-- Vehicle Details -->
            <div class="col-lg-8">
                <div class="info-card">
                    <h5 class="fw-bold mb-4"><i class="fas fa-car me-2"></i>Danh sách xe trong hợp đồng</h5>
                    <c:forEach var="d" items="${details}">
                        <div class="detail-card">
                            <div class="row align-items-center">
                                <div class="col-auto">
                                    <div class="vehicle-icon">
                                        <i class="fas fa-car"></i>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="d-flex justify-content-between align-items-start mb-2">
                                        <div>
                                            <h6 class="fw-bold mb-1">Xe #${d.vehicleId}</h6>
                                            <small class="text-muted">Chi tiết #${d.contractDetailId}</small>
                                        </div>
                                        <div class="text-end">
                                            <div class="fw-bold text-success" style="font-size: 1.25rem;">${d.price} VNĐ</div>
                                        </div>
                                    </div>
                                    <div class="row g-2 text-muted small">
                                        <div class="col-sm-6">
                                            <i class="far fa-calendar-check text-success me-1"></i>
                                            Nhận: ${d.rentStartDate}
                                        </div>
                                        <div class="col-sm-6">
                                            <i class="far fa-calendar-times text-danger me-1"></i>
                                            Trả: ${d.rentEndDate}
                                        </div>
                                    </div>
                                    <c:if test="${not empty d.note}">
                                        <div class="mt-2 p-2 bg-light rounded">
                                            <small><i class="fas fa-sticky-note me-1"></i>${d.note}</small>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
