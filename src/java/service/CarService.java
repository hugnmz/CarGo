/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.*;
import java.util.List;
import java.util.Optional;

public interface CarService {

    List<CarDTO> getAllCars();

    Optional<CarDTO> getCarById(Integer carId);

    boolean addCar(CarDTO carDTO);

    boolean updateCar(CarDTO carDTO);

    boolean deleteCar(Integer carID);

    List<CategoryDTO> getAllCategories();

    List<FuelDTO> getAllFuels();

    List<SeatingDTO> getAllSeatings();

    List<LocationDTO> getAllLocation();

    public boolean addPriceForCar(int carId, double price, double deposit);

    public int addCarAndGetId(CarDTO carDTO);

    public List<VehicleDTO> getVehicalByCarId(int carId);

    List<CarDTO> searchCars(Integer locationId, String name, Integer categoryId, Double price);

     public List<model.CarPrices> getPricesByCarId(int carId);
}
