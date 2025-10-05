<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Trang Chủ - CarGo</title>
    <style>
      body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }
      .header { background-color: #2c3e50; color: white; padding: 1rem; text-align: center; }
      .container { max-width: 1200px; margin: 0 auto; padding: 2rem; }
      .card { background-color: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,.1); margin-bottom: 2rem; }
      .btn { background-color: #3498db; color: white; padding: .5rem 1rem; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; margin-right: 1rem; }
      .btn:hover { background-color: #2980b9; }
      .btn-danger { background-color: #e74c3c; }
      .btn-danger:hover { background-color: #c0392b; }
    </style>
  </head>
  <body>
    <div class="header">
      <h1>🚗 CarGo - Hệ Thống Thuê Xe</h1>
    </div>

    <div class="container">
      <!-- Khối chào mừng & hành động -->
      <div class="card">
        <h2>Chào mừng đến với CarGo!</h2>

        <c:choose>
          <c:when test="${not empty sessionScope.username}">
            <p><strong>Xin chào, ${sessionScope.username}!</strong></p>

            <div>
              <a href="javascript:void(0)" class="btn" onclick="alert('Tính năng đang phát triển')">Xem Xe Có Sẵn</a>
              <a href="javascript:void(0)" class="btn" onclick="alert('Tính năng đang phát triển')">Giỏ Hàng</a>
              <a href="javascript:void(0)" class="btn" onclick="alert('Tính năng đang phát triển')">Lịch Sử Thuê</a>
              <a href="${pageContext.request.contextPath}/logout.jsp" class="btn btn-danger">Đăng Xuất</a>
            </div>
          </c:when>

          <c:otherwise>
            <p>Bạn chưa đăng nhập.
              <a href="${pageContext.request.contextPath}/login.jsp">Đăng nhập</a>
            </p>
          </c:otherwise>
        </c:choose>
      </div>

      <!-- Tính năng -->
      <div class="card">
        <h3>🎯 Các Tính Năng Chính</h3>
        <ul>
          <li>🔍 <strong>Tìm kiếm xe:</strong> Tìm xe phù hợp với nhu cầu</li>
          <li>🛒 <strong>Đặt xe:</strong> Thêm xe vào giỏ hàng</li>
          <li>📋 <strong>Quản lý đơn hàng:</strong> Theo dõi lịch sử thuê xe</li>
          <li>💳 <strong>Thanh toán:</strong> Hỗ trợ nhiều phương thức thanh toán</li>
          <li>⭐ <strong>Đánh giá:</strong> Chia sẻ trải nghiệm thuê xe</li>
        </ul>
      </div>
    </div>
  </body>
</html>
