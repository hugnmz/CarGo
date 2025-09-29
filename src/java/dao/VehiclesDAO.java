/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Vehicles;

/**
 *
 * @author admin
 */
public interface VehiclesDAO {
    List<Vehicles> getAllVehicles();
    Optional<Vehicles> getVehicleById(Integer vehicleId);
    Optional<Vehicles> getVehicleyPlateNumber(String plateNumber);
    boolean addVehicle(Vehicles vehicle);
    boolean updateVehicle(Vehicles vehicle);
    boolean deleteVehicle(Integer vehicleId);
    List<Vehicles> getVehiclesByCar(Integer CarId);
    List<Vehicles> getAvailableVehiclesByCar(Integer carId, LocalDateTime startDate
    ,LocalDateTime endDate);
}
