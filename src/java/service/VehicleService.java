/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dto.VehicleDTO;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author admin
 */
public interface VehicleService {

    List<VehicleDTO> getVehicleByCarId(Integer carId);

    Optional<VehicleDTO> getVehicleById(Integer vehicleId);
}
