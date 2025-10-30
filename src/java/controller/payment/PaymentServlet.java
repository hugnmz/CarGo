package controller.payment;



import dao.impl.PaymentsDAOImpl;
import dao.impl.ContractsDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Payments;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet("/paymentServlet")
public class PaymentServlet extends HttpServlet {

    private final ContractsDAOImpl contractsDAO = new ContractsDAOImpl();
    private final PaymentsDAOImpl paymentsDAO = new PaymentsDAOImpl();

    // Cấu hình QR mặc định (có thể đưa vào file config sau này)
    private static final String BANK_ID = "MB";
    private static final String ACCOUNT_NO = "0862671682";
    private static final String ACCOUNT_NAME = "NGUYEN THI VAN";
    private static final String TEMPLATE = "compact2";
    private static final int DEFAULT_METHOD_ID = 1;

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
            BigDecimal total = contractsDAO.getTotalAmount(contractId);

            if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
                req.setAttribute("error", "Không tìm thấy hợp đồng hoặc số tiền không hợp lệ");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            // Kiểm tra đã có payment PENDING chưa
            Payments existing = paymentsDAO.findPendingPayment(contractId, total);
            if (existing == null) {
                Payments payment = new Payments();
                payment.setContractId(contractId);
                payment.setAmount(total);
                payment.setMethodId(DEFAULT_METHOD_ID);
                payment.setStatus("PENDING");
                payment.setPaymentDate(LocalDateTime.now());
                paymentsDAO.addPayment(payment);
            }

            // Tạo QR
            String qr = "https://img.vietqr.io/image/" + BANK_ID + "-" + ACCOUNT_NO + "-" + TEMPLATE + ".jpg"
                    + "?amount=" + total.intValue()
                    + "&addInfo=" + java.net.URLEncoder.encode("Thanh toan hop dong " + contractId, "UTF-8")
                    + "&accountName=" + java.net.URLEncoder.encode(ACCOUNT_NAME, "UTF-8");

            req.setAttribute("contractId", contractId);
            req.setAttribute("totalAmount", total.intValue());
            req.setAttribute("qrUrl", qr);
            req.getRequestDispatcher("/payment.jsp").forward(req, resp);

        } catch (Exception ex) {
            req.setAttribute("error", "Lỗi hệ thống khi xử lý thanh toán.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}

