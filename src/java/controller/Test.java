package controller;

import dto.ContractDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/test-staff")
public class Test extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Không cần init gì vì chỉ tạo data test
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Tạo 3 contracts test
        List<ContractDTO> testContracts = new ArrayList<>();
        
        // Contract 1 - PENDING
        ContractDTO c1 = new ContractDTO();
        c1.setContractId(1);
        c1.setCustomerId(1);
        c1.setCustomerName("Nguyễn Văn A");
        c1.setStatus("PENDING");
        c1.setStartDate(LocalDateTime.now().plusDays(1));
        c1.setEndDate(LocalDateTime.now().plusDays(5));
        c1.setTotalAmount(new BigDecimal("5000000"));
        c1.setDepositAmount(new BigDecimal("1000000"));
        testContracts.add(c1);
        
        // Contract 2 - ACCEPTED
        ContractDTO c2 = new ContractDTO();
        c2.setContractId(2);
        c2.setCustomerId(2);
        c2.setCustomerName("Trần Thị B");
        c2.setStatus("ACCEPTED");
        c2.setStartDate(LocalDateTime.now().minusDays(5));
        c2.setEndDate(LocalDateTime.now().minusDays(3));
        c2.setTotalAmount(new BigDecimal("3000000"));
        c2.setDepositAmount(new BigDecimal("900000"));
        testContracts.add(c2);
        
        // Contract 3 - PENDING
        ContractDTO c3 = new ContractDTO();
        c3.setContractId(3);
        c3.setCustomerId(3);
        c3.setCustomerName("Lê Văn C");
        c3.setStatus("PENDING");
        c3.setStartDate(LocalDateTime.now().plusDays(10));
        c3.setEndDate(LocalDateTime.now().plusDays(15));
        c3.setTotalAmount(new BigDecimal("8000000"));
        c3.setDepositAmount(new BigDecimal("1600000"));
        testContracts.add(c3);
        
        // Set attribute
        request.setAttribute("contracts", testContracts);
        
        // Forward to staff.jsp
        request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);
    }
}