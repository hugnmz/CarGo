
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xử lý trả xe</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background: #f7f7f9;
            }
            a{
                text-decoration: none;
                color: white;
            }
            .navbar {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }

            .navbar-brand {
                font-weight: bold;
                color: white !important;
            }

            .staff-header {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                color: white;
                padding: 2rem 0;
                margin-bottom: 2rem;
                border-radius: 15px;
            }
            .staff-header {
                background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                color: white;
                padding: 2rem 0;
                margin-bottom: 2rem;
                border-radius: 15px;
            }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/staff/staff.jsp">
                    <i class="fas fa-car"></i> CarGo Staff Dashboard
                </a>
                <div class="navbar-nav ms-auto">
                    <span class="navbar-text me-3">
                        <i class="fas fa-user"></i> Staff ID: 001
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

                <div class="card mb-4">
                    <div class="card-body">
                        <p><strong>Tên KH:</strong> ${currentRequest.getContract().getCustomerName()}</p>
                        <p><strong>SĐT:</strong> ${currentRequest.getContract().getCustomerPhone()}</p>
                        <p><strong>Thời gian mượn:</strong> ${currentRequest.getContract().startDateToString()}</p>
                        <p><strong>Trả theo HĐ:</strong> ${currentRequest.getContract().endDateToString()}</p>
                        <p><strong>Trả thực tế:</strong> ${currentRequest.timeRequestToString()}</p>
                        <p>
                            <strong>Trạng thái:</strong>
                            <c:choose>
                                <c:when test="${currentRequest.late}"><span style="color: red">Trễ ${currentRequest.lateTime()}</span></c:when>
                                <c:otherwise><span style="color: green">Đúng/Sớm </span></c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                        <c:forEach var="v" items="${currentRequest.getContract().getContractDetails()}">
                        <div>
                            <p><strong>Tên xe:</strong> ${v.getCarName()}</p>
                        <p><strong>Giá:</strong> ${v.getPrice()}</p>
                        <p><strong>Biển số:</strong> ${v.getPlateNumber()}</p>
                       
                        </div>
                        </c:forEach>
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
                        <input type="number" name="customAmountLateFee" class="form-control" min="0" step="10000" placeholder="Nhập số tiền (đ)">
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
                        <input type="number" name="customAmountDamageFee" class="form-control" min="0" step="10000" placeholder="Nhập số tiền (đ)">
                        <div class="form-text text-muted">Nhập khi thiệt hại không nằm trong danh sách trên.</div>
                    </div>




                    <div class="mb-3">
                        <label class="form-label">Ghi chú</label>
                        <textarea name="note" class="form-control" rows="3"></textarea>
                    </div>

                    <div class="text-end">
                        <input type="hidden" name="confirm" value="1" />
                        <input type="hidden" name="csrf" value="${sessionScope.csrf}">
                        <button type="submit" class="btn btn-success">Xác nhận hoàn tất</button>
                        <a href="${pageContext.request.contextPath}/returncar" class="btn btn-secondary">Hủy</a>
                    </div>
                </form>
            </div>
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




            </script>
    </body>
</html>
