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
                    href="${pageContext.request.contextPath}/CSS/customer/contract-view.css"
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
                            <div class="d-flex gap-2 mt-3 mt-md-0">
                                <a
                                    class="btn btn-back"
                                    href="${pageContext.request.contextPath}/my-contracts"
                                    >
                                    <i class="fas fa-arrow-left me-2"></i>Danh sách HĐ
                                </a>
                                <form action="${pageContext.request.contextPath}/returncar" method="post">  
                                    <input type="hidden" name="contractId" value="${contract.contractId}" />
                                    <button type="submit" class="btn btn-back">Gửi yêu cầu trả xe</button>
                                </form>
                                <a
                                    class="btn btn-back"
                                    href="${pageContext.request.contextPath}/home"
                                    >
                                    <i class="fas fa-home me-2"></i>Trang chủ
                                </a>

                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-success" role="alert">
                        ${sessionScope.message}
                    </div>
                    <c:remove var="message" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger" role="alert" id="errorBox" style="display:none;">
                        ${sessionScope.error}
                    </div>



                    <c:remove var="error" scope="session"/>
                </c:if>


                <div class="container pb-5">
                    <div class="row">
                        <!-- Contract Info -->
                        <div class="col-lg-4 mb-4">
                            <div class="info-card">
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
                                <!-- Block trạng thái -->
                                <div class="info-row">
                                    <span class="info-label"><i class="fas fa-info-circle"></i>Trạng thái</span>
                                    <c:set
                                        var="statusClass"
                                        value="${contract.status == 'PENDING' ? 'status-pending' : contract.status == 'ACCEPTED' ? 'status-accepted' : contract.status == 'IN_PROGRESS' ? 'status-in-progress' : contract.status == 'COMPLETED' ? 'status-completed' : 'status-cancelled'}"
                                        />
                                    <span class="status-badge ${statusClass}">${contract.status}</span>
                                </div>

                                <!-- Lý do từ chối (nếu có) -->
                                <c:if test="${contract.status == 'REJECTED' && not empty contract.rejectionReason}">
                                    <div class="alert alert-danger mt-2" role="alert">
                                        <i class="fas fa-comment-dots me-1"></i>
                                        Lý do từ chối: ${contract.rejectionReason}
                                    </div>
                                </c:if>
                            </div>
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

                        <div class="total-amount">
                            <small class="d-block opacity-75">Tổng giá trị hợp đồng</small>
                            <h2>
                                <fmt:formatNumber
                                    value="${contract.totalAmount}"
                                    pattern="#,###"
                                    />
                            </h2>
                            <small class="opacity-75">VNĐ</small>
                        </div>
                    </div>

                    <!-- Vehicle Details -->
                    <div class="col-lg-8">
                        <div class="info-card">
                            <h5 class="fw-bold mb-4">
                                <i class="fas fa-car me-2"></i>Danh sách xe trong hợp đồng
                            </h5>
                            <c:forEach var="d" items="${details}">
                                <div class="detail-card">
                                    <div class="row align-items-center">
                                        <div class="col-auto">
                                            <div class="vehicle-icon">
                                                <i class="fas fa-car"></i>
                                            </div>
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
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                window.onload = function () {
                    var errorMsg = document.getElementById("errorBox").innerText;
                    if (errorMsg) {
                        // Hiển thị hộp thoại confirm
                        confirm(errorMsg);
                        // Sau khi bấm OK -> chuyển về trang home
                        window.location.href = "home";
                    }
                };
            </script>
        </body>
    </html>