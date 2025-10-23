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
        
        .booking-container {
            max-width: 900px;
            margin: 0 auto;
            padding-bottom: 3rem;
        }
        
        .form-card {
            background: white;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 2px 12px rgba(0,0,0,0.08);
            margin-bottom: 1.5rem;
        }
        
        .form-section-title {
            font-size: 1.25rem;
            font-weight: 700;
            color: var(--dark);
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
        }
        .form-section-title i {
            margin-right: 0.75rem;
            color: var(--primary);
            width: 30px;
            text-align: center;
        }
        
        .form-label {
            font-weight: 600;
            color: var(--dark);
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
        }
        .form-label i {
            margin-right: 0.5rem;
            color: var(--primary);
        }
        
        .form-control, .form-select {
            border: 2px solid #e5e7eb;
            padding: 0.75rem 1rem;
            border-radius: 10px;
            transition: all 0.3s;
        }
        .form-control:focus, .form-select:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(16,185,129,0.1);
        }
        
        .btn-submit {
            background: var(--primary);
            color: white;
            padding: 1rem 2rem;
            border-radius: 12px;
            font-weight: 700;
            font-size: 1.1rem;
            border: none;
            width: 100%;
            transition: all 0.3s;
        }
        .btn-submit:hover {
            background: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(16,185,129,0.3);
        }
        
        .btn-secondary-custom {
            background: white;
            color: var(--gray);
            padding: 1rem 2rem;
            border-radius: 12px;
            font-weight: 600;
            border: 2px solid #e5e7eb;
            transition: all 0.3s;
        }
        .btn-secondary-custom:hover {
            border-color: var(--primary);
            color: var(--primary);
        }
        
        .alert-modern {
            border: none;
            border-radius: 12px;
            padding: 1rem 1.5rem;
            margin-bottom: 1.5rem;
        }
        
        .progress-steps {
            display: flex;
            justify-content: space-between;
            margin-bottom: 2rem;
            position: relative;
        }
        .progress-steps::before {
            content: '';
            position: absolute;
            top: 20px;
            left: 10%;
            right: 10%;
            height: 2px;
            background: #e5e7eb;
        }
        .progress-step {
            text-align: center;
            position: relative;
            flex: 1;
        }
        .progress-step-circle {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: white;
            border: 3px solid #e5e7eb;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 0.5rem;
            font-weight: 700;
            position: relative;
            z-index: 1;
        }
        .progress-step.active .progress-step-circle {
            background: var(--primary);
            border-color: var(--primary);
            color: white;
        }
        .progress-step small {
            font-weight: 600;
            color: var(--gray);
        }
        .progress-step.active small {
            color: var(--primary);
        }
    </style>
</head>
<body>
    <div class="page-header">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h1 class="h2 mb-2"><i class="fas fa-calendar-check me-2"></i>Đặt xe</h1>
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
            <input type="hidden" name="vehicleId" value="${param.vehicleId}" />
            <input type="hidden" name="carId" value="${param.carId}" />

            <%-- Thông báo quy định --%>
            <div class="alert alert-info alert-modern" style="background: #f0f9ff; border-left: 4px solid #3b82f6;">
                <h6 class="mb-2"><i class="fas fa-info-circle me-2"></i><strong>Quy định thuê xe</strong></h6>
                <ul class="mb-0" style="font-size: 0.95rem; line-height: 1.8;">
                    <li><strong>Cùng hợp đồng, cùng thời gian:</strong> Các xe trong cùng một hợp đồng phải có cùng ngày nhận và ngày trả.</li>
                    <li><strong>Thời điểm khác nhau:</strong> Nếu muốn thuê xe trong các thời điểm khác nhau, bạn cần tạo nhiều hợp đồng riêng biệt.</li>
                    <li><strong>Đặt cọc khi nhận xe:</strong> Bạn cần đặt cọc 30% tổng tiền thuê (tối thiểu 500.000 VNĐ) khi đến nhận xe.</li>
                    <li><strong>Thanh toán:</strong> Thanh toán toàn bộ khi trả xe và nhận lại tiền cọc.</li>
                </ul>
            </div>

            <c:if test="${param.error == 'min_1h'}">
                <div class="alert alert-danger alert-modern">
                    <i class="fas fa-exclamation-circle me-2"></i>Thời gian thuê tối thiểu 1 giờ.
                </div>
            </c:if>
            <c:if test="${param.error == 'overlap'}">
                <div class="alert alert-warning alert-modern">
                    <i class="fas fa-exclamation-triangle me-2"></i>Khung giờ đã tồn tại trong giỏ hàng cho xe này.
                </div>
            </c:if>
            <c:if test="${param.error == 'add_failed'}">
                <div class="alert alert-danger alert-modern">
                    <i class="fas fa-times-circle me-2"></i>Không thể thêm vào giỏ hàng. Vui lòng thử lại.
                </div>
            </c:if>
            <c:if test="${param.error == 'past_time'}">
                <div class="alert alert-danger alert-modern">
                    <i class="fas fa-clock me-2"></i>Không thể đặt xe trong quá khứ. Vui lòng chọn thời gian trong tương lai.
                </div>
            </c:if>

            <div class="form-card">
                <h5 class="form-section-title">
                    <i class="far fa-calendar-alt"></i>Thời gian thuê xe
                </h5>
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label"><i class="far fa-calendar-check"></i>Ngày nhận</label>
                        <input type="date" class="form-control" name="startDate" value="${param.startDate}" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label"><i class="far fa-clock"></i>Giờ nhận</label>
                        <input type="time" class="form-control" name="startTime" value="${param.startTime != null ? param.startTime : '09:00'}" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label"><i class="far fa-calendar-times"></i>Ngày trả</label>
                        <input type="date" class="form-control" name="endDate" value="${param.endDate}" required />
                    </div>
                    <div class="col-md-6">
                        <label class="form-label"><i class="far fa-clock"></i>Giờ trả</label>
                        <input type="time" class="form-control" name="endTime" value="${param.endTime != null ? param.endTime : '17:00'}" required />
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
