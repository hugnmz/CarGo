<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Đặt xe - CarGo</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/customer/booking-form.css" rel="stylesheet">
    </head>
    <body>
        <%@ include file="/include/header.jsp" %>
        <main  style="padding-top:50px">
            <div class="page-header py-3">
                <div class="container">
                    <!-- khối này được căn giữa và thu hẹp bề ngang -->
                    <div class="d-flex align-items-center justify-content-between mx-auto" style="max-width: 960px;">
                        <div class="text-start">
                            <h1 class="h2 mb-2">
                                <i class="fas fa-calendar-check me-2"></i>Đặt xe
                            </h1>
                            <p class="mb-0 opacity-75">Điền thông tin để hoàn tất đặt xe</p>
                        </div>
                        <a class="btn btn-light" href="${pageContext.request.contextPath}/car-detail?carId=${param.carId}">
                            <i class="fas fa-arrow-left me-2"></i>Quay lại
                        </a>
                    </div>
                </div>
            </div>

            <div class="booking-container">
                <div class="progress-steps">
                    <div class="progress-step">
                        <div class="progress-step-circle">1</div>
                        <small>Chọn xe</small>
                    </div>
                    <div class="progress-step active">
                        <div class="progress-step-circle">2</div>
                        <small>Thông tin</small>
                    </div>
                    <div class="progress-step">
                        <div class="progress-step-circle">3</div>
                        <small>Xác nhận</small>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/Cart" method="post">
                    <input type="hidden" name="_back" value="/customer/booking-form.jsp" />
                    <input type="hidden" name="vehicleId" value="${param.vehicleId}" />
                    <input type="hidden" name="carId" value="${param.carId}" />

                    <%-- Thông báo quy định --%>
                    <div class="alert alert-info alert-modern" style="background: #f0f9ff; border-left: 4px solid #3b82f6;">
                        <h6 class="mb-2"><i class="fas fa-info-circle me-2"></i><strong>Quy định thuê xe</strong></h6>
                        <ul class="mb-0" style="font-size: 0.95rem; line-height: 1.8;">
                            <li><strong>Thuê theo ngày:</strong> Thời gian thuê xe được tính theo ngày, từ 6:00 sáng đến 22:00 đêm.</li>
                            <li><strong>Cùng hợp đồng, cùng thời gian:</strong> Các xe trong cùng một hợp đồng phải có cùng ngày nhận và ngày trả.</li>
                            <li><strong>Thời điểm khác nhau:</strong> Nếu muốn thuê xe trong các thời điểm khác nhau, bạn cần tạo nhiều hợp đồng riêng biệt.</li>
                            <li><strong>Đặt cọc khi nhận xe:</strong> Bạn cần đặt cọc 30.000.000 VNĐ khi đến nhận xe.</li>
                            <li><strong>Thanh toán:</strong> Thanh toán toàn bộ khi trả xe và nhận lại tiền cọc.</li>
                        </ul>
                    </div>

                    <c:if test="${not empty errors}">
                        <div class="alert alert-danger alert-modern">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            <strong>Có lỗi xảy ra:</strong>
                            <ul class="mb-0 mt-2">
                                <c:forEach var="error" items="${errors}">
                                    <li>${error}</li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </c:if>

                    <div class="form-card">
                        <h5 class="form-section-title">
                            <i class="far fa-calendar-alt"></i>Thời gian thuê xe
                        </h5>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label"><i class="far fa-calendar-check"></i>Ngày nhận xe</label>
                                <input type="date" class="form-control" name="startDate" value="${startDate != null ? startDate : param.startDate}" required />
                                <small class="text-muted">Thời gian nhận xe: 6:00 - 22:00</small>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label"><i class="far fa-calendar-times"></i>Ngày trả xe</label>
                                <input type="date" class="form-control" name="endDate" value="${endDate != null ? endDate : param.endDate}" required />
                                <small class="text-muted">Thời gian trả xe: 6:00 - 22:00</small>
                            </div>
                        </div>
                    </div>

                    <div class="form-card">
                        <h5 class="form-section-title">
                            <i class="fas fa-map-marker-alt"></i>Địa điểm nhận và trả xe
                        </h5>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label"><i class="fas fa-map-marker-alt text-success"></i>Địa điểm nhận</label>
                                <select class="form-select" name="pickupLocation" required>
                                    <option value="">-- Chọn địa điểm nhận --</option>
                                    <option value="1" ${param.pickupLocation == '1' ? 'selected' : ''}>Hà Nội - Sân bay Nội Bài</option>
                                    <option value="2" ${param.pickupLocation == '2' ? 'selected' : ''}>TP.HCM - Sân bay Tân Sơn Nhất</option>
                                    <option value="3" ${param.pickupLocation == '3' ? 'selected' : ''}>Đà Nẵng - Trung tâm</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label"><i class="fas fa-map-marker-alt text-danger"></i>Địa điểm trả</label>
                                <select class="form-select" name="returnLocation" required>
                                    <option value="">-- Chọn địa điểm trả --</option>
                                    <option value="1" ${param.returnLocation == '1' ? 'selected' : ''}>Hà Nội - Sân bay Nội Bài</option>
                                    <option value="2" ${param.returnLocation == '2' ? 'selected' : ''}>TP.HCM - Sân bay Tân Sơn Nhất</option>
                                    <option value="3" ${param.returnLocation == '3' ? 'selected' : ''}>Đà Nẵng - Trung tâm</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row g-3 mt-3">
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/ViewCartDetail" class="btn btn-secondary-custom w-100">
                                <i class="fas fa-shopping-cart me-2"></i>Xem giỏ hàng
                            </a>
                        </div>
                        <div class="col-md-6">
                            <button type="submit" class="btn btn-submit">
                                <i class="fas fa-check-circle me-2"></i>Thêm vào giỏ hàng
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </main>
        <%@ include file="/include/footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>