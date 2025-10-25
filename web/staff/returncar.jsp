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
        <style>
            body {
                background: #f7f7f9;
            }
            .a{
                text-decoration: none;
                color: white;
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
            .staff-header {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                color: white;
                padding: 2rem 0;
                margin-bottom: 2rem;
                border-radius: 15px;
            }
            .badge-late {
                background: #dc3545;
            }
            .badge-on-time {
                background: #198754;
            }
            .table thead th {
                white-space: nowrap;
            }
            .empty-state {
                text-align: center;
                padding: 60px 0;
                color: #6c757d; /* Màu xám nhẹ */
                font-size: 1.1rem;
                font-style: italic;
                opacity: 0.8;
            }
        </style>
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
                <a class="navbar-brand" href="${pageContext.request.contextPath}/staff/staff.jsp">
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
        <div class="container py-4">

            <div class="staff-header text-center">
                <h1><i class="fas fa-tachometer-alt"></i> Staff Dashboard</h1>
                <p class="mb-0">Xem và xử lý hợp đồng từ khách hàng</p>
            </div>
            

            <div class="d-flex justify-content-between align-items-center mb-3">
                <h1 class="mb-4">Danh sách yêu cầu trả xe</h1>
                <a href="${pageContext.request.contextPath}/returncar" 
                   class="btn btn-outline-primary btn-sm d-flex align-items-center">
                    <i class="bi bi-arrow-clockwise me-1"></i> Làm mới
                </a>
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
                                            <a href="${pageContext.request.contextPath}/returns/process?contractId=${r.contract.contractId}"
                                               class="btn btn-outline-primary btn-sm d-inline-flex align-items-center justify-content-center px-3 py-1">
                                                <i class="bi bi-gear me-1"></i> Xử lý
                                            </a>

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
        </div>




        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


    </body>
</html>
