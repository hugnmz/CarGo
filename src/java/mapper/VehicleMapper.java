package mapper;

import dto.VehicleDTO;
import model.Vehicles;
import model.Cars;
import model.Locations;
import util.di.annotation.Component;

/**
 * VehicleMapper - Chuyển đổi giữa VehicleDTO và Vehicles Model
 */
@Component
public class VehicleMapper {

    // Chuyen tu Model sang DTO
    public VehicleDTO toDTO(Vehicles vehicle) {
        // Kiem tra null
        if (vehicle == null) {
            return null;
        }

        VehicleDTO dto = new VehicleDTO();
        
        // Gan cac truong co ban cua xe
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setCarId(vehicle.getCarId());
        dto.setPlateNumber(vehicle.getPlateNumber());
        dto.setIsActive(vehicle.getIsActive());
        dto.setLocationId(vehicle.getLocationId());

        // Car information (nested)
        if (vehicle.getCar() != null) {
            Cars car = vehicle.getCar();
            dto.setCarName(car.getName());
            dto.setYear(car.getYear());
            dto.setDescription(car.getDescription());
            dto.setImage(car.getImage());
            
            // Category info
            if (car.getCategory() != null) {
                dto.setCategoryName(car.getCategory().getCategoryName());
            }
            
            // Fuel info
            if (car.getFuel() != null) {
                dto.setFuelType(car.getFuel().getFuelType());
            }
            
            // Seating info
            if (car.getSeating() != null) {
                dto.setSeatingType(car.getSeating().getSeatingType());
            }
            
            // Car prices (current price)
            if (car.getCarPrices() != null && !car.getCarPrices().isEmpty()) {
                // Find current price (endDate is null)
                car.getCarPrices().stream()
                    .filter(price -> price.getEndDate() == null)
                    .findFirst()
                    .ifPresent(price -> {
                        dto.setCurrentPrice(price.getDailyPrice());
                        dto.setDepositAmount(price.getDepositAmount());
                    });
            }
        }

        // Location information (nested)
        if (vehicle.getLocation() != null) {
            Locations location = vehicle.getLocation();
            dto.setCity(location.getCity());
            dto.setAddress(location.getAddress());
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Vehicles toModel(VehicleDTO dto) {
        // Kiem tra null
        if (dto == null) {
            return null;
        }

        Vehicles vehicle = new Vehicles();
        
        // Gan cac truong co ban cua xe
        vehicle.setVehicleId(dto.getVehicleId());
        vehicle.setCarId(dto.getCarId());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setIsActive(dto.getIsActive());
        vehicle.setLocationId(dto.getLocationId());

        // Create nested Car object if car info is provided
        if (dto.getCarName() != null || dto.getYear() != null) {
            Cars car = new Cars();
            car.setCarId(dto.getCarId());
            car.setName(dto.getCarName());
            car.setYear(dto.getYear());
            car.setDescription(dto.getDescription());
            car.setImage(dto.getImage());
            vehicle.setCar(car);
        }

        // Create nested Location object if location info is provided
        if (dto.getCity() != null || dto.getAddress() != null) {
            Locations location = new Locations();
            location.setLocationId(dto.getLocationId());
            location.setCity(dto.getCity());
            location.setAddress(dto.getAddress());
            vehicle.setLocation(location);
        }

        return vehicle;
    }
}
