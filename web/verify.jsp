<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Xác minh email</title>
    <style>
        body { font-family: sans-serif; max-width: 500px; margin: 40px auto; }
        .msg { padding:10px; border-radius:8px; margin-bottom:10px; }
        .err { background:#ffe6e6; color:#b30000; }
        .ok  { background:#e8fff0; color:#0a7a35; }
        form { display:flex; gap:8px; }
        input[type=text]{ flex:1; padding:10px; font-size:16px; }
        button { padding:10px 16px; font-size:16px; cursor:pointer; }
        .row { display:flex; gap:10px; margin-top:10px; }
    </style>
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
    <div class="msg ok" style="background:#eef5ff; color:#1d3f8b">${infoMessage}</div>
</c:if>

<p>Nhập mã 6 số đã gửi đến email của bạn.</p>

<form action="VerifyServlet" method="post">
    <input type="text" name="code" placeholder="Mã xác minh (6 số)" required maxlength="6" />
    <button type="submit">Xác minh</button>
</form>

<div class="row">
    <form action="resend-code" method="post">
        <button type="submit">Gửi lại mã</button>
    </form>
    <form action="login.jsp" method="get">
        <button type="submit">Về trang đăng nhập</button>
    </form>
</div>

</body>
</html>
