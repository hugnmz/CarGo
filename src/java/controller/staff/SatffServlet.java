package controller.staff;

import dto.ContractDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ContractService;
import service.UserService;
import util.MessageUtil;
import util.di.DIContainer;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import util.exception.ValidationException;

import java.io.IOException;
import java.util.List;

@WebServlet("/staff")
public class SatffServlet extends HttpServlet {

    private ContractService contractService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            contractService = DIContainer.get(ContractService.class);
            userService = DIContainer.get(UserService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer staffId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        try {
            String action = request.getParameter("action");

            // Trang hồ sơ Staff
            if ("manage".equals(action) || "profile".equals(action)) {
                if (staffId == null) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
                UserDTO user = userService.getUserById(staffId);
                request.setAttribute("user", user);
                request.setAttribute("locations", userService.getAllLocation());
                request.getRequestDispatcher("/manager/staffmanage.jsp").forward(request, response);
                return;
            }

            // Trang dashboard Staff (danh sách hợp đồng)
            if (staffId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            List<ContractDTO> contracts = contractService.getContractsByStaff(staffId);
            request.setAttribute("contracts", contracts);
            request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.setAttribute("error", MessageUtil.getErrorFromException(e));
            request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", MessageUtil.getError("error.system.staff"));
            request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);
        }
    }
}