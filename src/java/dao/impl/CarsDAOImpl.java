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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateCar(Cars car) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteCar(Integer carId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cars> getCarByCategory(Integer categoryId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cars> getCarByFuel(Integer fuelId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cars> getCarBySeating(Integer seatingId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cars> getCarByLocation(Integer locationId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cars> searchCars(String keyword) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cars> getCarWithCurrentPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
