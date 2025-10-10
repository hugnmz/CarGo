package mapper;

import dto.CarDTO;
import model.Cars;
import model.Categories;
import model.Fuels;
import model.Seatings;
import model.Locations;
import model.CarPrices;
import model.Vehicles;
import util.di.annotation.Component;
import java.util.List;
import java.util.ArrayList;

/**
 * CarMapper - Chuyển đổi giữa CarDTO và Cars Model
 */
@Component
public class CarMapper {

    // Chuyen tu Model sang DTO
    public CarDTO toDTO(Cars car) {
        // Kiem tra null
        if (car == null) {
            return null;
        }

        CarDTO dto = new CarDTO();
        
        // Gan cac truong co ban cua xe
        dto.setCarId(car.getCarId());
        dto.setName(car.getName());
        dto.setYear(car.getYear());
        dto.setDescription(car.getDescription());
        dto.setImage(car.getImage());

        // Thong tin danh muc xe (nested)
        if (car.getCategory() != null) {
            Categories category = car.getCategory();
            dto.setCategoryName(category.getCategoryName());
        }

        // Thong tin loai nhien lieu (nested)
        if (car.getFuel() != null) {
            Fuels fuel = car.getFuel();
            dto.setFuelType(fuel.getFuelType());
        }

        // Thong tin so cho ngoi (nested)
        if (car.getSeating() != null) {
            Seatings seating = car.getSeating();
            dto.setSeatingType(seating.getSeatingType());
        }

        // Thong tin dia diem (nested)
        if (car.getLocation() != null) {
            Locations location = car.getLocation();
            dto.setCity(location.getCity());
            dto.setAddress(location.getAddress());
        }

        // Thong tin gia hien tai (nested)
        if (car.getCarPrices() != null && !car.getCarPrices().isEmpty()) {
            // Tim gia hien tai (endDate la null)
            for (CarPrices price : car.getCarPrices()) {
                if (price.getEndDate() == null) {
                    dto.setCurrentPrice(price.getDailyPrice());
                    dto.setDepositAmount(price.getDepositAmount());
                    break; // Chi lay gia hien tai dau tien
                }
            }
        }

        // Danh sach xe co san (bien so)
        if (car.getVehicles() != null) {
            List<String> plateNumbers = new ArrayList<>();
            for (Vehicles vehicle : car.getVehicles()) {
                if (vehicle.getIsActive() != null && vehicle.getIsActive()) {
                    plateNumbers.add(vehicle.getPlateNumber());
                }
            }
            dto.setAvailableVehicles(plateNumbers);
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Cars toModel(CarDTO dto) {
        // Kiem tra null
        if (dto == null) {
            return null;
        }

        Cars car = new Cars();
        
        // Gan cac truong co ban cua xe
        car.setCarId(dto.getCarId());
        car.setName(dto.getName());
        car.setYear(dto.getYear());
        car.setDescription(dto.getDescription());
        car.setImage(dto.getImage());

        // Tao doi tuong Category neu co thong tin danh muc
        if (dto.getCategoryName() != null) {
            Categories category = new Categories();
            category.setCategoryName(dto.getCategoryName());
            car.setCategory(category);
        }

        // Tao doi tuong Fuel neu co thong tin nhien lieu
        if (dto.getFuelType() != null) {
            Fuels fuel = new Fuels();
            fuel.setFuelType(dto.getFuelType());
            car.setFuel(fuel);
        }

        // Tao doi tuong Seating neu co thong tin so cho
        if (dto.getSeatingType() != null) {
            Seatings seating = new Seatings();
            seating.setSeatingType(dto.getSeatingType());
            car.setSeating(seating);
        }

        // Tao doi tuong Location neu co thong tin dia diem
        if (dto.getCity() != null || dto.getAddress() != null) {
            Locations location = new Locations();
            location.setCity(dto.getCity());
            location.setAddress(dto.getAddress());
            car.setLocation(location);
        }

        return car;
    }
}