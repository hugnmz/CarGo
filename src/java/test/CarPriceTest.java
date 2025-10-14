package test;

import dao.CarsDAO;
import model.Cars;
import util.di.DIContainer;
import java.util.List;
import java.util.Optional;

/**
 * Test để kiểm tra CarPrices có được map đúng không
 */
public class CarPriceTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== CarPrice Test ===");
            
            // Lấy CarsDAO từ DI Container
            CarsDAO carsDAO = DIContainer.get(CarsDAO.class);
            System.out.println("CarsDAO: " + (carsDAO != null ? "OK" : "NULL"));
            
            // Test getAllCars
            System.out.println("\n--- Test getAllCars ---");
            List<Cars> allCars = carsDAO.getAllCars();
            System.out.println("Số xe: " + (allCars != null ? allCars.size() : "NULL"));
            
            if (allCars != null && !allCars.isEmpty()) {
                Cars firstCar = allCars.get(0);
                System.out.println("Xe đầu tiên: " + firstCar.getName());
                System.out.println("CarPrices null: " + (firstCar.getCarPrices() == null));
                
                if (firstCar.getCarPrices() != null) {
                    System.out.println("DailyPrice: " + firstCar.getCarPrices().getDailyPrice());
                    System.out.println("DepositAmount: " + firstCar.getCarPrices().getDepositAmount());
                } else {
                    System.out.println("CarPrices là NULL - MapResultSet không map được!");
                }
            }
            
            // Test getCarById
            System.out.println("\n--- Test getCarById ---");
            Optional<Cars> car = carsDAO.getCarById(1);
            if (car.isPresent()) {
                Cars c = car.get();
                System.out.println("Xe ID 1: " + c.getName());
                System.out.println("CarPrices null: " + (c.getCarPrices() == null));
                
                if (c.getCarPrices() != null) {
                    System.out.println("DailyPrice: " + c.getCarPrices().getDailyPrice());
                    System.out.println("DepositAmount: " + c.getCarPrices().getDepositAmount());
                } else {
                    System.out.println("CarPrices là NULL - MapResultSet không map được!");
                }
            } else {
                System.out.println("Không tìm thấy xe ID 1");
            }
            
            System.out.println("\n=== Test hoàn thành ===");
            
        } catch (Exception e) {
            System.err.println("Lỗi trong test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
