
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />
<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires", 0);

    String role = (String) session.getAttribute("roleName");
    if (role == null || !"MANAGER".equalsIgnoreCase(role)) {
        response.sendRedirect("LoginServletp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết xe - ${car.name}</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 40px;
                background-color: #f8f9fa;
            }
            .back-button {
                position: absolute;
                top: 20px;
                left: 20px;
                background-color: lightslategrey;
                color: white;
                border: none;
                padding: 8px 14px;
                border-radius: 6px;
                font-size: 14px;
                text-decoration: none;
                transition: background-color 0.3s ease;
            }
            .back-button:hover {
                background-color: #0056b3;
            }

            h2 {
                color: #007bff;
                text-align: center;
                margin-bottom: 30px;
            }
            .car-detail {
                display: flex;
                justify-content: space-between;
                align-items: flex-start;
                background-color: white;
                padding: 20px 30px;
                border-radius: 12px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
                margin-bottom: 25px;
                gap: 40px;
            }
            .car-image {
                flex: 1;
                text-align: end;
            }
            .car-image img {
                width: 100%;
                max-width: 450px;
                height: auto;
                border-radius: 10px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.15);
                object-fit: cover;
            }
            .car-info {
                flex: 1;
            }
            .car-info p {
                margin: 18px 0;
                font-size: 16px;
            }
            .vehicle-list, .add-vehicle {
                background-color: white;
                padding: 20px;
                border-radius: 12px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
                margin-bottom: 25px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
            }
            th, td {
                border: 1px solid #dee2e6;
                padding: 10px;
                text-align: center;
            }
            th {
                background-color: #007bff;
                color: white;
            }
            button {
                background-color: #dc3545;
                color: white;
                border: none;
                padding: 6px 10px;
                border-radius: 4px;
                cursor: pointer;
            }
            button:hover {
                background-color: #c82333;
            }
            form {
                display: inline;
            }
            input, select {
                margin: 8px 0;
                padding: 6px;
                width: 200px;
            }
            .price {
                color: #28a745;
                font-weight: bold;
            }
        </style>
    </head>
    <body>

        <!-- Nút quay về -->
        <a href="managecar" class="back-button">← Quay lại</a>

        <h2>Chi tiết xe: ${car.name}</h2>

        <div class="car-detail">
            <!-- Ảnh xe bên trái -->
            <div class="car-image">
                <c:if test="${not empty car.image}">
                    <img src="${car.image}" alt="Ảnh xe ${car.name}">
                </c:if>
            </div>

            <!-- Thông tin xe bên phải -->
            <div class="car-info">
                <p><strong>Năm sản xuất:</strong> ${car.year}</p>
                <p><strong>Hãng xe:</strong> ${car.categoryName}</p>
                <p><strong>Loại nhiên liệu:</strong> ${car.fuelType}</p>
                <p><strong>Số chỗ ngồi:</strong> ${car.seatingType}</p>
                <p><strong>Giá thuê/ngày:</strong> 
                    <span class="price">
                        <fmt:formatNumber value="${car.dailyPrice}" pattern="#,###" />₫
                    </span>
                </p>
                <p><strong>Tiền đặt cọc:</strong> 
                    <fmt:formatNumber value="${car.depositAmount}" pattern="#,###" />₫
                </p>
                <p><strong>Mô tả:</strong> ${car.description}</p>
            </div>
        </div>

        <!-- Thông báo -->
        <c:if test="${not empty message}">
            <div class="alert alert-success" style="font-weight: bold; color: green"
                 >${message}</div>
            </br>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger" style="font-weight: bold; color: red"
                 >${error}</div>
            </br>
        </c:if>
            
                    <!-- Bảng giá xe theo ngày -->
        <div class="vehicle-list">
            <h3>Bảng giá xe theo ngày</h3>
            
            <c:if test="${empty carPrices}">
                <p><em>Chưa có bảng giá nào cho xe này.</em></p>
            </c:if>
            
            <c:if test="${not empty carPrices}">
                <table>
                    <tr>
                        <th>Ngày bắt đầu</th>
                        <th>Ngày kết thúc</th>
                        <th>Giá thuê/ngày</th>
                        <th>Tiền đặt cọc</th>
                    </tr>
                    <c:forEach var="price" items="${carPrices}">
                        <tr>
                            <td>
                                ${price.startDate}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty price.endDate}">
                                        <span style="color: #28a745; font-weight: bold;">Đang áp dụng</span>
                                    </c:when>
                                    <c:otherwise>
                                        ${price.endDate}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="price">
                                <fmt:formatNumber value="${price.dailyPrice}" pattern="#,###" />₫
                            </td>
                            <td>
                                <fmt:formatNumber value="${price.depositAmount}" pattern="#,###" />₫
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
            
            
        <div class="vehicle-list">
            <h3>Danh sách xe (Vehicles)</h3>

            <c:if test="${empty vehicles}">
                <p><em>Chưa có vehicle nào cho xe này.</em></p>
            </c:if>

            <c:if test="${not empty vehicles}">
                <table>
                    <tr>
                        <th>Biển số</th>
                        <th>Trạng thái</th>
                        <th>Địa điểm</th>
                        <th>Hành động</th>
                    </tr>
                    <c:forEach var="v" items="${vehicles}">
                        <tr id="row-${v.vehicleId}">
                            <!-- Biển số -->
                            <td>${v.plateNumber}</td>
                            <!-- Trạng thái -->
                            <td>
                                <c:choose>
                                    <c:when test="${v.isActive}">Hoạt động</c:when>
                                    <c:otherwise>Không hoạt động</c:otherwise>
                                </c:choose>
                            </td>
                            <!-- Địa điểm -->
                            <td>${v.city}</td>
                            <!-- Hành động -->
                            <td>
                                <!-- Nút chế độ xem -->
                                <button type="button" class="view-mode" onclick="enableEdit(${v.vehicleId})">Sửa</button>

                                <form action="vehiclecontroller" method="post" class="edit-mode" style="display:none;" id="form-${v.vehicleId}">
                                    <input type="hidden" name="_back" value="/manager/manage_detail_car.jsp" />
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="vehicleId" value="${v.vehicleId}">
                                    <input type="hidden" name="carId" value="${car.carId}">

                                    <input type="text" name="plateNumber" value="${v.plateNumber}" style="width:120px;" required>
                                    <select name="isActive">
                                        <option value="true" ${v.isActive ? "selected" : ""}>Hoạt động</option>
                                        <option value="false" ${!v.isActive ? "selected" : ""}>Không hoạt động</option>
                                    </select>
                                    <select name="locationId">
                                        <c:forEach var="loc" items="${locations}">
                                            <option value="${loc.locationId}" ${v.city == loc.city ? "selected" : ""}>
                                                ${loc.city} - ${loc.address}
                                            </option>
                                        </c:forEach>
                                    </select>

                                    <button type="submit"
                                            onclick="return confirm('Bạn có chắc muốn cập nhật biển này không?')"
                                            style="background-color:#28a745;">Lưu</button>
                                    <button type="button" onclick="cancelEdit(${v.vehicleId})"
                                            style="background-color:gray;">Hủy</button>
                                </form>

                                <!-- Nút xóa -->
                                <form action="vehiclecontroller" method="post" class="view-mode" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="vehicleId" value="${v.vehicleId}">
                                    <input type="hidden" name="carId" value="${car.carId}">
                                    <button type="submit" onclick="return confirm('Bạn có chắc muốn xóa biển này không?')">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>

                </table>
            </c:if>
        </div>

        <div class="add-vehicle">
            <h3>Thêm Vehicle mới</h3>
            <form action="vehiclecontroller" method="post">
                <input type="hidden" name="_back" value="/manager/manage_detail_car.jsp" />
                <input type="hidden" name="action" value="create">
                <input type="hidden" name="carId" value="${car.carId}">

                <table style="width: 100%; border-collapse: collapse;">
                    <tr>
                        <td><label>Biển số xe:</label></td>
                        <td><input type="text" name="licensePlate" required></td>
                    </tr>
                    <tr>
                        <td><label>Địa điểm:</label></td>
                        <td>
                            <select name="locationId" required>
                                <option value="" hidden>-- Chọn địa điểm --</option>
                                <c:forEach var="loc" items="${locations}">
                                    <option value="${loc.locationId}">
                                        ${loc.city} - ${loc.address}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Trạng thái:</label></td>
                        <td>
                            <select name="isActive" required>
                                <option value="true">Hoạt động</option>
                                <option value="false">Không hoạt động</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center; padding: 20px;">
                            <button onclick="return confirm('Bạn có chắc muốn thêm biển này không?')"
                                    type="submit" 
                                    style="background-color: #007bff;
                                    color: white;
                                    padding: 12px 40px;
                                    border: none;
                                    border-radius: 8px;
                                    font-size: 16px;
                                    cursor: pointer;
                                    transition: 0.3s;">
                                Thêm
                            </button>
                    </tr>
                </table>
            </form>
        </div>

        <script>
            function enableEdit(id) {
                const row = document.getElementById('row-' + id);
                row.querySelectorAll('.view-mode').forEach(el => el.style.display = 'none');
                row.querySelectorAll('.edit-mode').forEach(el => el.style.display = 'inline-block');
            }

            function cancelEdit(id) {
                const row = document.getElementById('row-' + id);
                row.querySelectorAll('.edit-mode').forEach(el => el.style.display = 'none');
                row.querySelectorAll('.view-mode').forEach(el => el.style.display = 'inline-block');
            }

            // Cho phép double-click vào dòng để sửa
//            document.addEventListener("DOMContentLoaded", function () {
//                const rows = document.querySelectorAll("tr[id^='row-']");
//                rows.forEach(row => {
//                    row.addEventListener("dblclick", () => {
//                        const id = row.id.split('-')[1];
//                        enableEdit(id);
//                    });
//                });
//            });
        </script>

    </body>
</html>
