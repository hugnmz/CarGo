package test;
import dao.ContractsDAO;
import dao.ContractDetailsDAO;
import dao.CustomersDAO;
import mapper.ContractMapper;
import mapper.ContractDetailMapper;
import model.Contracts;
import model.ContractDetails;
import model.Customers;
import dto.ContractDTO;
import dto.ContractDetailDTO;
import java.math.BigDecimal;
import util.di.DIContainer;
import java.util.List;
import java.util.Optional;

/**
 * Test để kiểm tra mapper và DI injection cho contract
 */
public class ContractMapperTest {
    
    public static void main(String[] args) {
        System.out.println("=== CONTRACT MAPPER TEST ===");
        
        try {
            // Test 1: Kiểm tra DI injection cho các DAO
            System.out.println("\n1. Testing DI injection for DAOs:");
            testDAOInjection();
            
            // Test 2: Kiểm tra DI injection cho các Mapper
            System.out.println("\n2. Testing DI injection for Mappers:");
            testMapperInjection();
            
            // Test 3: Kiểm tra mapper hoạt động
            System.out.println("\n3. Testing Mapper functionality:");
            testMapperFunctionality();
            
            // Test 4: Kiểm tra toàn bộ flow từ DAO -> Mapper -> DTO
            System.out.println("\n4. Testing complete flow DAO -> Mapper -> DTO:");
            testCompleteFlow();
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== MAPPER TEST COMPLETED ===");
    }
    
    private static void testDAOInjection() {
        try {
            ContractsDAO contractsDAO = DIContainer.get(ContractsDAO.class);
            System.out.println("   ✓ ContractsDAO injected: " + contractsDAO.getClass().getName());
            
            ContractDetailsDAO contractDetailsDAO = DIContainer.get(ContractDetailsDAO.class);
            System.out.println("   ✓ ContractDetailsDAO injected: " + contractDetailsDAO.getClass().getName());
            
            CustomersDAO customersDAO = DIContainer.get(CustomersDAO.class);
            System.out.println("   ✓ CustomersDAO injected: " + customersDAO.getClass().getName());
            
        } catch (Exception e) {
            System.err.println("   ❌ DAO injection failed: " + e.getMessage());
        }
    }
    
    private static void testMapperInjection() {
        try {
            ContractMapper contractMapper = DIContainer.get(ContractMapper.class);
            System.out.println("   ✓ ContractMapper injected: " + contractMapper.getClass().getName());
            
            ContractDetailMapper contractDetailMapper = DIContainer.get(ContractDetailMapper.class);
            System.out.println("   ✓ ContractDetailMapper injected: " + contractDetailMapper.getClass().getName());
            
        } catch (Exception e) {
            System.err.println("   ❌ Mapper injection failed: " + e.getMessage());
        }
    }
    
    private static void testMapperFunctionality() {
        try {
            ContractMapper contractMapper = DIContainer.get(ContractMapper.class);
            ContractDetailMapper contractDetailMapper = DIContainer.get(ContractDetailMapper.class);
            
            // Test với dữ liệu mẫu
            System.out.println("   - Testing ContractMapper.toDTO():");
            
            // Tạo một Contracts object mẫu để test mapper
            Contracts sampleContract = new Contracts();
            sampleContract.setContractId(1);
            sampleContract.setCustomerId(1);
            sampleContract.setStatus("PENDING");
            sampleContract.setTotalAmount(BigDecimal.valueOf(100000.0));
            sampleContract.setDepositAmount(BigDecimal.valueOf(200000.0));
            
            ContractDTO contractDTO = contractMapper.toDTO(sampleContract);
            System.out.println("     ✓ Contract mapped successfully:");
            System.out.println("       - Contract ID: " + contractDTO.getContractId());
            System.out.println("       - Customer ID: " + contractDTO.getCustomerId());
            System.out.println("       - Status: " + contractDTO.getStatus());
            System.out.println("       - Total Amount: " + contractDTO.getTotalAmount());
            
            System.out.println("   - Testing ContractDetailMapper.toDTO():");
            
            // Tạo một ContractDetails object mẫu để test mapper
            ContractDetails sampleDetail = new ContractDetails();
            sampleDetail.setContractDetailId(1);
            sampleDetail.setContractId(1);
            sampleDetail.setVehicleId(1);
            sampleDetail.setPrice(BigDecimal.valueOf(500000.0));
            
            ContractDetailDTO detailDTO = contractDetailMapper.toDTO(sampleDetail);
            System.out.println("     ✓ ContractDetail mapped successfully:");
            System.out.println("       - Detail ID: " + detailDTO.getContractDetailId());
            System.out.println("       - Contract ID: " + detailDTO.getContractId());
            System.out.println("       - Vehicle ID: " + detailDTO.getVehicleId());
            System.out.println("       - Price: " + detailDTO.getPrice());
            
        } catch (Exception e) {
            System.err.println("   ❌ Mapper functionality failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testCompleteFlow() {
        try {
            ContractsDAO contractsDAO = DIContainer.get(ContractsDAO.class);
            ContractMapper contractMapper = DIContainer.get(ContractMapper.class);
            
            System.out.println("   - Testing complete flow:");
            
            // Lấy contracts từ database
            List<Contracts> contracts = contractsDAO.getAllContracts();
            System.out.println("     - Contracts from DB: " + contracts.size());
            
            if (contracts.isEmpty()) {
                System.out.println("     ❌ No contracts in database - cannot test complete flow");
                return;
            }
            
            // Test với contract đầu tiên
            Object firstContract = contracts.get(0);
            System.out.println("     - First contract from DB: " + firstContract.toString());
            
            // Kiểm tra xem có phải là Contracts object không
            if (firstContract instanceof Contracts) {
                Contracts contract = (Contracts) firstContract;
                
                // Map sang DTO
                ContractDTO contractDTO = contractMapper.toDTO(contract);
                System.out.println("     ✓ Complete flow successful:");
                System.out.println("       - DB Contract ID: " + contract.getContractId());
                System.out.println("       - DTO Contract ID: " + contractDTO.getContractId());
                System.out.println("       - DB Status: " + contract.getStatus());
                System.out.println("       - DTO Status: " + contractDTO.getStatus());
                
                // Kiểm tra tất cả fields được map đúng
                boolean mappingCorrect = true;
                if (!contract.getContractId().equals(contractDTO.getContractId())) {
                    System.out.println("     ❌ Contract ID mapping incorrect");
                    mappingCorrect = false;
                }
                if (!contract.getCustomerId().equals(contractDTO.getCustomerId())) {
                    System.out.println("     ❌ Customer ID mapping incorrect");
                    mappingCorrect = false;
                }
                if (!contract.getStatus().equals(contractDTO.getStatus())) {
                    System.out.println("     ❌ Status mapping incorrect");
                    mappingCorrect = false;
                }
                if (!contract.getTotalAmount().equals(contractDTO.getTotalAmount())) {
                    System.out.println("     ❌ Total Amount mapping incorrect");
                    mappingCorrect = false;
                }
                
                if (mappingCorrect) {
                    System.out.println("     ✓ All fields mapped correctly");
                }
                
            } else {
                System.out.println("     ❌ First contract is not Contracts object: " + firstContract.getClass().getName());
            }
            
            // Test với contract details
            System.out.println("   - Testing ContractDetails flow:");
            ContractDetailsDAO contractDetailsDAO = DIContainer.get(ContractDetailsDAO.class);
            ContractDetailMapper contractDetailMapper = DIContainer.get(ContractDetailMapper.class);
            
            if (firstContract instanceof Contracts) {
                Contracts contract = (Contracts) firstContract;
                List<ContractDetails> details = contractDetailsDAO.getContractDetailsByContractId(contract.getContractId());
                System.out.println("     - Contract details from DB: " + details.size());
                
                if (!details.isEmpty()) {
                    Object firstDetail = details.get(0);
                    if (firstDetail instanceof ContractDetails) {
                        ContractDetails detail = (ContractDetails) firstDetail;
                        ContractDetailDTO detailDTO = contractDetailMapper.toDTO(detail);
                        System.out.println("     ✓ ContractDetail flow successful:");
                        System.out.println("       - DB Detail ID: " + detail.getContractDetailId());
                        System.out.println("       - DTO Detail ID: " + detailDTO.getContractDetailId());
                        System.out.println("       - DB Vehicle ID: " + detail.getVehicleId());
                        System.out.println("       - DTO Vehicle ID: " + detailDTO.getVehicleId());
                    } else {
                        System.out.println("     ❌ First detail is not ContractDetails object: " + firstDetail.getClass().getName());
                    }
                } else {
                    System.out.println("     ❌ No contract details found");
                }
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Complete flow failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
