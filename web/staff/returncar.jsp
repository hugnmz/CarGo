<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@page import="model.RequestReturnCar" %>
<%@page import="java.util.*" %>
<%@page import="java.time.LocalDateTime" %>
<%@page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Yêu cầu trả xe</title>
        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/staff/returncar.css"/>

    </head>

    <%

        List<RequestReturnCar> requests = (List<RequestReturnCar>) request.getAttribute("requests");
        if (requests == null) {
            response.sendRedirect(request.getContextPath() + "/returncar");
            return;
        }
    %>


    <body>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/staff">
                    <i class="fas fa-car"></i> CarGo Staff Dashboard
                </a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        <i class="fas fa-user" name="staffId"></i> Staff ID: ${sessionScope.userId}
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
                <h1 class="mb-4">Danh sách yêu cầu trả xe</h1>
                <div class="d-flex gap-3">
                    <a href="${pageContext.request.contextPath}/staff" class="btn btn-outline-primary btn-sm d-flex align-items-center ">
                        <i class="fas"></i> ← Quay lại
                    </a>

                    <a href="${pageContext.request.contextPath}/returncar" 
                       class="btn btn-outline-primary btn-sm d-flex align-items-center ">
                        <i class="bi bi-arrow-clockwise me-1"></i> Làm mới
                    </a></div>
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
                                    <th>Ngày trả thực tế</th>
                                    <th>Trạng thái</th>
                                    <th></th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="r" items="${requests}">
                                    <tr>
                                        <td><strong>${r.contract.getContractId()}</strong></td>
                                        <td>${r.contract.getCustomerName()}</td>
                                        <td>${r.contract.getCustomerPhone()}</td>
                                        <td>${r.contract.startDateToString()}</td>
                                        <td>${r.contract.endDateToString()}</td>
                                        <td>${r.timeRequestToString()}</td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${r.late}">
                                                    <span class="badge badge-late">Trễ ~ ${r.lateTime()}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-on-time">Đúng/Sớm</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end">
                                            <form method="post" action="${pageContext.request.contextPath}/processreturncar"
                                                  class="d-inline">
                                                <input type="hidden" name="contractId" value="${r.contract.contractId}"/>
                                                <input type="hidden" name="csrf" value="${sessionScope.csrf}">
                                                <button class="btn btn-outline-primary btn-sm d-inline-flex align-items-center justify-content-center px-3 py-1">
                                                    <i class="bi bi-gear me-1"></i> Xử lý
                                                </button>
                                            </form>

                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                        </table>
                        <c:if test="${requests.size() == 0}">
                            <div class="empty-state">
                                <p>Danh sách yêu cầu trống</p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="pt-2">
                <h4>Tổng số hợp đồng: ${requests.size()}</h4>
            </div>
        </div>






        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


    </body>
</html>
