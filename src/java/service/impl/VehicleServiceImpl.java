/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.VehiclesDAO;
import dto.LocationDTO;
import dto.VehicleDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.VehicleMapper;
import model.Vehicle;
import service.VehicleService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import util.MessageUtil;
import util.exception.ApplicationException;
import util.exception.DataAccessException;
import util.exception.ValidationException;
import util.exception.BusinessException;
import dao.LocationDAO;

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

    @Autowired
    private LocationDAO locationsDAO;

    @Override
    public List<VehicleDTO> getVehicleByCarId(Integer carId) {
        List<Vehicle> vehicles = vehiclesDAO.getVehiclesByCar(carId);
        List<VehicleDTO> vehicleDTO = new ArrayList<>();

        for (Vehicle v : vehicles) {
            VehicleDTO dto = vehicleMapper.toDTO(v);
            vehicleDTO.add(dto);
        }
        return vehicleDTO;
    }

    @Override
    public Optional<VehicleDTO> getVehicleById(Integer vehicleId) {
        Optional<Vehicle> vehicle = vehiclesDAO.getVehicleById(vehicleId);
        if (vehicle.isPresent()) {
            VehicleDTO dto = vehicleMapper.toDTO(vehicle.get());
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override

    public List<VehicleDTO> getAvailableVehiclesByCar(Integer carId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Vehicle> vehicles = vehiclesDAO.getAvailableVehiclesByCar(carId, startDate, endDate);
        List<VehicleDTO> vehicleDTOs = new ArrayList<>();
        
        for (Vehicle v : vehicles) {
            VehicleDTO dto = vehicleMapper.toDTO(v);
            vehicleDTOs.add(dto);
        }
        
        return vehicleDTOs;
    }

    @Override
    public List<LocationDTO> getAllLocation() {
        try {
            var locations = locationsDAO.getAllLocations();

            return locations.stream()
                    .map(loc -> {
                        LocationDTO dto = new LocationDTO();
                        dto.setLocationId(loc.getLocationId());
                        dto.setCity(loc.getCity());
                        dto.setAddress(loc.getAddress());
                        return dto;
                    })
                    .toList();

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }
    }

    @Override
    public boolean addVehicle(VehicleDTO vehicleDTO) throws Exception {
        if (vehicleDTO == null) {
            throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
        }

        try {
            // Chuyển DTO sang Model
            Vehicle vehicle = vehicleMapper.toModel(vehicleDTO);

            // Kiểm tra biển số trùng
            if (vehiclesDAO.getVehicleyPlateNumber(vehicle.getPlateNumber()).isPresent()) {
                throw new BusinessException(MessageUtil.getError("error.vehicle.plate.exists"));
            }

            // Thêm vehicle
            boolean added = vehiclesDAO.addVehicle(vehicle);
            if (!added) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.vehicle.add.failed"));
            }
            
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.vehicle.add.failed"), e);
        }
    }

    @Override
    public boolean updateVehicle(VehicleDTO vehicleDTO) throws Exception {
        if (vehicleDTO == null) {
            throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
        }

        try {
            // Kiểm tra biển số trùng (ví dụ method riêng trong DAO/Service)
            if (vehiclesDAO.isPlateNumberExist(vehicleDTO.getPlateNumber(), vehicleDTO.getVehicleId())) {
                throw new BusinessException(MessageUtil.getError("error.vehicle.plate.exists"));
            }

            Vehicle vehicle = vehicleMapper.toModel(vehicleDTO);
            boolean updated = vehiclesDAO.updateVehicle(vehicle);

            if (!updated) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.vehicle.update.failed"));
            }

            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.vehicle.update.failed"), e);
        }
    }

    @Override
    public boolean deleteVehicle(Integer vehicleId) {
        try {
            boolean result = vehiclesDAO.deleteVehicle(vehicleId);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.vehicle.delete.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.vehicle.delete.failed"), e);
        }
    }

    @Override
    public int countVehical() {
        return vehiclesDAO.countVehicles();

    }

}
