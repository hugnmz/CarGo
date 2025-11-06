<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chỉnh sửa khách hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .container {
                max-width: 700px;
                margin: 50px auto;
                background: #fff;
                border-radius: 12px;
                padding: 30px 40px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            h2 {
                text-align: center;
                color: #0d6efd;
                margin-bottom: 25px;
            }
            label {
                font-weight: 600;
                margin-top: 10px;
            }
            input, select {
                margin-top: 5px;
            }
            .btn-primary {
                background-color: #0d6efd;
            }
            .btn-secondary {
                background-color: #6c757d;
            }
            .btn-secondary:hover {
                background-color: #5a6268;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2>Chỉnh sửa thông tin khách hàng</h2>

            <form action="controllerinformationcustomer" method="post">
                <input type="hidden" name="_back" value="/manager/editcustomer.jsp" />
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="customerId" value="${customer.customerId}">

                <div class="mb-3">
                    <label>Họ và tên</label>
                    <input type="text" name="fullName" value="${customer.fullName}" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label>Số điện thoại</label>
                    <input type="text" name="phone" value="${customer.phone}" class="form-control" pattern="^0[0-9]{9}$" required>
                </div>

                <div class="mb-3">
                    <label>Email</label>
                    <input type="email" name="email" value="${customer.email}" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label>Ngày sinh</label>
                    <input type="date" class="form-control" 
                           id="dateOfBirth" name="dateOfBirth" 
                           value="${not empty customer.dateOfBirth ? customer.dateOfBirth : dateOfBirth}">
                </div>

                <div class="mb-3">
                    <label>Thành phố</label>
                    <select name="locationId" class="form-select" required>
                        <c:forEach var="loc" items="${locations}">
                            <option value="${loc.locationId}" 
                                    <c:if test="${loc.locationId == customer.locationId}">selected</c:if>>
                                ${loc.city}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label>Trạng thái tài khoản</label>
                    <select name="isVerified" class="form-select">
                        <option value="1" <c:if test="${customer.isVerified}">selected</c:if>>Hoạt động</option>
                        <option value="0" <c:if test="${not customer.isVerified}">selected</c:if>>không hoạt động</option>
                    </select>
                </div>

                <div class="d-flex justify-content-end mt-4">
                    <button type="submit" class="btn btn-primary me-2">Lưu thay đổi</button>
                    <a href="managecus" class="btn btn-secondary"> Hủy</a>
                </div>
            </form>
        </div>

    </body>
</html>