/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package constant.customer;

import dto.CustomerDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.Contracts;
import service.CustomerService;
import util.di.DIContainer;

/**
 *
 * @author admin
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/customer"})
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        try {
            customerService = DIContainer.get(CustomerService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra session
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("errorMessage", "Bạn cần đăng nhập để xem thông tin");
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            return;
        }
        
        String username = (String) session.getAttribute("username");
        if (username == null) {
            request.setAttribute("errorMessage", "Bạn cần đăng nhập để xem thông tin");
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            return;
        }
        
        try {
            // Lấy thông tin customer
            Optional<CustomerDTO> customerOpt = customerService.getCustomerByUsername(username);
            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();
                request.setAttribute("customer", customer);
                
                // Load contracts của customer này
                List<Contracts> contracts = customerService.getCustomerContracts(customer.getCustomerId());
                request.setAttribute("contracts", contracts);
                
                System.out.println("[DEBUG] Loaded " + contracts.size() + " contracts for customer: " + customer.getFullName());
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin khách hàng");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải thông tin");
        }
        
        request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String city = request.getParameter("city");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String customerIdStr = request.getParameter("customerId");

        List<String> validationErrors = new ArrayList<>();
        Map<String, String> fieldErrors = new HashMap<>();

        if (fullName == null || fullName.trim().isEmpty()) {
            fieldErrors.put("fullName", "Họ và tên không được để trống");
            validationErrors.add("Họ và tên không được để trống");
        }

        if (email == null || email.trim().isEmpty()) {
            fieldErrors.put("email", "Email không được để trống");
            validationErrors.add("Email không được để trống");
        }

        if (phone == null || phone.trim().isEmpty()) {
            fieldErrors.put("phone", "Số điện thoại không được để trống");
            validationErrors.add("Số điện thoại không được để trống");
        }

        if (!validationErrors.isEmpty()) {
            request.setAttribute("validationErrors", validationErrors);
            request.setAttribute("fieldErrors", fieldErrors);
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            return;
        }

        // lay session nguoi dung
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("errorMessage", "Bạn cần đăng nhập để cập nhật thông tin");
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            return;
        }
        
        String username = (String) session.getAttribute("username");
        if (username == null) {
            request.setAttribute("errorMessage", "Bạn cần đăng nhập để cập nhật thông tin");
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            return;
        }

        Optional<CustomerDTO> dtoOpt = customerService.getCustomerByUsername(username);
        if (dtoOpt.isEmpty()) {
            request.setAttribute("errorMessage", "Người dùng không tồn tại");
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            return;
        } else {
            // customer hien tai
            CustomerDTO currCus = dtoOpt.get();

            CustomerDTO cDTO = new CustomerDTO();
            cDTO.setCustomerId(currCus.getCustomerId()); // QUAN TRỌNG: Set customerId
            cDTO.setUsername(currCus.getUsername()); // Set username
            cDTO.setCity(city);
            cDTO.setDateOfBirth(dateOfBirthStr != null && !dateOfBirthStr.isEmpty()
                    ? LocalDate.parse(dateOfBirthStr) : null);
            cDTO.setFullName(fullName);
            cDTO.setPhone(phone);
            cDTO.setEmail(email);

            // Debug: In ra thông tin
            System.out.println("[DEBUG] Customer ID: " + cDTO.getCustomerId());
            System.out.println("[DEBUG] Username: " + cDTO.getUsername());
            System.out.println("[DEBUG] FullName: " + cDTO.getFullName());
            System.out.println("[DEBUG] Email: " + cDTO.getEmail());
            System.out.println("[DEBUG] Phone: " + cDTO.getPhone());
            System.out.println("[DEBUG] City: " + cDTO.getCity());
            System.out.println("[DEBUG] DateOfBirth: " + cDTO.getDateOfBirth());
            
            boolean ok = customerService.updateCustomer(cDTO);
            System.out.println("[DEBUG] Update result: " + ok);
            
            if (ok) {
                request.setAttribute("successMessage", "Update thành công");
                session.setAttribute("fullName", fullName);
                session.setAttribute("email", email);
                session.setAttribute("phone", phone);
                session.setAttribute("city", city);
            } else {
                request.setAttribute("errorMessage", "update ko thành công");
            }
        }

        request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);

    }

}
