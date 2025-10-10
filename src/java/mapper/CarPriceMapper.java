package mapper;

import dto.CarPriceDTO;
import model.CarPrices;
import model.Cars;
import util.di.annotation.Component;
import java.time.LocalDate;

/**
 * CarPriceMapper - Chuyển đổi giữa CarPriceDTO và CarPrices Model
 */
@Component
public class CarPriceMapper {

    // Chuyen tu Model sang DTO
    public CarPriceDTO toDTO(CarPrices carPrice) {
        if (carPrice == null) {
            return null;
        }

        CarPriceDTO dto = new CarPriceDTO();
        
        // Basic car price fields
        dto.setPriceId(carPrice.getPriceId());
        dto.setCarId(carPrice.getCarId());
        dto.setDailyPrice(carPrice.getDailyPrice());
        dto.setDepositAmount(carPrice.getDepositAmount());
        dto.setStartDate(carPrice.getStartDate());
        dto.setEndDate(carPrice.getEndDate());
        dto.setCreateAt(carPrice.getCreateAt());

        // Car information (nested)
        if (carPrice.getCar() != null) {
            Cars car = carPrice.getCar();
            dto.setCarName(car.getName());
            dto.setCarImage(car.getImage());
            dto.setYear(car.getYear());
            
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
        }

        // Status flags
        dto.setIsCurrent(carPrice.getEndDate() == null);
        dto.setIsActive(carPrice.getEndDate() == null || carPrice.getEndDate().isAfter(LocalDate.now()));

        return dto;
    }

    // Chuyen tu DTO sang Model
    public CarPrices toModel(CarPriceDTO dto) {
        if (dto == null) {
            return null;
        }

        CarPrices carPrice = new CarPrices();
        
        // Basic car price fields
        carPrice.setPriceId(dto.getPriceId());
        carPrice.setCarId(dto.getCarId());
        carPrice.setDailyPrice(dto.getDailyPrice());
        carPrice.setDepositAmount(dto.getDepositAmount());
        carPrice.setStartDate(dto.getStartDate());
        carPrice.setEndDate(dto.getEndDate());
        carPrice.setCreateAt(dto.getCreateAt());

        // Create nested Car object if car info is provided
        if (dto.getCarName() != null || dto.getCarImage() != null) {
            Cars car = new Cars();
            car.setCarId(dto.getCarId());
            car.setName(dto.getCarName());
            car.setImage(dto.getCarImage());
            car.setYear(dto.getYear());
            carPrice.setCar(car);
        }

        return carPrice;
    }
}
