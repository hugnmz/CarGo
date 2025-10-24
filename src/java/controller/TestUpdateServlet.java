package controller;

import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CustomerService;
import util.di.DIContainer;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Test servlet để kiểm tra update customer
 */
@WebServlet("/TestUpdateServlet")
public class TestUpdateServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Test Update Customer</title></head><body>");
        out.println("<h1>Test Update Customer</h1>");
        
        try {
            // Test 1: Khởi tạo CustomerService
            out.println("<h2>Step 1: Initialize CustomerService</h2>");
            CustomerService customerService = DIContainer.get(CustomerService.class);
            
            if (customerService == null) {
                out.println("<p style='color:red'>❌ CustomerService is NULL!</p>");
                out.println("<p>DIContainer cannot create CustomerService</p>");
                return;
            }
            
            out.println("<p style='color:green'>✅ CustomerService initialized successfully</p>");
            out.println("<p>Service class: " + customerService.getClass().getName() + "</p>");
            
            // Test 2: Tạo CustomerDTO với ID thực tế
            out.println("<h2>Step 2: Create CustomerDTO</h2>");
            CustomerDTO customer = new CustomerDTO();
            customer.setCustomerId(56); // ID thực tế từ database
            customer.setUsername("1");
            customer.setFullName("Test User Updated");
            customer.setEmail("testupdated@example.com");
            customer.setPhone("0987654321");
            customer.setCity("TP.HCM");
            customer.setDateOfBirth(LocalDate.of(1995, 3, 20));
            
            out.println("<p>CustomerDTO created:</p>");
            out.println("<ul>");
            out.println("<li>ID: " + customer.getCustomerId() + "</li>");
            out.println("<li>Username: " + customer.getUsername() + "</li>");
            out.println("<li>FullName: " + customer.getFullName() + "</li>");
            out.println("<li>Email: " + customer.getEmail() + "</li>");
            out.println("<li>Phone: " + customer.getPhone() + "</li>");
            out.println("<li>City: " + customer.getCity() + "</li>");
            out.println("<li>DateOfBirth: " + customer.getDateOfBirth() + "</li>");
            out.println("</ul>");
            
            // Test 3: Update customer
            out.println("<h2>Step 3: Update Customer</h2>");
            boolean result = customerService.updateCustomer(customer);
            
            if (result) {
                out.println("<p style='color:green'>✅ Update successful!</p>");
            } else {
                out.println("<p style='color:red'>❌ Update failed!</p>");
            }
            
            // Test 4: Verify update
            out.println("<h2>Step 4: Verify Update</h2>");
            var updatedCustomerOpt = customerService.getCustomerById(customer.getCustomerId());
            
            if (updatedCustomerOpt.isPresent()) {
                CustomerDTO updatedCustomer = updatedCustomerOpt.get();
                out.println("<p style='color:green'>✅ Customer found after update:</p>");
                out.println("<ul>");
                out.println("<li>FullName: " + updatedCustomer.getFullName() + "</li>");
                out.println("<li>Email: " + updatedCustomer.getEmail() + "</li>");
                out.println("<li>Phone: " + updatedCustomer.getPhone() + "</li>");
                out.println("<li>City: " + updatedCustomer.getCity() + "</li>");
                out.println("<li>DateOfBirth: " + updatedCustomer.getDateOfBirth() + "</li>");
                out.println("</ul>");
            } else {
                out.println("<p style='color:red'>❌ Customer not found after update</p>");
            }
            
        } catch (Exception e) {
            out.println("<p style='color:red'>❌ Exception: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
        
        out.println("</body></html>");
    }
}
