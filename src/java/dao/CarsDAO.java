/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import model.Cars;

/**
 *
 * @author admin
 */
public interface CarsDAO {
    List<Cars> getAllCars();
    Optional<Cars> getCarById(Integer carId);
    boolean addCar(Cars car);
    boolean updateCar(Cars car);
    boolean deleteCar(Integer carId);
    List<Cars> getCarByCategory(Integer categoryId);
    List<Cars> getCarByFuel(Integer fuelId);
    List<Cars> getCarBySeating(Integer seatingId);
    List<Cars> getCarByLocation(Integer locationId);
    List<Cars> searchCars(String keyword);
    List<Cars> getCarWithCurrentPrice(BigDecimal minPrice, BigDecimal maxPrice);
}
