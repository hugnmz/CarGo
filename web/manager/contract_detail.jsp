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
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/manager/contract_detail.css"/>
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
                                    href="javascript:void(0);"
                                    onclick="history.back(); return false;"
                                    >
                                    <i class="fas fa-arrow-left me-2"></i> ← Quay lại
                                </a>

                            </div>
                        </div>
                    </div>
                </div>

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
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="fas fa-info-circle"></i>Trạng thái</span
                                    >
                                    <c:choose>
                                        <c:when test="${contract.status == 'ACCEPTED'}">
                                            <span class="status-badge">Chấp nhận</span>

                                        </c:when>
                                        <c:when test="${contract.status == 'PENDING'}">
                                            <span class="status-badge">Chờ xử lí</span>
                                        </c:when>
                                        <c:when test="${contract.status == 'REJECTED'}">
                                            <span class="status-badge">Từ chối</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge">Không xác định</span>
                                        </c:otherwise>
                                    </c:choose>

                                </div>
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="far fa-calendar-check"></i>Ngày bắt đầu</span
                                    >
                                    <span class="info-value"
                                          >${contract.startDateToString}</span
                                    >
                                </div>
                                <div class="info-row">
                                    <span class="info-label"
                                          ><i class="far fa-calendar-times"></i>Ngày kết thúc</span
                                    >
                                    <span class="info-value">${contract.endDateToString}</span>
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
                                <div class="info-row note">
                                    <span class="info-label"
                                          ><i class="fas fa-piggy-bank "></i>Ghi chú</span>

                                    <div> <p class="info-text">
                                            ${not empty ccontract.note?contract.note:'Trống'}
                                        </p></div>


                                </div>
                            </div>

                            <div class="total-amount">
                                <strong class="d-block ">Tổng giá trị hợp đồng</strong>
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
                                <c:choose>
                                    <c:when test="${not empty details}">
                                        <c:forEach var="d" items="${details}">
                                            <div class="detail-card">
                                                <div class="row align-items-center">

                                                    <div class="col">
                                                        <div
                                                            class="d-flex justify-content-between align-items-start mb-2"
                                                            >
                                                            <div>
                                                                <h6 class="fw-bold mb-1">
                                                                    ${d.plateNumber != null ? d.plateNumber : 'N/A'}
                                                                </h6>
                                                                <small class="text-muted"
                                                                       >${d.name}</small
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
                                    </c:when>
                                    <c:otherwise>
                                        <p  class="text-center text-muted">Danh sách trống</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            </body>
        </html>
