<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ page
contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <title>Đặt xe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/customer/booking-form.css" rel="stylesheet" />
  </head>
  <body>
    <form
      class="booking-wrapper"
      action="${pageContext.request.contextPath}/Cart"
      method="post"
    >
      <div
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;
        "
      >
        <h3 style="margin: 0">Đặt xe</h3>
        <a
                  href="${pageContext.request.contextPath}/car-detail?carId=${param.carId}"
          style="
            text-decoration: none;
            padding: 8px 12px;
            border: 1px solid #0d6efd;
            color: #0d6efd;
            border-radius: 6px;
          "
        >
          Quay lại xe
        </a>
      </div>
      <input type="hidden" name="vehicleId" value="${param.vehicleId}" />
      <input type="hidden" name="carId" value="${param.carId}" />

      <c:if test="${param.error == 'min_1h'}">
        <div class="alert alert-danger">Thời gian thuê tối thiểu 1 giờ.</div>
      </c:if>
      <c:if test="${param.error == 'overlap'}">
        <div class="alert alert-danger">
          Khung giờ đã tồn tại trong giỏ hàng cho xe này.
        </div>
      </c:if>
      <c:if test="${param.error == 'add_failed'}">
        <div class="alert alert-danger">
          Không thể thêm vào giỏ hàng. Vui lòng thử lại.
        </div>
      </c:if>
      <c:if test="${param.error == 'past_time'}">
        <div class="alert alert-danger">
          Không thể đặt xe trong quá khứ. Vui lòng chọn thời gian trong tương
          lai.
        </div>
      </c:if>

      <label>Ngày nhận</label>
      <input type="date" name="startDate" value="${param.startDate}" required />

      <label>Giờ nhận</label>
      <input type="time" name="startTime" value="${param.startTime != null ? param.startTime : '09:00'}" required />

      <label>Ngày trả</label>
      <input type="date" name="endDate" value="${param.endDate}" required />

      <label>Giờ trả</label>
      <input type="time" name="endTime" value="${param.endTime != null ? param.endTime : '17:00'}" required />

      <label>Địa điểm nhận</label>
      <select name="pickupLocation">
        <option value="">-- Chọn --</option>
        <option value="1" ${param.pickupLocation == '1' ? 'selected' : ''}>Hà Nội - Nội Bài</option>
        <option value="2" ${param.pickupLocation == '2' ? 'selected' : ''}>TP.HCM - Tân Sơn Nhất</option>
        <option value="3" ${param.pickupLocation == '3' ? 'selected' : ''}>Đà Nẵng</option>
      </select>

      <label>Địa điểm trả</label>
      <select name="returnLocation">
        <option value="">-- Chọn --</option>
        <option value="1" ${param.returnLocation == '1' ? 'selected' : ''}>Hà Nội - Nội Bài</option>
        <option value="2" ${param.returnLocation == '2' ? 'selected' : ''}>TP.HCM - Tân Sơn Nhất</option>
        <option value="3" ${param.returnLocation == '3' ? 'selected' : ''}>Đà Nẵng</option>
      </select>

      <div style="display: flex; gap: 10px; margin-top: 16px;">
        <button type="submit" class="btn btn-success">
          <i class="fa fa-shopping-cart me-1"></i>
          Thêm vào giỏ hàng
        </button>
        <a href="${pageContext.request.contextPath}/ViewCartDetail?carId=${param.carId}&vehicleId=${param.vehicleId}" class="btn btn-outline-primary">
          <i class="fa fa-eye me-1"></i>
          Xem giỏ hàng
        </a>
      </div>
    </form>
  </body>
</html>
