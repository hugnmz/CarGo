package mapper;

import dto.CarDTO;
import model.Cars;
import model.Categories;
import model.Fuels;
import model.Seatings;
import util.di.annotation.Component;

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

        // Map nested objects nếu có
        if (car.getCategory() != null) {
            dto.setCategoryName(car.getCategory().getCategoryName());
        }
        
        if (car.getFuel() != null) {
            dto.setFuelType(car.getFuel().getFuelType());
        }
        
        if (car.getSeating() != null) {
            dto.setSeatingType(car.getSeating().getSeatingType());
        }
        
        // Location - tạm thời set N/A
        dto.setLocationCity("N/A");
        
        if (car.getCarPrices() != null && car.getCarPrices().getDailyPrice() != null) {
            dto.setDailyPrice(car.getCarPrices().getDailyPrice().doubleValue());
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

        // Location - tạm thời bỏ vì LocationsDAO thiếu method
        // TODO: Implement location mapping khi có getLocationById

        return car;
    }
}