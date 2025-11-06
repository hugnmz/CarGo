
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
        <title>Chỉnh sửa xe</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #e8f0fe;
                font-family: "Segoe UI", sans-serif;
            }
            .container {
                max-width: 800px;
                margin: 50px auto;
            }
            .card {
                border-radius: 12px;
                box-shadow: 0 0 15px rgba(0,0,0,0.15);
                border: none;
            }
            .card-header {
                background-color: #007bff;
                color: white;
                border-top-left-radius: 12px;
                border-top-right-radius: 12px;
            }
            .form-label {
                font-weight: 600;
            }
            .form-control, .form-select {
                border-radius: 8px;
                border: 1px solid #bcd0f7;
            }
            .form-control:focus, .form-select:focus {
                border-color: #007bff;
                box-shadow: 0 0 5px rgba(0,123,255,0.3);
            }
            .btn-primary {
                background-color: #007bff;
                border: none;
            }
            .btn-primary:hover {
                background-color: #0069d9;
            }
            .btn-secondary {
                background-color: #6c757d;
                border: none;
            }
            .btn-secondary:hover {
                background-color: #5a6268;
            }
            .car-img-preview {
                width: 160px;
                height: 100px;
                object-fit: cover;
                border-radius: 8px;
                border: 1px solid #ddd;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="card">
                <div class="card-header text-center fs-5 fw-bold">
                    <i class="fa-solid fa-pen-to-square"></i> Chỉnh sửa thông tin xe
                </div>
                <div class="card-body bg-white">
                    <form action="controllerinformationcar" method="post">
                        <input type="hidden" name="_back" value="/manager/editcar.jsp" />
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="carId" value="${car.carId}">

                        <!-- Tên xe -->
                        <div class="mb-3">
                            <label class="form-label">Tên xe</label>
                            <input type="text" class="form-control" name="name" value="${car.name}" required>
                        </div>

                        <!-- Năm sản xuất -->
                        <div class="mb-3">
                            <label class="form-label">Năm sản xuất</label>
                            <input type="number" class="form-control" name="year" value="${car.year}" min="1990" max="2025" required>
                        </div>

                        <!-- Mô tả -->
                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control" name="description" rows="3">${car.description}</textarea>
                        </div>

                        <!-- Ảnh -->
                        <div class="mb-3">
                            <label class="form-label">Ảnh hiện tại</label><br>
                            <c:choose>
                                <c:when test="${not empty car.image}">
                                    <img src="${car.image}" alt="Car Image" class="car-img-preview mb-2">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/images/default_car.png" alt="Default Car" class="car-img-preview mb-2">
                                </c:otherwise>
                            </c:choose>
                            <br>
                            <label class="form-label mt-2">Chọn ảnh mới (nếu muốn thay đổi)</label>
                            <input type="text" class="form-control" name="image" placeholder="Nhập link ảnh trực tuyến" value="${car.image}">
                        </div>

                        <!-- Danh mục -->
                        <div class="mb-3">
                            <label class="form-label">Danh mục</label>
                            <select name="categoryId" class="form-select" required>
                            
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}" <c:if test="${cat.categoryId == car.categoryId}">selected</c:if>>
                                        ${cat.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Loại nhiên liệu -->
                        <div class="mb-3">
                            <label class="form-label">Loại nhiên liệu</label>
                            <select name="fuelId" class="form-select" required>
                            
                                <c:forEach var="f" items="${fuels}">
                                    <option value="${f.fuelId}" <c:if test="${f.fuelId == car.fuelId}">selected</c:if>>
                                        ${f.fuelType}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Số chỗ ngồi -->
                        <div class="mb-3">
                            <label class="form-label">Số chỗ ngồi</label>
                            <select name="seatingId" class="form-select" required>
                                
                                <c:forEach var="s" items="${seatings}">
                                    <option value="${s.seatingId}" <c:if test="${s.seatingId == car.seatingId}">selected</c:if>>
                                        ${s.seatingType}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Giá thuê -->
                        <div class="mb-3">
                            <label class="form-label">Giá thuê (VNĐ)</label>
                            <input type="number" class="form-control" name="price" min="0" step="10000" value="${car.dailyPrice}" required>
                        </div>

                        <!-- Tiền đặt cọc -->
                        <div class="mb-3">
                            <label class="form-label">Tiền đặt cọc (VNĐ)</label>
                            <input type="number" class="form-control" name="deposit" min="0" step="100000" value="${car.depositAmount}" required>
                        </div>

                        <!-- Nút hành động -->
                        <div class="text-end mt-4">
                            <a href="managecar" class="btn btn-secondary"><i class="fa-solid fa-arrow-left"></i> Quay lại</a>
                            <button type="submit" class="btn btn-primary text-white"><i class="fa-solid fa-save"></i> Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
