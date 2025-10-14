/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.CarDTO;
import java.util.List;
import java.util.Optional;

/**
 * CarService - Service layer cho Car business logic
 * 
 * MỤC ĐÍCH:
 * - Cung cấp business logic cho Car operations
 * - Trả về DTO thay vì Entity trực tiếp
 * - Abstract hóa việc truy cập database thông qua DAO
 */
public interface CarService {
    
    /**
     * Lấy tất cả xe từ database và chuyển thành DTO
     * @return List<CarDTO> - Danh sách tất cả xe dạng DTO
     */
    List<CarDTO> getAllCars();
    
    /**
     * Lấy xe theo ID và chuyển thành DTO
     * @param carId - ID của xe
     * @return Optional<CarDTO> - Xe tìm được hoặc empty
     */
    Optional<CarDTO> getCarById(Integer carId);
}
