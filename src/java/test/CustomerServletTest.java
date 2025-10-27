package test;

import dto.CustomerDTO;
import service.CustomerService;
import util.di.DIContainer;
import java.util.Optional;

public class CustomerServletTest {
    
    public static void main(String[] args) {
        System.out.println("=== TEST CUSTOMER SERVLET & MAPPING ===");
        
        try {
            CustomerService customerService = DIContainer.get(CustomerService.class);
            System.out.println("✓ CustomerService khởi tạo thành công");
            
            // Test getCustomerById
            System.out.println("\n--- TEST getCustomerById ---");
            Optional<CustomerDTO> customerOpt = customerService.getCustomerById(1);
            
            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();
                System.out.println("✓ Tìm thấy customer:");
                System.out.println("  - Customer ID: " + customer.getCustomerId());
                System.out.println("  - Username: " + customer.getUsername());
                System.out.println("  - Full Name: " + customer.getFullName());
                System.out.println("  - Created At: " + customer.getCreateAt());
                
                if (customer.getCreateAt() != null) {
                    System.out.println("✓ Ngày tạo: " + customer.getCreateAt());
                } else {
                    System.out.println("❌ Ngày tạo: NULL - Có vấn đề với mapping!");
                }
            } else {
                System.out.println("✗ Không tìm thấy customer");
            }
            
        } catch (Exception e) {
            System.err.println("❌ LỖI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}