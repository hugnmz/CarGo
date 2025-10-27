package controller.contract;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import dto.ContractDTO;
import service.ContractService;
import util.di.DIContainer;
import util.AuthUtil;


@WebServlet(name = "ListMyContractsServlet", urlPatterns = {"/my-contracts"})
public class ListMyContractsServlet extends HttpServlet {

    // service xu ly hop dong
    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao contract service tu di container
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            // nem loi neu khoi tao service that bai
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // kiem tra dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }
        // lay customer id tu session
        Integer customerId = AuthUtil.getCustomerId(request);
        
        // lay danh sach hop dong cua khach hang tu database
        List<ContractDTO> contracts = contractService.getContractsByCustomer(customerId);

        // dat danh sach hop dong vao request de truyen sang jsp
        request.setAttribute("contracts", contracts);
        // chuyen huong den trang danh sach hop dong cua khach hang
        request.getRequestDispatcher("/customer/my-contracts.jsp").forward(request, response);
    }
}
