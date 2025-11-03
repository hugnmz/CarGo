/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.contract;

import dto.ContractDTO;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import service.ContractService;
import util.di.DIContainer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ListContract", urlPatterns = {"/listcontract"})
public class ListContract extends HttpServlet {

    private ContractService contractService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //lay toan bo danh sach hop dong
        List<ContractDTO> allContracts = contractService.getAllContracts();

        //truyen sang jsp
        request.setAttribute("allContracts", allContracts);
        request.getRequestDispatcher("/manager/listContract.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("contractId");
            if (idStr == null || idStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Không có ID hợp đồng cần xóa!");
            }

            int contractId;
            try {
                contractId = Integer.parseInt(idStr.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID hợp đồng không hợp lệ!");
            }
            //gọi service xoá hợp đồng
            boolean deleted = contractService.deleteContract(contractId);
            if (deleted) {
                request.getSession().setAttribute("flash_message", "Xóa hợp đồng thành công!");
            } else {
                request.getSession().setAttribute("flash_error", "Không tìm thấy hợp đồng để xóa!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("flash_error", "Xoá không thành công: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/listcontract");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
