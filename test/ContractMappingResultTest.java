
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
 * Test để kiểm tra mapping result từ database
 */
public class ContractMappingResultTest {
    
    public static void main(String[] args) {
        System.out.println("=== CONTRACT MAPPING RESULT TEST ===");
        
        try {
            // Test 1: Kiểm tra mapping từ database
            System.out.println("\n1. Testing mapping from database:");
            testDatabaseMapping();
            
            // Test 2: Kiểm tra mapping với dữ liệu thực
            System.out.println("\n2. Testing mapping with real data:");
            testRealDataMapping();
            
            // Test 3: Kiểm tra mapping edge cases
            System.out.println("\n3. Testing mapping edge cases:");
            testEdgeCases();
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== MAPPING RESULT TEST COMPLETED ===");
    }
    
    private static void testDatabaseMapping() {
        try {
            ContractsDAO contractsDAO = DIContainer.get(ContractsDAO.class);
            ContractMapper contractMapper = DIContainer.get(ContractMapper.class);
            
            // Lấy contracts từ database
            List<Contracts> contracts = contractsDAO.getAllContracts();
            System.out.println("   - Total contracts in DB: " + contracts.size());
            
            if (contracts.isEmpty()) {
                System.out.println("   ❌ No contracts in database - cannot test mapping");
                return;
            }
            
            // Test mapping với từng contract
            for (int i = 0; i < Math.min(3, contracts.size()); i++) {
                Object contractObj = contracts.get(i);
                System.out.println("   - Testing contract " + (i+1) + ": " + contractObj.getClass().getName());
                
                if (contractObj instanceof Contracts) {
                    Contracts contract = (Contracts) contractObj;
                    ContractDTO contractDTO = contractMapper.toDTO(contract);
                    
                    System.out.println("     ✓ Mapping successful:");
                    System.out.println("       - DB Contract ID: " + contract.getContractId());
                    System.out.println("       - DTO Contract ID: " + contractDTO.getContractId());
                    System.out.println("       - DB Customer ID: " + contract.getCustomerId());
                    System.out.println("       - DTO Customer ID: " + contractDTO.getCustomerId());
                    System.out.println("       - DB Status: " + contract.getStatus());
                    System.out.println("       - DTO Status: " + contractDTO.getStatus());
                    System.out.println("       - DB Total Amount: " + contract.getTotalAmount());
                    System.out.println("       - DTO Total Amount: " + contractDTO.getTotalAmount());
                    
                    // Kiểm tra mapping có đúng không
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
                    System.out.println("     ❌ Contract is not Contracts object: " + contractObj.getClass().getName());
                }
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Database mapping failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testRealDataMapping() {
        try {
            ContractsDAO contractsDAO = DIContainer.get(ContractsDAO.class);
            ContractDetailsDAO contractDetailsDAO = DIContainer.get(ContractDetailsDAO.class);
            ContractMapper contractMapper = DIContainer.get(ContractMapper.class);
            ContractDetailMapper contractDetailMapper = DIContainer.get(ContractDetailMapper.class);
            
            // Lấy contract đầu tiên
            List<Contracts> contracts = contractsDAO.getAllContracts();
            if (contracts.isEmpty()) {
                System.out.println("   ❌ No contracts to test with");
                return;
            }
            
            Object firstContract = contracts.get(0);
            if (firstContract instanceof Contracts) {
                Contracts contract = (Contracts) firstContract;
                System.out.println("   - Testing with real contract ID: " + contract.getContractId());
                
                // Test contract mapping
                ContractDTO contractDTO = contractMapper.toDTO(contract);
                System.out.println("     ✓ Contract mapped:");
                System.out.println("       - Contract ID: " + contractDTO.getContractId());
                System.out.println("       - Customer ID: " + contractDTO.getCustomerId());
                System.out.println("       - Status: " + contractDTO.getStatus());
                System.out.println("       - Total Amount: " + contractDTO.getTotalAmount());
                
                // Test contract details mapping
                List<ContractDetails> details = contractDetailsDAO.getContractDetailsByContractId(contract.getContractId());
                System.out.println("     - Contract details count: " + details.size());
                
                for (int i = 0; i < Math.min(2, details.size()); i++) {
                    Object detailObj = details.get(i);
                    if (detailObj instanceof ContractDetails) {
                        ContractDetails detail = (ContractDetails) detailObj;
                        ContractDetailDTO detailDTO = contractDetailMapper.toDTO(detail);
                        
                        System.out.println("       Detail " + (i+1) + " mapped:");
                        System.out.println("         - Detail ID: " + detailDTO.getContractDetailId());
                        System.out.println("         - Contract ID: " + detailDTO.getContractId());
                        System.out.println("         - Vehicle ID: " + detailDTO.getVehicleId());
                        System.out.println("         - Price: " + detailDTO.getPrice());
                        
                    } else {
                        System.out.println("       ❌ Detail is not ContractDetails object: " + detailObj.getClass().getName());
                    }
                }
                
            } else {
                System.out.println("   ❌ First contract is not Contracts object: " + firstContract.getClass().getName());
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Real data mapping failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testEdgeCases() {
        try {
            ContractMapper contractMapper = DIContainer.get(ContractMapper.class);
            ContractDetailMapper contractDetailMapper = DIContainer.get(ContractDetailMapper.class);
            
            System.out.println("   - Testing with null values:");
            
            // Test với null values
            Contracts nullContract = new Contracts();
            // Không set gì cả - tất cả fields sẽ là null
            ContractDTO nullContractDTO = contractMapper.toDTO(nullContract);
            System.out.println("     ✓ Null contract mapped:");
            System.out.println("       - Contract ID: " + nullContractDTO.getContractId());
            System.out.println("       - Customer ID: " + nullContractDTO.getCustomerId());
            System.out.println("       - Status: " + nullContractDTO.getStatus());
            System.out.println("       - Total Amount: " + nullContractDTO.getTotalAmount());
            
            // Test với empty values
            Contracts emptyContract = new Contracts();
            emptyContract.setContractId(0);
            emptyContract.setCustomerId(0);
            emptyContract.setStatus("");
            emptyContract.setTotalAmount(BigDecimal.valueOf(0));
            emptyContract.setDepositAmount(BigDecimal.valueOf(0));
            
            ContractDTO emptyContractDTO = contractMapper.toDTO(emptyContract);
            System.out.println("     ✓ Empty contract mapped:");
            System.out.println("       - Contract ID: " + emptyContractDTO.getContractId());
            System.out.println("       - Customer ID: " + emptyContractDTO.getCustomerId());
            System.out.println("       - Status: " + emptyContractDTO.getStatus());
            System.out.println("       - Total Amount: " + emptyContractDTO.getTotalAmount());
            
            // Test với special values
            Contracts specialContract = new Contracts();
            specialContract.setContractId(-1);
            specialContract.setCustomerId(-1);
            specialContract.setStatus("SPECIAL_STATUS");
            specialContract.setTotalAmount(BigDecimal.valueOf(-999.99));
            specialContract.setDepositAmount(BigDecimal.valueOf(-100.0));
            
            ContractDTO specialContractDTO = contractMapper.toDTO(specialContract);
            System.out.println("     ✓ Special contract mapped:");
            System.out.println("       - Contract ID: " + specialContractDTO.getContractId());
            System.out.println("       - Customer ID: " + specialContractDTO.getCustomerId());
            System.out.println("       - Status: " + specialContractDTO.getStatus());
            System.out.println("       - Total Amount: " + specialContractDTO.getTotalAmount());
            
        } catch (Exception e) {
            System.err.println("   ❌ Edge cases test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
