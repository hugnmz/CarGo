package controller.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import service.ContractService;
import dto.ContractDTO;
import util.AuthUtil;
import util.MessageUtil;
import util.di.DIContainer;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

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
        super.doGet(request, response); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

        request.getRequestDispatcher("/ViewCartDetail").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // kiem tra dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }

        // lay customer id tu session
        Integer customerId = AuthUtil.getCustomerId(request);

        // lay danh sach id cac don hang duoc chon tu form
        String[] selectedIds = request.getParameterValues("selectedIds");
        Integer[] selectedOrderIds = null;

        // kiem tra neu co don hang duoc chon
        if (selectedIds != null && selectedIds.length > 0) {
            // tao mang integer tu mang string
            selectedOrderIds = new Integer[selectedIds.length];
            for (int i = 0; i < selectedIds.length; i++) {
                try {
                    // chuyen doi string sang integer
                    selectedOrderIds[i] = Integer.valueOf(selectedIds[i]);
                } catch (NumberFormatException e) {
                    // bo qua id khong hop le
                }
            }
        }

        try {
            // tao hop dong tu gio hang voi cac don hang duoc chon
            List<ContractDTO> createdContracts = contractService.createContractsFromCart(customerId, selectedOrderIds);

            if (createdContracts.isEmpty()) {
                // neu khong co hop dong nao duoc tao thi chuyen ve trang gio hang
                response.sendRedirect(request.getContextPath() + "/ViewCartDetail");
                return;
            }

            // dat danh sach hop dong da tao vao request de truyen sang jsp
            request.setAttribute("created", createdContracts);
            // chuyen huong den trang hien thi ket qua thanh toan
            request.getRequestDispatcher("/customer/checkout-result.jsp").forward(request, response);

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getErrorFromException(e));
            response.sendRedirect(request.getContextPath() + "/ViewCartDetail");
        } catch (Exception e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getError("error.system.checkout"));
            response.sendRedirect(request.getContextPath() + "/ViewCartDetail");
        }
    }
}
