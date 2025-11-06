package dao.impl;

import dao.CarsDAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import model.Cars;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

// lop trien khai dao cho cars
@Repository
public class CarsDAOImpl implements CarsDAO {

    @Override
    public List<Cars> getAllCars() {
        String sql = "SELECT "
                + "c.carId, c.name, c.year, c.description, c.image, c.categoryId, c.fuelId, c.seatingId, "
                + "cat.categoryId, cat.categoryName, "
                + "f.fuelId, f.fuelType, "
                + "s.seatingId, s.seatingType, "
                + "cp.priceId as cp_priceId, cp.dailyPrice as cp_dailyPrice, cp.depositAmount as cp_depositAmount, "
                + "cp.startDate as cp_startDate, cp.endDate as cp_endDate, cp.createAt as cp_createAt "
                + "FROM dbo.Cars c "
                + "LEFT JOIN dbo.Categories cat ON c.categoryId = cat.categoryId "
                + "LEFT JOIN dbo.Fuels f ON c.fuelId = f.fuelId "
                + "LEFT JOIN dbo.Seatings s ON c.seatingId = s.seatingId "
                + "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId AND cp.endDate IS NULL";


        return JdbcTemplateUtil.query(sql, Cars.class);
    }

     @Override
    public List<Cars> getAllCar() {
        String sql = """
        SELECT 
            c.carId, c.name, c.year, c.description, c.image,
            c.categoryId, c.fuelId, c.seatingId,
            cat.categoryName,
            f.fuelType,
            s.seatingType,
            cp.priceId AS cp_priceId,
            cp.dailyPrice AS cp_dailyPrice,
            cp.depositAmount AS cp_depositAmount,
            cp.startDate AS cp_startDate,
            cp.endDate AS cp_endDate,
            cp.createAt AS cp_createAt
        FROM dbo.Cars c
        LEFT JOIN dbo.Categories cat ON c.categoryId = cat.categoryId
        LEFT JOIN dbo.Fuels f ON c.fuelId = f.fuelId
        LEFT JOIN dbo.Seatings s ON c.seatingId = s.seatingId
        LEFT JOIN dbo.CarPrices cp ON cp.priceId = (
            SELECT TOP 1 priceId
            FROM dbo.CarPrices
            WHERE carId = c.carId
            ORDER BY createAt DESC
        );
    """;

        return JdbcTemplateUtil.query(sql, Cars.class);
    }

    @Override
    public Optional<Cars> getCarById(Integer carId) {

        String sql = "SELECT "
                + "c.carId, c.name, c.year, c.description, c.image, c.categoryId, c.fuelId, c.seatingId, "
                + "cat.categoryId, cat.categoryName, "
                + "f.fuelId, f.fuelType, "
                + "s.seatingId, s.seatingType, "
                + "cp.priceId as cp_priceId, cp.dailyPrice as cp_dailyPrice, cp.depositAmount as cp_depositAmount, "
                + "cp.startDate as cp_startDate, cp.endDate as cp_endDate, cp.createAt as cp_createAt "
                + "FROM dbo.Cars c "
                + "LEFT JOIN dbo.Categories cat ON c.categoryId = cat.categoryId "
                + "LEFT JOIN dbo.Fuels f ON c.fuelId = f.fuelId "
                + "LEFT JOIN dbo.Seatings s ON c.seatingId = s.seatingId "
                + "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId AND cp.endDate IS NULL "
                + "WHERE c.carId = ?";

        Cars c = JdbcTemplateUtil.queryOne(sql, Cars.class, carId);

        return Optional.ofNullable(c);
    }

    @Override
    public boolean addCar(Cars car) {
        String sql = "INSERT INTO dbo.Cars (name, year, description, image, categoryId, fuelId, seatingId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        int result = JdbcTemplateUtil.update(sql, 
            car.getName(), 
            car.getYear(), 
            car.getDescription(), 
            car.getImage(), 
            car.getCategoryId(), 
            car.getFuelId(), 
            car.getSeatingId()
        );
        
