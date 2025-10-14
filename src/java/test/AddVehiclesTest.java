package test;

import dao.impl.VehiclesDAOImpl;
import dao.impl.LocationsDAOImpl;
import model.Vehicles;
import java.util.List;

/**
 * Test class để thêm vehicles cho mỗi car model
 * Mỗi car sẽ có 5 vehicles với biển số khác nhau
 */
public class AddVehiclesTest {
    
    public static void main(String[] args) {
        try {
            VehiclesDAOImpl vehiclesDAO = new VehiclesDAOImpl();
            LocationsDAOImpl locationsDAO = new LocationsDAOImpl();
            
            // Lấy locationId cho Ha Noi và TP.HCM
            Integer haNoiId = locationsDAO.getOrCreateIdByCity("Ha Noi");
            Integer hcmId = locationsDAO.getOrCreateIdByCity("TP.HCM");
            
            System.out.println("Ha Noi Location ID: " + haNoiId);
            System.out.println("TP.HCM Location ID: " + hcmId);
            
            // Lấy danh sách tất cả cars
            List<Vehicles> allVehicles = vehiclesDAO.getAllVehicles();
            System.out.println("Current vehicles count: " + allVehicles.size());
            
            // Thêm vehicles cho mỗi car (giả sử có 8 cars với ID từ 1-8)
            for (int carId = 1; carId <= 8; carId++) {
                System.out.println("\n=== Adding vehicles for Car ID: " + carId + " ===");
                
                // Tạo 5 vehicles cho mỗi car
                for (int i = 1; i <= 5; i++) {
                    Vehicles vehicle = new Vehicles();
                    vehicle.setCarId(carId);
                    
                    // Tạo biển số theo pattern: 30A-{carId}{i}
                    String plateNumber = String.format("30A-%d%02d", carId, i);
                    vehicle.setPlateNumber(plateNumber);
                    vehicle.setIsActive(true);
                    
                    // Phân bố location: 3 xe ở Ha Noi, 2 xe ở TP.HCM
                    if (i <= 3) {
                        vehicle.setLocationId(haNoiId);
                    } else {
                        vehicle.setLocationId(hcmId);
                    }
                    
                    // Thêm vehicle vào database
                    boolean success = vehiclesDAO.addVehicle(vehicle);
                    if (success) {
                        System.out.println("✓ Added vehicle: " + plateNumber + " (Location: " + 
                            (i <= 3 ? "Ha Noi" : "TP.HCM") + ")");
                    } else {
                        System.out.println("✗ Failed to add vehicle: " + plateNumber);
                    }
                }
            }
            
            // Kiểm tra kết quả cuối cùng
            System.out.println("\n=== Final Result ===");
            List<Vehicles> finalVehicles = vehiclesDAO.getAllVehicles();
            System.out.println("Total vehicles after adding: " + finalVehicles.size());
            
            // Thống kê theo car
            for (int carId = 1; carId <= 8; carId++) {
                List<Vehicles> vehiclesForCar = vehiclesDAO.getVehiclesByCar(carId);
                System.out.println("Car ID " + carId + ": " + vehiclesForCar.size() + " vehicles");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
