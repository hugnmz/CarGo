package controller.contract;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import dto.ContractDTO;
import dto.ContractDetailDTO;
import service.ContractService;
import util.di.DIContainer;
import util.AuthUtil;

@WebServlet(name = "ViewContractServlet", urlPatterns = {"/view-contract"})
public class ViewContractServlet extends HttpServlet {

    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }

        String idStr = request.getParameter("contractId");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Integer contractId;
        try {
            contractId = Integer.valueOf(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Optional<ContractDTO> contractOpt = contractService.getContractById(contractId);
        
        if (!contractOpt.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        ContractDTO contract = contractOpt.get();

        Integer customerId = AuthUtil.getCustomerId(request);
        if (!customerId.equals(contract.getCustomerId())) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        List<ContractDetailDTO> details = contractService.getContractDetails(contractId);

        request.setAttribute("contract", contract);
        request.setAttribute("details", details);
        request.getRequestDispatcher("/customer/contract-view.jsp").forward(request, response);
    }
}
