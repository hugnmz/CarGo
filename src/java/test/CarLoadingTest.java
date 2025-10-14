package test;

import dao.CarsDAO;
import dao.impl.CarsDAOImpl;
import mapper.CarMapper;
import model.Cars;
import service.CarService;
import service.impl.CarServiceImpl;
import util.di.DIContainer;
import java.util.List;

/**
 * Test để kiểm tra tại sao không load được xe
 */
public class CarLoadingTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== TESTING CAR LOADING ===");
            
            // Test 1: Kiểm tra DI Container
            System.out.println("\n1. Testing DI Container...");
            CarService carService = DIContainer.get(CarService.class);
            System.out.println("CarService: " + (carService != null ? "OK" : "NULL"));
            
            if (carService == null) {
                System.err.println("❌ CarService is NULL - DI Container failed");
                return;
            }
            
            // Test 2: Kiểm tra CarsDAO trực tiếp
            System.out.println("\n2. Testing CarsDAO directly...");
            CarsDAO carsDAO = DIContainer.get(CarsDAO.class);
            System.out.println("CarsDAO: " + (carsDAO != null ? "OK" : "NULL"));
            
            if (carsDAO != null) {
                List<Cars> cars = carsDAO.getAllCars();
                System.out.println("Cars from DAO: " + (cars != null ? cars.size() : "null") + " cars");
                
                if (cars != null && !cars.isEmpty()) {
                    Cars firstCar = cars.get(0);
                    System.out.println("First car: " + firstCar.getName() + " (" + firstCar.getYear() + ")");
                    System.out.println("Category: " + (firstCar.getCategory() != null ? firstCar.getCategory().getCategoryName() : "NULL"));
                    System.out.println("Fuel: " + (firstCar.getFuel() != null ? firstCar.getFuel().getFuelType() : "NULL"));
                    System.out.println("Seating: " + (firstCar.getSeating() != null ? firstCar.getSeating().getSeatingType() : "NULL"));
                    System.out.println("Price: " + (firstCar.getCarPrices() != null ? firstCar.getCarPrices().getDailyPrice() : "NULL"));
                } else {
                    System.err.println("❌ No cars found in database");
                }
            }
            
            // Test 3: Kiểm tra CarMapper
            System.out.println("\n3. Testing CarMapper...");
            CarMapper carMapper = DIContainer.get(CarMapper.class);
            System.out.println("CarMapper: " + (carMapper != null ? "OK" : "NULL"));
            
            if (carMapper != null && carsDAO != null) {
                List<Cars> cars = carsDAO.getAllCars();
                if (cars != null && !cars.isEmpty()) {
                    Cars firstCar = cars.get(0);
                    System.out.println("Mapping first car...");
                    // Test mapping sẽ được thực hiện trong CarService
                }
            }
            
            // Test 4: Kiểm tra CarService
            System.out.println("\n4. Testing CarService...");
            List<dto.CarDTO> carDTOs = carService.getAllCars();
            System.out.println("CarDTOs from Service: " + (carDTOs != null ? carDTOs.size() : "null") + " cars");
            
            if (carDTOs != null && !carDTOs.isEmpty()) {
                dto.CarDTO firstDTO = carDTOs.get(0);
                System.out.println("First DTO: " + firstDTO.getName() + " (" + firstDTO.getYear() + ")");
                System.out.println("Category: " + firstDTO.getCategoryName());
                System.out.println("Fuel: " + firstDTO.getFuelType());
                System.out.println("Seating: " + firstDTO.getSeatingType());
                System.out.println("Location: " + firstDTO.getLocationCity());
                System.out.println("Price: " + firstDTO.getDailyPrice());
            } else {
                System.err.println("❌ No CarDTOs returned from service");
            }
            
            System.out.println("\n=== TEST COMPLETED ===");
            
        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
