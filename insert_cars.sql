-- Insert Cars vào database RentalCar
-- Chạy script này để thêm xe vào bảng dbo.Cars

USE RentalCar;
GO

-- Xóa duplicate data trước khi insert (xóa Vehicles trước)
DELETE FROM dbo.Vehicles WHERE carId IN (SELECT carId FROM dbo.Cars WHERE name LIKE '%2023%');
DELETE FROM dbo.CarPrices WHERE carId IN (SELECT carId FROM dbo.Cars WHERE name LIKE '%2023%');
DELETE FROM dbo.Cars WHERE name LIKE '%2023%';

-- Kiểm tra và insert Categories nếu chưa có
IF NOT EXISTS (SELECT 1 FROM dbo.Categories WHERE categoryName = 'Sedan')
    INSERT INTO dbo.Categories (categoryName) VALUES ('Sedan');

IF NOT EXISTS (SELECT 1 FROM dbo.Categories WHERE categoryName = 'SUV')
    INSERT INTO dbo.Categories (categoryName) VALUES ('SUV');

IF NOT EXISTS (SELECT 1 FROM dbo.Categories WHERE categoryName = 'Hatchback')
    INSERT INTO dbo.Categories (categoryName) VALUES ('Hatchback');

-- Kiểm tra và insert Fuels nếu chưa có
IF NOT EXISTS (SELECT 1 FROM dbo.Fuels WHERE fuelType = 'Xang')
    INSERT INTO dbo.Fuels (fuelType) VALUES ('Xang');

IF NOT EXISTS (SELECT 1 FROM dbo.Fuels WHERE fuelType = 'Diesel')
    INSERT INTO dbo.Fuels (fuelType) VALUES ('Diesel');

IF NOT EXISTS (SELECT 1 FROM dbo.Fuels WHERE fuelType = 'Hybrid')
    INSERT INTO dbo.Fuels (fuelType) VALUES ('Hybrid');

-- Kiểm tra và insert Seatings nếu chưa có
IF NOT EXISTS (SELECT 1 FROM dbo.Seatings WHERE seatingType = 5)
    INSERT INTO dbo.Seatings (seatingType) VALUES (5);

IF NOT EXISTS (SELECT 1 FROM dbo.Seatings WHERE seatingType = 7)
    INSERT INTO dbo.Seatings (seatingType) VALUES (7);

-- Kiểm tra và insert Locations nếu chưa có
IF NOT EXISTS (SELECT 1 FROM dbo.Locations WHERE city = 'Ha Noi')
    INSERT INTO dbo.Locations (city, address) VALUES ('Ha Noi', 'So 1 Dai Co Viet, Hai Ba Trung');

IF NOT EXISTS (SELECT 1 FROM dbo.Locations WHERE city = 'TP.HCM')
    INSERT INTO dbo.Locations (city, address) VALUES ('TP.HCM', 'So 1 Nguyen Hue, Quan 1');

-- Insert Cars (sử dụng dữ liệu có sẵn trong database)
INSERT INTO dbo.Cars (name, year, description, image, categoryId, fuelId, seatingId)
VALUES 
    ('Toyota Camry 2023', 2023, 'Xe sedan cao cap, tiet kiem nhien lieu', 'https://via.placeholder.com/400x250?text=Toyota+Camry', 
     (SELECT TOP 1 categoryId FROM dbo.Categories WHERE categoryName = 'Sedan'),
     (SELECT TOP 1 fuelId FROM dbo.Fuels WHERE fuelType = 'Xang'),
     (SELECT TOP 1 seatingId FROM dbo.Seatings WHERE seatingType = 5)),

    ('Honda CR-V 2023', 2023, 'Xe SUV 7 cho, phu hop gia dinh', 'https://via.placeholder.com/400x250?text=Honda+CR-V',
     (SELECT TOP 1 categoryId FROM dbo.Categories WHERE categoryName = 'SUV'),
     (SELECT TOP 1 fuelId FROM dbo.Fuels WHERE fuelType = 'Xang'),
     (SELECT TOP 1 seatingId FROM dbo.Seatings WHERE seatingType = 7)),

    ('Mazda CX-5 2023', 2023, 'Xe SUV sang trong, dong co manh me', 'https://via.placeholder.com/400x250?text=Mazda+CX-5',
     (SELECT TOP 1 categoryId FROM dbo.Categories WHERE categoryName = 'SUV'),
     (SELECT TOP 1 fuelId FROM dbo.Fuels WHERE fuelType = 'Xang'),
     (SELECT TOP 1 seatingId FROM dbo.Seatings WHERE seatingType = 5)),

    ('Hyundai Elantra 2023', 2023, 'Xe sedan hien dai, thiet ke tre trung', 'https://via.placeholder.com/400x250?text=Hyundai+Elantra',
     (SELECT TOP 1 categoryId FROM dbo.Categories WHERE categoryName = 'Sedan'),
     (SELECT TOP 1 fuelId FROM dbo.Fuels WHERE fuelType = 'Xang'),
     (SELECT TOP 1 seatingId FROM dbo.Seatings WHERE seatingType = 5)),

    ('Ford Ranger 2023', 2023, 'Xe ban tai manh me, phu hop cong viec', 'https://via.placeholder.com/400x250?text=Ford+Ranger',
     (SELECT TOP 1 categoryId FROM dbo.Categories WHERE categoryName = 'SUV'),
     (SELECT TOP 1 fuelId FROM dbo.Fuels WHERE fuelType = 'Diesel'),
     (SELECT TOP 1 seatingId FROM dbo.Seatings WHERE seatingType = 5)),

    ('Kia Seltos 2023', 2023, 'Xe SUV nho gon, tiet kiem nhien lieu', 'https://via.placeholder.com/400x250?text=Kia+Seltos',
     (SELECT TOP 1 categoryId FROM dbo.Categories WHERE categoryName = 'SUV'),
     (SELECT TOP 1 fuelId FROM dbo.Fuels WHERE fuelType = 'Xang'),
     (SELECT TOP 1 seatingId FROM dbo.Seatings WHERE seatingType = 5));

-- Insert CarPrices cho các xe (với startDate)
INSERT INTO dbo.CarPrices (carId, dailyPrice, startDate)
SELECT 
    c.carId,
    CASE 
        WHEN c.name LIKE '%Camry%' THEN 800
        WHEN c.name LIKE '%CR-V%' THEN 900
        WHEN c.name LIKE '%CX-5%' THEN 850
        WHEN c.name LIKE '%Elantra%' THEN 700
        WHEN c.name LIKE '%Ranger%' THEN 1000
        WHEN c.name LIKE '%Seltos%' THEN 750
    END as dailyPrice,
    GETDATE() as startDate
FROM dbo.Cars c
WHERE c.name IN ('Toyota Camry 2023', 'Honda CR-V 2023', 'Mazda CX-5 2023', 'Hyundai Elantra 2023', 'Ford Ranger 2023', 'Kia Seltos 2023');

-- Kiểm tra kết quả
SELECT 'Cars inserted:' as Status, COUNT(*) as Count FROM dbo.Cars;
SELECT 'CarPrices inserted:' as Status, COUNT(*) as Count FROM dbo.CarPrices;

SELECT * FROM Cars
