/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dto.VehicleDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author admin
 */
public interface VehicleService {

    List<VehicleDTO> getVehicleByCarId(Integer carId);

    Optional<VehicleDTO> getVehicleById(Integer vehicleId);
    
    /**
     * Lấy danh sách xe có sẵn theo carId và khoảng thời gian
     * @param carId - ID của loại xe
     * @param startDate - Ngày bắt đầu
     * @param endDate - Ngày kết thúc  
     * @return List<VehicleDTO> - Danh sách xe có sẵn
     */
    List<VehicleDTO> getAvailableVehiclesByCar(Integer carId, LocalDateTime startDate, LocalDateTime endDate);
}
