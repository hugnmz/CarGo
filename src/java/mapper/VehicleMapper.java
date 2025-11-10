package mapper;

import dto.VehicleDTO;
import model.Vehicle;
import model.Cars;
import model.Location;
import util.di.annotation.Component;

/**
 * VehicleMapper - Chuyển đổi giữa VehicleDTO và Vehicle Model
 */
@Component
public class VehicleMapper {

    // Chuyen tu Model sang DTO
    public VehicleDTO toDTO(Vehicle vehicle) {
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
            if (car.getCarPrices() != null) {
                if (car.getCarPrices().getDailyPrice() != null) {
                    dto.setCurrentPrice(car.getCarPrices().getDailyPrice());
                }
                if (car.getCarPrices().getDepositAmount() != null) {
                    dto.setDepositAmount(car.getCarPrices().getDepositAmount());
                }
            }
        }

        // Location information (nested)
        if (vehicle.getLocation() != null) {
            Location location = vehicle.getLocation();
            dto.setCity(location.getCity());
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Vehicle toModel(VehicleDTO dto) {
        // Kiem tra null
        if (dto == null) {
            return null;
        }

        Vehicle vehicle = new Vehicle();
        
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
        if (dto.getCity() != null) {
            Location location = new Location();
            location.setLocationId(dto.getLocationId());
            location.setCity(dto.getCity());
            vehicle.setLocation(location);
        }

        return vehicle;
    }
}
