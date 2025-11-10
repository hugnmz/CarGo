package controller.deposit;

import dto.ContractDTO;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import service.ContractService;
import util.AuthUtil;
import util.MessageUtil;
import util.di.DIContainer;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;

@WebServlet(name = "Deposit", urlPatterns = {"/deposit"})
public class DepositServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Staff xem danh sách yêu cầu đặt cọc
        try {
            List<ContractDTO> requests = contractService.getContractsWithDepositRequest();
            req.setAttribute("requests", requests);
            req.getRequestDispatcher("/staff/deposit.jsp").forward(req, resp);
        } catch (Exception e) {
            req.getSession().setAttribute("flash", "Lỗi khi lấy danh sách yêu cầu đặt cọc");
            resp.sendRedirect(req.getContextPath() + "/staff");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Customer gửi yêu cầu đặt cọc
        if (!AuthUtil.requireLogin(req, resp)) {
            return;
        }
        
        try {
            String contractIdParam = req.getParameter("contractId");
            int id = (contractIdParam != null && !contractIdParam.trim().isEmpty()) 
                ? Integer.parseInt(contractIdParam.trim()) : 0;

            boolean success = contractService.requestDeposit(id);
            if (success) {
                req.getSession().setAttribute("message", "Gửi yêu cầu đặt cọc thành công");
            } else {
                req.getSession().setAttribute("error", "Gửi yêu cầu đặt cọc thất bại");
            }

            resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + id);
        } catch (ValidationException | BusinessException | DataAccessException e) {
            req.getSession().setAttribute("error", MessageUtil.getErrorFromException(e));
            resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + req.getParameter("contractId"));
        } catch (Exception e) {
req.getSession().setAttribute("error", MessageUtil.getError("error.system.deposit"));
            resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + req.getParameter("contractId"));
        }
    }
}
