<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" 
    import="java.util.Set,
            java.util.LinkedHashSet,
            java.util.Map,
            java.util.LinkedHashMap,
            java.util.List,
            java.util.ArrayList,
            java.lang.reflect.Method" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <title>Giỏ hàng - CarGo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/customer/cart.css" rel="stylesheet" />
  </head>
  <body>
<%@ include file="/include/header.jsp" %>
<main class="container" style="flex:1">
    <c:choose>
      <c:when test="${not empty cartItems}">
        
        <%-- Đếm số nhóm ngày khác nhau --%>
        <%
            Set<String> uniqueDates = new LinkedHashSet<>();
            Map<String, List> dateGroupsMap = new LinkedHashMap<>();
            
            List cartItems = (List) request.getAttribute("cartItems");
            if (cartItems != null) {
                for (Object obj : cartItems) {
                    try {
                        // Sử dụng reflection để lấy rentStartDate và rentEndDate
                        Method getStart = obj.getClass().getMethod("getRentStartDate");
                        Method getEnd = obj.getClass().getMethod("getRentEndDate");
                        Object startDate = getStart.invoke(obj);
                        Object endDate = getEnd.invoke(obj);
                        
                        String dateKey = startDate + "_" + endDate;
                        uniqueDates.add(dateKey);
                        
                        // Nhóm items theo dateKey
                        if (!dateGroupsMap.containsKey(dateKey)) {
                            dateGroupsMap.put(dateKey, new ArrayList());
                        }
                        dateGroupsMap.get(dateKey).add(obj);
                    } catch (Exception e) {
                        throw new RuntimeException("error.system.cart", e);
                    }
                }
            }
            request.setAttribute("uniqueDateCount", uniqueDates.size());
            request.setAttribute("dateGroupsMap", dateGroupsMap);
        %>
        
        <%-- Cảnh báo nếu có nhiều nhóm ngày khác nhau --%>
        <c:if test="${uniqueDateCount > 1}">
          <div class="warning-box">
            <i class="fas fa-exclamation-triangle fa-lg"></i>
            <strong>Lưu ý:</strong> Giỏ hàng của bạn có xe với thời gian thuê khác nhau (${uniqueDateCount} nhóm). 
            Hệ thống sẽ tự động tạo ${uniqueDateCount} hợp đồng riêng biệt khi thanh toán.
            <br><small class="text-muted">💡 Mỗi hợp đồng yêu cầu đặt cọc riêng khi nhận xe.</small>
          </div>
        </c:if>
        
        <c:if test="${uniqueDateCount == 1}">
          <div class="info-box">
            <i class="fas fa-info-circle fa-lg"></i>
            Tất cả xe trong giỏ hàng có cùng thời gian thuê. Sẽ được gộp thành 1 hợp đồng.
          </div>
        </c:if>
        
        <form
          method="post"
          action="${pageContext.request.contextPath}/ViewCartDetail"
        >
          <input type="hidden" name="_back" value="/customer/cart.jsp" />
          <input type="hidden" name="carId" value="${carId}">
          <input type="hidden" name="vehicleId" value="${vehicleId}">
          <c:set var="total" value="0" />
          
          <%-- Hiển thị theo từng nhóm ngày --%>
          <c:forEach var="groupEntry" items="${dateGroupsMap}">
            <c:set var="groupItems" value="${groupEntry.value}" />
            <c:set var="firstItem" value="${groupItems[0]}" />
            
            <div class="date-group-header">
              <i class="fas fa-calendar-alt me-2"></i>
              Nhóm xe: ${firstItem.rentStartDate} → ${firstItem.rentEndDate} 
              <span class="badge bg-light text-dark ms-2">${groupItems.size()} xe</span>
            </div>
            
            <table class="cart-table" style="margin-top: 0; border-radius: 0 0 8px 8px;">
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
                <c:forEach var="item" items="${groupItems}">
                  <tr>
                    <td>
                      <input
                        type="checkbox"
                        name="selectedIds"
                        value="${item.cartDetailId}"
                      />
                    </td>
                    <td><span class="badge bg-dark">${item.plateNumber}</span></td>
                    <td><strong>${item.carName}</strong></td>
                    <td>${item.rentStartDate}</td>
                    <td>${item.rentEndDate}</td>
                    <td><strong><fmt:formatNumber value="${item.price}" pattern="#,###" /> VNĐ</strong></td>
                  </tr>
                  <c:set var="total" value="${total + item.price}" />
                </c:forEach>
              </tbody>
            </table>
          </c:forEach>
          
          <%-- Tổng cộng --%>
          <table class="cart-table" style="margin-top: 1.5rem;">
            <tfoot>
              <tr>
                <td colspan="5" style="text-align: right; font-weight: bold; font-size: 1.1rem;">
                  <i class="fas fa-receipt me-2"></i>Tổng tiền:
                </td>
                <td style="font-weight: bold; font-size: 1.2rem; color: #10b981;"><fmt:formatNumber value="${total}" pattern="#,###" /> VNĐ</td>
              </tr>
            </tfoot>
          </table>
          
          <div class="actions">
            <c:choose>
              <c:when test="${not empty refererUrl}">
                <a class="btn btn-outline-secondary" href="${refererUrl}">
                  <i class="fas fa-arrow-left me-1"></i>Quay lại
                </a>
              </c:when>
              <c:when test="${not empty carId}">
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/car-detail?carId=${carId}">
                  <i class="fas fa-arrow-left me-1"></i>Quay lại xem xe
                </a>
              </c:when>
              <c:otherwise>
                <button type="button" class="btn btn-outline-secondary" onclick="window.history.back();">
                  <i class="fas fa-arrow-left me-1"></i>Quay lại
                </button>
              </c:otherwise>
            </c:choose>
            <button type="submit" class="btn" name="action" value="remove" onclick="return confirm('Bạn có chắc muốn xóa các mục đã chọn?')">
              <i class="fas fa-trash me-1"></i>Xóa mục đã chọn
            </button>
            <button type="submit" class="btn secondary" name="action" value="clear" onclick="return confirm('Bạn có chắc muốn xóa tất cả?')">
              <i class="fas fa-trash-alt me-1"></i>Xóa tất cả
            </button>
            <button type="submit" class="btn btn-success" formaction="${pageContext.request.contextPath}/checkout">
              <i class="fas fa-credit-card me-1"></i>Thanh toán
            </button>
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
                <c:when test="${not empty refererUrl}">
                  <a class="btn btn-primary" href="${refererUrl}">
                    <i class="fa fa-arrow-left me-1"></i>
                    Quay lại
                  </a>
                </c:when>
                <c:when test="${not empty carId}">
                  <a class="btn btn-primary" href="${pageContext.request.contextPath}/car-detail?carId=${carId}">
                    <i class="fa fa-arrow-left me-1"></i>
                    Quay lại xem xe
                  </a>
                </c:when>
                <c:otherwise>
                  <button type="button" class="btn btn-primary" onclick="window.history.back();">
                    <i class="fa fa-arrow-left me-1"></i>
                    Quay lại
                  </button>
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
</main>
     <%@ include file="/include/footer.jsp" %>
  </body>
</html>