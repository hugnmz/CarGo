-- Insert Vehicles cho mỗi Car model
-- Mỗi car sẽ có 5 vehicles với biển số khác nhau
-- Chạy script này để thêm vehicles vào bảng dbo.Vehicles

USE RentalCar;
GO

-- Kiểm tra và tạo Locations nếu chưa có
IF NOT EXISTS (SELECT 1 FROM dbo.Locations WHERE city = 'Ha Noi')
    INSERT INTO dbo.Locations (city, address) VALUES ('Ha Noi', 'So 1 Dai Co Viet, Hai Ba Trung');

IF NOT EXISTS (SELECT 1 FROM dbo.Locations WHERE city = 'TP.HCM')
    INSERT INTO dbo.Locations (city, address) VALUES ('TP.HCM', 'So 1 Nguyen Hue, Quan 1');

-- Lấy locationId
DECLARE @haNoiLocationId INT = (SELECT TOP 1 locationId FROM dbo.Locations WHERE city = 'Ha Noi');
DECLARE @hcmLocationId INT = (SELECT TOP 1 locationId FROM dbo.Locations WHERE city = 'TP.HCM');

-- Xóa vehicles cũ nếu có (để tránh duplicate)
DELETE FROM dbo.Vehicles WHERE plateNumber LIKE '30A-%' OR plateNumber LIKE '30B-%' OR plateNumber LIKE '30C-%' OR plateNumber LIKE '30D-%' OR plateNumber LIKE '30E-%';

-- Insert Vehicles cho mỗi Car
-- Lấy danh sách tất cả Cars
DECLARE @carId INT;
DECLARE car_cursor CURSOR FOR 
    SELECT carId FROM dbo.Cars ORDER BY carId;

OPEN car_cursor;
FETCH NEXT FROM car_cursor INTO @carId;

WHILE @@FETCH_STATUS = 0
BEGIN
    -- Tạo 5 vehicles cho mỗi car
    -- Vehicle 1 - Ha Noi
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId)
    VALUES (@carId, '30A-' + RIGHT('0000' + CAST(@carId * 10 + 1 AS VARCHAR), 4), 1, @haNoiLocationId);
    
    -- Vehicle 2 - Ha Noi  
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId)
    VALUES (@carId, '30A-' + RIGHT('0000' + CAST(@carId * 10 + 2 AS VARCHAR), 4), 1, @haNoiLocationId);
    
    -- Vehicle 3 - TP.HCM
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId)
    VALUES (@carId, '30B-' + RIGHT('0000' + CAST(@carId * 10 + 3 AS VARCHAR), 4), 1, @hcmLocationId);
    
    -- Vehicle 4 - TP.HCM
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId)
    VALUES (@carId, '30B-' + RIGHT('0000' + CAST(@carId * 10 + 4 AS VARCHAR), 4), 1, @hcmLocationId);
    
    -- Vehicle 5 - Ha Noi (backup)
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId)
    VALUES (@carId, '30A-' + RIGHT('0000' + CAST(@carId * 10 + 5 AS VARCHAR), 4), 1, @haNoiLocationId);
    
    FETCH NEXT FROM car_cursor INTO @carId;
END

CLOSE car_cursor;
DEALLOCATE car_cursor;

-- Kiểm tra kết quả
SELECT 
    c.name as 'Car Model',
    COUNT(v.vehicleId) as 'Number of Vehicles',
    l.city as 'Location',
    STRING_AGG(v.plateNumber, ', ') as 'Plate Numbers'
FROM dbo.Cars c
LEFT JOIN dbo.Vehicles v ON c.carId = v.carId
LEFT JOIN dbo.Locations l ON v.locationId = l.locationId
GROUP BY c.carId, c.name, l.city
ORDER BY c.carId, l.city;

PRINT 'Đã thêm vehicles thành công!';
PRINT 'Mỗi car model giờ có 5 vehicles với biển số khác nhau.';
