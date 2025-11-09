<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thanh toán #${contractId}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            .qr-img {
                width:220px;
                height:220px;
                border:10px solid white;
                border-radius:12px;
                box-shadow:0 5px 15px rgba(0,0,0,0.2);
            }
            .amount {
                font-size:2rem;
                font-weight:700;
                color:#dc3545;
            }
            .spinner {
                width:1.2rem;
                height:1.2rem;
                border:2px solid #f3f3f3;
                border-top:2px solid #007bff;
                border-radius:50%;
                animation: spin 1s linear infinite;
                display:inline-block;
                margin-right:8px;
            }
            @keyframes spin {
                to {
                    transform: rotate(360deg);
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="card mt-4">
                <div class="card-header text-center py-3">
                    <h4 class="mb-0">Thanh toán hợp đồng #${contractId}</h4>
                </div>
                <div class="card-body text-center p-4">
                    <c:if test="${initialStatus ne 'SUCCESS'}">
                        <img id="qr" src="${qrUrl}" alt="QR Code" class="qr-img mb-4">
                    </c:if>

                    <p class="mb-1"><strong>Loại thanh toán:</strong></p>
                    <p class="text-info fw-bold">
                        <c:choose>
                            <c:when test="${payType eq 'deposit'}">Đặt cọc</c:when>
                            <c:when test="${payType eq 'full'}">Thanh toán phần còn lại</c:when>
                            <c:otherwise>Không xác định</c:otherwise>
                        </c:choose>
                    </p>

                    <p class="mb-1"><strong>Số tiền:</strong></p>
                    <p class="amount">${totalAmount} VNĐ</p>

                    <div id="status" class="mt-4">
                        <c:choose>
                            <c:when test="${initialStatus eq 'SUCCESS'}">
                                <div class="text-success text-center p-4">
                                    <i class="fas fa-check-circle fa-4x mb-3"></i>
                                    <h4 class="fw-bold">THÀNH CÔNG!</h4>
                                    <p>Hợp đồng #${contractId} đã được xác nhận thanh toán.</p>
                                    <p class="text-muted">Đang chuyển về trang xem hợp đồng...</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-warning"><span class="spinner"></span> Đang chờ thanh toán...</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <script>
    const contractId = "${contractId}";
    const initialStatus = "${initialStatus}";
    const amountToPay = "${totalAmount}";
    const payType = "${payType}";
    let intervalId = null;

    async function check() {
        try {
            const response = await fetch("${pageContext.request.contextPath}/checkPayment?contractId=" + contractId + "&t=" + Date.now(), {cache: "no-store"});
            const data = await response.json();
            console.log("[checkPayment]", data);

            if (data.status === "SUCCESS" || data.status === "PARTIAL") {
                if (intervalId)
                    clearInterval(intervalId);
                const qr = document.getElementById("qr");
                if (qr)
                    qr.style.display = "none";

                document.getElementById("status").innerHTML = `
                <div class="text-success text-center p-4">
                    <i class="fas fa-check-circle fa-4x mb-3"></i>
                    <h4 class="fw-bold">THÀNH CÔNG!</h4>
                    <p>${payType=='deposit' ? 'Đặt cọc' : 'Thanh toán còn lại'} đã được thanh toán: ${amountToPay} VNĐ</p>
                    <p class="text-muted">Đang chuyển về trang xem hợp đồng...</p>
                </div>`;

                setTimeout(() => location.href = "${pageContext.request.contextPath}/view-contract?contractId=" + contractId, 3000);
            } else if (data.status === "PENDING") {
                console.log("⏳ Đang chờ thanh toán...");
            } else {
                console.warn("⚠️", data.message);
            }
        } catch (err) {
            console.error("Lỗi khi gọi checkPayment:", err);
        }
    }

    window.onload = () => {
        if (initialStatus === "SUCCESS") {
            setTimeout(() => location.href = "${pageContext.request.contextPath}/view-contract?contractId=" + contractId, 1000);
        } else {
            check();
            intervalId = setInterval(check, 5000);
        }
    };
        </script>
    </body>
</html>