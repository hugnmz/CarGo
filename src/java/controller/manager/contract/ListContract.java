/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.contract;

import dto.ContractDTO;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import service.ContractService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;

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
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
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
            
            // Validate contractId
            if (idStr == null || idStr.trim().isEmpty()) {
                request.getSession().setAttribute("flash_error", MessageUtil.getError("error.validation.contract.id.missing"));
                response.sendRedirect(request.getContextPath() + "/listcontract");
                return;
            }
            
            Integer contractId;
            try {
                contractId = Integer.parseInt(idStr.trim());
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("flash_error", MessageUtil.getError("error.validation.contract.id.invalid"));
                response.sendRedirect(request.getContextPath() + "/listcontract");
                return;
            }
            
            // Validate contractId > 0
            if (contractId <= 0) {
                request.getSession().setAttribute("flash_error", MessageUtil.getError("error.validation.contract.id.invalid"));
                response.sendRedirect(request.getContextPath() + "/listcontract");
                return;
            }
            
            // Gọi service xóa hợp đồng - service sẽ check và throw exception nếu có lỗi
            boolean deleted = contractService.deleteContract(contractId);
            if (deleted) {
                request.getSession().setAttribute("flash_message", MessageUtil.getMessage("message.contract.delete.success"));
            } else {
                request.getSession().setAttribute("flash_error", MessageUtil.getError("error.contract.not.found"));
            }
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getErrorFromException(e));
        } catch (Exception e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getError("error.system.contract.management"));
        }

        response.sendRedirect(request.getContextPath() + "/listcontract");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}