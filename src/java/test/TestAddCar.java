package test;

import dto.CarDTO;
import service.CarService;
import util.di.DIContainer;

public class TestAddCar {

    public static void main(String[] args) {
        try {
            // ✅ Lấy instance CarService từ DIContainer
            CarService carService = DIContainer.get(CarService.class);

            // ✅ Tạo đối tượng CarDTO mới
            CarDTO carDTO = new CarDTO();
            carDTO.setName("Toyota Vios 2024");
            carDTO.setYear(2024);
            carDTO.setDescription("Xe tiết kiệm nhiên liệu, 5 chỗ, tự động");
            carDTO.setImage("vios2024.jpg");
            carDTO.setCategoryId(1);  // ID danh mục (ví dụ: sedan)
            carDTO.setFuelId(2);      // ID nhiên liệu (ví dụ: xăng)
            carDTO.setSeatingId(1);   // ID số ghế (ví dụ: 5 chỗ)
            //carDTO.setLocationCity(1);  // ID địa điểm (nếu có cột này)
            carDTO.setDailyPrice(850000.0); // 💰 Giá thuê mỗi ngày (VND)

            // ✅ Gọi service để thêm xe
            boolean result = carService.addCar(carDTO);

            if (result) {
                System.out.println("✅ Thêm xe và giá xe thành công!");
            } else {
                System.out.println("❌ Thêm xe thất bại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
