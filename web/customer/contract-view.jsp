<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
    prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ page
        contentType="text/html; charset=UTF-8" language="java" %>
        <!DOCTYPE html>
        <html lang="vi">
            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <title>Chi tiết hợp đồng #${contract.contractId} - CarGo</title>
                <link
                    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                    rel="stylesheet"
                    />
                <link
                    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
                    rel="stylesheet"
                    />
                <link
                    href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap"
                    rel="stylesheet"
                    />
                <link
                    href="${pageContext.request.contextPath}/css/customer/contract-view.css?v=2"
                    rel="stylesheet"
                    />
            </head>
            <body>
                <div class="page-header">
                    <div class="container">
                        <div
                            class="d-flex justify-content-between align-items-center flex-wrap"
                            >
                            <div>
                                <h1 class="h2 mb-2">
                                    <i class="fas fa-file-contract me-2"></i>Hợp đồng
                                    #${contract.contractId}
                                </h1>
                                <p class="mb-0 opacity-75">Chi tiết đầy đủ về hợp đồng thuê xe</p>
                            </div>
                            <div class="d-flex gap-2 mt-3 mt-md-0 toolbar">
                                <c:set var="backUrl" value="${pageContext.request.contextPath}/my-contracts" />
                                <c:if test="${sessionScope.roleName == 'STAFF' || sessionScope.userType == 'STAFF'}">
                                    <c:set var="backUrl" value="${pageContext.request.contextPath}/staff" />
                                </c:if>
                                <a class="btn btn-back" href="${backUrl}">
                                    <i class="fas fa-arrow-left me-2"></i>Danh sách HĐ
                                </a>
                                <a
                                    class="btn btn-back"
                                    href="${pageContext.request.contextPath}/home"
                                    >
                                    <i class="fas fa-home me-2"></i>Trang chủ
                                </a>
                                <button
                                    type="button"
                                    class="btn btn-back btn-print"
                                    onclick="window.print()"
                                    >
                                    <i class="fas fa-print me-2"></i>In hợp đồng
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-success" role="alert">
                        ${sessionScope.message}
                    </div>
                    <c:remove var="message" scope="session" />
                </c:if>
                <c:if test="${not empty sessionScope.error}">
                    <div
                        class="alert alert-danger"
                        role="alert"
                        id="errorBox"
                        >
                        ${sessionScope.error}
                    </div>

                    <c:remove var="error" scope="session" />
                </c:if>

                <div class="container pb-5">
                    <div class="row g-4">
                        <!-- Cột trái: Thông tin hợp đồng -->
                        <div class="col-lg-4">
                            <div class="info-card mb-3">
                                <h5 class="fw-bold mb-4">Thông tin hợp đồng</h5>
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="fas fa-hashtag"></i>Mã hợp đồng</span
                                    >
                                    <span class="info-value">#${contract.contractId}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="fas fa-user"></i>Khách hàng</span
                                    >
                                    <span class="info-value"
                                          ><strong>${contract.customerName}</strong></span
                                    >
                                </div>
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="fas fa-info-circle"></i>Trạng thái</span
                                    >
                                    <c:set
                                        var="statusClass"
                                        value="${contract.status == 'PENDING' ? 'status-pending' : contract.status == 'ACCEPTED' ? 'status-accepted' : contract.status == 'DEPOSIT_PAID' ? 'status-accepted' : contract.status == 'PICKED_UP' ? 'status-in-progress' : contract.status == 'RETURNED' ? 'status-warning' : contract.status == 'COMPLETED' ? 'status-completed' : 'status-cancelled'}"
                                        />
                                    <c:set
                                        var="statusText"
                                        value="${contract.status == 'PENDING' ? 'Chờ xử lý' : contract.status == 'ACCEPTED' ? 'Đã chấp nhận' : contract.status == 'DEPOSIT_PAID' ? 'Đã đặt cọc' : contract.status == 'PICKED_UP' ? 'Đã lấy xe' : contract.status == 'RETURNED' ? 'Đã trả xe' : contract.status == 'COMPLETED' ? 'Hoàn thành' : contract.status == 'REJECTED' ? 'Từ chối' : contract.status}"
                                        />
                                    <span class="status-badge ${statusClass}"
                                          >${statusText}</span
                                    >
                                </div>
                                <c:if
                                    test="${contract.status == 'REJECTED' && not empty contract.rejectionReason}"
                                    >
                                    <div class="alert alert-danger mt-2" role="alert">
                                        <i class="fas fa-comment-dots me-1"></i>
                                        Lý do từ chối: ${contract.rejectionReason}
                                    </div>
                                </c:if>
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="far fa-calendar-check"></i>Ngày bắt đầu</span
                                    >
                                    <span class="info-value"
                                          >${contract.startDate.toLocalDate()}</span
                                    >
                                </div>
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="far fa-calendar-times"></i>Ngày kết thúc</span
                                    >
                                    <span class="info-value">${contract.endDate.toLocalDate()}</span>
                                </div>
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="fas fa-piggy-bank"></i>Tiền đặt cọc</span
                                    >
                                    <span class="info-value"
                                          ><fmt:formatNumber
                                            value="${contract.depositAmount}"
                                            pattern="#,###"
                                            />
                                        VNĐ</span
                                    >
                                </div>
                            </div>
                        </div>

                        <!-- Cột phải: Danh sách xe trong hợp đồng -->
                        <div class="col-lg-8">
                            <div class="info-card">
                                <h5 class="fw-bold mb-4">
                                    <i class="fas fa-car me-2"></i>Danh sách xe trong hợp đồng
                                </h5>
                                <c:forEach var="d" items="${details}">
                                    <div class="detail-card">
                                        <div class="row align-items-center">
                                            <div class="col-auto">
                                                <div class="vehicle-icon"><i class="fas fa-car"></i></div>
                                            </div>
                                            <div class="col">
                                                <div
                                                    class="d-flex justify-content-between align-items-start mb-2"
                                                    >
                                                    <div>
                                                        <h6 class="fw-bold mb-1">
                                                            ${d.plateNumber != null ? d.plateNumber : 'N/A'}
                                                        </h6>
                                                        <small class="text-muted"
                                                               >Chi tiết #${d.contractDetailId}</small
                                                        >
                                                    </div>
                                                    <div class="text-end">
                                                        <div
                                                            class="fw-bold text-success"
                                                            style="font-size: 1.25rem"
                                                            >
                                                            <fmt:formatNumber
                                                                value="${d.price}"
                                                                pattern="#,###"
                                                                />
                                                            VNĐ
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row g-2 text-muted small">
                                                    <div class="col-sm-6">
                                                        <i class="far fa-calendar-check text-success me-1"></i>
                                                        Nhận: ${d.rentStartDate.toLocalDate()}
                                                    </div>
                                                    <div class="col-sm-6">
                                                        <i class="far fa-calendar-times text-danger me-1"></i>
                                                        Trả: ${d.rentEndDate.toLocalDate()}
                                                    </div>
                                                </div>
                                                <c:if test="${not empty d.note}">
                                                    <div class="mt-2 p-2 bg-light rounded">
                                                        <small
                                                            ><i class="fas fa-sticky-note me-1"></i
                                                            >${d.note}</small
                                                        >
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <!-- Điều khoản & Cam kết: full width dưới danh sách -->
                    <div class="row g-4 mt-1">
                        <div class="col-12">
                            <div class="terms-card">
                                <h5>
                                    <i class="fas fa-scale-balanced me-2"></i>Điều khoản & Cam kết
                                </h5>
                                <ol class="terms-list">
                                    <li>
                                        Bên thuê sử dụng xe đúng mục đích, không chở hàng cấm, không cho
                                        thuê lại.
                                    </li>
                                    <li>
                                        Không điều khiển xe vào khu vực ngập nước, đường bị cấm, đường
                                        nguy hiểm; nếu vi phạm tự chịu trách nhiệm chi phí sửa chữa và
                                        tổn thất phát sinh.
                                    </li>
                                    <li>
                                        Thời gian thuê tính theo hợp đồng. Trễ giờ trả xe: phụ thu
                                        100.000đ/giờ; vượt km: 3.000đ/km.
                                    </li>
                                    <li>
                                        Xe nhận với mức xăng ghi nhận trong hợp đồng, khi trả xe bổ sung
                                        tương đương (hoặc tính thêm theo thực tế).
                                    </li>
                                    <li>
                                        Mọi vi phạm giao thông trong thời gian thuê do Bên thuê chịu
                                        trách nhiệm nộp phạt.
                                    </li>
                                    <li>
                                        Đặt cọc hoàn lại sau khi thanh lý hợp đồng (trừ các chi phí phát
                                        sinh nếu có).
                                    </li>
                                    <li>
                                        Trong trường hợp bất khả kháng (tai nạn, hỏng hóc), hai bên phối
                                        hợp xử lý, ưu tiên an toàn và liên hệ ngay cho Bên cho thuê.
                                    </li>
                                    <li>
                                        Hợp đồng có hiệu lực từ thời điểm ký và được chấm dứt khi hai
                                        bên hoàn thành nghĩa vụ.
                                    </li>
                                </ol>

                                <div class="signature-box">
                                    <div class="signature">
                                        <strong>ĐẠI DIỆN BÊN A (Cho thuê)</strong>
                                        <div class="mt-3">Ký, ghi rõ họ tên</div>
                                    </div>
                                    <div class="signature">
                                        <strong>ĐẠI DIỆN BÊN B (Thuê)</strong>
                                        <div class="mt-3">Ký, ghi rõ họ tên</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Thanh toán: full width action bar dưới Điều khoản -->
                    <div class="row mt-3">
                        <div class="col-12">
                            <div
                                class="action-bar d-flex align-items-center justify-content-between flex-wrap gap-3"
                                >
                                <div class="action-amount">
                                    <small class="d-block opacity-75">Tổng giá trị hợp đồng</small>
                                    <h2 class="mb-0">
                                        <fmt:formatNumber
                                            value="${contract.totalAmount}"
                                            pattern="#,###"
                                            />
                                        <small class="opacity-75">VNĐ</small>
                                    </h2>
                                    <small class="d-block opacity-75 mt-1">
                                        Đặt cọc: 
                                        <fmt:formatNumber
                                            value="${contract.depositAmount}"
                                            pattern="#,###"
                                            />
                                        VNĐ
                                    </small>
                                </div>
                                <div class="action-cta d-flex gap-2 flex-wrap">
                                    <c:choose>
                                        <c:when test="${contract.status == 'ACCEPTED'}">
                                            <!-- Đã chấp nhận -> Cho phép đặt cọc -->
                                            <a href="${pageContext.request.contextPath}/paymentServlet?contractId=${contract.contractId}&type=deposit" 
                                               class="btn btn-primary btn-lg">
                                                <i class="fas fa-money-bill-wave me-2"></i>Đặt cọc
                                            </a>
                                        </c:when>
                                        <c:when test="${contract.status == 'DEPOSIT_PAID'}">
                                            <!-- Đã đặt cọc -> Cho phép lấy xe -->
                                            <form action="${pageContext.request.contextPath}/pickUpCar" method="post" class="d-inline">
                                                <input type="hidden" name="contractId" value="${contract.contractId}">
                                                <button type="submit" class="btn btn-success btn-lg">
                                                    <i class="fas fa-car me-2"></i>Xác nhận đã lấy xe
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${contract.status == 'PICKED_UP'}">
                                            <!-- Đã lấy xe -> Có thể trả xe -->
                                            <form
                                                action="${pageContext.request.contextPath}/returncar"
                                                method="post"
                                                class="d-inline"
                                                >
                                                <input
                                                    type="hidden"
                                                    name="contractId"
                                                    value="${contract.contractId}"
                                                    />
                                                <button type="submit" class="btn btn-warning btn-lg">
                                                    <i class="fas fa-undo me-2"></i>Gửi yêu cầu trả xe
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${contract.status == 'RETURNED'}">
                                            <!-- Đã trả xe -> Thanh toán tổng thể -->
                                            <a href="${pageContext.request.contextPath}/paymentServlet?contractId=${contract.contractId}&type=full" 
                                               class="btn btn-primary btn-lg">
                                                <i class="fas fa-credit-card me-2"></i>Thanh toán tổng thể
                                            </a>
                                        </c:when>
                                        <c:when test="${contract.status == 'COMPLETED'}">
                                            <!-- Đã hoàn thành -->
                                            <span class="btn btn-success btn-lg disabled">
                                                <i class="fas fa-check-circle me-2"></i>Đã hoàn thành
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Các trạng thái khác -->
                                            <span class="text-muted">Chờ xử lý...</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

            </body>
        </html>
