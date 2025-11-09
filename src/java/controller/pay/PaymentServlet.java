package controller.pay;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import service.ContractService;
import service.PaymentService;
import util.di.DIContainer;
import util.MessageUtil;
import dto.ContractDTO;
import dto.PaymentDTO;

import util.MessageUtil;

import java.nio.charset.StandardCharsets;

import java.nio.charset.StandardCharsets;

import java.nio.charset.StandardCharsets;

@WebServlet("/paymentServlet")
public class PaymentServlet extends HttpServlet {

    private PaymentService paymentService;
    private ContractService contractService;

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
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
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
            Optional<ContractDTO> contractOpt = contractService.getContractById(contractId);
            if (!contractOpt.isPresent()) {
                req.setAttribute("error", "Không tìm thấy hợp đồng");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            ContractDTO contract = contractOpt.get();

            String payType = null;
            BigDecimal amountToPay = null;
            String description = null;

            // === Xác định loại thanh toán ===
            if (contract.getStatus().equalsIgnoreCase("ACCEPTED") ) {
                payType = "deposit";
                amountToPay = contract.getDepositAmount();
                description = "deposit-contract-" + contractId;
            } else if ((contract.getStatus().equalsIgnoreCase("RETURNED") || contract.getStatus().equalsIgnoreCase("DEPOSIT_PAID"))
                    && !paymentService.hasCompleted(contractId)) {
                payType = "full";
                amountToPay = contract.getTotalAmount().subtract(contract.getDepositAmount());
                description = "fullpayment-contract-" + contractId;
            }
if (amountToPay == null || amountToPay.compareTo(BigDecimal.ZERO) <= 0) {
                req.setAttribute("error", "Không có số tiền thanh toán hợp lệ");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            // Tạo Payment nếu chưa có
            Optional<PaymentDTO> existingPending = paymentService.findPendingPayment(contractId, amountToPay);
            if (!existingPending.isPresent()) {
                paymentService.createPendingPayment(contractId, amountToPay);
            }

            // Tạo URL QR
            String qr = "https://img.vietqr.io/image/" + BANK_ID + "-" + ACCOUNT_NO + "-" + TEMPLATE + ".jpg"
                    + "?amount=" + amountToPay.intValue()
                    + "&addInfo=" + URLEncoder.encode(description, StandardCharsets.UTF_8)
                    + "&accountName=" + URLEncoder.encode(ACCOUNT_NAME, StandardCharsets.UTF_8);

            req.setAttribute("qrUrl", qr);
            req.setAttribute("contractId", contractId);
            req.setAttribute("totalAmount", amountToPay.intValue());
            req.setAttribute("initialStatus", "PENDING");
            req.setAttribute("payType", payType);

            req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", MessageUtil.getError("error.system.payment.processing"));
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}