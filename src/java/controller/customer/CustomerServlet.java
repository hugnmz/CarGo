/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer;

import dto.ContractDTO;
import dto.CustomerDTO;
import dto.LocationDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import service.ContractService;
import service.LocationService;
import util.di.DIContainer;
import util.AuthUtil;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import service.CustomersService;

@WebServlet(name = "CustomerServlet", urlPatterns = {"/CustomerServlet"})
public class CustomerServlet extends HttpServlet {

    // service xu ly thong tin khach hang
    private CustomersService customerService;
    // service xu ly hop dong
    private ContractService contractService;

    private LocationService locationService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao customer service tu di container
            customerService = DIContainer.get(CustomersService.class);
            // khoi tao contract service tu di container
            contractService = DIContainer.get(ContractService.class);

            locationService = DIContainer.get(LocationService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // kiem tra dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }

        // lay customer id tu session
        Integer customerId = AuthUtil.getCustomerId(request);

        try {
            // lay thong tin khach hang tu database bang customer id
            Optional<CustomerDTO> customerOpt = customerService.getCustomerById(customerId);

            CustomerDTO customer = customerOpt.get();

            //lay danh sach locations
            List<LocationDTO> locations = locationService.getAllLocations();
            request.setAttribute("locations", locations);

            // lay danh sach hop dong cua khach hang tu database
            List<ContractDTO> listContract = contractService.getContractsByCustomer(customer.getCustomerId());
            // dat danh sach hop dong vao request de truyen sang jsp
            request.setAttribute("listContract", listContract);

            // khong luu listContract vao session, chi load tu database khi can
            // chuyen huong den trang profile.jsp
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.setAttribute("listContract", new ArrayList<>());
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.customer"));
            request.setAttribute("listContract", new ArrayList<>());
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // kiem tra dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }

        // kiem tra neu co thong bao thanh cong hoac loi tu truoc do
        if (request.getAttribute("ok") != null || request.getAttribute("errorMess") != null) {
            // neu co thi chuyen den doGet de hien thi lai trang
            doGet(request, response);
            return;
        }

        // tao danh sach de luu cac loi
        List<String> errors = new ArrayList<>();
        try {
            // lay cac tham so tu form cap nhat thong tin
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String city = request.getParameter("city");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            String customerIdStr = request.getParameter("customerId");
            String username = request.getParameter("username");

            // tao doi tuong customer dto de cap nhat
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(Integer.valueOf(customerIdStr));
            customerDTO.setUsername(username);
            customerDTO.setFullName(fullName);
            customerDTO.setEmail(email);
            customerDTO.setPhone(phone);
            customerDTO.setCity(city);

            // Xóa tất cả check - đã chuyển vào service
            // Set ngay sinh neu co
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                customerDTO.setDateOfBirth(LocalDate.parse(dateOfBirthStr));
            }

            // goi service de cap nhat thong tin khach hang - service sẽ check trùng và throw WebException
            boolean success = customerService.updateCustomer(customerDTO);

            if (success) {
                // lay customer updated tu database
                Integer customerId = AuthUtil.getCustomerId(request);
                Optional<CustomerDTO> updatedOpt = customerService.getCustomerById(customerId);
                
                if (updatedOpt.isPresent()) {
                    // cap nhat lai customer object trong session
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.setAttribute("c", updatedOpt.get());
                    }
                }

                // chuyen huong den trang profile voi thong bao thanh cong
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?success=1");
            } else {
                // neu cap nhat that bai thi them loi vao danh sach
                errors.add(MessageUtil.getError("error.customer.update.failed"));
                request.setAttribute("errors", errors);
                // hien thi lai trang profile voi thong bao loi
                request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            }

        } catch (ValidationException | BusinessException | DataAccessException e) {
            errors.add(MessageUtil.getErrorFromException(e));
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
        } catch (Exception e) {
            errors.add(MessageUtil.getError("error.system.customer"));
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
        }
    }

}
