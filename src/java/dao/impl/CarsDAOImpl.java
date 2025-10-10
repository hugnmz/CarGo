package dao.impl;

import dao.CarsDAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import model.Cars;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class CarsDAOImpl implements CarsDAO {

    @Override
    public List<Cars> getAllCars() {
        String sql = "SELECT * FROM dbo.Cars";

        return JdbcTemplateUtil.query(sql, Cars.class);
    }

    @Override
    public Optional<Cars> getCarById(Integer carId) {
        String sql = "SELECT * FROM dbo.Cars where CarId = ?";

        Cars c = JdbcTemplateUtil.queryOne(sql, Cars.class, carId);
        
        return Optional.ofNullable(c);
    }

    @Override
    public boolean addCar(Cars car) {
        String sql = "INSERT INTO dbo.Cars (name, year, description, image, categoryId, fuelId, seatingId, locationId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        int result = JdbcTemplateUtil.update(sql, 
            car.getName(), 
            car.getYear(), 
            car.getDescription(), 
            car.getImage(), 
            car.getCategoryId(), 
            car.getFuelId(), 
            car.getSeatingId(), 
            car.getLocationId()
        );
        
        return result > 0;
    }

    @Override
    public boolean updateCar(Cars car) {
        String sql = "UPDATE dbo.Cars SET name = ?, year = ?, description = ?, image = ?, categoryId = ?, fuelId = ?, seatingId = ?, locationId = ? WHERE carId = ?";
        
        int result = JdbcTemplateUtil.update(sql, 
            car.getName(), 
            car.getYear(), 
            car.getDescription(), 
            car.getImage(), 
            car.getCategoryId(), 
            car.getFuelId(), 
            car.getSeatingId(), 
            car.getLocationId(),
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
    public List<Cars> searchCars(String keyword) {
        String sql = "SELECT * FROM dbo.Cars WHERE name LIKE ? OR description LIKE ?";
        String searchPattern = "%" + keyword + "%";
        
        return JdbcTemplateUtil.query(sql, Cars.class, searchPattern, searchPattern);
    }

    @Override
    public List<Cars> getCarWithCurrentPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT DISTINCT c.* FROM dbo.Cars c " +
                    "INNER JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "WHERE cp.endDate IS NULL " +
                    "AND cp.dailyPrice >= ? AND cp.dailyPrice <= ?";
        
        return JdbcTemplateUtil.query(sql, Cars.class, minPrice, maxPrice);
    }

}
