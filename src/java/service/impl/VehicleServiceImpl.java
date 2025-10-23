/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.VehiclesDAO;
import dto.VehicleDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.VehicleMapper;
import model.Vehicles;
import service.VehicleService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

/**
 *
 * @author admin
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehiclesDAO vehiclesDAO;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public List<VehicleDTO> getVehicleByCarId(Integer carId) {
        List<Vehicles> vehicles = vehiclesDAO.getVehiclesByCar(carId);
        List<VehicleDTO> vehicleDTO = new ArrayList<>();

        for (Vehicles v : vehicles) {
            VehicleDTO dto = vehicleMapper.toDTO(v);
            vehicleDTO.add(dto);
        }
        return vehicleDTO;
    }

    @Override
    public Optional<VehicleDTO> getVehicleById(Integer vehicleId) {
        Optional<Vehicles> vehicle = vehiclesDAO.getVehicleById(vehicleId);
        if (vehicle.isPresent()) {
            VehicleDTO dto = vehicleMapper.toDTO(vehicle.get());
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public List<VehicleDTO> getAvailableVehiclesByCar(Integer carId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Vehicles> vehicles = vehiclesDAO.getAvailableVehiclesByCar(carId, startDate, endDate);
        List<VehicleDTO> vehicleDTOs = new ArrayList<>();
        
        for (Vehicles v : vehicles) {
            VehicleDTO dto = vehicleMapper.toDTO(v);
            vehicleDTOs.add(dto);
        }
        
        return vehicleDTOs;
    }

}
