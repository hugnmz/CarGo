package controller.payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import service.PaymentService;
import service.ContractService;
import util.di.DIContainer;
import java.util.Optional;
import dto.ContractDTO;

@WebServlet("/paymentServlet")
public class PaymentServlet extends HttpServlet {

    private PaymentService paymentService;
    private ContractService contractService;

    // Cấu hình QR
    private static final String BANK_ID = "MB";
    private static final String ACCOUNT_NO = "0862671682";
    private static final String ACCOUNT_NAME = "NGUYEN THI VAN";
    private static final String TEMPLATE = "compact2";

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            paymentService = DIContainer.get(PaymentService.class);
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new ServletException(util.MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

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

            // Lấy thông tin contract để check status
            Optional<ContractDTO> contractOpt = contractService.getContractById(contractId);
            if (!contractOpt.isPresent()) {
                req.setAttribute("error", "Không tìm thấy hợp đồng");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            ContractDTO contract = contractOpt.get();
            BigDecimal amountToPay;
            String description;

            // Nếu ACCEPTED và chưa có payment completed -> đặt cọc
            if ("ACCEPTED".equalsIgnoreCase(contract.getStatus()) && !paymentService.hasCompleted(contractId)) {
                amountToPay = contract.getDepositAmount();
                description = "Dat coc hop dong " + contractId;
            } else {
                // Ngược lại -> thanh toán toàn bộ (giữ logic cũ)
                Optional<BigDecimal> totalOpt = paymentService.getContractTotalAmount(contractId);
                if (!totalOpt.isPresent()) {
                    req.setAttribute("error", "Không tìm thấy hợp đồng hoặc số tiền không hợp lệ");
                    req.getRequestDispatcher("/error.jsp").forward(req, resp);
                    return;
                }
                amountToPay = totalOpt.get();
                description = "Thanh toan hop dong " + contractId;
            }

            if (amountToPay == null || amountToPay.compareTo(BigDecimal.ZERO) <= 0) {
                req.setAttribute("error", "Số tiền thanh toán không hợp lệ");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            // CHỈ ĐỌC TRẠNG THÁI THANH TOÁN
            boolean completed = paymentService.hasCompleted(contractId);
            System.out.println("[PaymentServlet] contractId=" + contractId + ", completed=" + completed);

            req.setAttribute("contractId", contractId);
            req.setAttribute("totalAmount", amountToPay.intValue());

            if (completed) {
                // ĐÃ THANH TOÁN → không render QR
                req.setAttribute("initialStatus", "SUCCESS");
                req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);
                return;
            }
            
            // Tạo pending payment nếu chưa có (để callback có thể match)
            Optional<dto.PaymentDTO> existingPending = paymentService.findPendingPayment(contractId, amountToPay);
            if (!existingPending.isPresent()) {
                paymentService.createPendingPayment(contractId, amountToPay);
            }
            
            // CHƯA THANH TOÁN (PENDING/NONE) → render QR + bật polling
            String qr = "https://img.vietqr.io/image/" + BANK_ID + "-" + ACCOUNT_NO + "-" + TEMPLATE + ".jpg"
                    + "?amount=" + amountToPay.intValue()
                    + "&addInfo=" + java.net.URLEncoder.encode(description, "UTF-8")
                    + "&accountName=" + java.net.URLEncoder.encode(ACCOUNT_NAME, "UTF-8");

            req.setAttribute("qrUrl", qr);
            req.setAttribute("initialStatus", "PENDING");
            req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);

        } catch (util.exception.ValidationException | util.exception.BusinessException | util.exception.DataAccessException e) {
            req.setAttribute("error", util.MessageUtil.getErrorFromException(e));
            req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);
        } catch (Exception ex) {
            req.setAttribute("error", util.MessageUtil.getError("error.system.payment.processing"));
            req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);
        }
    }
}
