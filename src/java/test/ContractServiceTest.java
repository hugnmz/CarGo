package test;

import dto.ContractDTO;
import service.ContractService;
import util.di.DIContainer;
import java.util.Optional;

/**
 * Test class cho ContractService
 * Test method getContractById()
 */
public class ContractServiceTest {
    
    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU TEST CONTRACTSERVICE ===");
        
        try {
            // 1. Khởi tạo ContractService từ DI Container
            ContractService contractService = DIContainer.get(ContractService.class);
            System.out.println("✓ ContractService đã được khởi tạo thành công");
            
            // 2. Test case 1: Tìm hợp đồng tồn tại
            System.out.println("\n--- TEST CASE 1: Tìm hợp đồng tồn tại ---");
            Integer existingContractId = 1;
            testGetContractById(contractService, existingContractId, "Hợp đồng tồn tại");
            
            // 3. Test case 2: Tìm hợp đồng không tồn tại
            System.out.println("\n--- TEST CASE 2: Tìm hợp đồng không tồn tại ---");
            Integer nonExistingContractId = 99999;
            testGetContractById(contractService, nonExistingContractId, "Hợp đồng không tồn tại");
            
            // 4. Test case 3: Test với ID null
            System.out.println("\n--- TEST CASE 3: Test với ID null ---");
            testGetContractById(contractService, null, "ID null");
            
            // 5. Test case 4: Test với ID âm
            System.out.println("\n--- TEST CASE 4: Test với ID âm ---");
            Integer negativeId = -1;
            testGetContractById(contractService, negativeId, "ID âm");
            
             // 6. Test case 5: Test với ID = 0
             System.out.println("\n--- TEST CASE 5: Test với ID = 0 ---");
             Integer zeroId = 0;
             testGetContractById(contractService, zeroId, "ID = 0");
             
             // 7. Test case 6: Test getContractsByCustomer
             System.out.println("\n--- TEST CASE 6: Test getContractsByCustomer ---");
             testGetContractsByCustomer(contractService, 1);
             
             // 8. Test case 7: Test getContractDetails
             System.out.println("\n--- TEST CASE 7: Test getContractDetails ---");
             testGetContractDetails(contractService, 1);
             
             // 9. Test case 8: Test với ID hợp đồng khác
             System.out.println("\n--- TEST CASE 8: Test với ID hợp đồng khác ---");
             testGetContractById(contractService, 2, "Hợp đồng ID = 2");
             
             // 10. Test case 9: Test performance
             testPerformance(contractService);
             
             // 11. Test case 10: Test random IDs
             testRandomIds(contractService);
             
         } catch (Exception e) {
             System.err.println("❌ LỖI: Không thể khởi tạo ContractService");
             e.printStackTrace();
         }
        
