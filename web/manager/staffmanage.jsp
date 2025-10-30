<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Staff - CarGo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/manager/manage_cars.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/manager/manager_home.jsp">CarGo Manager</a>
        <div class="d-flex gap-2">
            <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
            <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/auth/login.jsp">Đăng xuất</a>
        </div>
    </div>
</nav>

<div class="container py-4">
    <div class="d-flex align-items-center justify-content-between mb-3">
        <h3 class="mb-0">Quản lý Staff</h3>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createStaffModal">
            + Thêm staff
        </button>
    </div>

    <form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/StaffManageServlet">
        <input type="hidden" name="action" value="list"/>
        <div class="col-sm-8 col-md-6">
            <input type="text" name="query" value="${param.query}" class="form-control" placeholder="Tìm theo tên, email, số điện thoại">
        </div>
        <div class="col-auto">
            <button class="btn btn-outline-primary">Tìm kiếm</button>
        </div>
        <div class="col-auto">
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/StaffManageServlet?action=list">Xóa lọc</a>
        </div>
    </form>

    <div class="card shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Họ tên</th>
                    <th>Email</th>
                    <th>SĐT</th>
                    <th>Trạng thái</th>
                    <th>Ngày tạo</th>
                    <th class="text-end">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty staffList}">
                        <c:forEach var="s" items="${staffList}" varStatus="st">
                            <tr>
                                <td>${s.id}</td>
                                <td class="fw-semibold">${s.fullName}</td>
                                <td>${s.email}</td>
                                <td>${s.phone}</td>
                                <td>
                                    <span class="badge ${s.status == 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">
                                        ${s.status}
                                    </span>
                                </td>
                                <td>
                                    <c:out value="${s.createdAt}"/>
                                </td>
                                <td class="text-end">
                                    <button
                                        class="btn btn-sm btn-outline-primary me-1"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editStaffModal"
                                        data-id="${s.id}"
                                        data-name="${s.fullName}"
                                        data-email="${s.email}"
                                        data-phone="${s.phone}"
                                        data-status="${s.status}">
                                        Sửa
                                    </button>

                                    <form class="d-inline" method="post" action="${pageContext.request.contextPath}/StaffManageServlet"
                                          onsubmit="return confirm('Bạn chắc chắn muốn xóa staff này?');">
                                        <input type="hidden" name="action" value="delete"/>
                                        <input type="hidden" name="id" value="${s.id}"/>
                                        <button class="btn btn-sm btn-outline-danger">Xóa</button>
                                    </form>

                                    <form class="d-inline" method="post" action="${pageContext.request.contextPath}/StaffManageServlet">
                                        <input type="hidden" name="action" value="toggle_status"/>
                                        <input type="hidden" name="id" value="${s.id}"/>
                                        <button class="btn btn-sm btn-outline-warning">
                                            ${s.status == 'ACTIVE' ? 'Khóa' : 'Mở khóa'}
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="text-center text-muted py-4">Không có staff nào.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages > 1}">
            <div class="card-footer d-flex justify-content-center">
                <nav>
                    <ul class="pagination pagination-sm mb-0">
                        <c:set var="currentPage" value="${page != null ? page : 1}"/>
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/StaffManageServlet?action=list&query=${param.query}&page=${currentPage-1}">«</a>
                        </li>
                        <c:forEach begin="1" end="${totalPages}" var="p">
                            <li class="page-item ${p == currentPage ? 'active' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/StaffManageServlet?action=list&query=${param.query}&page=${p}">${p}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/StaffManageServlet?action=list&query=${param.query}&page=${currentPage+1}">»</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </c:if>
    </div>
</div>

<!-- Modal: Create -->
<div class="modal fade" id="createStaffModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form class="modal-content" method="post" action="${pageContext.request.contextPath}/StaffManageServlet">
            <input type="hidden" name="action" value="create"/>
            <div class="modal-header">
                <h5 class="modal-title">Thêm staff</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <div class="mb-2">
                    <label class="form-label">Họ tên</label>
                    <input name="fullName" class="form-control" required>
                </div>
                <div class="mb-2">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" required>
                </div>
                <div class="mb-2">
                    <label class="form-label">Số điện thoại</label>
                    <input name="phone" class="form-control">
                </div>
                <div class="mb-2">
                    <label class="form-label">Mật khẩu</label>
                    <input type="password" name="password" class="form-control" minlength="6" required>
                </div>
                <div class="mb-2">
                    <label class="form-label">Trạng thái</label>
                    <select name="status" class="form-select">
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="INACTIVE">INACTIVE</option>
                    </select>
                </div>
                <input type="hidden" name="role" value="STAFF"/>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary">Lưu</button>
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Edit -->
<div class="modal fade" id="editStaffModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form class="modal-content" method="post" action="${pageContext.request.contextPath}/StaffManageServlet">
            <input type="hidden" name="action" value="update"/>
            <input type="hidden" name="id" id="edit-id">
            <div class="modal-header">
                <h5 class="modal-title">Cập nhật staff</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <div class="mb-2">
                    <label class="form-label">Họ tên</label>
                    <input name="fullName" id="edit-name" class="form-control" required>
                </div>
                <div class="mb-2">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" id="edit-email" class="form-control" required>
                </div>
                <div class="mb-2">
                    <label class="form-label">Số điện thoại</label>
                    <input name="phone" id="edit-phone" class="form-control">
                </div>
                <div class="mb-2">
                    <label class="form-label">Trạng thái</label>
                    <select name="status" id="edit-status" class="form-select">
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="INACTIVE">INACTIVE</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary">Cập nhật</button>
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
            </div>
        </form>
    </div>
</div>

<script>
document.getElementById('editStaffModal')?.addEventListener('show.bs.modal', function (event) {
    const btn = event.relatedTarget;
    document.getElementById('edit-id').value = btn.getAttribute('data-id');
    document.getElementById('edit-name').value = btn.getAttribute('data-name');
    document.getElementById('edit-email').value = btn.getAttribute('data-email');
    document.getElementById('edit-phone').value = btn.getAttribute('data-phone');
    document.getElementById('edit-status').value = btn.getAttribute('data-status');
});
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>