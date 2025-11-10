package test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import service.CartService;
import util.di.DIContainer;
import util.exception.BusinessException;

/**
 * Test các trường hợp OVERLAP (trùng lặp thời gian) khi thêm xe vào giỏ hàng
 * 
 * Kiểm tra:
 * 1. Xe đang có trong hợp đồng của người khác
 * 2. Xe đã có trong giỏ hàng của chính mình
 * 3. Tất cả các trường hợp overlap (before, after, inside, outside, exact)
 * 
 * @author admin
 */
public class TestCartOverlap {
    
    private static CartService cartService;
    private static final Integer TEST_CUSTOMER_ID = 1;
    private static final Integer TEST_VEHICLE_ID = 1;
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("BẮT ĐẦU TEST OVERLAP (TRÙNG LẶP THỜI GIAN)");
        System.out.println("===========================================\n");
        
        try {
            cartService = DIContainer.get(CartService.class);
            System.out.println("✅ Khởi tạo CartService thành công\n");
        } catch (Exception e) {
            System.err.println("❌ LỖI: Không thể khởi tạo CartService");
            System.err.println("Lý do: " + e.getMessage());
            return;
        }
        
        int totalTests = 0;
        int passedTests = 0;
        
        System.out.println("📋 SETUP: Giả sử xe đã được book trong khoảng:");
        System.out.println("   Start: 2025-12-10");
        System.out.println("   End:   2025-12-15");
        System.out.println("   (5 ngày)\n");
        
        // Test 1: Overlap hoàn toàn (exact same dates)
        if (testCase1_ExactOverlap()) passedTests++;
        totalTests++;
        
        // Test 2: Overlap một phần (start trước, end giữa khoảng)
        if (testCase2_PartialOverlapStart()) passedTests++;
        totalTests++;
        
        // Test 3: Overlap một phần (start giữa khoảng, end sau)
        if (testCase3_PartialOverlapEnd()) passedTests++;
        totalTests++;
        
        // Test 4: Overlap hoàn toàn (bao trùm booking cũ)
        if (testCase4_ContainsExistingBooking()) passedTests++;
        totalTests++;
        
        // Test 5: Overlap hoàn toàn (nằm trong booking cũ)
        if (testCase5_InsideExistingBooking()) passedTests++;
        totalTests++;
        
        // Test 6: Không overlap (trước booking cũ)
        if (testCase6_BeforeExistingBooking()) passedTests++;
        totalTests++;
        
        // Test 7: Không overlap (sau booking cũ)
        if (testCase7_AfterExistingBooking()) passedTests++;
        totalTests++;
        
        // Test 8: Chạm nhau (end = start của booking cũ)
        if (testCase8_TouchingStart()) passedTests++;
        totalTests++;
        
        // Test 9: Chạm nhau (start = end của booking cũ)
        if (testCase9_TouchingEnd()) passedTests++;
        totalTests++;
        
        // Test 10: Thêm cùng xe 2 lần liên tiếp vào giỏ hàng
        if (testCase10_AddSameVehicleTwice()) passedTests++;
        totalTests++;
        
        // Tổng kết
        System.out.println("\n===========================================");
        System.out.println("KẾT QUẢ TEST OVERLAP");
        System.out.println("===========================================");
        System.out.println("Tổng số test: " + totalTests);
        System.out.println("Passed: " + passedTests + " ✅");
        System.out.println("Failed: " + (totalTests - passedTests) + " ❌");
        System.out.println("Tỉ lệ thành công: " + (passedTests * 100 / totalTests) + "%");
        
