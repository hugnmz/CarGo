<%--
    Document   : create_contract_form
    Created on : 16 thg 10, 2025, 10:51:15
    Author     : HOANGNAM
    Purpose    : Form để nhân viên tạo một hợp đồng cho thuê xe mới.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.*, java.util.List, java.util.ArrayList" %>

<%--
    =============================================================================
    GIẢ LẬP DỮ LIỆU CHO DROPDOWN
    - Trong ứng dụng thực tế, 'customerList' và 'vehicleList' sẽ được
      truyền từ Servlet.
    - Ví dụ trong Servlet:
      List<Customers> customers = customerDAO.getAllActiveCustomers();
      request.setAttribute("customerList", customers);
      List<Vehicles> vehicles = vehicleDAO.getAllAvailableVehicles();
      request.setAttribute("vehicleList", vehicles);
    =============================================================================
--%>


<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tạo Hợp Đồng Thuê Xe Mới</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f4f7f6;
                color: #333;
            }
            .container {
                max-width: 900px;
                margin: 30px auto;
                padding: 30px;
                background-color: #fff;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
                border-radius: 12px;
            }
            h1 {
                color: #0056b3;
                text-align: center;
                margin-bottom: 30px;
            }
            .form-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 25px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: bold;
                color: #555;
            }
            .form-group input,
            .form-group select {
                width: 100%;
                padding: 12px;
                border: 1px solid #ccc;
                border-radius: 8px;
                box-sizing: border-box;
                background-color: #fff;
            }
            .form-group input:focus,
            .form-group select:focus {
                border-color: #0056b3;
                box-shadow: 0 0 8px rgba(0, 86, 179, 0.2);
                outline: none;
            }
            .full-width {
                grid-column: 1 / -1;
            }
            .summary-section {
                grid-column: 1 / -1;
                background-color: #eaf2f8;
                padding: 20px;
                border-radius: 8px;
                margin-top: 10px;
            }
            .summary-section h3 {
                margin-top: 0;
                color: #0056b3;
            }
            .summary-line {
                display: flex;
                justify-content: space-between;
                font-size: 1.1em;
                margin-bottom: 10px;
            }
            .summary-line strong {
                font-size: 1.2em;
                color: #d9534f;
            }
            .submit-btn {
                background-color: #0056b3;
                color: white;
                padding: 15px 25px;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 1.1em;
                font-weight: bold;
                width: 100%;
                margin-top: 10px;
            }
            .submit-btn:hover {
                background-color: #004494;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Tạo Hợp Đồng Mới</h1>
            <form action="CreateContractServlet" method="post" id="createContractForm">
                <div class="form-grid">

                    <%-- Thông tin khách hàng và xe --%>
                   <div class="form-group">
    <label for="customerId">Tên Khách Hàng</label>
    <input type="text" id="customerId" name="customerId" placeholder="Nhập tên hoặc mã khách hàng" required>
</div>

                   <div class="form-group">
    <label for="vehicleId">Thông Tin Xe Thuê</label>
    <input type="text" id="vehicleId" name="vehicleId" placeholder="Nhập mã hoặc tên xe" required oninput="updatePriceInfoManual()">
</div>

                    <%-- Thời gian thuê --%>
                    <div class="form-group">
                        <label for="rentStartDate">Thời Gian Nhận Xe</label>
                        <input type="datetime-local" id="rentStartDate" name="rentStartDate" required onchange="calculateTotal()">
                    </div>

                    <div class="form-group">
                        <label for="rentEndDate">Thời Gian Trả Xe</label>
                        <input type="datetime-local" id="rentEndDate" name="rentEndDate" required onchange="calculateTotal()">
                    </div>

                    <%-- Phần tóm tắt chi phí --%>
                    <div class="summary-section">
                        <h3>Tóm Tắt Chi Phí</h3>
                        <div class="summary-line">
                            <span>Đơn giá / ngày:</span>
                            <span id="dailyPriceDisplay">0 VNĐ</span>
                        </div>
                        <div class="summary-line">
                            <span>Số ngày thuê:</span>
                            <span id="rentalDaysDisplay">0 ngày</span>
                        </div>
                        <div class="summary-line">
                            <span>Tiền đặt cọc:</span>
                            <span id="depositAmountDisplay">0 VNĐ</span>
                        </div>
                        <hr>
                        <div class="summary-line">
                            <span><strong>TỔNG TIỀN THUÊ:</strong></span>
                            <strong id="totalAmountDisplay">0 VNĐ</strong>
                        </div>
                    </div>

                    <%-- Nút submit --%>
                    <div class="form-group full-width">
                        <button type="submit" class="submit-btn">Tạo Hợp Đồng</button>
                    </div>
                </div>
            </form>
        </div>

        <script>
            const currencyFormatter = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'});

            function updatePriceInfo() {
                const vehicleSelect = document.getElementById('vehicleId');
                const selectedOption = vehicleSelect.options[vehicleSelect.selectedIndex];

                if (!selectedOption || !selectedOption.dataset.price) {
                    document.getElementById('dailyPriceDisplay').textContent = currencyFormatter.format(0);
                    document.getElementById('depositAmountDisplay').textContent = currencyFormatter.format(0);
                    return;
                }

                const dailyPrice = parseFloat(selectedOption.dataset.price);
                const depositAmount = parseFloat(selectedOption.dataset.deposit);

                document.getElementById('dailyPriceDisplay').textContent = currencyFormatter.format(dailyPrice);
                document.getElementById('depositAmountDisplay').textContent = currencyFormatter.format(depositAmount);

                // Sau khi cập nhật giá, tính toán lại tổng tiền
                calculateTotal();
            }

            function calculateTotal() {
                const startDateInput = document.getElementById('rentStartDate').value;
                const endDateInput = document.getElementById('rentEndDate').value;
                const vehicleSelect = document.getElementById('vehicleId');
                const selectedOption = vehicleSelect.options[vehicleSelect.selectedIndex];

                if (!startDateInput || !endDateInput || !selectedOption.dataset.price) {
                    document.getElementById('totalAmountDisplay').textContent = currencyFormatter.format(0);
                    document.getElementById('rentalDaysDisplay').textContent = '0 ngày';
                    return;
                }

                const startDate = new Date(startDateInput);
                const endDate = new Date(endDateInput);

                if (endDate <= startDate) {
                    document.getElementById('totalAmountDisplay').textContent = currencyFormatter.format(0);
                    document.getElementById('rentalDaysDisplay').textContent = '0 ngày';
                    return;
                }

                const diffTime = Math.abs(endDate - startDate);
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

                const dailyPrice = parseFloat(selectedOption.dataset.price);
                const totalAmount = diffDays * dailyPrice;

                document.getElementById('rentalDaysDisplay').textContent = `${diffDays} ngày`;
                document.getElementById('totalAmountDisplay').textContent = currencyFormatter.format(totalAmount);
            }

            // Gọi lần đầu để khởi tạo giá trị nếu có xe được chọn sẵn
            // (trong trường hợp form load lại với dữ liệu cũ)
            updatePriceInfo();
        </script>
    </body>
</html>