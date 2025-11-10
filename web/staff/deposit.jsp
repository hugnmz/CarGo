<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Yêu cầu đặt cọc</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/staff/returncar.css"/>
    </head>

    <body>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/staff">
                    <i class="fas fa-car"></i> CarGo Staff Dashboard
                </a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        <i class="fas fa-user"></i> Staff ID: ${sessionScope.userId}
                    </span>
                    <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/LogoutServlet">
                        <i class="fas fa-sign-out-alt"></i> Đăng xuất
                    </a>
                </div>
            </div>
        </nav>
        <div class="container py-4">
            <div class="staff-header text-center">
                <h1><i class="fas fa-tachometer-alt"></i> Staff Dashboard</h1>
                <p class="mb-0">Xem và xử lý hợp đồng từ khách hàng</p>
            </div>

            <div class="d-flex justify-content-between align-items-center mb-3">
                <h1 class="mb-4">Danh sách yêu cầu đặt cọc</h1>
                <div class="d-flex gap-3">
                    <a href="${pageContext.request.contextPath}/staff" class="btn btn-outline-primary btn-sm d-flex align-items-center">
                        <i class="fas"></i> ← Quay lại
                    </a>
                    <a href="${pageContext.request.contextPath}/deposit" class="btn btn-outline-primary btn-sm d-flex align-items-center">
                        <i class="bi bi-arrow-clockwise me-1"></i> Làm mới
                    </a>
                </div>
            </div>

            <c:if test="${not empty sessionScope.flash}">
                <div class="alert alert-success">${sessionScope.flash}</div>
                <c:remove var="flash" scope="session"/>
            </c:if>

            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
<th>Mã HĐ</th>
                                    <th>Tên KH</th>
                                    <th>SĐT</th>
                                    <th>Ngày mượn</th>
                                    <th>Ngày trả</th>
                                    <th>Tiền đặt cọc</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="r" items="${requests}">
                                    <tr>
                                        <td><strong>${r.contractId}</strong></td>
                                        <td>${r.customerName}</td>
                                        <td>${r.customerPhone}</td>
                                        <td>${r.startDateToString()}</td>
                                        <td>${r.endDateToString()}</td>
                                        <td><fmt:formatNumber value="${r.depositAmount}" pattern="#,###"/> VNĐ</td>
                                        <td class="text-end">
                                            <form method="post" action="${pageContext.request.contextPath}/processdeposit" class="d-inline">
                                                <input type="hidden" name="contractId" value="${r.contractId}"/>
                                                <input type="hidden" name="action" value="approve">
                                                <button class="btn btn-success btn-sm me-2">
                                                    <i class="fas fa-check me-1"></i>Đồng ý
                                                </button>
                                            </form>
                                            <form method="post" action="${pageContext.request.contextPath}/processdeposit" class="d-inline">
                                                <input type="hidden" name="contractId" value="${r.contractId}"/>
                                                <input type="hidden" name="action" value="reject">
                                                <button class="btn btn-danger btn-sm">
                                                    <i class="fas fa-times me-1"></i>Không đồng ý
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <c:if test="${empty requests || requests.size() == 0}">
                            <div class="empty-state">
                                <p>Danh sách yêu cầu trống</p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="pt-2">
<h4>Tổng số hợp đồng: ${requests != null ? requests.size() : 0}</h4>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
