
import mapper.CarMapper;
import dto.CarDTO;
import java.math.BigDecimal;
import model.Cars;
import model.Categories;
import model.Fuels;
import model.Seatings;
import model.CarPrices;

public class TestCarMapper {

    public static void main(String[] args) {
        // 1️⃣ Tạo dữ liệu mẫu
        Categories category = new Categories();
        category.setCategoryName("Sedan");

        Fuels fuel = new Fuels();
        fuel.setFuelType("Xăng");

        Seatings seating = new Seatings();
        seating.setSeatingType(5);

        CarPrices carPrices = new CarPrices();
        carPrices.setDailyPrice(BigDecimal.valueOf(800000)); // gán trực tiếp

        Cars car = new Cars();
        car.setCarId(1);
        car.setName("Toyota Vios");
        car.setYear(2022);
        car.setDescription("Sedan 5 chỗ");
        car.setImage("https://example.com/vios.jpg");
        car.setCategory(category);
        car.setFuel(fuel);
        car.setSeating(seating);
        car.setCarPrices(carPrices);

        // 2️⃣ Khởi tạo mapper
        CarMapper mapper = new CarMapper();

        // 3️⃣ Chuyển từ Model sang DTO
        CarDTO dto = mapper.toDTO(car);
        System.out.println("=== CarDTO ===");
        System.out.println(dto);

        // 4️⃣ Chuyển từ DTO về Model
        Cars carFromDTO = mapper.toModel(dto);
        System.out.println("=== Car từ DTO ===");
        System.out.println("Name: " + carFromDTO.getName());
        System.out.println("Category: " + (carFromDTO.getCategory() != null ? carFromDTO.getCategory().getCategoryName() : "null"));
        System.out.println("Fuel: " + (carFromDTO.getFuel() != null ? carFromDTO.getFuel().getFuelType() : "null"));
        System.out.println("Seating: " + (carFromDTO.getSeating() != null ? carFromDTO.getSeating().getSeatingType() : "null"));
    }
}
