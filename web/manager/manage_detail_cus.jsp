
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="vi_VN" />
<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires", 0);

    String role = (String) session.getAttribute("roleName");
    if (role == null || !"MANAGER".equalsIgnoreCase(role)) {
        response.sendRedirect("LoginServlet");
        return;
    }
%>
<!-- Set default values for session attributes -->
<c:set var="username" value="${sessionScope.username}" />
<c:set var="avatar" value="${not empty sessionScope.avatar ? sessionScope.avatar : 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'}" />
<c:set var="email" value="${sessionScope.email}" />
<c:set var="phone" value="${sessionScope.phone}" />
<c:set var="fullName" value="${not empty sessionScope.fullName ? sessionScope.fullName : sessionScope.username}" />
<c:set var="city" value="${not empty sessionScope.city ? sessionScope.city : ''}"/>
<c:set var="dateOfBirth" value="${not empty sessionScope.dateOfBirth ? sessionScope.dateOfBirth : ''}"/>

<c:set var="isLoggedIn" value="${not empty username}" />

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thông tin cá nhân - CarRental</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background-color: white; /* nền sáng giống trang chi tiết xe */
                font-family: "Segoe UI", sans-serif;
            }

            /* --- Sidebar --- */
            .profile-sidebar {
                background: white;
                border-radius: 12px;
                padding: 1.5rem;
                box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
                margin-bottom: 2rem;
            }

            .profile-card {
                text-align: center;
                padding-bottom: 1.5rem;
                border-bottom: 1px solid #dee2e6;
                margin-bottom: 1.5rem;
            }

            .profile-avatar img {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                object-fit: cover;
                border: 3px solid #dee2e6;
            }

            .profile-stats {
                display: flex;
                justify-content: space-around;
                margin-top: 1rem;
            }

            .stat-number {
                font-size: 1.5rem;
                font-weight: 700;
                color: #6c757d !important; /* xám lam nhẹ */
            }

            .stat-label {
                font-size: 0.875rem;
                color: #868e96 !important;
            }

            /* --- Sidebar Menu --- */
            .profile-menu .nav-link {
                color: #495057;
                padding: 0.75rem 1rem;
                border-radius: 8px;
                margin-bottom: 0.5rem;
                display: flex;
                align-items: center;
                transition: all 0.3s;
            }

            .profile-menu .nav-link:hover {
                background: #e9ecef; /* lam xám nhạt khi hover */
                color: #343a40;
            }

            .profile-menu .nav-link.active {
                background: lightslategrey; /* lam nhạt chủ đạo */
                color: white;
                font-weight: 600;
            }

            /* --- Main Content --- */
            .profile-content {
                background: white;
                border-radius: 12px;
                padding: 2rem;
                box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
            }

            .content-header h4 {
                font-size: 1.5rem;
                font-weight: 700;
                color: #495057;
                border-bottom: 2px solid #dee2e6;
                padding-bottom: 0.75rem;
            }

            label.text-muted i {
                color: #adb5bd;
                margin-right: 6px;
            }

            .fw-semibold {
                color: #343a40;
            }

            /* --- Booking History --- */
            .booking-item {
                display: flex;
                align-items: center;
                padding: 1.5rem;
                background: #f8f9fa;
                border-radius: 12px;
                margin-bottom: 1rem;
                transition: all 0.3s;
            }

            .booking-item:hover {
                background: #e9ecef;
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            .booking-image img {
                width: 80px;
                height: 60px;
                object-fit: cover;
                border-radius: 8px;
                margin-right: 1rem;
            }

            .booking-details h6 {
                font-weight: 600;
                color: #343a40;
            }

            .booking-price h6 {
                font-weight: 700;
                color: lightslategrey;
            }

            /* --- Buttons --- */
            .btn-outline-primary {
                border-color: lightslategrey;
                color: #495057;
            }

            .btn-outline-primary:hover {
                background-color: lightslategrey;
                color: white;
            }

            .back-button {
                position: absolute;
                top: 20px;
                left: 20px;
                background-color: lightslategrey;
                color: white;
                border: none;
                padding: 8px 14px;
                border-radius: 6px;
                font-size: 14px;
                text-decoration: none;
                transition: background-color 0.3s ease;
            }

            .back-button:hover {
                background-color: #5a6771;
            }

            @media (max-width: 768px) {
                .booking-item {
                    flex-direction: column;
                    text-align: center;
                }
                .booking-image {
                    margin: 0 0 1rem 0;
                }
                .booking-price {
                    margin-top: 1rem;
                    text-align: center;
                }
            }
        </style>

    </head>
    <body>

        <!-- Nút quay lại -->
        <a href="managecus" class="back-button"
           style="position: absolute;
           top: 20px;
           left: 20px;
           background-color: lightslategrey;
           color: white;
           border: none;
           padding: 8px 14px;
           border-radius: 6px;
           font-size: 14px;
           text-decoration: none;
           transition: background-color 0.3s ease;">
            ← Quay lại</a>

        <!-- Main Content -->
        <div class="container mt-5 pt-5">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3">
                    <div class="profile-sidebar">
                        <div class="profile-card text-center">
                            <div class="profile-avatar">
                                <img src="${avatar}" alt="Avatar" class="avatar-img">
                            </div>
                            <h5 class="mt-3 mb-1">${customer.fullName}</h5>
                            <p class="text-muted mb-3">@Customer</p>
                            <div class="profile-stats">
                                <div class="stat-item">
                                    <span class="stat-number">12</span>
                                    <span class="stat-label">Chuyến đi</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-number">4.8</span>
                                    <span class="stat-label">Đánh giá</span>
                                </div>
                            </div>
                        </div>

                        <!-- Navigation Menu -->
                        <div class="profile-menu">
                            <ul class="nav flex-column">
                                <li class="nav-item">
                                    <a class="nav-link active" href="#profile-info" data-bs-toggle="tab">
                                        <i class="fa fa-user"></i> Thông tin cá nhân
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#booking-history" data-bs-toggle="tab">
                                        <i class="fa fa-history"></i> Lịch sử đặt xe
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Main Content Area -->
                <div class="col-md-9">
                    <div class="tab-content">

                        <!-- Profile Information Tab -->
                        <div class="tab-pane fade show active" id="profile-info">
                            <div class="profile-content">
                                <div class="content-header">
                                    <h4><i class="fa fa-user text-primary"></i> Thông tin cá nhân</h4>
                                </div>

                                <div class="profile-details mt-4">
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label class="text-muted"><i class="fa fa-user"></i> Họ và tên</label>
                                            <p class="fw-semibold">${customer.fullName}</p>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label class="text-muted"><i class="fa fa-envelope"></i> Email</label>
                                            <p class="fw-semibold">${customer.email}</p>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label class="text-muted"><i class="fa fa-phone"></i> Số điện thoại</label>
                                            <p class="fw-semibold">${customer.phone}</p>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label class="text-muted"><i class="fa fa-calendar"></i> Ngày tham gia</label>
                                            <p class="fw-semibold">${customer.createAt}</p>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label class="text-muted"><i class="fa fa-map-marker-alt"></i> Thành phố</label>
                                            <p class="fw-semibold">${customer.city}</p>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label class="text-muted"><i class="fa fa-birthday-cake"></i> Ngày sinh</label>
                                            <p class="fw-semibold">${customer.dateOfBirth}</p>
                                        </div>
                                    </div>
                                </div>

                                <!-- Recent Activity -->
                                <div class="recent-activity mt-4">
                                    <h5><i class="fa fa-clock text-primary"></i> Hoạt động gần đây</h5>
                                    <div class="activity-list">
                                        <div class="activity-item">
                                            <div class="activity-icon">
                                                <i class="fa fa-car text-success"></i>
                                            </div>
                                            <div class="activity-content">
                                                <p class="mb-1">Đặt xe Toyota Camry 2024</p>
                                                <small class="text-muted">2 ngày trước</small>
                                            </div>
                                        </div>
                                        <div class="activity-item">
                                            <div class="activity-icon">
                                                <i class="fa fa-star text-warning"></i>
                                            </div>
                                            <div class="activity-content">
                                                <p class="mb-1">Đánh giá chuyến đi</p>
                                                <small class="text-muted">1 tuần trước</small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Booking History Tab -->
                        <div class="tab-pane fade" id="booking-history">
                            <div class="profile-content">
                                <div class="content-header">
                                    <h4><i class="fa fa-history text-primary"></i> Lịch sử đặt xe</h4>
                                </div>

                                <div class="booking-list">
                                    <c:choose>
                                        <c:when test="${not empty listContract}">
                                            <c:forEach var="contract" items="${listContract}">
                                                <div class="booking-item">
                                                    <div class="booking-image">
                                                        <img src="https://via.placeholder.com/100x80?text=Car" alt="Car">
                                                    </div>
                                                    <div class="booking-details">
                                                        <h6>Hợp đồng #${contract.contractId}</h6>
                                                        <p class="text-muted mb-1">Từ ${contract.startDate} đến ${contract.endDate}</p>
                                                        <p class="text-muted mb-1">Tạo lúc: ${contract.createAt}</p>
                                                        <span class="badge ${contract.status == 'ACCEPTED' ? 'bg-success' : contract.status == 'PENDING' ? 'bg-warning' : 'bg-danger'}">
                                                            ${contract.status == 'ACCEPTED' ? 'ACCEPTED' : contract.status == 'PENDING' ? 'PENDING' : 'Từ chối'}
                                                        </span>
                                                    </div>
                                                    <div class="booking-price">
                                                        <h6 class="text-success"><fmt:formatNumber value="${contract.totalAmount}" pattern="#,###" /> VNĐ</h6>
                                                        <p class="text-muted small">Cọc: <fmt:formatNumber value="${contract.depositAmount}" pattern="#,###" /> VNĐ</p>
                                                        <a href="contractdetail?contractId=${contract.contractId}" class="btn btn-info btn-sm text-white">
                                                            <i class="fa-solid fa-circle-info"></i> Chi tiết
                                                        </a>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-center py-4">
                                                <i class="fa fa-car fa-3x text-muted mb-3"></i>
                                                <h5 class="text-muted">Chưa có lịch sử đặt xe</h5>
                                                <p class="text-muted">Bạn chưa có hợp đồng thuê xe nào</p>
                                                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                                                    <i class="fa fa-search"></i> Tìm xe ngay
                                                </a>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
