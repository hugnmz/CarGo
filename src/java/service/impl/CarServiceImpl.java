/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.CarsDAO;
import dto.CarDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.CarMapper;
import model.Cars;
import service.CarService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

/**
 * CarServiceImpl - Implementation của CarService
 * 
 * MỤC ĐÍCH:
 * - Implement business logic cho Car operations
 * - Chuyển đổi Entity sang DTO
 * - Sử dụng CarsDAO để truy cập database
 */
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarsDAO carsDAO;
    
    @Autowired
    private CarMapper carMapper;
    
    @Override
    public List<CarDTO> getAllCars() {
        List<Cars> cars = carsDAO.getAllCars();
        List<CarDTO> carDTOs = new ArrayList<>();
        
        for (Cars car : cars) {
            CarDTO dto = carMapper.toDTO(car);
            carDTOs.add(dto);
        }
        
        return carDTOs;
    }

    @Override
    public Optional<CarDTO> getCarById(Integer carId) {
        Optional<Cars> car = carsDAO.getCarById(carId);
        if (car.isPresent()) {
            CarDTO dto = carMapper.toDTO(car.get());
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
