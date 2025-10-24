package test.java;

import java.time.LocalDateTime;
import java.util.List;
import service.CartService;
import service.VehicleService;
import dto.OrderDTO;
import dto.VehicleDTO;
import util.di.DIContainer;

/**
 * Test overlap logic với user 62 và 63
 */
public class TestOverlapLogic {
    
    public static void main(String[] args) {
        try {
            // Khởi tạo services
            CartService cartService = DIContainer.get(CartService.class);
            VehicleService vehicleService = DIContainer.get(VehicleService.class);
            
            // Test data
            Integer user62 = 62;
            Integer user63 = 63;
            Integer vehicleId = 1; // Thay bằng vehicleId thực tế
            
            // Test case 1: User 62 book 09:00-11:00
            System.out.println("=== TEST CASE 1: User 62 book 09:00-11:00 ===");
            LocalDateTime start1 = LocalDateTime.of(2025, 11, 1, 9, 0);
            LocalDateTime end1 = LocalDateTime.of(2025, 11, 1, 11, 0);
            
            System.out.println("User 62 booking: " + start1 + " to " + end1);
            boolean result1 = cartService.addToCart(user62, vehicleId, start1, end1);
            System.out.println("Result: " + result1);
            
            // Test case 2: User 63 book 14:00-16:00 (same day, different time)
            System.out.println("\n=== TEST CASE 2: User 63 book 14:00-16:00 ===");
            LocalDateTime start2 = LocalDateTime.of(2025, 11, 1, 14, 0);
            LocalDateTime end2 = LocalDateTime.of(2025, 11, 1, 16, 0);
            
            System.out.println("User 63 booking: " + start2 + " to " + end2);
            boolean result2 = cartService.addToCart(user63, vehicleId, start2, end2);
            System.out.println("Result: " + result2);
            
            // Test case 3: User 62 book 10:00-12:00 (overlap with old booking)
            System.out.println("\n=== TEST CASE 3: User 62 book 10:00-12:00 (overlap) ===");
            LocalDateTime start3 = LocalDateTime.of(2025, 11, 1, 10, 0);
            LocalDateTime end3 = LocalDateTime.of(2025, 11, 1, 12, 0);
            
            System.out.println("User 62 booking: " + start3 + " to " + end3);
            boolean result3 = cartService.addToCart(user62, vehicleId, start3, end3);
            System.out.println("Result: " + result3);
            
            // Test case 4: User 63 book 12:00-14:00 (no overlap)
            System.out.println("\n=== TEST CASE 4: User 63 book 12:00-14:00 (no overlap) ===");
            LocalDateTime start4 = LocalDateTime.of(2025, 11, 1, 12, 0);
            LocalDateTime end4 = LocalDateTime.of(2025, 11, 1, 14, 0);
            
            System.out.println("User 63 booking: " + start4 + " to " + end4);
            boolean result4 = cartService.addToCart(user63, vehicleId, start4, end4);
            System.out.println("Result: " + result4);
            
            // Kiểm tra cart items
            System.out.println("\n=== CART ITEMS ===");
            List<OrderDTO> cart62 = cartService.getCartItems(user62);
            List<OrderDTO> cart63 = cartService.getCartItems(user63);
            
            System.out.println("User 62 cart items: " + cart62.size());
            for (OrderDTO item : cart62) {
                System.out.println("  - " + item.getVehicleId() + ": " + item.getRentStartDate() + " to " + item.getRentEndDate());
            }
            
            System.out.println("User 63 cart items: " + cart63.size());
            for (OrderDTO item : cart63) {
                System.out.println("  - " + item.getVehicleId() + ": " + item.getRentStartDate() + " to " + item.getRentEndDate());
            }
            
            // Test vehicle availability using CartService
            System.out.println("\n=== VEHICLE AVAILABILITY TEST ===");
            boolean available1 = cartService.isVehicleAvailable(vehicleId, start1, end1);
            boolean available2 = cartService.isVehicleAvailable(vehicleId, start2, end2);
            boolean available3 = cartService.isVehicleAvailable(vehicleId, start3, end3);
            boolean available4 = cartService.isVehicleAvailable(vehicleId, start4, end4);
            
            System.out.println("Vehicle " + vehicleId + " available for 09:00-11:00: " + available1);
            System.out.println("Vehicle " + vehicleId + " available for 14:00-16:00: " + available2);
            System.out.println("Vehicle " + vehicleId + " available for 10:00-12:00: " + available3);
            System.out.println("Vehicle " + vehicleId + " available for 12:00-14:00: " + available4);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
