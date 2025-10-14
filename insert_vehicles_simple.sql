-- Script đơn giản để thêm vehicles
-- Chạy script này để thêm 5 vehicles cho mỗi car model

USE RentalCar;
GO

-- Kiểm tra locations
IF NOT EXISTS (SELECT 1 FROM dbo.Locations WHERE city = 'Ha Noi')
    INSERT INTO dbo.Locations (city, address) VALUES ('Ha Noi', 'So 1 Dai Co Viet, Hai Ba Trung');

IF NOT EXISTS (SELECT 1 FROM dbo.Locations WHERE city = 'TP.HCM')
    INSERT INTO dbo.Locations (city, address) VALUES ('TP.HCM', 'So 1 Nguyen Hue, Quan 1');

-- Lấy locationId
DECLARE @haNoiId INT = (SELECT TOP 1 locationId FROM dbo.Locations WHERE city = 'Ha Noi');
DECLARE @hcmId INT = (SELECT TOP 1 locationId FROM dbo.Locations WHERE city = 'TP.HCM');

-- Thêm vehicles cho Car ID 1 (nếu có)
IF EXISTS (SELECT 1 FROM dbo.Cars WHERE carId = 1)
BEGIN
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId) VALUES
    (1, '30A-1001', 1, @haNoiId),
    (1, '30A-1002', 1, @haNoiId),
    (1, '30B-1003', 1, @hcmId),
    (1, '30B-1004', 1, @hcmId),
    (1, '30A-1005', 1, @haNoiId);
END

-- Thêm vehicles cho Car ID 2 (nếu có)
IF EXISTS (SELECT 1 FROM dbo.Cars WHERE carId = 2)
BEGIN
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId) VALUES
    (2, '30A-2001', 1, @haNoiId),
    (2, '30A-2002', 1, @haNoiId),
    (2, '30B-2003', 1, @hcmId),
    (2, '30B-2004', 1, @hcmId),
    (2, '30A-2005', 1, @haNoiId);
END

-- Thêm vehicles cho Car ID 3 (nếu có)
IF EXISTS (SELECT 1 FROM dbo.Cars WHERE carId = 3)
BEGIN
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId) VALUES
    (3, '30A-3001', 1, @haNoiId),
    (3, '30A-3002', 1, @haNoiId),
    (3, '30B-3003', 1, @hcmId),
    (3, '30B-3004', 1, @hcmId),
    (3, '30A-3005', 1, @haNoiId);
END

-- Thêm vehicles cho Car ID 4 (nếu có)
IF EXISTS (SELECT 1 FROM dbo.Cars WHERE carId = 4)
BEGIN
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId) VALUES
    (4, '30A-4001', 1, @haNoiId),
    (4, '30A-4002', 1, @haNoiId),
    (4, '30B-4003', 1, @hcmId),
    (4, '30B-4004', 1, @hcmId),
    (4, '30A-4005', 1, @haNoiId);
END

-- Thêm vehicles cho Car ID 5 (nếu có)
IF EXISTS (SELECT 1 FROM dbo.Cars WHERE carId = 5)
BEGIN
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId) VALUES
    (5, '30A-5001', 1, @haNoiId),
    (5, '30A-5002', 1, @haNoiId),
    (5, '30B-5003', 1, @hcmId),
    (5, '30B-5004', 1, @hcmId),
    (5, '30A-5005', 1, @haNoiId);
END

-- Thêm vehicles cho Car ID 6 (nếu có)
IF EXISTS (SELECT 1 FROM dbo.Cars WHERE carId = 6)
BEGIN
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId) VALUES
    (6, '30A-6001', 1, @haNoiId),
    (6, '30A-6002', 1, @haNoiId),
    (6, '30B-6003', 1, @hcmId),
    (6, '30B-6004', 1, @hcmId),
    (6, '30A-6005', 1, @haNoiId);
END

-- Thêm vehicles cho Car ID 7 (nếu có)
IF EXISTS (SELECT 1 FROM dbo.Cars WHERE carId = 7)
BEGIN
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId) VALUES
    (7, '30A-7001', 1, @haNoiId),
    (7, '30A-7002', 1, @haNoiId),
    (7, '30B-7003', 1, @hcmId),
    (7, '30B-7004', 1, @hcmId),
    (7, '30A-7005', 1, @haNoiId);
END

-- Thêm vehicles cho Car ID 8 (nếu có)
IF EXISTS (SELECT 1 FROM dbo.Cars WHERE carId = 8)
BEGIN
    INSERT INTO dbo.Vehicles (carId, plateNumber, isActive, locationId) VALUES
    (8, '30A-8001', 1, @haNoiId),
    (8, '30A-8002', 1, @haNoiId),
    (8, '30B-8003', 1, @hcmId),
    (8, '30B-8004', 1, @hcmId),
    (8, '30A-8005', 1, @haNoiId);
END

-- Kiểm tra kết quả
SELECT 
    c.carId,
    c.name as 'Car Model',
    COUNT(v.vehicleId) as 'Number of Vehicles'
FROM dbo.Cars c
LEFT JOIN dbo.Vehicles v ON c.carId = v.carId
GROUP BY c.carId, c.name
ORDER BY c.carId;

PRINT 'Đã thêm vehicles thành công!';
