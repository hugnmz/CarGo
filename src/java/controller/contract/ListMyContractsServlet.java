package controller.contract;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import dto.ContractDTO;
import service.ContractService;
import util.di.DIContainer;
import util.AuthUtil;

@WebServlet(name = "ListMyContractsServlet", urlPatterns = {"/my-contracts"})
public class ListMyContractsServlet extends HttpServlet {

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }
        Integer customerId = AuthUtil.getCustomerId(request);
        
        // Sử dụng service thay vì DAO trực tiếp
        List<ContractDTO> contracts = contractService.getContractsByCustomer(customerId);

        request.setAttribute("contracts", contracts);
        request.getRequestDispatcher("/customer/my-contracts.jsp").forward(request, response);
    }
}
