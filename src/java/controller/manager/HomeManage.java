/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ContractService;
import service.VehicleService;
import util.di.DIContainer;
import service.UserService;
import service.CustomersService;

/**
 *
 * @author DELL
 */
@WebServlet(name = "HomeManage", urlPatterns = {"/homemange"})
public class HomeManage extends HttpServlet {

    private VehicleService vehiclesService;
    private CustomersService customerService;
    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        try {
            vehiclesService = DIContainer.get(VehicleService.class);
            customerService = DIContainer.get(CustomersService.class);
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Kiểm tra quyền
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null
                || !"MANAGER".equals(session.getAttribute("roleName"))) {
            response.sendRedirect("LoginServlet");
            return;
        }

        int totalVehicles = vehiclesService.countVehical();
        int totalCustomers = customerService.countCustomer();
        int totalContracts = contractService.countContract();

        request.setAttribute("totalCars", totalVehicles);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("totalContracts", totalContracts);

        request.getRequestDispatcher("manager/manager_home.jsp").forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
