<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@page
import="java.time.LocalDateTime, java.time.format.DateTimeFormatter"%> <% String
customerName = request.getParameter("customerName"); String customerPhone =
request.getParameter("customerPhone"); String customerAddress =
request.getParameter("customerAddress"); String carName =
request.getParameter("carName"); String plateNumber =
request.getParameter("plateNumber"); String rentStart =
request.getParameter("rentStart"); String rentEnd =
request.getParameter("rentEnd"); String pricePerDay =
request.getParameter("pricePerDay"); String deposit =
request.getParameter("deposit"); String staffName =
request.getParameter("staffName"); DateTimeFormatter inputFormat =
DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"); DateTimeFormatter
outputFormat = DateTimeFormatter.ofPattern("HH:mm 'ngày' dd 'tháng' MM 'năm'
yyyy"); LocalDateTime startDate = (rentStart != null && !rentStart.isEmpty()) ?
LocalDateTime.parse(rentStart, inputFormat) : null; LocalDateTime endDate =
(rentEnd != null && !rentEnd.isEmpty()) ? LocalDateTime.parse(rentEnd,
inputFormat) : null; %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <title>Hợp Đồng Thuê Xe Ô Tô</title>
    <link
      href="${pageContext.request.contextPath}/css/admin/contractDetail.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="page-container">
      <div class="confirmation-box">
        <strong>✅ Hợp đồng đã được tạo thành công!</strong><br />
        Mã hợp đồng: <strong>HD-<%= System.currentTimeMillis() %></strong>
      </div>

      <div class="contract-header">
        <div class="contract-title">Hợp Đồng Thuê Xe Ô Tô</div>
        <div class="contract-subtitle">Car Rental Agreement</div>
        <div class="contract-number">HD-<%= System.currentTimeMillis() %></div>
      </div>

      <div class="section">
        <div class="section-title">Thông Tin Khách Hàng</div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Họ và tên:</span>
            <span class="info-value"
              ><%= customerName != null ? customerName : "N/A" %></span
            >
          </div>
          <div class="info-item">
            <span class="info-label">Số điện thoại:</span>
            <span class="info-value"
              ><%= customerPhone != null ? customerPhone : "N/A" %></span
            >
          </div>
          <div class="info-item">
            <span class="info-label">Địa chỉ:</span>
            <span class="info-value"
              ><%= customerAddress != null ? customerAddress : "N/A" %></span
            >
          </div>
        </div>
      </div>

      <div class="section">
        <div class="section-title">Thông Tin Xe</div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Tên xe:</span>
            <span class="info-value"
              ><%= carName != null ? carName : "N/A" %></span
            >
          </div>
          <div class="info-item">
            <span class="info-label">Biển số:</span>
            <span class="info-value"
              ><%= plateNumber != null ? plateNumber : "N/A" %></span
            >
          </div>
          <div class="info-item">
            <span class="info-label">Giá thuê/ngày:</span>
            <span class="info-value"
              ><%= pricePerDay != null ? pricePerDay + " VNĐ" : "N/A" %></span
            >
          </div>
        </div>
      </div>

      <div class="section">
        <div class="section-title">Thời Gian Thuê</div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Ngày bắt đầu:</span>
            <span class="info-value"
              ><%= startDate != null ? startDate.format(outputFormat) : "N/A"
              %></span
            >
          </div>
          <div class="info-item">
            <span class="info-label">Ngày kết thúc:</span>
            <span class="info-value"
              ><%= endDate != null ? endDate.format(outputFormat) : "N/A"
              %></span
            >
          </div>
        </div>
      </div>

      <div class="section">
        <div class="section-title">Thông Tin Tài Chính</div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Tiền cọc:</span>
            <span class="info-value"
              ><%= deposit != null ? deposit + " VNĐ" : "N/A" %></span
            >
          </div>
          <div class="info-item">
            <span class="info-label">Nhân viên tạo:</span>
            <span class="info-value"
              ><%= staffName != null ? staffName : "N/A" %></span
            >
          </div>
        </div>
      </div>

      <div class="terms-section">
        <div class="terms-title">Điều Khoản Và Điều Kiện</div>
        <ul class="terms-list">
          <li>
            Khách hàng cam kết sử dụng xe đúng mục đích và tuân thủ luật giao
            thông.
          </li>
          <li>Khách hàng chịu trách nhiệm bảo quản xe trong thời gian thuê.</li>
          <li>
            Mọi hư hỏng do sử dụng không đúng cách sẽ được khách hàng thanh
            toán.
          </li>
          <li>Hợp đồng có hiệu lực từ ngày ký và kết thúc khi trả xe.</li>
        </ul>
      </div>

      <div class="signature-section">
        <div class="signature-box">
          <div class="signature-title">Khách Hàng</div>
          <div class="signature-line"></div>
          <div class="signature-title">Ký tên</div>
        </div>
        <div class="signature-box">
          <div class="signature-title">Nhân Viên</div>
          <div class="signature-line"></div>
          <div class="signature-title">Ký tên</div>
        </div>
      </div>

      <div class="actions">
        <button class="print-btn" onclick="window.print()">
          🖨️ In Hợp Đồng
        </button>
      </div>
    </div>
  </body>
</html>
