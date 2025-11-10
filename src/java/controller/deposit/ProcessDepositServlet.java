package controller.deposit;

import constant.ConstractStatus;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import service.ContractService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;

@WebServlet(name = "ProcessDeposit", urlPatterns = {"/processdeposit"})
public class ProcessDepositServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/deposit");
            return;
        }

        try {
            Integer contractId = Integer.valueOf(req.getParameter("contractId"));
            String action = req.getParameter("action"); // "approve" hoặc "reject"

            if ("approve".equals(action)) {
                // Duyệt: update status thành DEPOSIT_PAID và xóa yêu cầu
                contractService.updateContractStatus(contractId, ConstractStatus.DEPOSIT_PAID.name(), null);
                contractService.cancelDepositRequest(contractId);

                session.setAttribute("flash", "Đã duyệt đặt cọc cho hợp đồng #" + contractId);
            } else if ("reject".equals(action)) {
                // Từ chối: chỉ xóa yêu cầu, status vẫn là ACCEPTED
                contractService.cancelDepositRequest(contractId);

                session.setAttribute("flash", "Đã từ chối yêu cầu đặt cọc cho hợp đồng #" + contractId);
            } else {
                session.setAttribute("flash", "Hành động không hợp lệ.");
            }

            resp.sendRedirect(req.getContextPath() + "/deposit");
        } catch (ValidationException | BusinessException | DataAccessException e) {
            session.setAttribute("flash", MessageUtil.getErrorFromException(e));
            resp.sendRedirect(req.getContextPath() + "/deposit");
        } catch (Exception ex) {
            session.setAttribute("flash", MessageUtil.getError("error.system.deposit"));
            resp.sendRedirect(req.getContextPath() + "/deposit");
        }
    }
}
