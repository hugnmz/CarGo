<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán - Car Rental</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f9f9f9; }
        .payment-card { max-width: 500px; margin: 80px auto; background: #fff; padding: 30px; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); border: 2px solid #28a745; }
        .btn-green { background-color: #28a745; color: white; border: none; }
        .btn-green:hover { background-color: #218838; color: white; }
        .payment-details { padding: 15px; border: 1px solid #ddd; border-radius: 5px; margin-top: 10px; }
        .payment-details.hidden { display: none; }
        .form-check { margin-bottom: 10px; }
    </style>
    <script>
        function toggleDetails(radio) {
            var method = radio.value;
            document.getElementById("credit-card-details").classList.add("hidden");
            document.getElementById("cash-details").classList.add("hidden");
            document.getElementById("banking-details").classList.add("hidden");

            if (method === "2") document.getElementById("credit-card-details").classList.remove("hidden");
            else if (method === "1") document.getElementById("cash-details").classList.remove("hidden");
            else if (method === "3") document.getElementById("banking-details").classList.remove("hidden");
        }

        window.onload = function() {
            const selected = document.querySelector('input[name="methodId"]:checked');
            if (selected) toggleDetails(selected);
        };
    </script>
</head>
<body>
<div class="payment-card">
    <h3 class="text-center text-success mb-4">Thanh toán hợp đồng</h3>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>

    <c:if test="${empty paymentCompleted}">
    <form action="paymentServlet" method="post">
        <input type="hidden" name="contractId" value="${contractId}">
        <div class="mb-3">
            <label class="form-label">Số tiền (VNĐ)</label>
            <input type="number" name="amount" class="form-control" value="${totalAmount}" readonly>
        </div>

        <div class="payment-details">
            <div class="form-check">
                <input type="radio" name="methodId" value="1" onchange="toggleDetails(this)">
                <label class="form-check-label">Tiền mặt</label>
            </div>
            <div id="cash-details" class="payment-details hidden">
                <p class="text-info">Thanh toán trực tiếp tại địa điểm thuê xe.</p>
            </div>
        </div>

        <div class="payment-details">
            <div class="form-check">
                <input type="radio" name="methodId" value="2" onchange="toggleDetails(this)" checked>
                <label class="form-check-label">Thẻ tín dụng</label>
            </div>
            <div id="credit-card-details" class="payment-details">
                <div class="mb-2">
                    <label class="form-label">Số thẻ</label>
                    <input type="text" name="cardNumber" class="form-control" placeholder="1234567812345678">
                </div>
                <div class="mb-2">
                    <label class="form-label">Ngày hết hạn (MM/YY)</label>
                    <input type="text" name="expiryDate" class="form-control" placeholder="MM/YY">
                </div>
                <div class="mb-2">
                    <label class="form-label">CVV</label>
                    <input type="text" name="cvv" class="form-control" placeholder="123">
                </div>
            </div>
        </div>

        <div class="payment-details">
            <div class="form-check">
                <input type="radio" name="methodId" value="3" onchange="toggleDetails(this)">
                <label class="form-check-label">Chuyển khoản</label>
            </div>
            <div id="banking-details" class="payment-details hidden">
                <p class="text-info">Quét mã QR để chuyển khoản:</p>
                <img src="https://via.placeholder.com/150?text=QR+Code" class="img-fluid">
                <p class="text-muted mt-2">Số tiền: ${totalAmount} VNĐ</p>
            </div>
        </div>

        <button type="submit" class="btn btn-green w-100 mt-3">Thanh toán ngay</button>
    </form>
    </c:if>

    <div class="mt-3 text-center">
        <a href="contract-detail.jsp?contractId=${contractId}" class="btn btn-outline-secondary">Quay lại</a>
    </div>
</div>
</body>
</html>
