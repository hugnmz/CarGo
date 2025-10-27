package test;

import dto.ContractDTO;
import service.ContractService;
import util.di.DIContainer;
import java.util.Optional;

/**
 * Test đơn giản cho ContractService
 * Chỉ test method getContractById() cơ bản
 */
public class SimpleContractTest {
    
    public static void main(String[] args) {
        System.out.println("=== SIMPLE CONTRACT TEST ===");
        
        try {
            // Khởi tạo service
            ContractService contractService = DIContainer.get(ContractService.class);
            System.out.println("✓ Service khởi tạo thành công");
            
            // Test cơ bản
            System.out.println("\n1. Test getContractById(1):");
            Optional<ContractDTO> contract = contractService.getContractById(1);
            
            if (contract.isPresent()) {
                ContractDTO c = contract.get();
                System.out.println("✓ Tìm thấy hợp đồng:");
                System.out.println("   ID: " + c.getContractId());
                System.out.println("   Customer: " + c.getCustomerName());
                System.out.println("   Status: " + c.getStatus());
                System.out.println("   Amount: " + c.getTotalAmount());
            } else {
                System.out.println("✗ Không tìm thấy hợp đồng");
            }
            
            // Test với ID không tồn tại
            System.out.println("\n2. Test getContractById(999):");
            Optional<ContractDTO> contract2 = contractService.getContractById(999);
            
            if (contract2.isPresent()) {
                System.out.println("✗ Không mong đợi tìm thấy hợp đồng 999");
            } else {
                System.out.println("✓ Đúng - không tìm thấy hợp đồng 999");
            }
            
            // Test với null
            System.out.println("\n3. Test getContractById(null):");
            try {
                Optional<ContractDTO> contract3 = contractService.getContractById(null);
                if (contract3.isPresent()) {
                    System.out.println("✗ Không mong đợi tìm thấy với null");
                } else {
                    System.out.println("✓ Đúng - null trả về empty");
                }
            } catch (Exception e) {
                System.out.println("✓ Đúng - null gây exception: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== KẾT THÚC TEST ===");
    }
}
