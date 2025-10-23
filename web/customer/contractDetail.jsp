<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Car Rental Contract</title>
    <link
      href="${pageContext.request.contextPath}/css/admin/contractDetail.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="page-container">
      <div class="confirmation-box">
        <strong>✅ Contract has been created successfully!</strong><br />
        Contract ID: <strong>HD-1234567890</strong>
      </div>

      <div class="contract-header">
        <div class="contract-title">Car Rental Contract</div>
        <div class="contract-subtitle">Car Rental Agreement</div>
        <div class="contract-number">HD-1234567890</div>
      </div>

      <div class="section">
        <div class="section-title">Customer Information</div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Full Name:</span>
            <span class="info-value">John Doe</span>
          </div>
          <div class="info-item">
            <span class="info-label">Phone Number:</span>
            <span class="info-value">+84 123 456 789</span>
          </div>
          <div class="info-item">
            <span class="info-label">Address:</span>
            <span class="info-value">123 Main Street, Ho Chi Minh City</span>
          </div>
        </div>
      </div>

      <div class="section">
        <div class="section-title">Vehicle Information</div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Car Name:</span>
            <span class="info-value">Toyota Camry 2023</span>
          </div>
          <div class="info-item">
            <span class="info-label">License Plate:</span>
            <span class="info-value">51A-12345</span>
          </div>
          <div class="info-item">
            <span class="info-label">Daily Rental Price:</span>
            <span class="info-value">500,000 VND</span>
          </div>
        </div>
      </div>

      <div class="section">
        <div class="section-title">Rental Period</div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Start Date:</span>
            <span class="info-value">2024-01-15</span>
          </div>
          <div class="info-item">
            <span class="info-label">End Date:</span>
            <span class="info-value">2024-01-20</span>
          </div>
        </div>
      </div>

      <div class="section">
        <div class="section-title">Financial Information</div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">Deposit:</span>
            <span class="info-value">1,000,000 VND</span>
          </div>
          <div class="info-item">
            <span class="info-label">Created by Staff:</span>
            <span class="info-value">Jane Smith</span>
          </div>
        </div>
      </div>

      <div class="terms-section">
        <div class="terms-title">Terms and Conditions</div>
        <ul class="terms-list">
          <li>
            The customer commits to using the vehicle for its intended purpose
            and complying with traffic laws.
          </li>
          <li>
            The customer is responsible for maintaining the vehicle during the
            rental period.
          </li>
          <li>
            Any damage caused by improper use will be paid for by the customer.
          </li>
          <li>
            The contract is effective from the signing date and ends when the
            vehicle is returned.
          </li>
        </ul>
      </div>

      <div class="signature-section">
        <div class="signature-box">
          <div class="signature-title">Customer</div>
          <div class="signature-line"></div>
          <div class="signature-title">Signature</div>
        </div>
        <div class="signature-box">
          <div class="signature-title">Staff</div>
          <div class="signature-line"></div>
          <div class="signature-title">Signature</div>
        </div>
      </div>

      <div class="actions">
        <button class="print-btn" onclick="window.print()">
          🖨️ Print Contract
        </button>
      </div>
    </div>
  </body>
</html>
