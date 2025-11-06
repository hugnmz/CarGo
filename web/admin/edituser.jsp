<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Sửa thông tin User</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <h3 class="text-center text-primary mb-3">Sửa thông tin User</h3>
    <form action="ControllerAdmin" method="post" class="row g-3">
        <input type="hidden" name="_back" value="/HomeAdmin" />
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="userId" value="${editUser.userId}">

<!--        <div class="col-md-6">
            <label class="form-label">Tên đăng nhập:</label>
            <input type="text" name="username" class="form-control" value="${editUser.username}" required>
        </div>-->

        <div class="col-md-6">
            <label class="form-label">Họ tên:</label>
            <input type="text" name="fullname" class="form-control" value="${editUser.fullName}" required>
        </div>

        <div class="col-md-6">
            <label class="form-label">Email:</label>
            <input type="email" name="email" class="form-control" value="${editUser.email}">
        </div>

        <div class="col-md-6">
            <label class="form-label">Số điện thoại:</label>
            <input type="text" name="phone" class="form-control" 
                   value="${editUser.phone}" pattern="^0[0-9]{9}$">
        </div>

        <div class="col-md-6">
            <label class="form-label">Vai trò:</label>
            <select name="roleid" class="form-control" required>
                <c:forEach var="r" items="${roles}">
                    <option value="${r.roleId}" <c:if test="${r.roleId == editUser.roleId}">selected</c:if>>
                        ${r.roleName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="col-md-6">
            <label class="form-label">Thành phố:</label>
            <select name="locationid" class="form-control">
                <c:forEach var="l" items="${locations}">
                    <option value="${l.locationId}" <c:if test="${l.locationId == editUser.locationId}">selected</c:if>>
                        ${l.city}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="col-12 d-flex justify-content-end mt-3">
            <button type="submit" class="btn btn-primary px-5"
                    onclick="return confirm('Bạn có chắc chắn muốn sửa các thông tin không?')">
                Cập nhật</button>
            <a href="HomeAdmin" class="btn btn-secondary ml-2">Hủy</a>
        </div>
    </form>
</div>
</body>
</html>