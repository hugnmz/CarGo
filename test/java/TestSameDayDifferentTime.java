package test.java;

import java.time.LocalDateTime;
import java.util.List;
import service.CartService;
import dto.OrderDTO;
import util.di.DIContainer;

/**
 * Test booking cùng ngày nhưng khác khung giờ không chèn nhau
 * VD: User A book xe A 09:00-11:00, User B book xe B 14:00-16:00
 */
public class TestSameDayDifferentTime {
    
    public static void main(String[] args) {
        try {
            // Khởi tạo service
            CartService cartService = DIContainer.get(CartService.class);
            
            // Test data - cùng ngày 1/11/2025
            Integer userA = 62;
            Integer userB = 63;
            Integer vehicleA = 1; // Xe A
            Integer vehicleB = 2; // Xe B
            
            System.out.println("=== TEST: Cùng ngày, khác khung giờ, khác xe ===");
            
            // Test case 1: User A book xe A từ 09:00-11:00
            System.out.println("\n--- Test 1: User A book xe A 09:00-11:00 ---");
            LocalDateTime startA = LocalDateTime.of(2025, 11, 1, 9, 0);
            LocalDateTime endA = LocalDateTime.of(2025, 11, 1, 11, 0);
            
            System.out.println("User A (" + userA + ") booking vehicle " + vehicleA + ": " + startA + " to " + endA);
            boolean resultA = cartService.addToCart(userA, vehicleA, startA, endA);
            System.out.println("Result: " + resultA);
            
            // Test case 2: User B book xe B từ 14:00-16:00 (cùng ngày, khác xe, khác giờ)
            System.out.println("\n--- Test 2: User B book xe B 14:00-16:00 ---");
            LocalDateTime startB = LocalDateTime.of(2025, 11, 1, 14, 0);
            LocalDateTime endB = LocalDateTime.of(2025, 11, 1, 16, 0);
            
            System.out.println("User B (" + userB + ") booking vehicle " + vehicleB + ": " + startB + " to " + endB);
            boolean resultB = cartService.addToCart(userB, vehicleB, startB, endB);
            System.out.println("Result: " + resultB);
            
            // Test case 3: User A book xe A từ 12:00-14:00 (cùng xe, khác giờ, không overlap)
            System.out.println("\n--- Test 3: User A book xe A 12:00-14:00 (cùng xe, khác giờ) ---");
            LocalDateTime startA2 = LocalDateTime.of(2025, 11, 1, 12, 0);
            LocalDateTime endA2 = LocalDateTime.of(2025, 11, 1, 14, 0);
            
            System.out.println("User A (" + userA + ") booking vehicle " + vehicleA + ": " + startA2 + " to " + endA2);
            boolean resultA2 = cartService.addToCart(userA, vehicleA, startA2, endA2);
            System.out.println("Result: " + resultA2);
            
            // Test case 4: User B book xe B từ 17:00-19:00 (cùng xe, khác giờ, không overlap)
            System.out.println("\n--- Test 4: User B book xe B 17:00-19:00 (cùng xe, khác giờ) ---");
            LocalDateTime startB2 = LocalDateTime.of(2025, 11, 1, 17, 0);
            LocalDateTime endB2 = LocalDateTime.of(2025, 11, 1, 19, 0);
            
            System.out.println("User B (" + userB + ") booking vehicle " + vehicleB + ": " + startB2 + " to " + endB2);
            boolean resultB2 = cartService.addToCart(userB, vehicleB, startB2, endB2);
            System.out.println("Result: " + resultB2);
            
            // Test case 5: User A book xe A từ 10:00-12:00 (cùng xe, OVERLAP với booking cũ)
            System.out.println("\n--- Test 5: User A book xe A 10:00-12:00 (OVERLAP) ---");
            LocalDateTime startA3 = LocalDateTime.of(2025, 11, 1, 10, 0);
            LocalDateTime endA3 = LocalDateTime.of(2025, 11, 1, 12, 0);
            
            System.out.println("User A (" + userA + ") booking vehicle " + vehicleA + ": " + startA3 + " to " + endA3);
            boolean resultA3 = cartService.addToCart(userA, vehicleA, startA3, endA3);
            System.out.println("Result: " + resultA3 + " (should be false - overlap)");
            
            // Kiểm tra cart items
            System.out.println("\n=== CART ITEMS SUMMARY ===");
            List<OrderDTO> cartA = cartService.getCartItems(userA);
            List<OrderDTO> cartB = cartService.getCartItems(userB);
            
            System.out.println("User A cart items: " + cartA.size());
            for (OrderDTO item : cartA) {
                System.out.println("  - Vehicle " + item.getVehicleId() + ": " + 
                                 item.getRentStartDate() + " to " + item.getRentEndDate());
            }
            
            System.out.println("User B cart items: " + cartB.size());
            for (OrderDTO item : cartB) {
                System.out.println("  - Vehicle " + item.getVehicleId() + ": " + 
                                 item.getRentStartDate() + " to " + item.getRentEndDate());
            }
            
            // Kết quả mong đợi
            System.out.println("\n=== EXPECTED RESULTS ===");
            System.out.println("Test 1 (User A, xe A, 09:00-11:00): " + resultA + " (should be true)");
            System.out.println("Test 2 (User B, xe B, 14:00-16:00): " + resultB + " (should be true)");
            System.out.println("Test 3 (User A, xe A, 12:00-14:00): " + resultA2 + " (should be true)");
            System.out.println("Test 4 (User B, xe B, 17:00-19:00): " + resultB2 + " (should be true)");
            System.out.println("Test 5 (User A, xe A, 10:00-12:00): " + resultA3 + " (should be false - overlap)");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
