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

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ContractService", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra đăng nhập
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }
        
        Integer customerId = AuthUtil.getCustomerId(request);
        
        // Lấy danh sách orders được chọn
        String[] selectedIds = request.getParameterValues("selectedIds");
        Integer[] selectedOrderIds = null;
        
        if (selectedIds != null && selectedIds.length > 0) {
            selectedOrderIds = new Integer[selectedIds.length];
            for (int i = 0; i < selectedIds.length; i++) {
                try {
                    selectedOrderIds[i] = Integer.valueOf(selectedIds[i]);
                } catch (NumberFormatException e) {
                    // Bỏ qua ID không hợp lệ
                }
            }
        }
        
        try {
            // Tạo hợp đồng từ giỏ hàng
            List<ContractDTO> createdContracts = contractService.createContractsFromCart(customerId, selectedOrderIds);
            
            if (createdContracts.isEmpty()) {
                // Không có gì để checkout, redirect về giỏ hàng
                response.sendRedirect(request.getContextPath() + "/ViewCartDetail");
                return;
            }
            
            // Truyền kết quả xuống JSP
            request.setAttribute("created", createdContracts);
            request.getRequestDispatcher("/customer/checkout-result.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", MessageUtil.getError("error.checkout.failed") + ": " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ViewCartDetail");
        }
    }
}
