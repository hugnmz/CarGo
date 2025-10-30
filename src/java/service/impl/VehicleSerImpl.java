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
import model.Vehicles;
import service.VehicleService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import dao.LocDAO;

/**
 *
 * @author admin
 */
@Service
public class VehicleSerImpl implements VehicleService {

    @Autowired
    private VehiclesDAO vehiclesDAO;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private LocDAO locationsDAO;

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

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Trả về danh sách rỗng nếu lỗi
        }

    }

    @Override
    public boolean addVehicle(VehicleDTO vehicleDTO) throws Exception {
        if (vehicleDTO == null) {
            throw new IllegalArgumentException("Vehicle không được null");
        }

        // Chuyển DTO sang Model
        Vehicles vehicle = vehicleMapper.toModel(vehicleDTO);

        // Kiểm tra biển số trùng
        if (vehiclesDAO.getVehicleyPlateNumber(vehicle.getPlateNumber()).isPresent()) {
            throw new Exception("Biển số " + vehicle.getPlateNumber() + " đã tồn tại.");
        }

        // Thêm vehicle
        boolean added = vehiclesDAO.addVehicle(vehicle);
        if (!added) {
            throw new Exception("Không thể thêm vehicle. Lỗi cơ sở dữ liệu.");
        }
        
        return true;
    }

    @Override
    public boolean updateVehicle(VehicleDTO vehicleDTO) throws Exception {
        if (vehicleDTO == null) {
            throw new Exception("Vehicle không tồn tại!");
        }

        // Kiểm tra biển số trùng (ví dụ method riêng trong DAO/Service)
        if (vehiclesDAO.isPlateNumberExist(vehicleDTO.getPlateNumber(), vehicleDTO.getVehicleId())) {
            throw new Exception("Biển số '" + vehicleDTO.getPlateNumber() + "' đã tồn tại!");
        }

        Vehicles vehicle = vehicleMapper.toModel(vehicleDTO);
        boolean updated = vehiclesDAO.updateVehicle(vehicle);

        if (!updated) {
            throw new Exception("Cập nhật thất bại do dữ liệu không hợp lệ hoặc lỗi hệ thống!");
        }

        return true;
    }

    @Override
    public boolean deleteVehicle(Integer vehicleId) {
        return vehiclesDAO.deleteVehicle(vehicleId);
    }

    @Override
    public int countVehical() {
        return vehiclesDAO.countVehicles();
    }

}
