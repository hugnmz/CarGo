CREATE DATABASE CarRental;
GO

USE CarRental;
GO

-- ========================
-- ĐỊA ĐIỂM
-- ========================
CREATE TABLE Locations (
    locationId INT IDENTITY(1,1) PRIMARY KEY,
    city NVARCHAR(100),
    address NVARCHAR(255)
);

-- ========================
-- KHÁCH HÀNG
-- ========================
CREATE TABLE Customers (
    customerId INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fullName NVARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    dateOfBirth DATE,
    createAt DATETIME DEFAULT GETDATE(),
    locationId INT,
    FOREIGN KEY (locationId) REFERENCES Locations(locationId)
);

-- ========================
-- NGƯỜI DÙNG & PHÂN QUYỀN
-- ========================
CREATE TABLE Users (
    userId INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fullName NVARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    dateOfBirth DATE,
    createAt DATETIME DEFAULT GETDATE(),
    locationId INT,
    FOREIGN KEY (locationId) REFERENCES Locations(locationId)
);

CREATE TABLE Roles (
    roleId INT IDENTITY(1,1) PRIMARY KEY,
    roleName VARCHAR(50) UNIQUE NOT NULL -- CUSTOMER, STAFF, MANAGER, ADMIN
);

CREATE TABLE UserRoles (
    userId INT,
    roleId INT,
    PRIMARY KEY (userId, roleId),
    FOREIGN KEY (userId) REFERENCES Users(userId),
    FOREIGN KEY (roleId) REFERENCES Roles(roleId)
);

-- ========================
-- DANH MỤC XE
-- ========================
CREATE TABLE Categories (
    categoryId INT IDENTITY(1,1) PRIMARY KEY,
    categoryName NVARCHAR(50) -- sedan, suv
);

CREATE TABLE Fuels (
    fuelId INT IDENTITY(1,1) PRIMARY KEY,
    fuelType NVARCHAR(50) -- Xăng, Dầu, Điện
);

CREATE TABLE Seatings (
    seatingId INT IDENTITY(1,1) PRIMARY KEY,
    seatingType INT
);

CREATE TABLE Cars (
    carId INT IDENTITY(1,1) PRIMARY KEY,
    plateNumber VARCHAR(30) UNIQUE,
    name NVARCHAR(100),    
    year INT,
    description NVARCHAR(255),
    amount INT,
    image VARCHAR(255),
    categoryId INT,
    fuelId INT,
    seatingId INT,
    locationId INT,
    FOREIGN KEY (categoryId) REFERENCES Categories(categoryId),
    FOREIGN KEY (fuelId) REFERENCES Fuels(fuelId),
    FOREIGN KEY (seatingId) REFERENCES Seatings(seatingId),
    FOREIGN KEY (locationId) REFERENCES Locations(locationId)
);

CREATE TABLE CarPrices (
    priceId INT IDENTITY(1,1) PRIMARY KEY,
    carId INT NOT NULL,
    dailyPrice DECIMAL(12,2) NOT NULL,
    depositAmount DECIMAL(12,2) NULL,
    startDate DATE NOT NULL,
    endDate DATE NULL,  -- NULL = hiện hành
    createAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (carId) REFERENCES Cars(carId),
    CONSTRAINT CK_CarPrices_Date CHECK (endDate IS NULL OR endDate > startDate)
);
CREATE INDEX IX_CarPrices_carId ON CarPrices(carId);

-- ========================
-- HỢP ĐỒNG
-- ========================
CREATE TABLE Contracts (
    contractId INT IDENTITY(1,1) PRIMARY KEY,
    customerId INT,
    staffId INT,
    startDate DATETIME NOT NULL,
    endDate DATETIME NOT NULL,
    status VARCHAR(20) DEFAULT 'pending', -- pending, accepted, rejected
    createAt DATETIME DEFAULT GETDATE(),
    totalAmount DECIMAL(12,2),
    depositAmount DECIMAL(12,2),
    FOREIGN KEY (customerId) REFERENCES Customers(customerId),
    FOREIGN KEY (staffId) REFERENCES Users(userId),
    CHECK (endDate > startDate)
);

CREATE TABLE ContractDetails (
    contractDetailId INT IDENTITY(1,1) PRIMARY KEY,
    contractId INT,
    carId INT,
    price DECIMAL(12,2), -- giá tại thời điểm ký
    depositAmount DECIMAL(12,2) NULL,
    note NVARCHAR(255),
    FOREIGN KEY (contractId) REFERENCES Contracts(contractId),
    FOREIGN KEY (carId) REFERENCES Cars(carId)
);
CREATE INDEX IX_ContractDetails_contractId ON ContractDetails(contractId);

-- ========================
-- GIỎ HÀNG
-- ========================
CREATE TABLE Carts (
    cartId INT IDENTITY(1,1) PRIMARY KEY,
    customerId INT UNIQUE,
    createAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (customerId) REFERENCES Customers(customerId)
);

CREATE TABLE Orders (
    cartDetailId INT IDENTITY(1,1) PRIMARY KEY,
    cartId INT,
    carId INT,
    rentStartDate DATETIME,
    rentEndDate DATETIME,
    price DECIMAL(12,2),
    FOREIGN KEY (cartId) REFERENCES Carts(cartId),
    FOREIGN KEY (carId) REFERENCES Cars(carId)
);

-- ========================
-- THANH TOÁN
-- ========================
CREATE TABLE PaymentMethods (
    methodId INT IDENTITY(1,1) PRIMARY KEY,
    methodName NVARCHAR(50) -- Cash, Credit Card, Banking
);

CREATE TABLE Payments (
    paymentId INT IDENTITY(1,1) PRIMARY KEY,
    contractId INT,
    amount DECIMAL(12,2),
    methodId INT,
    status VARCHAR(20), -- pending, completed, failed
    paymentDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (contractId) REFERENCES Contracts(contractId),
    FOREIGN KEY (methodId) REFERENCES PaymentMethods(methodId)
);

-- ========================
-- PHẢN HỒI & SỰ CỐ
-- ========================
CREATE TABLE Feedbacks (
    feedbackId INT IDENTITY(1,1) PRIMARY KEY,
    customerId INT,
    carId INT,
    comment NVARCHAR(255),
    createAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (customerId) REFERENCES Customers(customerId),
    FOREIGN KEY (carId) REFERENCES Cars(carId)
);

CREATE TABLE IncidentTypes (
    incidentTypeId INT IDENTITY(1,1) PRIMARY KEY,
    typeName NVARCHAR(100) -- Tai nạn, Vi phạm giao thông, Phạt nguội
);

CREATE TABLE Incidents (
    incidentId INT IDENTITY(1,1) PRIMARY KEY,    
    description NVARCHAR(255),
    fineAmount DECIMAL(12,2),
    incidentDate DATETIME,
    status VARCHAR(20),
    contractDetailId INT,
    incidentTypeId INT,
    FOREIGN KEY (incidentTypeId) REFERENCES IncidentTypes(incidentTypeId),
    FOREIGN KEY (contractDetailId) REFERENCES ContractDetails(contractDetailId)
);
