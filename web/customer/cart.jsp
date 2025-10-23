<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ page
contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <title>Giỏ hàng</title>
    <link href="${pageContext.request.contextPath}/css/customer/cart.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
      .empty-cart {
        text-align: center;
        padding: 60px 20px;
      }
      .empty-cart-content {
        max-width: 400px;
        margin: 0 auto;
      }
    </style>
  </head>
  <body>
    <div class="cart-wrapper">
      <div class="cart-header">
        <h3 style="margin:0">Giỏ hàng</h3>
        <div>
          <c:choose>
            <c:when test="${not empty carId}">
              <a class="btn outline" href="${pageContext.request.contextPath}/car-detail?carId=${carId}">
                <i class="fa fa-arrow-left me-1"></i>
                Quay lại xem xe
              </a>
            </c:when>
            <c:otherwise>
              <a class="btn outline" href="${pageContext.request.contextPath}/home">
                <i class="fa fa-arrow-left me-1"></i>
                Về trang chủ
              </a>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    <c:choose>
      <c:when test="${not empty cartItems}">
        <form
          method="post"
          action="${pageContext.request.contextPath}/ViewCartDetail"
        >
          <input type="hidden" name="carId" value="${carId}">
          <input type="hidden" name="vehicleId" value="${vehicleId}">
          <c:set var="total" value="0" />
          <table class="cart-table">
            <thead>
              <tr>
                <th>Chọn</th>
                <th>Biển số</th>
                <th>Tên xe</th>
                <th>Nhận</th>
                <th>Trả</th>
                <th>Giá</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" items="${cartItems}">
                <tr>
                  <td>
                    <input
                      type="checkbox"
                      name="selectedIds"
                      value="${item.cartDetailId}"
                    />
                  </td>
                  <td>${item.plateNumber}</td>
                  <td>${item.carName}</td>
                  <td>${item.rentStartDate}</td>
                  <td>${item.rentEndDate}</td>
                  <td>${item.price}</td>
                </tr>
                <c:set var="total" value="${total + item.price}" />
              </c:forEach>
            </tbody>
            <tfoot>
              <tr>
                <td colspan="5" style="text-align: right; font-weight: bold">
                  Tổng tiền:
                </td>
                <td style="font-weight: bold">${total}</td>
              </tr>
            </tfoot>
          </table>
          <div class="actions">
            <button type="submit" class="btn" name="action" value="remove" onclick="return confirm('Bạn có chắc muốn xóa các mục đã chọn?')">Xóa mục đã chọn</button>
            <button type="submit" class="btn secondary" name="action" value="clear" onclick="return confirm('Bạn có chắc muốn xóa tất cả?')">Xóa tất cả</button>
          </div>
          
        </form>
      </c:when>
      <c:otherwise>
        <div class="empty-cart">
          <div class="empty-cart-content">
            <i class="fa fa-shopping-cart fa-3x text-muted mb-3"></i>
            <h3 class="text-muted">Giỏ hàng trống</h3>
            <p class="text-muted mb-4">Bạn chưa có sản phẩm nào trong giỏ hàng</p>
            <div class="d-flex justify-content-center gap-3">
              <c:choose>
                <c:when test="${not empty carId}">
                  <a class="btn btn-primary" href="${pageContext.request.contextPath}/car-detail?carId=${carId}">
                    <i class="fa fa-arrow-left me-1"></i>
                    Quay lại xem xe
                  </a>
                </c:when>
                <c:otherwise>
                  <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">
                    <i class="fa fa-arrow-left me-1"></i>
                    Về trang chủ
                  </a>
                </c:otherwise>
              </c:choose>
              <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/home">
                <i class="fa fa-home me-1"></i>
                Khám phá thêm
              </a>
            </div>
          </div>
        </div>
      </c:otherwise>
    </c:choose>
  </body>
</html>
