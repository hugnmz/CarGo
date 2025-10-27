<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Liên Hệ - Dịch Vụ Cho Thuê Xe</title>
    <link
      href="${pageContext.request.contextPath}/css/customer/contact.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="container">
      <h1>Liên Hệ Với Chúng Tôi</h1>
      <p class="header-subtitle">Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7</p>

      <div class="contact-grid">
        <!-- Contact Information -->
        <div class="contact-info">
          <h2>Thông Tin Liên Hệ</h2>

          <div class="contact-item">
            <i class="fas fa-map-marker-alt"></i>
            <div class="info">
              <strong>Địa Chỉ</strong>
              <span>123 Đường ABC, Quận 1, TP.HCM</span>
            </div>
          </div>

          <div class="contact-item">
            <i class="fas fa-phone"></i>
            <div class="info">
              <strong>Điện Thoại</strong>
              <span>+84 123 456 789</span>
            </div>
          </div>

          <div class="contact-item">
            <i class="fas fa-envelope"></i>
            <div class="info">
              <strong>Email</strong>
              <span>info@carrental.com</span>
            </div>
          </div>

          <div class="contact-item">
            <i class="fas fa-clock"></i>
            <div class="info">
              <strong>Giờ Làm Việc</strong>
              <span>24/7 - Hỗ trợ khách hàng</span>
            </div>
          </div>
        </div>

        <!-- Contact Form -->
        <div class="contact-form">
          <h2>Gửi Tin Nhắn</h2>
          <form action="#" method="post">
            <div class="form-group">
              <label for="name">Họ và Tên *</label>
              <input type="text" id="name" name="name" required />
            </div>

            <div class="form-group">
              <label for="email">Email *</label>
              <input type="email" id="email" name="email" required />
            </div>

            <div class="form-group">
              <label for="phone">Số Điện Thoại</label>
              <input type="tel" id="phone" name="phone" />
            </div>

            <div class="form-group">
              <label for="subject">Chủ Đề *</label>
              <select id="subject" name="subject" required>
                <option value="">-- Chọn chủ đề --</option>
                <option value="booking">Đặt xe</option>
                <option value="support">Hỗ trợ kỹ thuật</option>
                <option value="complaint">Khiếu nại</option>
                <option value="feedback">Góp ý</option>
                <option value="other">Khác</option>
              </select>
            </div>

            <div class="form-group">
              <label for="message">Nội Dung *</label>
              <textarea
                id="message"
                name="message"
                placeholder="Nhập nội dung tin nhắn của bạn..."
                required
              ></textarea>
            </div>

            <button type="submit" class="submit-btn">
              <i class="fas fa-paper-plane"></i> Gửi Tin Nhắn
            </button>
          </form>
        </div>
      </div>

      <!-- Map Section -->
      <div class="map-section">
        <h2>Vị Trí Của Chúng Tôi</h2>
        <div class="map-container">
          <!-- ✅ Google Map embed -->
          <iframe
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.468857406848!2d106.70042497478457!3d10.776530059245973!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f3ee0a4382f%3A0xf7f91ef92d7a8682!2zMTIzIMSQLiBBQkMsIFF14buRYyAxLCBQaMO6IEjhu5MgQ2jDrSBNaW5oLCBI4buTIENow60gTWluaA!5e0!3m2!1svi!2s!4v1730036480000!5m2!1svi!2s"
            width="100%"
            height="400"
            style="border:0; border-radius:12px;"
            allowfullscreen=""
            loading="lazy"
            referrerpolicy="no-referrer-when-downgrade"
          ></iframe>
        </div>
      </div>

      <!-- FAQ Section -->
      <div class="faq-section">
        <h2>Câu Hỏi Thường Gặp</h2>

        <div class="faq-item">
          <div class="faq-question" onclick="toggleFAQ(this)">
            <i class="fas fa-question-circle"></i> Làm thế nào để đặt xe?
          </div>
          <div class="faq-answer">
            <p>
              Bạn có thể đặt xe trực tuyến thông qua website của chúng tôi hoặc
              gọi hotline 24/7. Chúng tôi sẽ hỗ trợ bạn trong vòng 15 phút.
            </p>
          </div>
        </div>

        <div class="faq-item">
          <div class="faq-question" onclick="toggleFAQ(this)">
            <i class="fas fa-question-circle"></i> Phí thuê xe được tính như thế
            nào?
          </div>
          <div class="faq-answer">
            <p>
              Phí thuê xe được tính theo ngày với mức giá cạnh tranh. Chúng tôi
              có nhiều gói ưu đãi cho khách hàng thuê dài hạn.
            </p>
          </div>
        </div>

        <div class="faq-item">
          <div class="faq-question" onclick="toggleFAQ(this)">
            <i class="fas fa-question-circle"></i> Tôi có thể hủy đặt xe không?
          </div>
          <div class="faq-answer">
            <p>
              Có, bạn có thể hủy đặt xe miễn phí trước 24 giờ. Sau thời gian này
              sẽ có phí hủy theo quy định.
            </p>
          </div>
        </div>
      </div>
    </div>

    <script>
      function toggleFAQ(element) {
        const answer = element.nextElementSibling;
        const isOpen = answer.classList.contains("show");
        document.querySelectorAll(".faq-answer.show").forEach((item) => {
          item.classList.remove("show");
        });
        if (!isOpen) answer.classList.add("show");
      }
    </script>
  </body>
</html>
