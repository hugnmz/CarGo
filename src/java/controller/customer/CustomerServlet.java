/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dto.ContractDTO;
import dto.CustomerDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import service.ContractService;
import service.CustomerService;
import util.AuthUtil;
import util.di.DIContainer;

/**
 *
 * @author admin
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/CustomerServlet"})
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService;
    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        try {
            customerService = DIContainer.get(CustomerService.class);
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check login
        if (!AuthUtil.requireLogin(request, response)) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        try {
            // Lấy customerId từ session
            HttpSession session = request.getSession();
            Integer customerId = (Integer) session.getAttribute("customerId");

            // Load thông tin khách hàng từ database
            Optional<CustomerDTO> customerOpt = customerService.getCustomerById(customerId);

            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();
                request.setAttribute("customer", customer);
            }

            List<ContractDTO> listContract = contractService.getContractsByCustomer(customerId);
            request.setAttribute("listContract", listContract);

            // Forward đến trang profile
            request.getRequestDispatcher("/CustomerServlet").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Vẫn forward đến profile, sử dụng session data
            request.getRequestDispatcher("/CustomerServlet").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> errors = new ArrayList<>();
        try {
            // Lấy thông tin từ form
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String city = request.getParameter("city");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            String customerIdStr = request.getParameter("customerId");
            String username = request.getParameter("username");
            String isVerifiedStr = request.getParameter("isVerified");

            // Tạo CustomerDTO
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(Integer.valueOf(customerIdStr));
            customerDTO.setUsername(username);
            customerDTO.setFullName(fullName);
            customerDTO.setEmail(email);
            customerDTO.setPhone(phone);
            customerDTO.setCity(city);

            // Xử lý isVerified nếu có
            if (isVerifiedStr != null && !isVerifiedStr.isEmpty()) {
                customerDTO.setIsVerified(Boolean.valueOf(isVerifiedStr));
            }

            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                customerDTO.setDateOfBirth(java.time.LocalDate.parse(dateOfBirthStr));
            }

            // Cập nhật thông tin
            boolean success = customerService.updateCustomer(customerDTO);

            if (success) {
                // Cập nhật session
                HttpSession session = request.getSession();
                session.setAttribute("fullName", fullName);
                session.setAttribute("email", email);
                session.setAttribute("phone", phone);
                session.setAttribute("city", city);
                if (dateOfBirthStr != null) {
                    session.setAttribute("dateOfBirth", dateOfBirthStr);
                }
                // Redirect về Servlet để load data
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?success=1");
            } else {
                errors.add("Có lỗi xảy ra khi cập nhật thông tin. Vui lòng thử lại.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add("Có lỗi xảy ra khi xử lý yêu cầu. Vui lòng thử lại.");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
        }
    }

}
