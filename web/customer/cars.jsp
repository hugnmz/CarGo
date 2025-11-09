<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="vi_VN" />

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Tất cả xe - CarGo</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/customer/cars.css" rel="stylesheet">
    </head>
    <body>
        <%@ include file="/include/header.jsp" %>

        <main class="container" style="padding-top: 2rem; padding-bottom: 2rem;">
            <h2 class="section-title">Tất cả xe</h2>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fa fa-exclamation-triangle me-2"></i>${error}
                </div>
            </c:if>

            <div class="row g-4">
                <c:choose>
                    <c:when test="${not empty allCars}">
                        <c:forEach var="car" items="${allCars}">
                            <div class="col-md-6 col-lg-4">
                                <div class="card car-card-modern car-card-clickable" onclick="window.location.href = '${pageContext.request.contextPath}/car-detail?carId=${car.carId}'">
                                    <div class="position-relative overflow-hidden">
                                        <c:choose>
                                            <c:when test="${not empty car.image}">
                                                <img src="${car.image}" class="card-img-top car-image" alt="${car.name}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="https://via.placeholder.com/400x250?text=No+Image" class="card-img-top car-image" alt="No Image">
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Icon đặc biệt (góc trái trên) -->
                                        <div class="special-icon">
                                            <i class="fa fa-bolt text-warning"></i>
                                        </div>

                                        <!-- Badge giảm giá (góc phải dưới) - chỉ hiển thị nếu có giá -->
                                        <c:if test="${not empty car.dailyPrice}">
                                            <div class="discount-badge">
                                                <span class="badge bg-warning text-dark">Giảm 18%</span>
                                            </div>
                                        </c:if>
                                    </div>

                                    <div class="card-body p-3">
                                        <div class="d-flex justify-content-between align-items-start mb-2">
                                            <span class="badge badge-modern"><i class="fas fa-shield-alt me-1"></i>Miễn thế chấp</span>
                                            <span class="badge bg-warning text-dark"><i class="fas fa-bolt me-1"></i>Nổi bật</span>
                                        </div>
                                        <h5 class="fw-bold mb-2">${car.name.toUpperCase()} ${car.year}</h5>
                                        <div class="d-flex gap-3 mb-3 text-muted small">
                                            <span><i class="fas fa-cog me-1"></i>Tự động</span>
                                            <span><i class="fas fa-users me-1"></i>${car.seatingType != null ? car.seatingType : 'N/A'} chỗ</span>
                                            <span><i class="fas fa-gas-pump me-1"></i>${car.fuelType != null ? car.fuelType : 'N/A'}</span>
                                        </div>
                                        <div class="d-flex align-items-center mb-3">
                                            <i class="fas fa-map-marker-alt text-danger me-2"></i>
                                            <small class="text-muted">${car.locationCity != null ? car.locationCity : 'N/A'}</small>
                                            <span class="ms-auto">
                                                <i class="fas fa-star text-warning"></i>
                                                <small class="fw-semibold">5.0</small>
                                            </span>
                                        </div>
                                        <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                                            <c:choose>
                                                <c:when test="${not empty car.dailyPrice}">
                                                    <div>
                                                        <c:set var="currentPrice" value="${car.dailyPrice}" />
                                                        <c:set var="originalPrice" value="${currentPrice * 1.18}" />
                                                        <small class="price-old d-block"><fmt:formatNumber value="${originalPrice}" pattern="#,###" /> VNĐ</small>
                                                        <span class="price-modern"><fmt:formatNumber value="${currentPrice}" pattern="#,###" /> VNĐ<small class="text-muted fw-normal">/ngày</small></span>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Liên hệ</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <button class="btn btn-sm btn-primary-custom">Đặt xe</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12 text-center">
                            <div class="alert alert-info">
                                <i class="fa fa-info-circle"></i>
                                Hiện tại chưa có xe nào trong hệ thống.
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <%@ include file="/include/footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>