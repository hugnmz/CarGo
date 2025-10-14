package test;

import model.Vehicles;
import dao.VehiclesDAO;
import util.di.DIContainer;

public class VehicleMapTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== Vehicle MapResultSet Test ===");
            
            // Test VehiclesDAO
            VehiclesDAO vehiclesDAO = DIContainer.get(VehiclesDAO.class);
            System.out.println("VehiclesDAO: OK");
            
            // Test getVehiclesByCar
            System.out.println("--- Test getVehiclesByCar ---");
            var vehicles = vehiclesDAO.getVehiclesByCar(1);
            System.out.println("Vehicles count: " + vehicles.size());
            
            for (Vehicles vehicle : vehicles) {
                System.out.println("Vehicle: " + vehicle.getPlateNumber());
                System.out.println("Car: " + (vehicle.getCar() != null ? vehicle.getCar().getName() : "NULL"));
                
                if (vehicle.getCar() != null) {
                    System.out.println("CarPrices: " + (vehicle.getCar().getCarPrices() != null ? "NOT NULL" : "NULL"));
                    
                    if (vehicle.getCar().getCarPrices() != null) {
                        System.out.println("DailyPrice: " + vehicle.getCar().getCarPrices().getDailyPrice());
                        System.out.println("DepositAmount: " + vehicle.getCar().getCarPrices().getDepositAmount());
                    }
                }
                System.out.println("---");
            }
            
            System.out.println("=== Test hoàn thành ===");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
