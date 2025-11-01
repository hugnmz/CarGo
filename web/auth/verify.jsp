<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="jakarta.tags.core" %>
<html>
  <head>
    <title>Xác minh email</title>
    <link
      href="${pageContext.request.contextPath}/css/auth/verify.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <h2>Xác minh email</h2>

    <c:if test="${not empty errorMessage}">
      <div class="msg err">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
      <div class="msg ok">${successMessage}</div>
    </c:if>
    <c:if test="${not empty infoMessage}">
      <div class="msg ok" style="background: #eef5ff; color: #1d3f8b">
        ${infoMessage}
      </div>
    </c:if>

    <p>Nhập mã 6 số đã gửi đến email của bạn.</p>

  <form action="${pageContext.request.contextPath}/VerifyServlet" method="post">
      <input type="hidden" name="_back" value="/auth/verify.jsp" />
      <input
        type="text"
        name="code"
        placeholder="Mã xác minh (6 số)"
        required
        maxlength="6"
      />
      <button type="submit">Xác minh</button>
    </form>

    <div class="row">
      <form action="${pageContext.request.contextPath}/resend-code" method="post">
        <button type="submit">Gửi lại mã</button>
      </form>
      <form action="${pageContext.request.contextPath}/auth/login.jsp" method="get">
        <button type="submit">Về trang đăng nhập</button>
      </form>
    </div>
  </body>
</html>
