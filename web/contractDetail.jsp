<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.time.LocalDateTime, java.time.format.DateTimeFormatter"%>

<%
    String customerName = request.getParameter("customerName");
    String customerPhone = request.getParameter("customerPhone");
    String customerAddress = request.getParameter("customerAddress");
    String carName = request.getParameter("carName");
    String plateNumber = request.getParameter("plateNumber");
    String rentStart = request.getParameter("rentStart");
    String rentEnd = request.getParameter("rentEnd");
    String pricePerDay = request.getParameter("pricePerDay");
    String deposit = request.getParameter("deposit");
    String staffName = request.getParameter("staffName");

    DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("HH:mm 'ngày' dd 'tháng' MM 'năm' yyyy");

    LocalDateTime startDate = (rentStart != null && !rentStart.isEmpty()) ? LocalDateTime.parse(rentStart, inputFormat) : null;
    LocalDateTime endDate = (rentEnd != null && !rentEnd.isEmpty()) ? LocalDateTime.parse(rentEnd, inputFormat) : null;
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hợp Đồng Thuê Xe Ô Tô</title>
    <style>
        body {
            font-family: 'Times New Roman', Times, serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }
        .page-container {
            max-width: 900px;
            margin: auto;
            background: #fff;
            padding: 50px 60px;
            border: 1px solid #ccc;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .confirmation-box {
            background: #e8f5e9;
            border-left: 5px solid #28a745;
            padding: 15px 20px;
            margin-bottom: 30px;
            color: #155724;
            border-radius: 5px;
        }
        h1, h2, h3, p { margin: 0; }
        .header {
            text-align: center;
            border-bottom: 2px solid #000;
            padding-bottom: 15px;
        }
        .header h1 { font-size: 20px; text-transform: uppercase; }
        .header h2 { font-size: 26px; margin: 10px 0; }
        .section {
            margin-top: 25px;
            line-height: 1.6;
            text-align: justify;
        }
        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 40px;
            margin-top: 20px;
        }
        .party h3 {
            border-bottom: 1px solid #333;
            padding-bottom: 5px;
            font-size: 16px;
        }
        .party p { margin: 5px 0; }
        .signatures {
            display: grid;
            grid-template-columns: 1fr 1fr;
            margin-top: 60px;
            text-align: center;
        }
        .signatures strong {
            display: block;
            margin-bottom: 5px;
            text-transform: uppercase;
        }
        .actions {
            text-align: center;
            margin-top: 40px;
        }
        .print-btn {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
        }
        .print-btn:hover { background-color: #0056b3; }
        @media print {
            .actions { display: none; }
            body { background: white; }
            .page-container {
                box-shadow: none;
                border: none;
                padding: 20px;
            }
        }
    </style>
</head>
<body>

<div class="page-container">
    <div class="confirmation-box">
        ✅ <strong>Hợp đồng thuê xe đã được tạo thành công!</strong><br>
        Vui lòng kiểm tra kỹ thông tin trước khi in hoặc ký xác nhận.
    </div>

    <div class="header">
        <h1>CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</h1>
        <p><strong>Độc lập - Tự do - Hạnh phúc</strong></p>
        <p>––––––––––––––––––––––––––––––––</p>
        <h2>HỢP ĐỒNG THUÊ XE Ô TÔ</h2>
        <p><i>Hôm nay, <%= LocalDateTime.now().format(outputFormat) %></i></p>
    </div>

    <div class="info-grid">
        <div class="party">
            <h3>BÊN CHO THUÊ (BÊN A)</h3>
            <p><strong>Họ tên:</strong> Vuxnguyencoder</p>
            <p><strong>Địa chỉ:</strong> Công ty TNHH Dịch vụ thuê xe ABC</p>
            <p><strong>Điện thoại:</strong> 0901 234 567</p>
        </div>
        <div class="party">
            <h3>BÊN THUÊ (BÊN B)</h3>
            <p><strong>Họ tên:</strong> <%= customerName %></p>
            <p><strong>Địa chỉ:</strong> <%= customerAddress %></p>
            <p><strong>Điện thoại:</strong> <%= customerPhone %></p>
        </div>
    </div>

    <div class="section">
        <h3>ĐIỀU 1. ĐỐI TƯỢNG HỢP ĐỒNG</h3>
        <p><strong>Tên xe:</strong> <%= carName %></p>
        <p><strong>Biển số xe:</strong> <%= plateNumber %></p>
    </div>

    <div class="section">
        <h3>ĐIỀU 2. THỜI GIAN THUÊ</h3>
        <p><strong>Thời gian nhận xe:</strong> <%= startDate != null ? startDate.format(outputFormat) : "" %></p>
        <p><strong>Thời gian trả xe:</strong> <%= endDate != null ? endDate.format(outputFormat) : "" %></p>
    </div>

    <div class="section">
        <h3>ĐIỀU 3. CHI PHÍ VÀ THANH TOÁN</h3>
        <p><strong>Đơn giá thuê:</strong> <%= pricePerDay %> VNĐ/ngày</p>
        <p><strong>Tiền đặt cọc:</strong> <%= deposit %> VNĐ</p>
    </div>

    <div class="section">
        <h3>Điều 6: Nghĩa vụ và quyền của Bên A</h3>
        <p><b>1. Bên A có các nghĩa vụ sau đây:</b></p>
        <p>a) Chuyển giao tài sản cho thuê đúng thỏa thuận ghi trong Hợp đồng;</p>
        <p>b) Bảo đảm giá trị sử dụng của tài sản cho thuê;</p>
        <p>c) Bảo đảm quyền sử dụng tài sản cho Bên B;</p>

        <p><b>2. Bên A có quyền sau đây:</b></p>
        <p>a) Nhận đủ tiền thuê tài sản theo phương thức đã thỏa thuận;</p>
        <p>b) Nhận lại tài sản thuê khi hết hạn Hợp đồng;</p>
        <p>c) Đơn phương đình chỉ thực hiện Hợp đồng và yêu cầu bồi thường thiệt hại nếu Bên B có một trong các hành vi sau đây:</p>
        <p>- Không trả tiền thuê trong ……. tháng liên tiếp;</p>
        <p>- Sử dụng tài sản thuê không đúng công dụng, mục đích của tài sản;</p>
        <p>- Làm tài sản thuê mất mát, hư hỏng;</p>
        <p>- Sửa chữa, đổi hoặc cho người khác thuê lại mà không có sự đồng ý của Bên A;</p>
    </div>

    <div class="section">
        <h3>Điều 7: Nghĩa vụ và quyền của Bên B</h3>
        <p><b>1. Bên B có các nghĩa vụ sau đây:</b></p>
        <p>a) Bảo quản tài sản thuê như tài sản của chính mình, không được thay đổi tình trạng tài sản, không được cho thuê lại nếu không có sự đồng ý của Bên A;</p>
        <p>b) Sử dụng tài sản thuê đúng công dụng, mục đích của tài sản;</p>
        <p>c) Trả đủ tiền thuê tài sản theo phương thức đã thỏa thuận;</p>
        <p>d) Trả lại tài sản thuê đúng thời hạn và phương thức đã thỏa thuận;</p>
        <p>e) Chịu toàn bộ chi phí liên quan đến chiếc xe trong quá trình thuê. Nếu gây tai nạn hoặc hư hỏng xe, Bên B phải thông báo ngay cho Bên A và chịu trách nhiệm sửa chữa, phục hồi nguyên trạng xe.</p>

        <p><b>2. Bên B có các quyền sau đây:</b></p>
        <p>a) Nhận tài sản thuê theo đúng thỏa thuận;</p>
        <p>b) Được sử dụng tài sản thuê theo đúng công dụng, mục đích của tài sản;</p>
        <p>c) Đơn phương đình chỉ thực hiện Hợp đồng thuê tài sản và yêu cầu bồi thường thiệt hại nếu:</p>
        <p>- Bên A chậm giao tài sản theo thỏa thuận gây thiệt hại cho Bên B;</p>
        <p>- Bên A giao tài sản thuê không đúng đặc điểm, tình trạng như mô tả tại Điều 1 Hợp đồng;</p>
    </div>

    <div class="section">
        <h3>Điều 8: Cam đoan của các bên</h3>
        <p><b>Bên A cam đoan:</b></p>
        <p>- Thông tin và tình trạng xe là đúng sự thật, xe không bị tranh chấp, thế chấp hay ràng buộc pháp lý nào;</p>
        <p>- Việc giao kết hợp đồng là hoàn toàn tự nguyện, không bị ép buộc;</p>
        <p>- Thực hiện đầy đủ các thỏa thuận trong hợp đồng này.</p>

        <p><b>Bên B cam đoan:</b></p>
        <p>a) Thông tin cá nhân đúng sự thật;</p>
        <p>b) Đã xem xét kỹ tài sản thuê;</p>
        <p>c) Tự nguyện giao kết và thực hiện đầy đủ các điều khoản hợp đồng.</p>

        <p><b>Hai bên cam đoan:</b></p>
        <p>- Tất cả giấy tờ và thông tin là thật, hợp pháp và có giá trị pháp lý;</p>
        <p>- Mọi tranh chấp phát sinh sẽ được giải quyết theo quy định của pháp luật.</p>
    </div>

    <div class="section">
        <h3>Điều 9: Điều khoản cuối cùng</h3>
        <p>1. Nếu muốn chấm dứt hợp đồng trước hạn, bên yêu cầu phải thông báo cho bên kia biết trước ……. tháng;</p>
        <p>2. ……. tháng trước khi hợp đồng hết hiệu lực, hai bên tiến hành thanh lý hoặc ký phụ lục gia hạn;</p>
        <p>3. Hợp đồng có hiệu lực kể từ ngày ký. Mọi sửa đổi bổ sung phải được lập thành văn bản;</p>
        <p>4. Nếu phát sinh tranh chấp, hai bên thương lượng; nếu không thành, sẽ khởi kiện tại tòa án có thẩm quyền;</p>
        <p>5. Hai bên đã đọc, hiểu và đồng ý toàn bộ nội dung hợp đồng, tự nguyện ký tên dưới đây.</p>
    </div>
    <div class="signatures">
        <div>
            <strong>BÊN A</strong>
            <p>(Ký, ghi rõ họ tên)</p><br><br><br>
            <strong><%= staffName %></strong>
        </div>
        <div>
            <strong>BÊN B</strong>
            <p>(Ký, ghi rõ họ tên)</p><br><br><br>
            <strong><%= customerName %></strong>
        </div>
    </div>

    <div class="actions">
        <button class="print-btn" onclick="window.print()">🖨️ In hợp đồng</button>
    </div>
</div>

</body>
</html>
