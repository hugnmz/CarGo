/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.customer;

import dto.ContractDTO;
import dto.CustomerDTO;
import dto.LocationDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import service.ContractService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import service.CustomersService;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ControllerInforCustomer", urlPatterns = {"/controllerinformationcustomer"})
public class ControllerInforCustomer extends HttpServlet {

    private CustomersService customerService;
    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CustomersService.class);
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    private void showDetailCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("customerId");
            // Xóa check - chỉ parse
            Integer customerId = (idStr != null) ? Integer.parseInt(idStr) : null;

            // Load thông tin khách hàng từ database
            Optional<CustomerDTO> customerOpt = customerService.getCustomerById(customerId);

            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();
                request.setAttribute("customer", customer);
            }

            List<ContractDTO> listContract = contractService.getContractsByCustomer(customerId);
            request.setAttribute("listContract", listContract);

            // Forward đến trang profile
            request.getRequestDispatcher("/manager/manage_detail_cus.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/manager/manage_detail_cus.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.customer.management"));
            request.getRequestDispatcher("/manager/manage_detail_cus.jsp").forward(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("customerId");
            // Xóa check - chỉ parse
            Integer customerId = (idStr != null) ? Integer.parseInt(idStr) : null;

            // Load thông tin khách hàng từ database
            Optional<CustomerDTO> customerOpt = customerService.getCustomerById(customerId);
            List<LocationDTO> locations = customerService.getAllLocation();

            if (customerOpt.isPresent()) {
                CustomerDTO customer = customerOpt.get();
                request.setAttribute("customer", customer);
            }
            request.setAttribute("locations", locations);

            // Forward đến trang profile
            request.getRequestDispatcher("/manager/editcustomer.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/manager/editcustomer.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.customer.management"));
            request.getRequestDispatcher("/manager/editcustomer.jsp").forward(request, response);
        }
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idStr = request.getParameter("customerId");
            // Xóa check - service sẽ check và throw WebException
            Integer customerId = (idStr != null) ? Integer.parseInt(idStr) : null;

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String dob = request.getParameter("dateOfBirth");
            String city = request.getParameter("locationId");
            Integer locationId = (city != null && !city.isEmpty()) ? Integer.parseInt(city) : null;

            String isVerify = request.getParameter("isVerified");

            LocalDate dateOfBirth = null;
            if (dob != null && !dob.isEmpty()) {
                dateOfBirth = LocalDate.parse(dob);
            }

            Boolean isVerified = "1".equals(isVerify);

            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(customerId);
            customerDTO.setFullName(fullName);
            customerDTO.setEmail(email);
            customerDTO.setPhone(phone);
            customerDTO.setDateOfBirth(dateOfBirth);
            customerDTO.setLocationId(locationId);
            customerDTO.setIsVerified(isVerified);

            // Gọi service - service sẽ check và throw WebException
            customerService.updateCustomer(customerDTO);

            request.setAttribute("message", MessageUtil.getError("error.user.update.success"));

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.customer.management"));
        }
        // Forward về trang quản lý để hiển thị thông báo
        request.getRequestDispatcher("managecus").forward(request, response);
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("customerId");
            // Xóa check - service sẽ check và throw WebException
            int customerId = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;

            // Gọi service để xóa - service sẽ check và throw WebException
            boolean deleted = customerService.deleteCustomer(customerId);

            if (deleted) {
                request.setAttribute("message", MessageUtil.getError("error.user.delete.success"));
            } else {
                throw new BusinessException("error.user.not.found");
            }

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.customer.management"));
        }

        // Quay lại danh sách sau khi xóa
        request.getRequestDispatcher("managecus").forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("detail".equals(action)) {
            showDetailCustomer(request, response);
        } else if ("edit".equals(action)) {
            showEditForm(request, response);
        } else if ("update".equals(action)) {
            editCustomer(request, response);
        } else if ("delete".equals(action)) {
            deleteCustomer(request, response);
        } else {
            response.sendRedirect("managecus");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}