<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Liên Hệ - Dịch Vụ Cho Thuê Xe</title>

  <!-- Bootstrap & Icons cho header/footer dùng chung -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet" />

  <!-- CSS riêng của trang liên hệ -->
  <link href="${pageContext.request.contextPath}/css/customer/contact.css" rel="stylesheet" />

  <style>
    html, body { height: 100%; }
    body {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      padding-top: 88px; /* tránh navbar fixed-top che nội dung; chỉnh theo chiều cao header thực tế */
      background-color: #f8f9fa;
    }
    main { flex: 1 0 auto; }
  </style>
</head>
<body>
  <%@ include file="/include/header.jsp" %>

  <main>
    <div class="container">
      <header>
        <h1>Liên Hệ Với Chúng Tôi</h1>
        <p class="header-subtitle">
          Chúng tôi luôn sẵn sàng lắng nghe bạn. Gửi yêu cầu hoặc ghé thăm văn phòng của chúng tôi.
        </p>
      </header>

      <section class="contact-grid">
        <%-- Form liên hệ bên trái --%>
        <div class="contact-form">
          <h3>Gửi Yêu Cầu Tư Vấn</h3>
          <form action="handleContact" method="post">
            <input type="hidden" name="_back" value="/customer/contact.jsp" />
            <div class="form-group">
              <label for="fullName">Họ và Tên</label>
              <input type="text" id="fullName" name="fullName" placeholder="Ví dụ: Nguyễn Văn A" required />
            </div>
            <div class="form-group">
              <label for="email">Email</label>
              <input type="email" id="email" name="email" placeholder="vidu@email.com" required />
            </div>
            <div class="form-group">
              <label for="phone">Số Điện Thoại</label>
              <input type="tel" id="phone" name="phone" placeholder="0987xxxxxx" required />
            </div>
            <div class="form-group">
              <label for="message">Nội dung</label>
              <textarea id="message" name="message" placeholder="Nội dung bạn muốn trao đổi..." required></textarea>
            </div>
            <button type="submit" class="submit-btn">Gửi Yêu Cầu</button>
          </form>
        </div>

        <%-- Thông tin và bản đồ bên phải --%>
        <aside class="contact-info">
          <h3>Thông Tin Công Ty</h3>
          <div class="info-item">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg>
            <span>Sơn Tây, Hà Nội, Việt Nam</span>
          </div>
          <div class="info-item">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.02.74-.25 1.02l-2.2 2.2z"/></svg>
            <span>(+84) 123 456 789</span>
          </div>
          <div class="info-item">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/></svg>
            <span>hotro@thuexe.com.vn</span>
          </div>

          <div class="map-container">
            <iframe
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d59587.97785318588!2d105.47414336829827!3d21.022738700000003!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31345c589e41e319%3A0x868b1a3c8e54146a!2zSOG7jWMgdmnhu4duIEjDoG5nIGtow7RuZyBWaeG7h3QgTmFt!5e0!3m2!1svi!2s!4v1729050146313!5m2!1svi!2s"
              width="600"
              height="450"
              style="border:0"
              allowfullscreen=""
              loading="lazy"
              referrerpolicy="no-referrer-when-downgrade">
            </iframe>
          </div>
        </aside>
      </section>
    </div>
  </main>

  <%@ include file="/include/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>