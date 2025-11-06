<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="c" value="${sessionScope.c}" />
<c:set var="username" value="${c != null ? c.username : sessionScope.username}" />
<c:set var="avatar" value="${not empty sessionScope.avatar ? sessionScope.avatar : 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'}" />
 <nav class="navbar navbar-expand-lg navbar-modern fixed-top">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                    <i class="fas fa-car"></i> CarGo
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto align-items-center">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/cars">Xe cho thuê</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/customer/contact.jsp">Chúng Tôi</a></li>

                        <c:choose>
                            <c:when test="${not empty username}">
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/my-contracts">Hợp đồng</a></li>
                                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/ViewCartDetail"><i class="fas fa-shopping-cart"></i></a></li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                        <img src="${avatar}" alt="Avatar" style="width:35px; height:35px; border-radius:50%; margin-right:8px;">
                                        ${username}
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/CustomerServlet"><i class="fas fa-user me-2"></i>Thông tin cá nhân</a></li>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/LogoutServlet"><i class="fas fa-sign-out-alt me-2"></i>Đăng xuất</a></li>
                                    </ul>
                                    </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item">
                                    <a class="btn btn-primary-custom" href="${pageContext.request.contextPath}/auth/login.jsp">Đăng nhập</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </nav>
                        <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="cg-footer">
  <div class="cg-container">
    <div class="cg-row">
      <div class="cg-col">
        <div class="cg-brand">CarGo</div>
        <div class="cg-text">Dịch vụ thuê xe tự lái cao cấp. Trải nghiệm an tâm, giá minh bạch.</div>
      </div>
      <div class="cg-col">
        <div class="cg-title">Dịch vụ</div>
        <ul class="cg-list">
          <li><a href="#">Thuê xe tự lái</a></li>
          <li><a href="#">Thuê xe có tài</a></li>
          <li><a href="#">Thuê dài hạn</a></li>
        </ul>
      </div>
      <div class="cg-col">
        <div class="cg-title">Liên kết</div>
        <ul class="cg-list">
          <li><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
          <li><a href="${pageContext.request.contextPath}/cars">Xe cho thuê</a></li>
          <li><a href="${pageContext.request.contextPath}/customer/contact.jsp">Chúng tôi</a></li>
        </ul>
      </div>
      <div class="cg-col">
        <div class="cg-title">Hỗ trợ</div>
        <ul class="cg-list">
          <li><a href="mailto:support@cargo.vn">support@cargo.vn</a></li>
          <li><a href="tel:19001234">1900 1234</a></li>
          <li><span>TP. Hồ Chí Minh</span></li>
        </ul>
      </div>
    </div>
    <div class="cg-divider"></div>
    <div class="cg-bottom">
      <div>© 2025 CarGo. All rights reserved.</div>
      <div class="cg-links">
        <a href="#">Điều khoản</a>
        <a href="#">Bảo mật</a>
      </div>
    </div>
  </div>
</footer>

<style>
  .cg-footer{background:#0f172a;color:#e2e8f0;font-family:Inter,system-ui,-apple-system,Segoe UI,Roboto,"Helvetica Neue",Arial,sans-serif;padding:40px 0 16px;margin-top:40px}
  .cg-container{max-width:1140px;margin:0 auto;padding:0 16px}
  .cg-row{display:flex;flex-wrap:wrap;gap:24px}
  .cg-col{flex:1 1 220px;min-width:200px}
  .cg-brand{font-weight:700;font-size:20px;margin-bottom:8px}
  .cg-title{font-weight:600;margin:6px 0 10px}
  .cg-text{opacity:.8;line-height:1.6}
  .cg-list{list-style:none;padding:0;margin:0}
  .cg-list li{margin:8px 0}
  .cg-list a{color:#e2e8f0;text-decoration:none;opacity:.9}
  .cg-list a:hover{opacity:1;text-decoration:underline}
  .cg-divider{height:1px;background:rgba(255,255,255,.08);margin:20px 0}
  .cg-bottom{display:flex;flex-wrap:wrap;justify-content:space-between;align-items:center;gap:12px;font-size:14px;opacity:.8}
  .cg-links a{color:#e2e8f0;text-decoration:none;margin-left:16px}
  .cg-links a:hover{text-decoration:underline}
  @media (max-width:576px){.cg-row{gap:12px}.cg-footer{padding:28px 0 12px}}
</style>
        