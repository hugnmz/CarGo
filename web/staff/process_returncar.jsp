
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xử lý trả xe</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/staff/process_returncar.css"/>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/staff/staff.jsp">
                    <i class="fas fa-car"></i> CarGo Staff Dashboard
                </a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        <i class="fas fa-user" name="staffId"></i> Staff ID: ${sessionScope.staffId}
                    </span>
                    <a class="btn btn-outline-light" href="#">
                        <i class="fas fa-sign-out-alt"></i> Logout
                    </a>
                </div>
            </div>
        </nav>
        <div class="container py-4">

            <div class="staff-header text-center">
                <h1><i class="fas fa-tachometer-alt"></i> Staff Dashboard</h1>
                <p class="mb-0">Xem và xử lý hợp đồng từ khách hàng</p>
            </div>


            <div class="container py-4">
                <h2 class="mb-4">Xử lý trả xe - Chi tiết hợp đồng mã: #${currentRequest.getContract().getContractId()}</h2>

                <div class="contract-wrapper">
                    <div class="row g-4 align-items-start">
                        <!-- Cột trái: Thông tin khách hàng -->
                        <div class="col-12 col-lg-6">
                            <h5 class="contract-header">Thông tin khách hàng</h5>
                            <p><span class="info-label">Tên KH:</span> ${currentRequest.getContract().getCustomerName()}</p>
                            <p><span class="info-label">SĐT:</span> ${currentRequest.getContract().getCustomerPhone()}</p>
                            <p><span class="info-label">Thời gian mượn:</span> ${currentRequest.getContract().startDateToString()}</p>
                            <p><span class="info-label">Trả theo HĐ:</span> ${currentRequest.getContract().endDateToString()}</p>
                            <p><span class="info-label">Trả thực tế:</span> ${currentRequest.timeRequestToString()}</p>
                            <p>
                                <span class="info-label">Trạng thái:</span>
                                <c:choose>
                                    <c:when test="${currentRequest.late}">
                                        <span class="status-late">Trễ ${currentRequest.lateTime()}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-ontime">Đúng/Sớm</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>

                        <!-- Cột phải: Thông tin xe -->
                        <div class="col-12 col-lg-6">
                            <h5 class="contract-header">Thông tin xe</h5>
                            <c:forEach var="v" items="${currentRequest.getContract().getContractDetails()}">
                                <div class="vehicle-card">
                                    <p><span class="info-label">Tên xe:</span> ${v.name}</p>
                                    <p><span class="info-label">Giá:</span> ${v.price}</p>
                                    <p><span class="info-label">Biển số:</span> ${v.plateNumber}</p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <form method="post" action="${pageContext.request.contextPath}/processreturncar">
                    <input type="hidden" name="contractId" value="${currentRequest.getContract().getContractId()}">
                    <input type="hidden" name="staffId" value="001">
                    <div class="mb-3">
                        <label class="form-label">Tiền phạt trả xe muộn</label>
                        <select name="lateFee" id="lateFee" class="form-select">
                            <option value="0">Không trễ / Miễn phí</option>
                            <option value="300000">Dưới 1 ngày: 300.000đ</option>
                            <option value="800000">1–3 ngày: 800.000đ</option>
                            <option value="1500000">3–5 ngày: 1.500.000đ</option>
                            <option value="3000000">5–7 ngày: 3.000.000đ</option>
                            <option value="5000000">Trên 7 ngày: 5.000.000đ</option>
                            <option value="custom_lateFee">Khác </option>
                        </select>
                    </div>
                    <div class="mb-3" id="customAmountWrapper1" style="display: none;">
                        <label class="form-label">Số tiền phạt muộn khác </label>
                        <input type="number" name="customAmountLateFee" class="form-control" min="0" step="1000" placeholder="Nhập số tiền (đ)">
                        <div class="form-text text-muted">Nhập khi tiền phạt không nằm trong danh sách trên.</div>
                    </div>


                    <div class="mb-3">
                        <label class="form-label">Mức độ hư hại</label>
                        <select name="damageFee" class="form-select" id="damageFee">
                            <option value="0">Không hư hại (0đ)</option>
                            <option value="300000">Trầy xước nhẹ / Bong sơn (300.000đ)</option>
                            <option value="800000">Móp nhẹ / Rách nội thất (800.000đ)</option>
                            <option value="2000000">Nứt đèn / Hư cảm biến / Vỡ gương (2.000.000đ)</option>
                            <option value="5000000">Biến dạng thân xe / Hư điện / Máy (5.000.000đ)</option>
                            <option value="10000000">Tai nạn nghiêm trọng (≥10.000.000đ)</option>
                            <option value="custom_damageFee">Khác </option>
                        </select>
                    </div>

                    <div class="mb-3" id="customAmountWrapper2" style="display: none;">
                        <label class="form-label">Số tiền hư hại khác </label>
                        <input type="number" name="customAmountDamageFee" class="form-control" min="0" step="1000" placeholder="Nhập số tiền (đ)">
                        <div class="form-text text-muted">Nhập khi thiệt hại không nằm trong danh sách trên.</div>
                    </div>




                    <div class="mb-3">
                        <label class="form-label">Ghi chú</label>
                        <textarea name="note" class="form-control" rows="3"></textarea>
                    </div>

                    <div class="text-end">
                        <input type="hidden" name="confirm" value="1" />
                        <input type="hidden" name="csrf" value="${sessionScope.csrf}">
                        <button type="button" id="confirmBtn" class="btn btn-success">Xác nhận hoàn tất</button>
                        <a href="${pageContext.request.contextPath}/returncar" class="btn btn-secondary">Hủy</a>
                    </div>
                </form>
            </div>
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

            <script>
                //nhập số tiền phạt muộn khác
                document.getElementById("lateFee").addEventListener("change", function () {
                    const wrapper = document.getElementById("customAmountWrapper1");
                    if (this.value === "custom_lateFee") {
                        wrapper.style.display = "block";
                    } else {
                        wrapper.style.display = "none";
                    }
                });
                //nhập số tiền phạt hư hỏng khác
                document.getElementById("damageFee").addEventListener("change", function () {
                    const wrapper = document.getElementById("customAmountWrapper2");
                    if (this.value === "custom_damageFee") {
                        wrapper.style.display = "block";
                    } else {
                        wrapper.style.display = "none";
                    }
                });
                //hộp thoại xác nhận thông tin
                document.getElementById("confirmBtn").addEventListener("click", function () {
                    // Lấy dữ liệu từ form (ví dụ totalAmount và note)

                    function getDamageFee() {
                        const select = document.getElementById("damageFee");
                        let value = 0;
                        if (select.value === "custom_damageFee") {
                            // Lấy giá trị người dùng nhập vào ô input khác
                            const custom = document.querySelector("[name='customAmountDamageFee']");
                            value = parseInt(custom?.value) || 0;
                        } else {
                            // Lấy giá trị bình thường từ select
                            value = parseInt(select.value) || 0;
                        }

                        return value;
                    }

                    function getLateFee() {
                        const select = document.getElementById("lateFee");
                        let value = 0;
                        if (select.value === "custom_lateFee") {
                            // Lấy giá trị người dùng nhập vào ô input khác
                            const custom = document.querySelector("[name='customAmountLateFee']");
                            value = parseInt(custom?.value) || 0;
                        } else {
                            // Lấy giá trị bình thường từ select
                            value = parseInt(select.value) || 0;
                        }

                        return value;
                    }

                    const damageFee = getDamageFee();
                    const lateFee = getLateFee();
                    const note = document.querySelector("textarea[name='note']").value || "(Không có ghi chú)";
                    const totalAmount = damageFee + lateFee;
                    const contractId = "${currentRequest.getContract().getContractId()}";
                    const html =
                            '<p><strong>Mã hợp đồng:</strong> ' + contractId + '</p>' +
                            '<p><strong>Tổng phí:</strong> ' + totalAmount.toLocaleString() + 'đ</p>' +
                            '<p><strong>Ghi chú:</strong> ' + note + '</p>' +
                            '<p class="text-danger mt-2"><i class="fas fa-exclamation-triangle"></i> Bạn có chắc chắn muốn xác nhận hoàn tất không?</p>';
                    Swal.fire({
                        title: "Xác nhận hoàn tất?",
                        html: html,
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonText: "Có, xác nhận!",
                        cancelButtonText: "Hủy",
                        reverseButtons: true,
                        focusCancel: true
                    }).then((result) => {
                        if (result.isConfirmed) {
                            document.querySelector("form").submit();
                        }
                    });
                });




            </script>
    </body>
</html>
