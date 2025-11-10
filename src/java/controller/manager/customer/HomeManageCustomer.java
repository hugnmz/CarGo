
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.customer;

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
import java.util.List;
import util.di.DIContainer;
import service.UserService;
import service.CustomersService;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ControllerInfoCustomer", urlPatterns = {"/managecus"})
public class HomeManageCustomer extends HttpServlet {

    private CustomersService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            customerService = DIContainer.get(CustomersService.class);
        } catch (Exception e) {
            throw new RuntimeException("Dependency injection error", e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Kiểm tra quyền
        HttpSession session = request.getSession(false);
        if (session == null || !"MANAGER".equals(session.getAttribute("roleName"))) {
            response.sendRedirect("LoginServlet");
            return;
        }
        try {
            List<CustomerDTO> customer = customerService.getAllCustomers();
            List<LocationDTO> location = customerService.getAllLocation();
            request.setAttribute("allCustomers", customer);
            request.setAttribute("allLocations", location);
            request.getRequestDispatcher("manager/manage_customers.jsp").forward(request, response);
        } catch (Exception e) {
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
