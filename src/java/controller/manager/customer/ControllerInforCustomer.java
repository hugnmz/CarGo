/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.customer;

import dto.ContractDTO;
import dto.CustomerDTO;
import dto.LocationDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import service.ContractService;
import util.di.DIContainer;
import service.CusService;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ControllerInforCustomer", urlPatterns = {"/controllerinformationcustomer"})
public class ControllerInforCustomer extends HttpServlet {

    private CusService customerService;
    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CusService.class);
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDetailCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("customerId");
            if (idStr == null) {
                response.sendRedirect("managecus");
                return;
            }
            Integer customerId = Integer.parseInt(idStr);

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

        } catch (Exception e) {
            e.printStackTrace();
            // Vẫn forward đến profile, sử dụng session data
            request.getRequestDispatcher("/manager/manage_detail_cus.jsp").forward(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("customerId");
            if (idStr == null) {
                response.sendRedirect("managecus");
                return;
            }
            Integer customerId = Integer.parseInt(idStr);

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

        } catch (Exception e) {
            e.printStackTrace();
            // Vẫn forward đến profile, sử dụng session data
            request.getRequestDispatcher("/manager/editcustomer.jsp").forward(request, response);
        }
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idStr = request.getParameter("customerId");
            if (idStr == null) {
                throw new IllegalArgumentException("Không có customerId trong request!");
            }
            Integer customerId = Integer.parseInt(idStr);

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

            // Gọi service
            customerService.updateCustomer(customerDTO);

            request.setAttribute("message", "Cập nhật khách hàng thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            // Đẩy lỗi ra
            request.setAttribute("error", "Thêm thất bại: " + e.getMessage());
        }
        // Forward về trang quản lý để hiển thị thông báo
        request.getRequestDispatcher("managecus").forward(request, response);
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("customerId");
            if (idStr == null || idStr.isEmpty()) {
                throw new IllegalArgumentException("Không có ID khách hàng cần xóa!");
            }

            int customerId = Integer.parseInt(idStr);

            // Gọi service để xóa
            boolean deleted = customerService.deleteCustomer(customerId);

            if (deleted) {
                request.setAttribute("message", "Xóa khách hàng thành công!");
            } else {
                request.setAttribute("error", "Không tìm thấy khách hàng để xóa!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Xóa thất bại: " + e.getMessage());
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
