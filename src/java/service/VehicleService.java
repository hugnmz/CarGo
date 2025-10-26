/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dto.LocationDTO;
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
    
    List<LocationDTO> getAllLocation();

    boolean addVehicle(VehicleDTO vehicleDTO) throws Exception;

    boolean updateVehicle(VehicleDTO vehicleDTO) throws Exception;
    
    boolean deleteVehicle(Integer vehicleId);
}
