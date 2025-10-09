<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hợp Đồng Thuê Xe</title>
    <style>
        body { font-family: "Arial", sans-serif; background: #f2f2f2; margin: 0; padding: 20px; }
        .contract-container { max-width: 900px; margin: auto; background: #fff; padding: 30px 50px; 
                              box-shadow: 0 0 10px rgba(0,0,0,0.2); border-radius: 8px; }
        h1, h2 { text-align: center; margin: 5px 0; }
        h2 { font-weight: normal; color: #555; }
        .section { margin-top: 30px; }
        .section-title { font-size: 18px; font-weight: bold; color: #333; border-bottom: 2px solid #3498db; 
                         padding-bottom: 5px; margin-bottom: 15px; }
        label { display: inline-block; width: 180px; font-weight: bold; vertical-align: top; margin-top: 10px; }
        input, textarea, select { width: 70%; padding: 8px; margin-bottom: 12px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px; }
        textarea { height: 60px; resize: vertical; }
        .contract-text { background: #f9f9f9; padding: 15px; border-radius: 5px; border: 1px solid #ddd; margin-bottom: 15px; }
        button { background: #3498db; color: white; border: none; padding: 12px 25px; border-radius: 5px; cursor: pointer; font-size: 16px; }
        button:hover { background: #2980b9; }
    </style>
</head>
<body>

<div class="contract-container">
    <h1>CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</h1>
    <h2>Độc lập – Tự do – Hạnh phúc</h2>
    <h2>HỢP ĐỒNG THUÊ XE</h2>

    <form action="SaveContractServlet" method="post">

        <!-- Thông tin Bên A -->
        <div class="section">
            <div class="section-title">BÊN CHO THUÊ (Bên A)</div>
            <label>Ông:</label><input type="text" name="customerA.maleName"/><br/>
            <label>Sinh năm:</label><input type="text" name="customerA.maleYear"/><br/>
            <label>CMND/CCCD/Hộ chiếu:</label><input type="text" name="customerA.maleId"/><br/>
            <label>Do cơ quan cấp:</label><input type="text" name="customerA.maleIdPlace"/><br/>
            <label>Ngày cấp:</label><input type="date" name="customerA.maleIdDate"/><br/>
            <label>Hộ khẩu thường trú:</label><textarea name="customerA.maleAddress"></textarea>

            <label>Bà:</label><input type="text" name="customerA.femaleName"/><br/>
            <label>Sinh năm:</label><input type="text" name="customerA.femaleYear"/><br/>
            <label>CMND/CCCD/Hộ chiếu:</label><input type="text" name="customerA.femaleId"/><br/>
            <label>Do cơ quan cấp:</label><input type="text" name="customerA.femaleIdPlace"/><br/>
            <label>Ngày cấp:</label><input type="date" name="customerA.femaleIdDate"/><br/>
            <label>Hộ khẩu thường trú:</label><textarea name="customerA.femaleAddress"></textarea>
        </div>

        <!-- Thông tin Bên B -->
        <div class="section">
            <div class="section-title">BÊN THUÊ (Bên B)</div>
            <label>Ông:</label><input type="text" name="customerB.maleName"/><br/>
            <label>Sinh năm:</label><input type="text" name="customerB.maleYear"/><br/>
            <label>CMND/CCCD/Hộ chiếu:</label><input type="text" name="customerB.maleId"/><br/>
            <label>Do cơ quan cấp:</label><input type="text" name="customerB.maleIdPlace"/><br/>
            <label>Ngày cấp:</label><input type="date" name="customerB.maleIdDate"/><br/>
            <label>Hộ khẩu thường trú:</label><textarea name="customerB.maleAddress"></textarea>

            <label>Bà:</label><input type="text" name="customerB.femaleName"/><br/>
            <label>Sinh năm:</label><input type="text" name="customerB.femaleYear"/><br/>
            <label>CMND/CCCD/Hộ chiếu:</label><input type="text" name="customerB.femaleId"/><br/>
            <label>Do cơ quan cấp:</label><input type="text" name="customerB.femaleIdPlace"/><br/>
            <label>Ngày cấp:</label><input type="date" name="customerB.femaleIdDate"/><br/>
            <label>Hộ khẩu thường trú:</label><textarea name="customerB.femaleAddress"></textarea>
        </div>

        <!-- Điều khoản hợp đồng -->
        <div class="section">
            <div class="section-title">ĐIỀU KHOẢN HỢP ĐỒNG</div>

            <div class="contract-text">
                <b>Điều 1. Đặc điểm và thỏa thuận thuê xe</b><br/>
                Nhãn hiệu: <input type="text" name="vehicle.brand"/><br/>
                Số loại: <input type="text" name="vehicle.model"/><br/>
                Loại xe: <input type="text" name="vehicle.type"/><br/>
                Màu sơn: <input type="text" name="vehicle.color"/><br/>
                Số máy: <input type="text" name="vehicle.engineNo"/><br/>
                Số khung: <input type="text" name="vehicle.chassisNo"/><br/>
                Số chỗ ngồi: <input type="text" name="vehicle.seats"/><br/>
                Biển số: <input type="text" name="vehicle.plate"/><br/>
                Đăng ký xe có giá trị đến ngày: <input type="date" name="vehicle.registrationDate"/><br/>
                Tên chủ sở hữu: <input type="text" name="vehicle.ownerName"/><br/>
                Địa chỉ: <textarea name="vehicle.ownerAddress"></textarea><br/>
                Giấy chứng nhận kiểm định số: <input type="text" name="vehicle.certNo"/><br/>
                Ngày cấp: <input type="date" name="vehicle.certDate"/>
            </div>

            <div class="contract-text">
                <b>Điều 2. Thời hạn thuê xe ô tô</b><br/>
                Thời hạn thuê: <input type="text" name="contract.duration"/> tháng<br/>
            </div>

            <div class="contract-text">
                <b>Điều 3. Mục đích thuê</b><br/>
                <textarea name="contract.purpose">Nhập mục đích thuê</textarea>
            </div>

            <div class="contract-text">
                <b>Điều 4. Giá thuê và phương thức thanh toán</b><br/>
                Giá thuê: <input type="text" name="contract.price"/> VNĐ (Bằng chữ: <input type="text" name="contract.priceInWords"/>)<br/>
                Phương thức thanh toán: <input type="text" name="contract.paymentMethod"/><br/>
                Ngày thanh toán: <input type="date" name="contract.paymentDate"/>
            </div>

            <div class="contract-text">
                <b>Điều 5. Phương thức giao, trả lại tài sản thuê</b><br/>
                <textarea name="contract.returnMethod">Nhập cách thức giao trả xe</textarea>
            </div>

            <div class="contract-text">
                <b>Điều 6-9. Nghĩa vụ, quyền và cam đoan của các bên</b><br/>
                <textarea name="contract.obligations" style="height:200px;">
Nhập nội dung nghĩa vụ, quyền và cam đoan của Bên A & B, các điều khoản cuối cùng...
                </textarea>
            </div>
        </div>

        <div class="section" style="text-align:center;">
            <button type="submit">Lưu hợp đồng</button>
        </div>

    </form>
</div>

</body>
</html>