        System.out.println("\n=== KẾT THÚC TEST CONTRACTSERVICE ===");
    }
    
    /**
     * Test method getContractById với các trường hợp khác nhau
     */
    private static void testGetContractById(ContractService contractService, Integer contractId, String testCase) {
        try {
            System.out.println("Testing: " + testCase + " (ID: " + contractId + ")");
            
            // Gọi method cần test
            Optional<ContractDTO> contractOpt = contractService.getContractById(contractId);
            
            // Kiểm tra kết quả
            if (contractOpt.isPresent()) {
                ContractDTO contract = contractOpt.get();
                System.out.println("✓ Tìm thấy hợp đồng:");
                System.out.println("  - Contract ID: " + contract.getContractId());
                System.out.println("  - Customer ID: " + contract.getCustomerId());
                System.out.println("  - Customer Name: " + contract.getCustomerName());
                System.out.println("  - Status: " + contract.getStatus());
                System.out.println("  - Total Amount: " + contract.getTotalAmount());
                System.out.println("  - Created Date: " + contract.getCreateAt());
            } else {
                System.out.println("✓ Không tìm thấy hợp đồng (Optional.empty())");
            }
            
        } catch (Exception e) {
            System.err.println("❌ LỖI trong test case: " + testCase);
            e.printStackTrace();
        }
    }
    
    /**
     * Test method getContractsByCustomer
     */
    private static void testGetContractsByCustomer(ContractService contractService, Integer customerId) {
        try {
            System.out.println("\n--- TEST getContractsByCustomer ---");
            System.out.println("Testing với Customer ID: " + customerId);
            
            var contracts = contractService.getContractsByCustomer(customerId);
            
            System.out.println("✓ Tìm thấy " + contracts.size() + " hợp đồng cho khách hàng " + customerId);
            
            for (int i = 0; i < contracts.size(); i++) {
                ContractDTO contract = contracts.get(i);
                System.out.println("  Hợp đồng " + (i + 1) + ":");
                System.out.println("    - ID: " + contract.getContractId());
                System.out.println("    - Status: " + contract.getStatus());
                System.out.println("    - Total: " + contract.getTotalAmount());
            }
            
        } catch (Exception e) {
            System.err.println("❌ LỖI trong test getContractsByCustomer");
            e.printStackTrace();
        }
    }
    
    /**
     * Test method getContractDetails
     */
    private static void testGetContractDetails(ContractService contractService, Integer contractId) {
        try {
            System.out.println("\n--- TEST getContractDetails ---");
            System.out.println("Testing với Contract ID: " + contractId);
            
            var details = contractService.getContractDetails(contractId);
            
            System.out.println("✓ Tìm thấy " + details.size() + " chi tiết hợp đồng cho contract " + contractId);
            
            for (int i = 0; i < details.size(); i++) {
                var detail = details.get(i);
                System.out.println("  Chi tiết " + (i + 1) + ":");
                System.out.println("    - Detail ID: " + detail.getContractDetailId());
                System.out.println("    - Vehicle ID: " + detail.getVehicleId());
                System.out.println("    - Plate: " + detail.getPlateNumber());
                System.out.println("    - Price: " + detail.getPrice());
                System.out.println("   - Name : " + detail.getName());
            }
            
         } catch (Exception e) {
             System.err.println("❌ LỖI trong test getContractDetails");
             e.printStackTrace();
         }
     }
     
     /**
      * Test performance với nhiều lần gọi
      */
     private static void testPerformance(ContractService contractService) {
         try {
             System.out.println("\n--- TEST PERFORMANCE ---");
             System.out.println("Testing performance với 100 lần gọi getContractById(1)");
             
             long startTime = System.currentTimeMillis();
             
             for (int i = 0; i < 100; i++) {
                 contractService.getContractById(1);
             }
             
             long endTime = System.currentTimeMillis();
             long duration = endTime - startTime;
             
             System.out.println("✓ Hoàn thành 100 lần gọi trong " + duration + "ms");
             System.out.println("✓ Trung bình: " + (duration / 100.0) + "ms/lần");
             
         } catch (Exception e) {
             System.err.println("❌ LỖI trong test performance");
             e.printStackTrace();
         }
     }
     
     /**
      * Test với các ID ngẫu nhiên
      */
     private static void testRandomIds(ContractService contractService) {
         try {
             System.out.println("\n--- TEST RANDOM IDs ---");
             System.out.println("Testing với các ID ngẫu nhiên từ 1-10");
             
             for (int i = 1; i <= 10; i++) {
                 Optional<ContractDTO> contract = contractService.getContractById(i);
                 if (contract.isPresent()) {
                     System.out.println("✓ ID " + i + ": Tìm thấy hợp đồng " + contract.get().getContractId());
                 } else {
                     System.out.println("✗ ID " + i + ": Không tìm thấy");
                 }
             }
             
         } catch (Exception e) {
             System.err.println("❌ LỖI trong test random IDs");
             e.printStackTrace();
         }
     }
 }
