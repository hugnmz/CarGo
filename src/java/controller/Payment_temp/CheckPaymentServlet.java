package controller.payment;

import dao.impl.PaymentsDAOImpl;
import dao.impl.ContractsDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/checkPayment")
public class CheckPaymentServlet extends HttpServlet {

    private final PaymentsDAOImpl paymentsDAO = new PaymentsDAOImpl();
    private final ContractsDAOImpl contractsDAO = new ContractsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin contractId từ param
        String contractIdParam = request.getParameter("contractId");
        int contractId = -1;
        try {
            contractId = Integer.parseInt(contractIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "ContractId không hợp lệ");
            request.getRequestDispatcher("/paymentPage.jsp").forward(request, response);
            return;
        }

        // Tìm payment đang PENDING
        var pendingPayment = paymentsDAO.findPendingPayment(contractId, null); // null = kiểm tra tất cả số tiền
        if (pendingPayment != null && !paymentsDAO.isPaymentCompleted(pendingPayment.getPaymentId())) {
            // Cập nhật payment
            boolean updatedPayment = paymentsDAO.updatePaymentStatus(pendingPayment.getPaymentId(), "COMPLETED");
            boolean updatedContract = contractsDAO.updateContractStatus(contractId, "ACCEPTED");

            request.setAttribute("message", "Payment đã cập nhật thành công cho hợp đồng " + contractId);
        } else {
            request.setAttribute("message", "Không tìm thấy payment đang PENDING hoặc đã hoàn tất cho hợp đồng " + contractId);
        }

        // Forward về JSP hiển thị kết quả
        request.getRequestDispatcher("/paymentPage.jsp").forward(request, response);
    }
}
