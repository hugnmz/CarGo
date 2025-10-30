package test;

import dao.VehiclesDAO;
import mapper.VehicleMapper;
import dto.VehicleDTO;
import service.impl.CarSerImpl;
import java.util.List;
import util.di.DIContainer;

public class TestGetVehicleByCarId {
    public static void main(String[] args) {
        try {
            // Khởi tạo service
            

            // ⚙️ Inject thủ công các dependency cần thiết
            CarSerImpl carService = DIContainer.get(CarSerImpl.class);

            // ✅ Giả sử bạn muốn test carId = 2
            int carId = 2;

            // Gọi hàm lấy danh sách vehicle theo carId
            List<VehicleDTO> vehicles = carService.getVehicalByCarId(carId);

            // In ra kết quả
            if (vehicles == null || vehicles.isEmpty()) {
                System.out.println("❌ Không tìm thấy vehicle nào cho carId = " + carId);
            } else {
                System.out.println("✅ Danh sách vehicle cho carId = " + carId + ":");
                for (VehicleDTO v : vehicles) {
                    System.out.println("----------------------------------");
                    System.out.println("Vehicle ID: " + v.getVehicleId());
                    System.out.println("Car ID: " + v.getCarId());
                    System.out.println("Biển số xe: " + v.getPlateNumber());
                    System.out.println("Trạng thái: " + v.getIsActive());
                    System.out.println("Địa điểm: " + v.getLocationId());
                    System.out.println("Mapping vehicleId = " + v.getVehicleId());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
