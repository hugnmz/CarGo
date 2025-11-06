/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.contract;

import dto.ContractDTO;
import dto.ContractDetailDTO;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import service.ContractService;
import util.di.DIContainer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ContractDetail", urlPatterns = {"/contractdetail"})
public class ContractDetail extends HttpServlet {

    private ContractService contractService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ContractService", e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contractIdStr = request.getParameter("contractId");
        if (contractIdStr != null) {
            try {
                Integer contractId = Integer.parseInt(contractIdStr);

                Optional<ContractDTO> contract = contractService.getContractById(contractId);
                if (contract.isPresent()) {
                    request.setAttribute("contract", contract.get());
                    List<ContractDetailDTO> details = contractService.getContractDetails(contractId);
                    request.setAttribute("details", details);
                    request.getRequestDispatcher("/manager/contract_detail.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Không tìm thấy hợp đồng");
                    request.getRequestDispatcher("/listcontract").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Mã hợp đồng không hợp lệ :" + e.getMessage());
                request.getRequestDispatcher("/listcontract").forward(request, response);

            }
        } else {
            request.setAttribute("error", "Mã hợp đồng không hợp lệ ");
            request.getRequestDispatcher("/listcontract").forward(request, response);

        }
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