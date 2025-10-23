<%-- Document : create_contract_form Created on : 16 thg 10, 2025, 10:51:15
Author : HOANGNAM Purpose : Form để nhân viên tạo một hợp đồng cho thuê xe mới.
--%> <%@page contentType="text/html" pageEncoding="UTF-8"%> <%@page
import="model.*, java.util.List, java.util.ArrayList" %> <%--
=============================================================================
GIẢ LẬP DỮ LIỆU CHO DROPDOWN - Trong ứng dụng thực tế, 'customerList' và
'vehicleList' sẽ được truyền từ Servlet. - Ví dụ trong Servlet: List<Customers>
  customers = customerDAO.getAllActiveCustomers();
  request.setAttribute("customerList", customers); List<Vehicles>
    vehicles = vehicleDAO.getAllAvailableVehicles();
    request.setAttribute("vehicleList", vehicles);
    =============================================================================
    --%>

    <!DOCTYPE html>
    <html lang="vi">
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Tạo Hợp Đồng Thuê Xe Mới</title>
        <link
          href="${pageContext.request.contextPath}/css/admin/contract.css"
          rel="stylesheet"
        />
      </head>
      <body>
        <div class="container">
          <h1>Tạo Hợp Đồng Mới</h1>
          <form
            action="CreateContractServlet"
            method="post"
            id="createContractForm"
          >
            <div class="form-grid">
              <%-- Thông tin khách hàng và xe --%>
              <div class="form-group">
                <label for="customerId">Tên Khách Hàng</label>
                <input
                  type="text"
                  id="customerId"
                  name="customerId"
                  placeholder="Nhập tên hoặc mã khách hàng"
                  required
                />
              </div>

              <div class="form-group">
                <label for="vehicleId">Thông Tin Xe Thuê</label>
                <input
                  type="text"
                  id="vehicleId"
                  name="vehicleId"
                  placeholder="Nhập mã hoặc tên xe"
                  required
                  oninput="updatePriceInfoManual()"
                />
              </div>

              <%-- Thời gian thuê --%>
              <div class="form-group">
                <label for="rentStartDate">Thời Gian Nhận Xe</label>
                <input
                  type="datetime-local"
                  id="rentStartDate"
                  name="rentStartDate"
                  required
                  onchange="calculateTotal()"
                />
              </div>

              <div class="form-group">
                <label for="rentEndDate">Thời Gian Trả Xe</label>
                <input
                  type="datetime-local"
                  id="rentEndDate"
                  name="rentEndDate"
                  required
                  onchange="calculateTotal()"
                />
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
                <hr />
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
          const currencyFormatter = new Intl.NumberFormat("vi-VN", {
            style: "currency",
            currency: "VND",
          });

          function updatePriceInfo() {
            const vehicleSelect = document.getElementById("vehicleId");
            const selectedOption =
              vehicleSelect.options[vehicleSelect.selectedIndex];

            if (!selectedOption || !selectedOption.dataset.price) {
              document.getElementById("dailyPriceDisplay").textContent =
                currencyFormatter.format(0);
              document.getElementById("depositAmountDisplay").textContent =
                currencyFormatter.format(0);
              return;
            }

            const dailyPrice = parseFloat(selectedOption.dataset.price);
            const depositAmount = parseFloat(selectedOption.dataset.deposit);

            document.getElementById("dailyPriceDisplay").textContent =
              currencyFormatter.format(dailyPrice);
            document.getElementById("depositAmountDisplay").textContent =
              currencyFormatter.format(depositAmount);

            // Sau khi cập nhật giá, tính toán lại tổng tiền
            calculateTotal();
          }

          function calculateTotal() {
            const startDateInput =
              document.getElementById("rentStartDate").value;
            const endDateInput = document.getElementById("rentEndDate").value;
            const vehicleSelect = document.getElementById("vehicleId");
            const selectedOption =
              vehicleSelect.options[vehicleSelect.selectedIndex];

            if (
              !startDateInput ||
              !endDateInput ||
              !selectedOption.dataset.price
            ) {
              document.getElementById("totalAmountDisplay").textContent =
                currencyFormatter.format(0);
              document.getElementById("rentalDaysDisplay").textContent =
                "0 ngày";
              return;
            }

            const startDate = new Date(startDateInput);
            const endDate = new Date(endDateInput);

            if (endDate <= startDate) {
              document.getElementById("totalAmountDisplay").textContent =
                currencyFormatter.format(0);
              document.getElementById("rentalDaysDisplay").textContent =
                "0 ngày";
              return;
            }

            const diffTime = Math.abs(endDate - startDate);
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

            const dailyPrice = parseFloat(selectedOption.dataset.price);
            const totalAmount = diffDays * dailyPrice;

            document.getElementById(
              "rentalDaysDisplay"
            ).textContent = `${diffDays} ngày`;
            document.getElementById("totalAmountDisplay").textContent =
              currencyFormatter.format(totalAmount);
          }

          // Gọi lần đầu để khởi tạo giá trị nếu có xe được chọn sẵn
          // (trong trường hợp form load lại với dữ liệu cũ)
          updatePriceInfo();
        </script>
      </body>
    </html></Vehicles
  ></Customers
>
