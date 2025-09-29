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

    boolean endCurrentPrice(Integer carId, LocalDate endDate);
    
    Optional<BigDecimal> getCurrentDailyPrice(Integer carId);
    
    Optional<BigDecimal> getCurrentDepositAmount(Integer carId);
}
