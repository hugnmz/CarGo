
import dao.ContractsDAO;
import dao.ContractDetailsDAO;
import dao.CustomersDAO;
import util.di.DIContainer;
import java.util.List;
import java.util.Optional;
import model.ContractDetails;
import model.Contracts;
import model.Customers;

/**
 * Test đơn giản để kiểm tra database
 */
public class SimpleDatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== SIMPLE DATABASE TEST ===");
        
        try {
            // Lấy các DAO (DI Container tự động scan và register)
            ContractsDAO contractsDAO = DIContainer.get(ContractsDAO.class);
            ContractDetailsDAO contractDetailsDAO = DIContainer.get(ContractDetailsDAO.class);
            CustomersDAO customersDAO = DIContainer.get(CustomersDAO.class);
            
            System.out.println("✓ DAOs initialized successfully");
            
            // Test 1: Kiểm tra customers
            System.out.println("\n1. Testing Customers:");
            List<Customers> allCustomers = customersDAO.getAllCustomers();
            System.out.println("   - Tổng số customers: " + allCustomers.size());
            
            if (allCustomers.isEmpty()) {
                System.out.println("   ❌ KHÔNG CÓ CUSTOMERS TRONG DATABASE!");
                System.out.println("   - Cần tạo customer trước khi test contracts");
                return;
            }
            
            // Hiển thị customer đầu tiên
            Object firstCustomer = allCustomers.get(0);
            System.out.println("   - Customer đầu tiên: " + firstCustomer.toString());
            
            // Test 2: Kiểm tra contracts
            System.out.println("\n2. Testing Contracts:");
            List<Contracts> allContracts = contractsDAO.getAllContracts();
            System.out.println("   - Tổng số contracts: " + allContracts.size());
            
            if (allContracts.isEmpty()) {
                System.out.println("   ❌ KHÔNG CÓ CONTRACTS TRONG DATABASE!");
                System.out.println("   - Đây có thể là nguyên nhân không thấy contracts");
                return;
            }
            
            // Hiển thị contract đầu tiên
            Object firstContract = allContracts.get(0);
            System.out.println("   - Contract đầu tiên: " + firstContract.toString());
            
            // Test 3: Kiểm tra contracts theo customer
            System.out.println("\n3. Testing Contracts by Customer:");
            // Lấy customerId từ customer đầu tiên (giả sử có method getCustomerId)
            try {
                // Sử dụng reflection để lấy customerId
                Integer customerId = (Integer) firstCustomer.getClass().getMethod("getCustomerId").invoke(firstCustomer);
                System.out.println("   - Testing với customerId: " + customerId);
                
                List<Contracts> customerContracts = contractsDAO.getContractByCustomer(customerId);
                System.out.println("   - Số contracts của customer này: " + customerContracts.size());
                
                if (customerContracts.isEmpty()) {
                    System.out.println("   ❌ CUSTOMER NÀY KHÔNG CÓ CONTRACTS!");
                    System.out.println("   - Đây là nguyên nhân không thấy contracts trong my-contracts");
                } else {
                    System.out.println("   ✓ Customer có contracts:");
                    for (Object contract : customerContracts) {
                        System.out.println("     - " + contract.toString());
                    }
                }
            } catch (Exception e) {
                System.out.println("   ❌ Lỗi khi lấy customerId: " + e.getMessage());
            }
            
            // Test 4: Kiểm tra contract details
            System.out.println("\n4. Testing Contract Details:");
            if (!allContracts.isEmpty()) {
                try {
                    // Lấy contractId từ contract đầu tiên
                    Integer contractId = (Integer) firstContract.getClass().getMethod("getContractId").invoke(firstContract);
                    System.out.println("   - Testing với contractId: " + contractId);
                    
                    List<ContractDetails> details = contractDetailsDAO.getContractDetailsByContractId(contractId);
                    System.out.println("   - Số contract details: " + details.size());
                    
                    if (details.isEmpty()) {
                        System.out.println("   ❌ CONTRACT KHÔNG CÓ DETAILS!");
                    } else {
                        System.out.println("   ✓ Contract có details:");
                        for (Object detail : details) {
                            System.out.println("     - " + detail.toString());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("   ❌ Lỗi khi lấy contractId: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== DATABASE TEST COMPLETED ===");
        System.out.println("\nCÁC VẤN ĐỀ CÓ THỂ GẶP:");
        System.out.println("1. Database không có dữ liệu contracts");
        System.out.println("2. Customer không có contracts");
        System.out.println("3. Contract không có details");
        System.out.println("4. Lỗi kết nối database");
        System.out.println("5. Lỗi trong DAO implementation");
    }
}
