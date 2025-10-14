package test;

import dto.CarDTO;
import dto.VehicleDTO;
import service.CarService;
import service.VehicleService;
import util.di.DIContainer;

public class CarDetailTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== CarDetail Test ===");
            
            // Test CarService
            CarService carService = DIContainer.get(CarService.class);
            System.out.println("CarService: OK");
            
            // Test getCarById
            System.out.println("--- Test getCarById ---");
            var carOpt = carService.getCarById(1);
            if (carOpt.isPresent()) {
                CarDTO car = carOpt.get();
                System.out.println("Car: " + car.getName() + " " + car.getYear());
                System.out.println("DailyPrice: " + car.getDailyPrice());
                System.out.println("DailyPrice null: " + (car.getDailyPrice() == null));
            }
            
            // Test VehicleService
            System.out.println("--- Test VehicleService ---");
            VehicleService vehicleService = DIContainer.get(VehicleService.class);
            var vehicles = vehicleService.getVehicleByCarId(1);
            System.out.println("Vehicles count: " + vehicles.size());
            
            for (VehicleDTO vehicle : vehicles) {
                System.out.println("Vehicle: " + vehicle.getPlateNumber());
                System.out.println("CurrentPrice: " + vehicle.getCurrentPrice());
                System.out.println("CurrentPrice null: " + (vehicle.getCurrentPrice() == null));
                System.out.println("---");
            }
            
            System.out.println("=== Test hoàn thành ===");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}