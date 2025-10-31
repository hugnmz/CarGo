<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết hợp đồng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="text-center mb-4">Chi tiết hợp đồng</h2>
            <div class="card">
                <div class="card-body">
                  
                        <a href="${pageContext.request.contextPath}/calculateTotalAmount?contractId=5" >
                        <input type="button" value="Return Car">
                    </a>
<!--                    <a href="calculateTotalAmount?contractId=${param.contractId}" >
                        <input type="button" value="Return Car">
                    </a>-->
                </div>
            </div>
        </div>
    </body>
</html>