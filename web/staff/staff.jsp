<%-- 
    Document   : staff
    Created on : 23 thg 10, 2025, 11:31:55
    Author     : HOANGNAM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Staff Dashboard - CarGo</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            .navbar {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }

            .navbar-brand {
                font-weight: bold;
                color: white !important;
            }

            .staff-header {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                color: white;
                padding: 2rem 0;
                margin-bottom: 2rem;
                border-radius: 15px;
            }

            .contract-card {
                background: white;
                border-radius: 15px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                transition: all 0.3s ease;
                margin-bottom: 1.5rem;
                overflow: hidden;
            }

            .contract-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 25px rgba(0,0,0,0.15);
            }

            .status-badge {
                padding: 0.5rem 1rem;
                border-radius: 25px;
                font-weight: bold;
                font-size: 0.85rem;
            }

            .status-pending {
                background-color: #fff3cd;
                color: #856404;
                border: 1px solid #ffeaa7;
            }

            .status-accepted {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }

            .status-rejected {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }

            .btn-action {
                border-radius: 25px;
                padding: 0.5rem 1.5rem;
                font-weight: bold;
                transition: all 0.3s ease;
            }

            .btn-view {
                background: linear-gradient(45deg, #667eea, #764ba2);
                border: none;
                color: white;
            }

            .btn-view:hover {
                transform: scale(1.05);
                color: white;
            }

            .btn-update {
                background: linear-gradient(45deg, #43e97b, #38f9d7);
                border: none;
                color: white;
            }

            .btn-update:hover {
                transform: scale(1.05);
                color: white;
            }

            .stats-card {
                background: white;
                border-radius: 15px;
                padding: 1.5rem;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                text-align: center;
                margin-bottom: 1rem;
            }

            .stats-number {
                font-size: 2rem;
                font-weight: bold;
                color: #667eea;
            }

            .alert {
                border-radius: 10px;
                border: none;
            }

            .form-control {
                border-radius: 10px;
                border: 1px solid #e0e0e0;
                padding: 0.75rem 1rem;
            }

            .form-control:focus {
                border-color: #667eea;
                box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
            }

            .filter-tabs {
                background: white;
                border-radius: 15px;
                padding: 1rem;
                margin-bottom: 2rem;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }

            .filter-tab {
                padding: 0.75rem 1.5rem;
                border-radius: 25px;
                border: none;
                background: #f8f9fa;
                color: #6c757d;
                transition: all 0.3s ease;
                margin-right: 0.5rem;
                margin-bottom: 0.5rem;
            }

            .filter-tab.active {
                background: linear-gradient(45deg, #667eea, #764ba2);
                color: white;
            }

            .filter-tab:hover {
                transform: scale(1.05);
            }
        </style>
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
            <!-- Contract List -->
            <div class="row">
                <div class="col-12">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h3><i class="fas fa-list"></i> Danh sách hợp đồng từ khách hàng</h3>
                        <button class="btn btn-primary btn-action" onclick="location.reload()">
                            <i class="fas fa-sync-alt"></i> Làm mới
                        </button>
                    </div>
                </div>
            </div>


            <c:choose>
                <c:when test="${empty contracts}">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> Chưa có hợp đồng nào từ khách hàng
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="contract" items="${contracts}">
                        <!-- Contract Item -->
                        <!-- Contract Item -->
                        <div class="contract-card">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <!-- Contract ID -->
                                    <div class="col-md-2">
                                        <div class="text-center">
                                            <i class="fas fa-file-contract fa-3x text-primary mb-2"></i>
                                            <div class="fw-bold">#${contract.contractId}</div>
                                        </div>
                                    </div>

                                    <!-- Customer info -->
                                    <div class="col-md-2">
                                        <h6 class="mb-1">Khách hàng</h6>
                                        <div class="fw-bold">${contract.customerName}</div>
                                        <small class="text-muted">
                                            <i class="fas fa-phone"></i> ---
                                        </small>
                                    </div>

                                    <!-- Contract time -->
                                    <div class="col-md-2">
                                        <h6 class="mb-1">Thời gian</h6>
                                        <div class="small">
                                            <i class="fas fa-calendar-start"></i> ${contract.startDate}<br>
                                            <i class="fas fa-calendar-end"></i> ${contract.endDate}
                                        </div>
                                    </div>

                                    <!-- Amount -->
                                    <div class="col-md-2">
                                        <h6 class="mb-1">Số tiền</h6>
                                        <div class="fw-bold text-success">
                                            <i class="fas fa-dollar-sign"></i> ${contract.totalAmount} VNĐ
                                        </div>
                                        <small class="text-muted">Cọc: ${contract.depositAmount} VNĐ</small>
                                    </div>

                                    <!-- Contract status -->
                                    <div class="col-md-1">
                                        <h6 class="mb-1">Trạng thái</h6>
                                        <c:choose>
                                            <c:when test="${contract.status == 'PENDING'}">
                                                <span class="status-badge status-pending">Chờ xử lý</span>
                                            </c:when>
                                            <c:when test="${contract.status == 'ACCEPTED'}">
                                                <span class="status-badge status-accepted">Đã chấp nhận</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge status-rejected">Từ chối</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <!-- Payment info -->
                                    <div class="col-md-2">
                                        <h6 class="mb-1">Thanh toán</h6>
                                        <c:if test="${not empty contract.payments}">
                                            <c:forEach var="payment" items="${contract.payments}">
                                                <div class="small mb-1">
                                                    <i class="fas fa-dollar-sign"></i> ${payment.amount} VNĐ<br>
                                                    <i class="fas fa-credit-card"></i> ${payment.methodName}<br>
                                                    <span class="text-muted">
                                                        <c:choose>
                                                            <c:when test="${payment.status == 'pending'}">Chờ xử lý</c:when>
                                                            <c:when test="${payment.status == 'completed'}">Hoàn tất</c:when>
                                                            <c:otherwise>${payment.status}</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${empty contract.payments}">
                                            <div class="text-muted">Chưa thanh toán</div>
                                        </c:if>
                                    </div>

                                    <!-- Action buttons -->
                                    <div class="col-md-1">
                                        <div class="btn-group-vertical w-100" role="group">
                                            <button type="button" class="btn btn-view btn-action btn-sm mb-1"
                                                    onclick="window.location.href = '${pageContext.request.contextPath}/ContractServlet?action=view&contractId=${contract.contractId}'">
                                                <i class="fas fa-eye"></i> Xem chi tiết
                                            </button>

                                            <button type="button" class="btn btn-info btn-action btn-sm mb-1"
                                                    onclick="window.location.href = '${pageContext.request.contextPath}/PaymentServlet?action=by_contract&contractId=${contract.contractId}'">
                                                <i class="fas fa-money-check"></i> Thanh toán
                                            </button>
                                            <c:if test="${contract.status == 'PENDING'}">
                                                <div class="btn-group" role="group">
                                                    <button type="button" class="btn btn-success btn-sm" 
                                                            onclick="updateStatus(${contract.contractId}, 'ACCEPTED')">
                                                        <i class="fas fa-check"></i>
                                                    </button>
                                                    <button type="button" class="btn btn-danger btn-sm"
                                                            onclick="updateStatus(${contract.contractId}, 'REJECTED')">
                                                        <i class="fas fa-times"></i>
                                                    </button>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </c:forEach>
                </c:otherwise>
            </c:choose>
            <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

            <!-- Debug: kiểm tra số hợp đồng nhận được -->
            <div class="alert alert-info">
                Contracts received: ${fn:length(contracts)}
            </div>

        </div>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                                function updateStatus(contractId, status) {
                                                                    if (confirm('Bạn có chắc muốn cập nhật trạng thái hợp đồng?')) {
                                                                        var form = document.createElement('form');
                                                                        form.method = 'POST';
                                                                        form.action = '${pageContext.request.contextPath}/ContractServlet';

                                                                        var actionInput = document.createElement('input');
                                                                        actionInput.type = 'hidden';
                                                                        actionInput.name = 'action';
                                                                        actionInput.value = 'update_status';
                                                                        form.appendChild(actionInput);

                                                                        var contractIdInput = document.createElement('input');
                                                                        contractIdInput.type = 'hidden';
                                                                        contractIdInput.name = 'contractId';
                                                                        contractIdInput.value = contractId;
                                                                        form.appendChild(contractIdInput);

                                                                        var statusInput = document.createElement('input');
                                                                        statusInput.type = 'hidden';
                                                                        statusInput.name = 'status';
                                                                        statusInput.value = status;
                                                                        form.appendChild(statusInput);

                                                                        document.body.appendChild(form);
                                                                        form.submit();
                                                                    }
                                                                }

        </script>
    </body>
</html>