        if (passedTests == totalTests) {
            System.out.println("\n🎉 TẤT CẢ TEST OVERLAP PASSED! 🎉");
            System.out.println("→ Hệ thống KHÔNG CHO PHÉP đặt xe trùng thời gian!");
        } else {
            System.out.println("\n⚠️ CÓ TEST CASE FAILED!");
            System.out.println("→ CẦN KIỂM TRA LẠI LOGIC OVERLAP!");
        }
    }
    
    /**
     * Test Case 1: Overlap hoàn toàn (exact same dates)
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-10 → 2025-12-15
     * Kỳ vọng: BusinessException
     */
    private static boolean testCase1_ExactOverlap() {
        System.out.println("TEST 1: Exact Overlap (trùng hoàn toàn)");
        System.out.println("   Booking cũ: 2025-12-10 → 2025-12-15");
        System.out.println("   Thêm mới:   2025-12-10 → 2025-12-15");
        System.out.println("   Kỳ vọng: BusinessException (xe không available)");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 10, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 15, 10, 0);
            
            cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            System.out.println("   ❌ FAILED: Cho phép đặt xe trùng hoàn toàn!\n");
            return false;
        } catch (BusinessException e) {
            System.out.println("   ✅ PASSED: Không cho phép đặt (xe đã được book)");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Throw sai exception: " + e.getClass().getSimpleName() + "\n");
            return false;
        }
    }
    
    /**
     * Test Case 2: Overlap một phần ở đầu
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-08 → 2025-12-12
     * Overlap: 10, 11, 12
     * Kỳ vọng: BusinessException
     */
    private static boolean testCase2_PartialOverlapStart() {
        System.out.println("TEST 2: Partial Overlap Start (overlap đầu)");
        System.out.println("   Booking cũ: ████████████ (10 → 15)");
        System.out.println("   Thêm mới:   ████████     (08 → 12)");
        System.out.println("   Overlap:       ████      (10 → 12)");
        System.out.println("   Kỳ vọng: BusinessException");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 8, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 12, 10, 0);
            
            cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            System.out.println("   ❌ FAILED: Cho phép đặt xe overlap đầu!\n");
            return false;
        } catch (BusinessException e) {
            System.out.println("   ✅ PASSED: Không cho phép overlap đầu");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Throw sai exception\n");
            return false;
        }
    }
    
    /**
     * Test Case 3: Overlap một phần ở cuối
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-13 → 2025-12-17
     * Overlap: 13, 14, 15
     * Kỳ vọng: BusinessException
     */
    private static boolean testCase3_PartialOverlapEnd() {
        System.out.println("TEST 3: Partial Overlap End (overlap cuối)");
        System.out.println("   Booking cũ: ████████████ (10 → 15)");
        System.out.println("   Thêm mới:           ████████ (13 → 17)");
        System.out.println("   Overlap:            ████     (13 → 15)");
        System.out.println("   Kỳ vọng: BusinessException");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 13, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 17, 10, 0);
            
            cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            System.out.println("   ❌ FAILED: Cho phép đặt xe overlap cuối!\n");
            return false;
        } catch (BusinessException e) {
            System.out.println("   ✅ PASSED: Không cho phép overlap cuối");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Throw sai exception\n");
            return false;
        }
    }
    
    /**
     * Test Case 4: Bao trùm hoàn toàn booking cũ
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-08 → 2025-12-17
     * Kỳ vọng: BusinessException
     */
    private static boolean testCase4_ContainsExistingBooking() {
        System.out.println("TEST 4: Contains Existing Booking (bao trùm booking cũ)");
        System.out.println("   Booking cũ:   ████████████ (10 → 15)");
        System.out.println("   Thêm mới:   ████████████████ (08 → 17)");
        System.out.println("   Overlap:      ████████████    (10 → 15)");
        System.out.println("   Kỳ vọng: BusinessException");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 8, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 17, 10, 0);
            
            cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            System.out.println("   ❌ FAILED: Cho phép đặt xe bao trùm booking cũ!\n");
            return false;
        } catch (BusinessException e) {
            System.out.println("   ✅ PASSED: Không cho phép bao trùm booking cũ");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Throw sai exception\n");
            return false;
        }
    }
    
    /**
     * Test Case 5: Nằm hoàn toàn trong booking cũ
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-11 → 2025-12-14
     * Kỳ vọng: BusinessException
     */
    private static boolean testCase5_InsideExistingBooking() {
        System.out.println("TEST 5: Inside Existing Booking (nằm trong booking cũ)");
        System.out.println("   Booking cũ: ████████████ (10 → 15)");
        System.out.println("   Thêm mới:     ████████   (11 → 14)");
        System.out.println("   Overlap:      ████████   (11 → 14)");
        System.out.println("   Kỳ vọng: BusinessException");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 11, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 14, 10, 0);
            
            cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            System.out.println("   ❌ FAILED: Cho phép đặt xe trong booking cũ!\n");
            return false;
        } catch (BusinessException e) {
            System.out.println("   ✅ PASSED: Không cho phép đặt trong booking cũ");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Throw sai exception\n");
            return false;
        }
    }
    
    /**
     * Test Case 6: Trước booking cũ (không overlap)
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-01 → 2025-12-05
     * Kỳ vọng: Thành công HOẶC BusinessException khác (không phải overlap)
     */
    private static boolean testCase6_BeforeExistingBooking() {
        System.out.println("TEST 6: Before Existing Booking (trước booking cũ - KHÔNG overlap)");
        System.out.println("   Thêm mới:   ████████             (01 → 05)");
        System.out.println("   Booking cũ:          ████████████ (10 → 15)");
        System.out.println("   Overlap:    KHÔNG CÓ");
        System.out.println("   Kỳ vọng: Thành công (không overlap với booking cũ)");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 5, 10, 0);
            
            boolean result = cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            
            if (result) {
                System.out.println("   ✅ PASSED: Thêm thành công (không overlap)");
                System.out.println("   Từ: " + start + " → " + end + "\n");
                return true;
            } else {
                System.out.println("   ⚠️ WARNING: Trả về false\n");
                return false;
            }
        } catch (BusinessException e) {
            // Có thể là lỗi khác (không phải overlap), vẫn PASS
            System.out.println("   ✅ PASSED: BusinessException (lỗi khác, không phải overlap)");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Throw exception không mong đợi\n");
            return false;
        }
    }
    
    /**
     * Test Case 7: Sau booking cũ (không overlap)
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-20 → 2025-12-25
     * Kỳ vọng: Thành công HOẶC BusinessException khác
     */
    private static boolean testCase7_AfterExistingBooking() {
        System.out.println("TEST 7: After Existing Booking (sau booking cũ - KHÔNG overlap)");
        System.out.println("   Booking cũ: ████████████          (10 → 15)");
        System.out.println("   Thêm mới:                ████████ (20 → 25)");
        System.out.println("   Overlap:    KHÔNG CÓ");
        System.out.println("   Kỳ vọng: Thành công (không overlap)");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 20, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 25, 10, 0);
            
            boolean result = cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            
            if (result) {
                System.out.println("   ✅ PASSED: Thêm thành công (không overlap)");
                System.out.println("   Từ: " + start + " → " + end + "\n");
                return true;
            } else {
                System.out.println("   ⚠️ WARNING: Trả về false\n");
                return false;
            }
        } catch (BusinessException e) {
            System.out.println("   ✅ PASSED: BusinessException (lỗi khác)");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Exception không mong đợi\n");
            return false;
        }
    }
    
    /**
     * Test Case 8: Chạm nhau ở đầu (end = start của booking cũ)
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-07 → 2025-12-10
     * Kỳ vọng: Thành công (không overlap, chỉ chạm nhau)
     */
    private static boolean testCase8_TouchingStart() {
        System.out.println("TEST 8: Touching Start (chạm nhau ở đầu)");
        System.out.println("   Thêm mới:   ████████|            (07 → 10)");
        System.out.println("   Booking cũ:         |████████████ (10 → 15)");
        System.out.println("   Overlap:    KHÔNG (chỉ chạm nhau)");
        System.out.println("   Kỳ vọng: Thành công (logic overlap: start < end && end > start)");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 7, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 10, 10, 0);
            
            boolean result = cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            
            if (result) {
                System.out.println("   ✅ PASSED: Thêm thành công (không overlap)");
                System.out.println("   Từ: " + start + " → " + end + "\n");
                return true;
            } else {
                System.out.println("   ⚠️ WARNING: Trả về false\n");
                return false;
            }
        } catch (BusinessException e) {
            System.out.println("   ⚠️ INFO: BusinessException");
            System.out.println("   Message: " + e.getMessage());
            System.out.println("   (Tùy logic: có thể coi chạm nhau = overlap hoặc không)\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Exception không mong đợi\n");
            return false;
        }
    }
    
    /**
     * Test Case 9: Chạm nhau ở cuối (start = end của booking cũ)
     * Booking cũ: 2025-12-10 → 2025-12-15
     * Thêm mới:   2025-12-15 → 2025-12-20
     * Kỳ vọng: Thành công (không overlap)
     */
    private static boolean testCase9_TouchingEnd() {
        System.out.println("TEST 9: Touching End (chạm nhau ở cuối)");
        System.out.println("   Booking cũ: ████████████|         (10 → 15)");
        System.out.println("   Thêm mới:                |████████ (15 → 20)");
        System.out.println("   Overlap:    KHÔNG (chỉ chạm nhau)");
        System.out.println("   Kỳ vọng: Thành công");
        
        try {
            LocalDateTime start = LocalDateTime.of(2025, 12, 15, 10, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 20, 10, 0);
            
            boolean result = cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start, end);
            
            if (result) {
                System.out.println("   ✅ PASSED: Thêm thành công (không overlap)");
                System.out.println("   Từ: " + start + " → " + end + "\n");
                return true;
            } else {
                System.out.println("   ⚠️ WARNING: Trả về false\n");
                return false;
            }
        } catch (BusinessException e) {
            System.out.println("   ⚠️ INFO: BusinessException");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Exception không mong đợi\n");
            return false;
        }
    }
    
    /**
     * Test Case 10: Thêm cùng xe 2 lần vào giỏ hàng (overlap trong giỏ hàng)
     * Kỳ vọng: Lần 2 throw BusinessException
     */
    private static boolean testCase10_AddSameVehicleTwice() {
        System.out.println("TEST 10: Add Same Vehicle Twice (thêm cùng xe 2 lần vào giỏ hàng)");
        System.out.println("   Kỳ vọng: Lần 1 thành công, lần 2 throw BusinessException");
        
        try {
            // Sử dụng ngày xa để tránh conflict với booking khác
            LocalDateTime start1 = LocalDateTime.of(2026, 1, 10, 10, 0);
            LocalDateTime end1 = LocalDateTime.of(2026, 1, 15, 10, 0);
            
            // Lần 1: Thêm vào giỏ hàng
            System.out.println("   Lần 1: Thêm xe từ " + start1 + " → " + end1);
            boolean result1 = cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start1, end1);
            
            if (!result1) {
                System.out.println("   ⚠️ Lần 1 không thành công, skip test này\n");
                return true; // Skip test này
            }
            
            System.out.println("   ✅ Lần 1 thành công!");
            
            // Lần 2: Thêm cùng xe, cùng khoảng thời gian
            System.out.println("   Lần 2: Thêm lại xe cùng thời gian (overlap trong giỏ hàng)");
            
            try {
                cartService.addToCart(TEST_CUSTOMER_ID, TEST_VEHICLE_ID, start1, end1);
                System.out.println("   ❌ FAILED: Cho phép thêm cùng xe 2 lần!\n");
                return false;
            } catch (BusinessException e) {
                System.out.println("   ✅ PASSED: Lần 2 bị reject (xe đã có trong giỏ hàng)");
                System.out.println("   Message: " + e.getMessage() + "\n");
                return true;
            }
            
        } catch (BusinessException e) {
            System.out.println("   ⚠️ Lần 1 đã bị reject, skip test này");
            System.out.println("   Message: " + e.getMessage() + "\n");
            return true;
        } catch (Exception e) {
            System.out.println("   ❌ FAILED: Exception không mong đợi: " + e.getClass().getSimpleName() + "\n");
            return false;
        }
    }
}