        return result > 0;
    }
    
    @Override
    public int addCarAndReturnId(Cars car) {
        String sql = "INSERT INTO dbo.Cars (name, year, description, image, categoryId, fuelId, seatingId) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        return JdbcTemplateUtil.insertAndReturnKey(sql,
                car.getName(),
                car.getYear(),
                car.getDescription(),
                car.getImage(),
                car.getCategoryId(),
                car.getFuelId(),
                car.getSeatingId()
        );
    }

    @Override
    public boolean updateCar(Cars car) {
        String sql = "UPDATE dbo.Cars SET name = ?, year = ?, description = ?, image = ?, categoryId = ?, fuelId = ?, seatingId = ? WHERE carId = ?";

        int result = JdbcTemplateUtil.update(sql,
                car.getName(),
                car.getYear(),
                car.getDescription(),
                car.getImage(),
                car.getCategoryId(),
                car.getFuelId(),
                car.getSeatingId(),
                car.getCarId()
        );

        return result > 0;
    }

    @Override
    public boolean deleteCar(Integer carId) {
        String sql = "DELETE FROM dbo.Cars WHERE carId = ?";

        int result = JdbcTemplateUtil.update(sql, carId);

        return result > 0;
    }

    @Override
    public List<Cars> getCarByCategory(Integer categoryId) {
        String sql = "SELECT * FROM dbo.Cars WHERE categoryId = ?";

        return JdbcTemplateUtil.query(sql, Cars.class, categoryId);
    }

    @Override
    public List<Cars> getCarByFuel(Integer fuelId) {
        String sql = "SELECT * FROM dbo.Cars WHERE fuelId = ?";

        return JdbcTemplateUtil.query(sql, Cars.class, fuelId);
    }

    @Override
    public List<Cars> getCarBySeating(Integer seatingId) {
        String sql = "SELECT * FROM dbo.Cars WHERE seatingId = ?";

        return JdbcTemplateUtil.query(sql, Cars.class, seatingId);
    }

    @Override
    public List<Cars> getCarByLocation(Integer locationId) {
        String sql = "SELECT * FROM dbo.Cars WHERE locationId = ?";

        return JdbcTemplateUtil.query(sql, Cars.class, locationId);
    }

    @Override
    public List<Cars> getCarWithCurrentPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT DISTINCT c.* FROM dbo.Cars c "
                + "INNER JOIN dbo.CarPrices cp ON c.carId = cp.carId "
                + "WHERE cp.endDate IS NULL "
                + "AND cp.dailyPrice >= ? AND cp.dailyPrice <= ?";

        return JdbcTemplateUtil.query(sql, Cars.class, minPrice, maxPrice);
    }

    @Override
    public List<Cars> searchCars(Integer locationId, String name, Integer categoryId, Double price) {
        String condition = JdbcTemplateUtil.formatConditionSearchCar(locationId, name, categoryId, price);
        String sql = """
        SELECT 
            c.carId, c.name, c.year, c.description, c.image,
            c.categoryId, c.fuelId, c.seatingId,
            cat.categoryName,
            f.fuelType,
            s.seatingType,
            cp.priceId AS cp_priceId,
            cp.dailyPrice AS cp_dailyPrice,
            cp.depositAmount AS cp_depositAmount,
            cp.startDate AS cp_startDate,
            cp.endDate AS cp_endDate,
            cp.createAt AS cp_createAt
        FROM dbo.Cars c
        JOIN dbo.CarPrices cp ON cp.priceId = (
            SELECT TOP 1 priceId
            FROM dbo.CarPrices
            WHERE carId = c.carId
            ORDER BY createAt DESC
        )
        JOIN dbo.Categories cat ON c.categoryId = cat.categoryId
        JOIN dbo.Fuels f ON f.fuelId = c.fuelId
        JOIN dbo.Seatings s ON s.seatingId = c.seatingId
        """ + condition;
        return JdbcTemplateUtil.query(sql, Cars.class);
    }

    @Override
    public Optional<Cars> findByNameAndYear(String name, int year) {
        String sql = "SELECT * FROM dbo.Cars WHERE name = ? AND year = ?";
        Cars car = JdbcTemplateUtil.queryOne(sql, Cars.class, name, year);
        return Optional.ofNullable(car);
    }
}
