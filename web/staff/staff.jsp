<%-- 
    Document   : staff
    Author     : CarGo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
                transform: translateY(-3px);
                box-shadow: 0 8px 25px rgba(0,0,0,0.12);
            }
            .status-badge {
                padding: 0.35rem 0.7rem;
                border-radius: 25px;
                font-weight: 600;
                font-size: 0.8rem;
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
                padding: 0.45rem 1rem;
                font-weight: 600;
            }
            .btn-view {
                background: linear-gradient(45deg, #667eea, #764ba2);
                border: none;
                color: white;
            }
            .btn-update {
                background: linear-gradient(45deg, #43e97b, #38f9d7);
                border: none;
                color: white;
            }
            .sidebar .list-group-item {
                border: none;
                padding: 0.75rem 1rem;
            }
            .sidebar .list-group-item i {
                width: 20px;
            }
            .sidebar .list-group-item.active {
                background: #eef2ff;
                color: #3730a3;
                font-weight: 600;
            }
        </style>
    </head>
    <body>
        <!-- Navigation -->
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/staff">
                    <i class="fas fa-car"></i> CarGo Staff Dashboard
                </a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        <i class="fas fa-user"></i> Staff ID: ${sessionScope.userId}
                    </span>
                </div>
            </div>
        </nav>

        <div class="container mt-4">
            <div class="staff-header text-center">
                <h1><i class="fas fa-tachometer-alt"></i> Staff Dashboard</h1>
                <p class="mb-0">Xem và xử lý hợp đồng từ khách hàng</p>
            </div>

            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 mb-3">
                    <div class="list-group shadow-sm sidebar">
                        <a class="list-group-item list-group-item-action active" href="${pageContext.request.contextPath}/staff">
                            <i class="fas fa-file-contract"></i> Danh sách hợp đồng
                        </a>
                        <a class="list-group-item list-group-item-action" href="${pageContext.request.contextPath}/returncar">
                            <i class="fas fa-undo"></i> Danh sách trả xe
                        </a>
                        <a class="list-group-item list-group-item-action text-primary" 
                           href="${pageContext.request.contextPath}/staff?action=manage">
                            <i class="fas fa-user"></i> Hồ sơ cá nhân
                        </a>

                        <a class="list-group-item list-group-item-action text-danger" href="${pageContext.request.contextPath}/LogoutServlet">
                            <i class="fas fa-sign-out-alt"></i> Đăng xuất
                        </a>
                    </div>
                </div>

                <!-- Main content -->
                <div class="col-md-9 col-lg-10">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h3><i class="fas fa-list"></i> Danh sách hợp đồng từ khách hàng</h3>
                        <div class="d-flex gap-2">
                            <button class="btn btn-outline-primary btn-action" type="button" onclick="location.reload()">
                                Làm mới
                            </button>
                        </div>
                    </div>

                    <c:if test="${empty contracts}">
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle"></i> Chưa có hợp đồng nào từ khách hàng
                        </div>
                    </c:if>

                    <c:forEach var="contract" items="${contracts}">
                        <div class="contract-card">
                            <div class="p-3">
                                <div class="row g-3 align-items-center">
                                    <!-- ID -->
                                    <div class="col-6 col-md-2">
                                        <div class="fw-bold">#${contract.contractId}</div>
                                        <div class="small text-muted">Hợp đồng</div>
                                    </div>

                                    <!-- Khách hàng -->
                                    <div class="col-6 col-md-2">
                                        <div class="small text-muted">Khách hàng</div>
                                        <div class="fw-semibold">${contract.customerName}</div>
                                    </div>

                                    <!-- Thời gian -->
                                    <div class="col-6 col-md-3">
                                        <div class="small text-muted">Thời gian</div>
                                        <div class="small">
                                            Bắt đầu: ${contract.startDate}<br>
                                            Kết thúc: ${contract.endDate}
                                        </div>
                                    </div>

                                    <!-- Số tiền -->
                                    <div class="col-6 col-md-3">
                                        <div class="small text-muted">Số tiền</div>
                                        <div class="fw-bold text-success">${contract.totalAmount} VNĐ</div>
                                        <div class="small text-muted">Cọc: ${contract.depositAmount} VNĐ</div>
                                    </div>

                                    <!-- Trạng thái + Hành động -->
                                    <div class="col-12 col-md-2 text-md-end">
                                        <div class="mb-2">
                                            <span class="status-badge
                                                  ${contract.status == 'PENDING' ? 'status-pending' : (contract.status == 'ACCEPTED' ? 'status-accepted' : 'status-rejected')}">
                                                ${contract.status}
                                            </span>
                                        </div>

                                        <div class="d-flex flex-wrap gap-2 justify-content-md-end">
                                            <button type="button" class="btn btn-sm btn-view btn-action"
                                                    onclick="window.location.href = '${pageContext.request.contextPath}/staff/staffview.jsp?contractId=${contract.contractId}'">
                                                Xem chi tiết
                                            </button>

                                            <c:if test="${contract.status == 'PENDING'}">
                                                <button type="button" class="btn btn-sm btn-success btn-action"
                                                        onclick="updateStatus(${contract.contractId}, 'ACCEPTED')">
                                                    Chấp nhận
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger btn-action"
                                                        onclick="openRejectModal(${contract.contractId})">
                                                    Từ chối
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- Debug nhỏ -->
                    <div class="alert alert-light border">
                        Contracts received: ${fn:length(contracts)}
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal nhập lý do từ chối (dùng chung) -->
        <div class="modal fade" id="rejectReasonModal" tabindex="-1" aria-labelledby="rejectReasonLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="rejectReasonLabel">Nhập lý do từ chối</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="rejectReasonTextarea" class="form-label">Lý do</label>
                            <textarea id="rejectReasonTextarea" class="form-control" rows="4" placeholder="Nhập lý do từ chối..." required></textarea>
                        </div>
                        <div class="form-text">Lý do sẽ hiển thị cho khách hàng trong chi tiết hợp đồng.</div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="button" class="btn btn-danger" id="confirmRejectBtn">Từ chối</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS bundle (Popper included) - chỉ include 1 lần -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                            let rejectContractId = null;

                                                            function openRejectModal(contractId) {
                                                                rejectContractId = contractId;
                                                                document.getElementById('rejectReasonTextarea').value = '';
                                                                const modal = new bootstrap.Modal(document.getElementById('rejectReasonModal'));
                                                                // Đảm bảo binding 1 lần cho nút confirm
                                                                const btn = document.getElementById('confirmRejectBtn');
                                                                btn.onclick = function () {
                                                                    const reason = document.getElementById('rejectReasonTextarea').value.trim();
                                                                    if (!reason) {
                                                                        document.getElementById('rejectReasonTextarea').focus();
                                                                        return;
                                                                    }
                                                                    submitStatusForm(rejectContractId, 'REJECTED', reason);
                                                                };
                                                                modal.show();
                                                            }

                                                            function updateStatus(contractId, status) {
                                                                if (status === 'REJECTED') {
                                                                    openRejectModal(contractId);
                                                                    return;
                                                                }
                                                                if (confirm('Xác nhận cập nhật trạng thái hợp đồng #' + contractId + ' thành ' + status + ' ?')) {
                                                                    submitStatusForm(contractId, status, null);
                                                                }
                                                            }

                                                            function submitStatusForm(contractId, status, reason) {
                                                                const form = document.createElement('form');
                                                                form.method = 'POST';
                                                                form.action = '${pageContext.request.contextPath}/ContractServlet';

                                                                const add = (name, value) => {
                                                                    const i = document.createElement('input');
                                                                    i.type = 'hidden';
                                                                    i.name = name;
                                                                    i.value = value;
                                                                    form.appendChild(i);
                                                                };

                                                                add('action', 'update_status');
                                                                add('contractId', contractId);
                                                                add('status', status);
                                                                if (reason !== null && reason !== undefined) {
                                                                    add('reason', reason);
                                                                }

                                                                document.body.appendChild(form);
                                                                form.submit();
                                                            }
        </script>
    </body>
</html>