package controller.payment;

import dao.impl.ContractsDAOImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/paymentServlet")
public class PaymentServlet extends HttpServlet {
    private final ContractsDAOImpl dao = new ContractsDAOImpl();
    private static final String BANK_ID = "MB";
    private static final String ACCOUNT_NO = "0862671682";
    private static final String ACCOUNT_NAME = "NGUYEN THI VAN";
    private static final String TEMPLATE = "compact";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, jakarta.servlet.ServletException {
        String idStr = req.getParameter("contractId");
        if (idStr == null || idStr.isEmpty()) {
            req.setAttribute("errorMessage", "Không tìm thấy contractId.");
            req.getRequestDispatcher("/incident.jsp").forward(req, resp);
            return;
        }
        try {
            int contractId = Integer.parseInt(idStr);
            BigDecimal total = dao.getTotalAmount(contractId);
            if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
                req.setAttribute("errorMessage", "Không tìm thấy tổng tiền cho contractId: " + contractId);
                req.getRequestDispatcher("/incident.jsp").forward(req, resp);
                return;
            }
            double amount = total.doubleValue();
            String qr = "https://img.vietqr.io/image/" + BANK_ID + "-" + ACCOUNT_NO + "-" + TEMPLATE + ".png"
                    + "?amount=" + amount
                    + "&addInfo=Thanh toan hop dong " + contractId
                    + "&accountName=" + ACCOUNT_NAME;

            req.setAttribute("contractId", contractId);
            req.setAttribute("totalAmount", amount);
            req.setAttribute("qrUrl", qr);
            req.getRequestDispatcher("/payment.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            req.getRequestDispatcher("/incident.jsp").forward(req, resp);
        }
    }
}