package test;

import dto.ContractDTO;
import dto.ContractDetailDTO;
import service.ContractService;
import util.di.DIContainer;
import java.util.List;
import java.util.Optional;

/**
 * Test đơn giản để debug vấn đề contracts
 */
public class SimpleContractTest {
    
    public static void main(String[] args) {
        System.out.println("=== SIMPLE CONTRACT TEST ===");
        
        try {
            // Lấy ContractService (DI Container tự động scan và register)
            ContractService contractService = DIContainer.get(ContractService.class);
            System.out.println("✓ ContractService initialized successfully");
            
            // Test với customerId = 1
            Integer customerId = 1;
            System.out.println("\n1. Testing getContractsByCustomer for customerId: " + customerId);
            
            List<ContractDTO> contracts = contractService.getContractsByCustomer(customerId);
            System.out.println("   - Số lượng contracts: " + contracts.size());
            
            if (contracts.isEmpty()) {
                System.out.println("   ❌ KHÔNG TÌM THẤY CONTRACTS!");
                System.out.println("   - Nguyên nhân có thể:");
                System.out.println("     + Database không có contracts cho customer này");
                System.out.println("     + Customer ID không tồn tại");
                System.out.println("     + Lỗi trong ContractService");
            } else {
                System.out.println("   ✓ Tìm thấy contracts:");
                for (int i = 0; i < contracts.size(); i++) {
                    ContractDTO contract = contracts.get(i);
                    System.out.println("     Contract " + (i+1) + ":");
                    System.out.println("       - ID: " + contract.getContractId());
                    System.out.println("       - Customer ID: " + contract.getCustomerId());
                    System.out.println("       - Status: " + contract.getStatus());
                    System.out.println("       - Total Amount: " + contract.getTotalAmount());
                }
                
                // Test xem chi tiết contract đầu tiên
                if (!contracts.isEmpty()) {
                    Integer firstContractId = contracts.get(0).getContractId();
                    System.out.println("\n2. Testing getContractById for contractId: " + firstContractId);
                    
                    Optional<ContractDTO> contractOpt = contractService.getContractById(firstContractId);
                    if (contractOpt.isPresent()) {
                        ContractDTO contract = contractOpt.get();
                        System.out.println("   ✓ Contract found:");
                        System.out.println("     - ID: " + contract.getContractId());
                        System.out.println("     - Customer ID: " + contract.getCustomerId());
                        System.out.println("     - Customer Name: " + contract.getCustomerName());
                        System.out.println("     - Status: " + contract.getStatus());
                        
                        // Test lấy contract details
                        System.out.println("\n3. Testing getContractDetails for contractId: " + firstContractId);
                        List<ContractDetailDTO> details = contractService.getContractDetails(firstContractId);
                        System.out.println("   - Số lượng contract details: " + details.size());
                        
                        if (details.isEmpty()) {
                            System.out.println("   ❌ KHÔNG TÌM THẤY CONTRACT DETAILS!");
                        } else {
                            System.out.println("   ✓ Contract details found:");
                            for (int i = 0; i < details.size(); i++) {
                                ContractDetailDTO detail = details.get(i);
                                System.out.println("     Detail " + (i+1) + ":");
                                System.out.println("       - Contract Detail ID: " + detail.getContractDetailId());
                                System.out.println("       - Vehicle ID: " + detail.getVehicleId());
                                System.out.println("       - Price: " + detail.getPrice());
                            }
                        }
                        
                    } else {
                        System.out.println("   ❌ CONTRACT KHÔNG TỒN TẠI!");
                        System.out.println("   - Đây là nguyên nhân redirect về home trong ViewContractServlet");
                    }
                }
            }
            
            // Test với contractId không tồn tại
            System.out.println("\n4. Testing với contractId không tồn tại (999)");
            Optional<ContractDTO> nonExistentContract = contractService.getContractById(999);
            if (nonExistentContract.isEmpty()) {
                System.out.println("   ✓ Đúng - contract không tồn tại, sẽ redirect về home");
            }
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== TEST COMPLETED ===");
        System.out.println("\nCÁC NGUYÊN NHÂN CÓ THỂ GÂY VẤN ĐỀ:");
        System.out.println("1. Không có contracts cho customer này");
        System.out.println("2. ContractId parameter bị null hoặc không hợp lệ");
        System.out.println("3. Contract không tồn tại trong database");
        System.out.println("4. Customer không phải owner của contract");
        System.out.println("5. Lỗi trong ContractServiceImpl");
    }
}
