package controller.staff;

import dto.ContractDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ContractService;
import util.di.DIContainer;
import java.io.IOException;
import java.util.List;

@WebServlet("/staff")
public class SatffServlet extends HttpServlet {

    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new ServletException(util.MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer staffId = (Integer) session.getAttribute("userId");

        try {
            // Hiển thị tất cả hợp đồng cho dashboard staff
            List<ContractDTO> contracts = contractService.getContractsByStaff(staffId);
            request.setAttribute("contracts", contracts);
            request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);

        } catch (util.exception.ValidationException | util.exception.BusinessException | util.exception.DataAccessException e) {
            request.setAttribute("error", util.MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", util.MessageUtil.getError("error.system.staff"));
            request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);
        }
    }
}
