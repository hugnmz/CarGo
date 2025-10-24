/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.CarDTO;
import java.util.List;
import java.util.Optional;


public interface CarService {
    

    List<CarDTO> getAllCars();
    

    Optional<CarDTO> getCarById(Integer carId);
}
