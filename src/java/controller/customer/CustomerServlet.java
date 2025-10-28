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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import service.ContractService;
import service.CustomerService;
import service.LocationService;
import util.di.DIContainer;

@WebServlet(name = "CustomerServlet", urlPatterns = {"/CustomerServlet"})
public class CustomerServlet extends HttpServlet {

    // service xu ly thong tin khach hang
    private CustomerService customerService;
    // service xu ly hop dong
    private ContractService contractService;

    private LocationService locationService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao customer service tu di container
            customerService = DIContainer.get(CustomerService.class);
            // khoi tao contract service tu di container
            contractService = DIContainer.get(ContractService.class);

            locationService = DIContainer.get(LocationService.class);
        } catch (Exception e) {
            // log loi khi khoi tao service
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // lay session hien tai, neu khong co thi null
        HttpSession session = request.getSession(false);
        if (session == null) {
            // neu khong co session thi chuyen den trang dang nhap
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        // lay customer id tu session
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            // neu khong co customer id thi chuyen den trang dang nhap
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        try {
            // lay thong tin khach hang tu database bang customer id
            Optional<CustomerDTO> customerOpt = customerService.getCustomerById(customerId);

            if (customerOpt.isPresent()) {
                // neu tim thay khach hang thi lay thong tin
                CustomerDTO customer = customerOpt.get();
                // dat thong tin khach hang vao request de truyen sang jsp
                request.setAttribute("customer", customer);
                // khong luu customer object vao session de tiet kiem bo nho
            }

            //lay danh sach locations
            List<LocationDTO> locations = locationService.getAllLocations();
            request.setAttribute("locations", locations);

            // lay danh sach hop dong cua khach hang tu database
            List<ContractDTO> listContract = contractService.getContractsByCustomer(customerId);
            // dat danh sach hop dong vao request de truyen sang jsp
            request.setAttribute("listContract", listContract);

            // khong luu listContract vao session, chi load tu database khi can
            // chuyen huong den trang profile.jsp
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);

        } catch (Exception e) {
            // log loi chi tiet ra console
            System.err.println("Error in CustomerServlet: " + e.getMessage());
            e.printStackTrace();

            // dat thong bao loi cho nguoi dung
            request.setAttribute("error", "Có lỗi xảy ra khi tải thông tin. Vui lòng thử lại sau.");
            // dat danh sach hop dong rong neu co loi
            request.setAttribute("listContract", new ArrayList<>());
            // van chuyen den trang profile de hien thi thong bao loi
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
            String isVerifiedStr = request.getParameter("isVerified");

            // tao doi tuong customer dto de cap nhat
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(Integer.valueOf(customerIdStr));
            customerDTO.setUsername(username);
            customerDTO.setFullName(fullName);
            customerDTO.setEmail(email);
            customerDTO.setPhone(phone);
            customerDTO.setCity(city);

            // kiem tra va set trang thai xac thuc neu co
            if (isVerifiedStr != null && !isVerifiedStr.isEmpty()) {
                customerDTO.setIsVerified(Boolean.valueOf(isVerifiedStr));
            }

            // kiem tra va set ngay sinh neu co
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                customerDTO.setDateOfBirth(java.time.LocalDate.parse(dateOfBirthStr));
            }

            // goi service de cap nhat thong tin khach hang
            boolean success = customerService.updateCustomer(customerDTO);

            if (success) {
                // neu cap nhat thanh cong thi cap nhat lai session
                HttpSession session = request.getSession();
                session.setAttribute("fullName", fullName);
                session.setAttribute("email", email);
                session.setAttribute("phone", phone);
                session.setAttribute("city", city);
                // cap nhat ngay sinh neu co
                if (dateOfBirthStr != null) {
                    session.setAttribute("dateOfBirth", dateOfBirthStr);
                }
                // chuyen huong den trang profile voi thong bao thanh cong
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?success=1");
            } else {
                // neu cap nhat that bai thi them loi vao danh sach
                errors.add("Có lỗi xảy ra khi cập nhật thông tin. Vui lòng thử lại.");
                request.setAttribute("errors", errors);
                // hien thi lai trang profile voi thong bao loi
                request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
            }

        } catch (Exception e) {
            // log loi ra console
            e.printStackTrace();
            // them loi vao danh sach
            errors.add("Có lỗi xảy ra khi xử lý yêu cầu. Vui lòng thử lại.");
            request.setAttribute("errors", errors);
            // hien thi lai trang profile voi thong bao loi
            request.getRequestDispatcher("/customer/profile.jsp").forward(request, response);
        }
    }

}
