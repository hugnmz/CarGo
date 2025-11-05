/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import model.CarPrices;
import model.Cars;

/**
 *
 * @author admin
 */
public interface CarPricesDAO {

    List<CarPrices> getAllCarPrices();

    Optional<CarPrices> getCurrentPriceByCar(Integer carId);

    List<CarPrices> getPricesByCar(Integer carId);

    boolean addCarPrice(CarPrices carPrice);

    boolean updateCarPrice(CarPrices carPrice);

    boolean deleteCarPrice(Integer priceId);
    
    boolean deleteCarPricesByCarId(Integer carId);

    boolean endCurrentPrice(Integer carId);
    
    Optional<BigDecimal> getCurrentDailyPrice(Integer carId);
    
    Optional<BigDecimal> getCurrentDepositAmount(Integer carId);

}
