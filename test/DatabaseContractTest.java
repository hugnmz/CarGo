package test;

import dao.ContractsDAO;
import dao.ContractDetailsDAO;
import dao.CustomersDAO;
import dao.impl.ContractsDAOImpl;
import dao.impl.ContractDetailsDAOImpl;
import dao.impl.CustomersDAOImpl;
import model.Contracts;
import model.ContractDetails;
import model.Customers;
import util.di.DIContainer;
import java.util.List;
import java.util.Optional;

/**
 * Test để kiểm tra database và dữ liệu contracts
 */
public class DatabaseContractTest {
    
    public static void main(String[] args) {
        System.out.println("=== DATABASE CONTRACT TEST ===");
        
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
            
            // Hiển thị một vài customers
            for (int i = 0; i < Math.min(3, allCustomers.size()); i++) {
                Customers customer = allCustomers.get(i);
                System.out.println("   Customer " + (i+1) + ": ID=" + customer.getCustomerId() + 
                                 ", Name=" + customer.getFullName() + 
                                 ", Email=" + customer.getEmail());
            }
            
            // Test 2: Kiểm tra contracts
            System.out.println("\n2. Testing Contracts:");
            List<Contracts> allContracts = contractsDAO.getAllContracts();
            System.out.println("   - Tổng số contracts: " + allContracts.size());
            
            if (allContracts.isEmpty()) {
                System.out.println("   ❌ KHÔNG CÓ CONTRACTS TRONG DATABASE!");
                System.out.println("   - Đây có thể là nguyên nhân không thấy contracts");
                System.out.println("   - Cần tạo contracts trước khi test");
                return;
            }
            
            // Hiển thị một vài contracts
            for (int i = 0; i < Math.min(3, allContracts.size()); i++) {
                Contracts contract = allContracts.get(i);
                System.out.println("   Contract " + (i+1) + ": ID=" + contract.getContractId() + 
                                 ", Customer ID=" + contract.getCustomerId() + 
                                 ", Status=" + contract.getStatus() + 
                                 ", Total=" + contract.getTotalAmount());
            }
            
            // Test 3: Kiểm tra contracts theo customer
            System.out.println("\n3. Testing Contracts by Customer:");
            Integer testCustomerId = allCustomers.get(0).getCustomerId();
            System.out.println("   - Testing với customerId: " + testCustomerId);
            
            List<Contracts> customerContracts = contractsDAO.getContractByCustomer(testCustomerId);
            System.out.println("   - Số contracts của customer này: " + customerContracts.size());
            
            if (customerContracts.isEmpty()) {
                System.out.println("   ❌ CUSTOMER NÀY KHÔNG CÓ CONTRACTS!");
                System.out.println("   - Đây là nguyên nhân không thấy contracts trong my-contracts");
            } else {
                System.out.println("   ✓ Customer có contracts:");
                for (Contracts contract : customerContracts) {
                    System.out.println("     - Contract ID: " + contract.getContractId());
                    System.out.println("     - Status: " + contract.getStatus());
                    System.out.println("     - Total Amount: " + contract.getTotalAmount());
                }
            }
            
            // Test 4: Kiểm tra contract details
            System.out.println("\n4. Testing Contract Details:");
            if (!allContracts.isEmpty()) {
                Integer testContractId = allContracts.get(0).getContractId();
                System.out.println("   - Testing với contractId: " + testContractId);
                
                List<ContractDetails> details = contractDetailsDAO.getContractDetailsByContractId(testContractId);
                System.out.println("   - Số contract details: " + details.size());
                
                if (details.isEmpty()) {
                    System.out.println("   ❌ CONTRACT KHÔNG CÓ DETAILS!");
                } else {
                    System.out.println("   ✓ Contract có details:");
                    for (ContractDetails detail : details) {
                        System.out.println("     - Detail ID: " + detail.getContractDetailId());
                        System.out.println("     - Vehicle ID: " + detail.getVehicleId());
                        System.out.println("     - Price: " + detail.getPrice());
                    }
                }
            }
            
            // Test 5: Kiểm tra getContractById
            System.out.println("\n5. Testing getContractById:");
            if (!allContracts.isEmpty()) {
                Integer testContractId = allContracts.get(0).getContractId();
                System.out.println("   - Testing với contractId: " + testContractId);
                
                Optional<Contracts> contractOpt = contractsDAO.getContractById(testContractId);
                if (contractOpt.isPresent()) {
                    Contracts contract = contractOpt.get();
                    System.out.println("   ✓ Contract found:");
                    System.out.println("     - ID: " + contract.getContractId());
                    System.out.println("     - Customer ID: " + contract.getCustomerId());
                    System.out.println("     - Status: " + contract.getStatus());
                } else {
                    System.out.println("   ❌ CONTRACT KHÔNG TÌM THẤY!");
                }
            }
            
            // Test 6: Kiểm tra với contractId không tồn tại
            System.out.println("\n6. Testing với contractId không tồn tại (999):");
            Optional<Contracts> nonExistentContract = contractsDAO.getContractById(999);
            if (nonExistentContract.isEmpty()) {
                System.out.println("   ✓ Đúng - contract không tồn tại");
            } else {
                System.out.println("   ❌ Lạ - contract 999 lại tồn tại!");
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
