<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="vi_VN" />

<c:set var="c" value="${sessionScope.c}" />
<c:set var="username" value="${c != null ? c.username : sessionScope.username}" />
<c:set var="avatar" value="${not empty sessionScope.avatar ? sessionScope.avatar : 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'}" />

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>CarGo - Thuê xe tự lái cao cấp</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/customer/home.css" rel="stylesheet">
    </head>

    <body>
        <%@ include file="/include/header.jsp" %>


        <!-- Hero Section -->
        <div class="hero-modern">
            <div class="container text-center">
                <h1>Thuê xe tự lái cao cấp<br>Trải nghiệm đẳng cấp</h1>
                <p>Hơn 1000+ xe sang trên toàn quốc - Giá tốt nhất - Dịch vụ 5 sao</p>
                <div class="d-flex justify-content-center gap-3 flex-wrap">
                    <div class="d-flex align-items-center text-white">
                        <i class="fas fa-shield-alt fa-2x me-2"></i>
                        <div class="text-start">
                            <small>Bảo hiểm</small><br>
                            <strong>Toàn diện</strong>
                        </div>
                    </div>
                    <div class="d-flex align-items-center text-white">
                        <i class="fas fa-clock fa-2x me-2"></i>
                        <div class="text-start">
                            <small>Hỗ trợ</small><br>
                            <strong>24/7</strong>
                        </div>
                    </div>
                    <div class="d-flex align-items-center text-white">
                        <i class="fas fa-star fa-2x me-2"></i>
                        <div class="text-start">
                            <small>Đánh giá</small><br>
                            <strong>5.0/5.0</strong>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <!-- Modern Search Box -->
            <div class="search-modern">
                <form class="row g-4" action="${pageContext.request.contextPath}/searchcar" method="post">
                    <!-- Địa điểm -->
                    <div class="col-md-6 col-lg-3">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-map-marker-alt text-primary me-2"></i>Địa điểm
                        </label>
                        <select class="form-select" name="location">
                            <option selected disabled value="0">Chọn địa điểm...</option>
                            <c:if test="${not empty allLocations}">
                                <c:forEach var="l" items="${allLocations}">
                                    <option value="${l.locationId}">${l.city}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>

                    <!-- Tên xe -->
                    <div class="col-md-6 col-lg-3">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-car-side text-success me-2"></i>Tên xe
                        </label>
                        <input name="carName" type="text" class="form-control" placeholder="Nhập tên xe...">
                    </div>

                    <!-- Loại xe -->
                    <div class="col-md-6 col-lg-2">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-car text-warning me-2"></i>Loại xe
                        </label>
                        <select name="category" class="form-select">
                            <option selected disabled value="0">Chọn loại xe...</option>
                            <c:if test="${not empty allCategories}">
                                <c:forEach var="c" items="${allCategories}">
                                    <option value="${c.categoryId}">${c.categoryName}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>

                    <!-- Giá tiền -->
                    <div class="col-md-6 col-lg-2">
                        <label class="form-label fw-semibold">
                            <i class="fas fa-money-bill-wave text-danger me-2"></i>Giá tiền (VND)
                        </label>
                        <input name="price" type="number" class="form-control" step="10000" min="0" placeholder="Nhập giá tối đa...">
                    </div>

                    <!-- Nút tìm kiếm -->
                    <div class="col-12 col-lg-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-search w-100">
                            <i class="fas fa-search me-2"></i>Tìm xe
                        </button>
                    </div>
                </form>
            </div>

            <!-- Stats Section -->
            <div class="stats-modern">
                <div class="row">
                    <div class="col-6 col-md-3">
                        <div class="stat-item">
                            <span class="stat-number">1000+</span>
                            <span class="stat-label">Xe cao cấp</span>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="stat-item">
                            <span class="stat-number">50K+</span>
                            <span class="stat-label">Khách hàng</span>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="stat-item">
                            <span class="stat-number">63</span>
                            <span class="stat-label">Tỉnh thành</span>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="stat-item">
                            <span class="stat-number">5.0</span>
                            <span class="stat-label">Đánh giá TB</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Featured Cars -->
            <h2 class="section-title">Xe nổi bật</h2>
            <div class="row g-4">
                <c:choose>
                    <c:when test="${not empty allCars}">
                        <c:forEach var="car" items="${allCars}" varStatus="status" end="5">
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

            <div class="text-center mt-5">
                <a href="${pageContext.request.contextPath}/cars" class="btn btn-primary-custom btn-lg">
                    <i class="fas fa-th me-2"></i>Xem tất cả xe
                </a>
            </div>

            <!-- Testimonials / Feedback -->
            <div class="d-flex align-items-center justify-content-between mt-5">
                <h2 class="section-title m-0">Khách hàng nói gì về chúng tôi</h2>
                <button id="btnOpenFeedback" class="btn btn-primary-custom btn-sm">
                    <i class="fa fa-comment-dots me-1"></i> Feedback
                </button>
            </div>

            <!-- Dynamic Feedback List -->
            <div id="feedbackList" class="row g-4 mb-3 mt-2"></div>
            <div class="text-center mb-5">
                <button id="btnLoadMoreFeedback" class="btn btn-outline-primary" style="display:none;">
                    Xem thêm
                </button>
                <button id="btnCollapseFeedback" class="btn btn-outline-secondary ms-2" style="display:none;">
                    Rút gọn
                </button>
            </div>

            <!-- Feedback Modal -->
            <div class="modal fade" id="feedbackModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="fa fa-comment me-2"></i>Gửi nhận xét</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <c:choose>
                                <c:when test="${empty sessionScope.c}">
                                    <div class="alert alert-warning">
                                        Bạn cần <a href='${pageContext.request.contextPath}/auth/login.jsp'>đăng nhập</a> để gửi nhận xét.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <form id="feedbackForm">
                                        <div class="mb-3">
                                            <label class="form-label">Nội dung nhận xét</label>
                                            <textarea class="form-control" name="comment" rows="4" maxlength="255" placeholder="Chia sẻ trải nghiệm của bạn..." required></textarea>
                                        </div>
                                        <div class="text-end">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                            <button type="submit" class="btn btn-primary">Gửi</button>
                                        </div>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                (function () {
                    var ctx = '${pageContext.request.contextPath}';
                    var listEl = document.getElementById('feedbackList');
                    var btnMore = document.getElementById('btnLoadMoreFeedback');
                    var btnCollapse = document.getElementById('btnCollapseFeedback');
                    var btnOpen = document.getElementById('btnOpenFeedback');
                    var modalEl = document.getElementById('feedbackModal');

                    var offset = 0;
                    var limit = 3;        // chỉ tải 3 mỗi lần
                    var expanded = false; // đã bấm xem thêm hay chưa

                    if (btnOpen) {
                        btnOpen.addEventListener('click', function () {
                            var m = new bootstrap.Modal(modalEl);
                            m.show();
                        });
                    }

                    function renderItem(item) {
                        var created = item.createAt ? new Date(item.createAt).toLocaleString() : '';
                        var name = item.customerName || 'Khách hàng ẩn danh';
                        var img = item.carImage || ('https://i.pravatar.cc/100?u=' + (item.customerEmail || name));
                        return ''
                                + '<div class="col-md-4">'
                                + '  <div class="testimonial-card">'
                                + '    <div class="text-warning mb-2">'
                                + '      <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>'
                                + '      <i class="fas fa-star"></i><i class="fas fa-star"></i>'
                                + '    </div>'
                                + '    <p class="mb-3">' + escapeHtml(item.comment || '') + '</p>'
                                + '    <div class="d-flex align-items-center">'
                                + '      <img src="' + img + '" class="rounded-circle me-3" width="50" height="50"/>'
                                + '      <div>'
                                + '        <strong>' + escapeHtml(name) + '</strong><br/>'
                                + '        <small class="text-muted">' + created + '</small>'
                                + '      </div>'
                                + '    </div>'
                                + '  </div>'
                                + '</div>';
                    }

                    function escapeHtml(str) {
                        return String(str)
                                .replace(/&/g, '&amp;')
                                .replace(/</g, '&lt;')
                                .replace(/>/g, '&gt;')
                                .replace(/\"/g, '&quot;')
                                .replace(/'/g, '&#039;');
                    }

                    async function load(pageOffset) {
                        var url = ctx + '/feedbacks?offset=' + pageOffset + '&limit=' + limit;
                        var res = await fetch(url);
                        if (!res.ok)
                            return;

                        var data = await res.json();
                        if (Array.isArray(data)) {
                            var html = data.map(renderItem).join('');
                            listEl.insertAdjacentHTML('beforeend', html);
                            offset += data.length;

                            // Hiện "Xem thêm" nếu còn khả năng có dữ liệu tiếp (server trả đủ limit)
                            if (btnMore)
                                btnMore.style.display = (data.length === limit ? 'inline-block' : 'none');

                            // Hiện "Rút gọn" nếu đã từng mở rộng và hiện có hơn 3 item
                            if (btnCollapse)
                                btnCollapse.style.display = (expanded && listEl.children.length > limit) ? 'inline-block' : 'none';
                        }
                    }

                    if (btnMore) {
                        btnMore.addEventListener('click', function () {
                            expanded = true;
                            load(offset);
                        });
                    }

                    if (btnCollapse) {
                        btnCollapse.addEventListener('click', function () {
                            // Reset về trạng thái ban đầu: chỉ 3 item
                            expanded = false;
                            offset = 0;
                            listEl.innerHTML = '';
                            // Tải lại 3 feedback đầu
                            load(offset);
                            // Ẩn nút rút gọn, nút xem thêm sẽ được quyết định sau khi load
                            btnCollapse.style.display = 'none';
                            // Cuộn tới danh sách
                            listEl.scrollIntoView({behavior: 'smooth', block: 'start'});
                        });
                    }

                    // Submit feedback
                    var form = document.getElementById('feedbackForm');
                    if (form) {
                        form.addEventListener('submit', async function (e) {
                            e.preventDefault();
                            var fd = new FormData(form);

                            var submitBtn = form.querySelector('button[type="submit"]');
                            if (submitBtn)
                                submitBtn.disabled = true;

                            try {
                                var res = await fetch(ctx + '/feedbacks', {
                                    method: 'POST',
                                    body: new URLSearchParams(fd)
                                });

                                if (!res.ok) {
                                    var msg = '';
                                    try {
                                        msg = await res.text();
                                    } catch (err) {
                                    }
                                    alert('Gửi nhận xét thất bại (' + res.status + '): ' + (msg || ''));
                                    return;
                                }

                                var item = await res.json();

                                // Sau khi gửi, reset về trạng thái rút gọn để đảm bảo nhất quán
                                expanded = false;
                                offset = 0;
                                listEl.innerHTML = '';
                                load(offset);

                                var inst = bootstrap.Modal.getInstance(modalEl);
                                if (inst)
                                    inst.hide();
                                form.reset();
                                if (btnCollapse)
                                    btnCollapse.style.display = 'none';
                            } catch (err) {
                                console.error(err);
                                alert('Có lỗi kết nối khi gửi nhận xét.');
                            } finally {
                                if (submitBtn)
                                    submitBtn.disabled = false;
                            }
                        });
                    }

                    // Initial load
                    load(offset);
                })();
            </script>
            <!-- Footer -->
            <%@ include file="/include/footer.jsp" %>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>

</html>