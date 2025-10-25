<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xử lý trả xe</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container py-4">
            <h2 class="mb-4">Xử lý trả xe - Hợp đồng ${r.contractId}</h2>

            <div class="card mb-4">
                <div class="card-body">
                    <p><strong>Tên KH:</strong> ${r.customerName}</p>
                    <p><strong>SĐT:</strong> ${r.phone}</p>
                    <p><strong>Thời gian mượn:</strong> ${r.borrowAt.format(fmt)}</p>
                    <p><strong>Trả theo HĐ:</strong> ${r.dueAt.format(fmt)}</p>
                    <p><strong>Trả thực tế:</strong> ${r.actualReturn.format(fmt)}</p>
                    <p>
                        <strong>Trạng thái:</strong>
                        <c:choose>
                            <c:when test="${r.late}">Trễ ${r.lateMinutes()} phút</c:when>
                            <c:otherwise>Đúng/Sớm</c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/returns/process">
                <input type="hidden" name="contractId" value="${r.contractId}">

                <div class="mb-3">
                    <label class="form-label">Tiền phạt trả xe muộn</label>
                    <select name="lateFee" class="form-select">
                        <option value="0">Không trễ / Miễn phí</option>
                        <option value="300000">Dưới 1 ngày: 300.000đ</option>
                        <option value="800000">1–3 ngày: 800.000đ</option>
                        <option value="1500000">3–5 ngày: 1.500.000đ</option>
                        <option value="3000000">5–7 ngày: 3.000.000đ</option>
                        <option value="5000000">Trên 7 ngày: 5.000.000đ</option>
                    </select>
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
                        <option value="custom">Khác (tùy chỉnh)</option>
                    </select>
                </div>

                <div class="mb-3" id="customAmountWrapper" style="display: none;">
                    <label class="form-label">Số tiền khác (tùy chỉnh)</label>
                    <input type="number" name="customAmount" class="form-control" min="0" step="10000" placeholder="Nhập số tiền (đ)">
                    <div class="form-text text-muted">Nhập khi thiệt hại không nằm trong danh sách trên.</div>
                </div>




                <div class="mb-3">
                    <label class="form-label">Ghi chú</label>
                    <textarea name="note" class="form-control" rows="3"></textarea>
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-success">Xác nhận hoàn tất</button>
                    <a href="${pageContext.request.contextPath}/returncar" class="btn btn-secondary">Hủy</a>
                </div>
            </form>
        </div>
        <script>
            const damageFeeSelect = document.getElementById('damageFee');
            const customWrapper = document.getElementById('customAmountWrapper');

            damageFeeSelect.addEventListener('change', () => {
                customWrapper.style.display = (damageFeeSelect.value === 'custom') ? 'block' : 'none';
            });
        </script>
    </body>
</html>
