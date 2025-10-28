<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>.qr-code img {max-width:200px;margin:20px auto;display:block;}</style>
</head>
<body>
<div class="container mt-5">
    <h2 class="text-center mb-4">Thanh toán hợp đồng</h2>
    <div class="card">
        <div class="card-body">
            <p>Hợp đồng: <c:out value="${contractId}" /></p>
            <p>Số tiền: <strong><c:out value="${totalAmount}" /> VNĐ</strong></p>
            <div class="qr-code">
                <img src="<c:out value='${qrUrl}' />" alt="QR Code">
            </div>
            <a href="calculateTotalAmount?contractId=${contractId}" class="btn btn-secondary mt-3">Return Car</a>
            <div id="paymentStatus" class="mt-3"></div>
        </div>
    </div>
</div>

<script>
    function checkPayment() {
        const cid = "${contractId}", amt = "${totalAmount}";
        fetch(`/CarGo/checkPayment?contractId=\${cid}&amount=\${amt}`)
            .then(r => r.json())
            .then(d => {
                document.getElementById("paymentStatus").innerHTML =
                    `<div class="alert alert-\${d.status === 'SUCCESS' ? 'success' : 'warning'}">
                        Trạng thái: \${d.status}
                    </div>`;
                if (d.status === "SUCCESS") alert("Thanh toán thành công!");
            })
            .catch(e => console.error(e));
    }
    checkPayment();
    setInterval(checkPayment, 30000);
</script>
</body>
</html>