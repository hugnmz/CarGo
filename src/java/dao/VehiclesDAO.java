/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Vehicle;

/**
 *
 * @author admin
 */
public interface VehiclesDAO {

    List<Vehicle> getAllVehicles();

    Optional<Vehicle> getVehicleById(Integer vehicleId);

    Optional<Vehicle> getVehicleyPlateNumber(String plateNumber);

    boolean addVehicle(Vehicle vehicle);

    boolean updateVehicle(Vehicle vehicle);

    boolean deleteVehicle(Integer vehicleId);

    public boolean deleteVehiclesByCarId(Integer carId);

    List<Vehicle> getVehiclesByCar(Integer CarId);

    List<Vehicle> getAvailableVehiclesByCar(Integer carId, LocalDateTime startDate,
            LocalDateTime endDate);

    boolean isVehicleAvailable(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);

    public boolean deleteVehicleByCarId(Integer carId);

    boolean isPlateNumberExist(String plateNumber, Integer excludeVehicleId);
    
    public int countVehicles();
}
