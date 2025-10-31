package controller.payment;

import dao.impl.PaymentsDAOImpl;
import dao.impl.ContractsDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/paymentServlet")
public class PaymentServlet extends HttpServlet {

    private final ContractsDAOImpl contractsDAO = new ContractsDAOImpl();
    private final PaymentsDAOImpl paymentsDAO = new PaymentsDAOImpl();

    // Cấu hình QR
    private static final String BANK_ID = "MB";
    private static final String ACCOUNT_NO = "0862671682";
    private static final String ACCOUNT_NAME = "NGUYEN THI VAN";
    private static final String TEMPLATE = "compact2";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("contractId");
        if (idStr == null || idStr.isEmpty()) {
            req.setAttribute("error", "Thiếu contractId");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        try {
            int contractId = Integer.parseInt(idStr);

            // Đọc tổng tiền để render (không ghi DB)
            BigDecimal total = contractsDAO.getTotalAmount(contractId);
            if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
                req.setAttribute("error", "Không tìm thấy hợp đồng hoặc số tiền không hợp lệ");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            // CHỈ ĐỌC TRẠNG THÁI THANH TOÁN
            boolean completed = paymentsDAO.hasCompleted(contractId);
            System.out.println("[PaymentServlet] contractId=" + contractId + ", completed=" + completed);

            req.setAttribute("contractId", contractId);
            req.setAttribute("totalAmount", total.intValue());

            if (completed) {
                // ĐÃ THANH TOÁN → không render QR
                req.setAttribute("initialStatus", "SUCCESS");
                req.getRequestDispatcher("/payment.jsp").forward(req, resp);
                return;
            }

            // CHƯA THANH TOÁN (PENDING/NONE) → render QR + bật polling
            String qr = "https://img.vietqr.io/image/" + BANK_ID + "-" + ACCOUNT_NO + "-" + TEMPLATE + ".jpg"
                    + "?amount=" + total.intValue()
                    + "&addInfo=" + java.net.URLEncoder.encode("Thanh toan hop dong " + contractId, "UTF-8")
                    + "&accountName=" + java.net.URLEncoder.encode(ACCOUNT_NAME, "UTF-8");

            req.setAttribute("qrUrl", qr);
            req.setAttribute("initialStatus", "PENDING"); // chỉ hiển thị, KHÔNG tạo bản ghi mới
            req.getRequestDispatcher("/payment.jsp").forward(req, resp);

        } catch (Exception ex) {
            req.setAttribute("error", "Lỗi hệ thống khi xử lý thanh toán.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}