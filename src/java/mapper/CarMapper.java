package mapper;

import dto.CarDTO;
import java.math.BigDecimal;
import model.CarPrices;
import model.Cars;
import model.Categories;
import model.Fuels;
import model.Seatings;
import util.di.annotation.Component;

@Component
public class CarMapper {

    // Model -> DTO
    public CarDTO toDTO(Cars car) {
        if (car == null) {
            return null;
        }

        CarDTO dto = new CarDTO();
        dto.setCarId(car.getCarId());
        dto.setName(car.getName());
        dto.setYear(car.getYear());
        dto.setDescription(car.getDescription());
        dto.setImage(car.getImage());

        if (car.getCategory() != null) {
            dto.setCategoryId(car.getCategory().getCategoryId());
            dto.setCategoryName(car.getCategory().getCategoryName());
        }
        if (car.getFuel() != null) {
            dto.setFuelId(car.getFuel().getFuelId());
            dto.setFuelType(car.getFuel().getFuelType());
        }
        if (car.getSeating() != null) {
            dto.setSeatingId(car.getSeating().getSeatingId());
            dto.setSeatingType(car.getSeating().getSeatingType());
        }

        dto.setLocationCity("N/A");
        if (car.getCarPrices() != null) {
            if (car.getCarPrices().getDailyPrice() != null) {
                dto.setDailyPrice(car.getCarPrices().getDailyPrice().doubleValue());
            }
            if (car.getCarPrices().getDepositAmount() != null) {
                dto.setDepositAmount(car.getCarPrices().getDepositAmount().doubleValue());
            }
        }

        return dto;
    }

    // DTO -> Model
    public Cars toModel(CarDTO dto) {
        if (dto == null) {
            return null;
        }

        Cars car = new Cars();
        car.setCarId(dto.getCarId());
        car.setName(dto.getName());
        car.setYear(dto.getYear());
        car.setDescription(dto.getDescription());
        car.setImage(dto.getImage());

        // Category
        if (dto.getCategoryId() != null || dto.getCategoryName() != null) {
            Categories category = new Categories();
            if (dto.getCategoryId() != null) {
                category.setCategoryId(dto.getCategoryId());
            }
            if (dto.getCategoryName() != null) {
                category.setCategoryName(dto.getCategoryName());
            }
            car.setCategory(category);
            car.setCategoryId(dto.getCategoryId());
        }

        // Fuel
        if (dto.getFuelId() != null || dto.getFuelType() != null) {
            Fuels fuel = new Fuels();
            if (dto.getFuelId() != null) {
                fuel.setFuelId(dto.getFuelId());
            }
            if (dto.getFuelType() != null) {
                fuel.setFuelType(dto.getFuelType());
            }
            car.setFuel(fuel);
            car.setFuelId(dto.getFuelId());
        }

        // Seating
        if (dto.getSeatingId() != null || dto.getSeatingType() != null) {
            Seatings seating = new Seatings();
            if (dto.getSeatingId() != null) {
                seating.setSeatingId(dto.getSeatingId());
            }
            if (dto.getSeatingType() != null) {
                seating.setSeatingType(dto.getSeatingType());
            }
            car.setSeating(seating);
            car.setSeatingId(dto.getSeatingId());
        }

        // Price & Deposit
        if (dto.getDailyPrice() != null || dto.getDepositAmount() != null) {
            CarPrices price = new CarPrices();
            if (dto.getDailyPrice() != null) {
                price.setDailyPrice(BigDecimal.valueOf(dto.getDailyPrice()));
            }
            if (dto.getDepositAmount() != null) {
                price.setDepositAmount(BigDecimal.valueOf(dto.getDepositAmount()));
            }
            car.setCarPrices(price);
        }

        return car;
    }

}

