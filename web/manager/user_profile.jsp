<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires", 0);

    String role = (String) session.getAttribute("roleName");
    if (role == null || !"MANAGER".equalsIgnoreCase(role)) {
        response.sendRedirect("LoginServlet");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thông tin cá nhân - Manager</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .sidebar {
                height: 100vh;
                background-color: #212529;
                color: white;
                position: fixed;
                width: 240px;
                top: 0;
                left: 0;
                padding-top: 20px;
            }
            .sidebar a {
                color: #adb5bd;
                text-decoration: none;
                display: block;
                padding: 10px 20px;
            }
            .sidebar a:hover {
                background-color: #343a40;
                color: #fff;
            }
            .sidebar a.active {
                background-color: #0d6efd;
                color: #fff;
            }
            .content {
                margin-left: 250px;
                padding: 20px;
            }
            .card {
                border-radius: 12px;
            }
            .profile-label {
                font-weight: bold;
                color: #0d6efd;
            }
            .profile-value {
                color: #343a40;
            }
            .btn-edit {
                background-color: #0d6efd;
                color: white;
            }
            .btn-edit:hover {
                background-color: #0b5ed7;
            }
        </style>
    </head>
    <body>

        <!-- Sidebar -->
        <div class="sidebar">
            <h4 class="text-center text-white mb-4"><i class="fa-solid fa-car"></i> Car Rental</h4>
            <a href="homemange"><i class="fa-solid fa-house"></i> Trang chủ</a>
            <a href="profile" class="active"><i class="fa-solid fa-user-gear"></i> Thông tin cá nhân</a>
            <a href="managecus"><i class="fa-solid fa-users"></i> Quản lý khách hàng</a>
            <a href="managecar"><i class="fa-solid fa-car-side"></i> Quản lý xe</a>
            <a href="#"><i class="fa-solid fa-file-contract"></i> Hợp đồng</a>
            <a href="${pageContext.request.contextPath}/LogoutServlet">
                <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
            </a>
        </div>

        <!-- Nội dung chính -->
        <div class="content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="fa-solid fa-user"></i> Hồ sơ cá nhân</h2>
                <span class="badge bg-primary">Vai trò: <c:out value="${sessionScope.roleName}" /></span>
            </div>

            <!-- Thông báo lỗi / thành công -->
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <c:if test="${not empty ok}">
                <div class="alert alert-success">${ok}</div>
            </c:if>
            <c:if test="${not empty errorMess}">
                <div class="alert alert-danger">${errorMess}</div>
            </c:if>

            <c:if test="${not empty errors}">
                <div class="alert alert-danger mt-2">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <strong>Có lỗi xảy ra:</strong>
                    <ul class="mb-0 mt-2">
                        <c:forEach var="error" items="${errors}">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </div>
            </c:if>

            <!-- Thẻ thông tin -->
            <div class="card shadow-sm p-4 mb-3">

                <div class="row mb-3">
                    <div class="col-md-6">
                        <p><span class="profile-label">🆔 Mã người dùng:</span>
                            <span class="profile-value"><c:out value="${user.userId}" /></span>
                        </p>
                    </div>
                    <div class="col-md-6">
                        <p><span class="profile-label">💻 Username:</span>
                            <span class="profile-value"><c:out value="${user.username}" /></span>
                        </p>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <p><span class="profile-label">👤 Họ và tên:</span>
                            <span class="profile-value"><c:out value="${user.fullName}" /></span>
                        </p>
                    </div>
                    <div class="col-md-6">
                        <p><span class="profile-label">📧 Email:</span>
                            <span class="profile-value"><c:out value="${user.email}" /></span>
                        </p>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-6">
                        <p><span class="profile-label">📞 Số điện thoại:</span>
                            <span class="profile-value"><c:out value="${user.phone}" /></span>
                        </p>
                    </div>
                    <div class="col-md-6">
                        <p><span class="profile-label">🏠 Địa chỉ:</span>
                            <span class="profile-value">
                                <c:forEach var="loc" items="${locations}">
                                    <c:if test="${loc.locationId == user.locationId}">
                                        <c:out value="${loc.city}" />
                                    </c:if>
                                </c:forEach>
                            </span>
                        </p>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-6">
                        <p><span class="profile-label">🧩  Quyền hạn:</span>
                            <span class="profile-value"><c:out value="${sessionScope.roleName}" /></span>
                        </p>
                    </div>
                    <div class="col-md-6">
                        <p><span class="profile-label">📅 Ngày tạo:</span>
                            <span class="profile-value"><c:out value="${user.createdAtFormatted}" /></span>
                        </p>
                    </div>
                </div>

                <div class="text-center mt-4">
                    <button class="btn btn-edit me-2" type="button" data-bs-toggle="collapse" 
                            data-bs-target="#editProfileForm" aria-expanded="false" 
                            aria-controls="editProfileForm">
                        <i class="fa-solid fa-pen-to-square"></i> Chỉnh sửa thông tin
                    </button>
                    <button type="button" class="btn btn-warning" data-bs-toggle="modal" 
                            data-bs-target="#changePasswordModal">
                        <i class="fa-solid fa-key"></i> Đổi mật khẩu
                    </button>
                </div>
            </div>

            <!-- Form chỉnh sửa thông tin cá nhân (collapse) -->
            <div class="collapse" id="editProfileForm">
                <div class="card shadow-sm p-4 mb-3">
                    <h5><i class="fa-solid fa-pen-to-square"></i> Chỉnh sửa thông tin cá nhân</h5>

                    <form action="${pageContext.request.contextPath}/updateinfor" method="post">
                        <input type="hidden" name="_back" value="/profile" />
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Họ và tên</label>
                                <input type="text" class="form-control" name="fullName" value="${user.fullName}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" value="${user.email}" required>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Số điện thoại</label>
                                <input type="text" class="form-control" name="phone" 
                                       value="${user.phone}" pattern="^0[0-9]{9}$" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Địa chỉ</label>
                                <select class="form-select" name="locationId" required>
                                    <c:forEach var="loc" items="${locations}">
                                        <option value="${loc.locationId}" 
                                                <c:if test="${loc.locationId == user.locationId}">
                                                    selected</c:if>>
                                                ${loc.city}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <input type="hidden" name="userId" value="${user.userId}">

                        <div class="text-center">
                            <button type="submit" class="btn btn-edit">
                                <i class="fa-solid fa-floppy-disk"></i> Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>

            <!--Modal đổi mật khẩu--> 
            <div class="modal fade" id="changePasswordModal" tabindex="-1" 
                 aria-labelledby="changePasswordModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/changepass" method="post">
                            <div class="modal-header">
                                <h5 class="modal-title" id="changePasswordModalLabel">
                                    <i class="fa-solid fa-key"></i> Đổi mật khẩu
                                </h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">


                                <div class="mb-3">
                                    <label for="oldPassword" class="form-label">Mật khẩu cũ</label>
                                    <input type="password" class="form-control" id="oldPassword" name="oldPassword" required>
                                </div>
                                <div class="mb-3">
                                    <label for="newPassword" class="form-label">Mật khẩu mới</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword" required>
                                </div>
                                <div class="mb-3">
                                    <label for="reNewPassword" class="form-label">Nhập lại mật khẩu</label>
                                    <input type="password" class="form-control" id="reNewPassword" name="reNewPassword" required>
                                </div>
                                <input type="hidden" name="userId" value="${user.userId}">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- JS kiểm tra trùng mật khẩu -->
            <script>
                const form = document.querySelector('#changePasswordModal form');
                form.addEventListener('submit', function (e) {
                    const newPass = document.getElementById('newPassword').value;
                    const reNewPass = document.getElementById('reNewPassword').value;
                    if (newPass !== reNewPass) {
                        e.preventDefault();
                        alert("Mật khẩu mới và nhập lại mật khẩu phải giống nhau!");
                    }
                });
            </script>
        </div>


        <!-- JS Bootstrap -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

        <!-- JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>