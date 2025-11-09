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
import util.MessageUtil;
import util.exception.*;

@WebServlet(name = "ViewContractServlet", urlPatterns = {"/view-contract"})
public class ViewContractServlet extends HttpServlet {

    // service xu ly hop dong
    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao contract service tu di container
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // kiem tra dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }

        try {
            // lay contract id tu tham so request
            String idStr = request.getParameter("contractId");
            
            // chuyen doi contract id tu string sang integer
            Integer contractId = (idStr != null) ? Integer.valueOf(idStr) : null;

            // lay thong tin hop dong tu database
            Optional<ContractDTO> contractOpt = contractService.getContractById(contractId);
            
            if (contractOpt.isPresent()) {
                
                // lay doi tuong hop dong
                ContractDTO contract = contractOpt.get();

                // lay danh sach chi tiet hop dong tu database
                List<ContractDetailDTO> details = contractService.getContractDetails(contractId);

                // dat thong tin hop dong vao request de truyen sang jsp
                request.setAttribute("contract", contract);
                // dat danh sach chi tiet hop dong vao request de truyen sang jsp
                request.setAttribute("details", details);
            }
        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.getSession().setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/customer/contract-view.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            request.getSession().setAttribute("error", MessageUtil.getError("error.system.contract.view"));
            request.getRequestDispatcher("/customer/contract-view.jsp").forward(request, response);
            return;
        }
        
        // chuyen huong den trang xem chi tiet hop dong
        request.getRequestDispatcher("/customer/contract-view.jsp").forward(request, response);
    }
}
