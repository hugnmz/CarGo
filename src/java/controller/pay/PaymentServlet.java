package controller.pay;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import service.PaymentService;
import service.ContractService;
import util.di.DIContainer;
import util.MessageUtil;
import dto.ContractDTO;
import dto.PaymentDTO;

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
        if(idStr == null || idStr.isEmpty()){
            req.setAttribute("error", "Thiếu contractId");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        try{
            int contractId = Integer.parseInt(idStr);
            Optional<ContractDTO> contractOpt = contractService.getContractById(contractId);
            if(!contractOpt.isPresent()){
                req.setAttribute("error", "Không tìm thấy hợp đồng");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            ContractDTO contract = contractOpt.get();
            BigDecimal amountToPay;
            String description;

            // Nếu chưa thanh toán deposit
            if("ACCEPTED".equalsIgnoreCase(contract.getStatus()) && !paymentService.hasCompletedDeposit(contractId)){
                amountToPay = contract.getDepositAmount();
                description = "Dat coc hop dong " + contractId;
            } else {
                Optional<BigDecimal> totalOpt = paymentService.getContractTotalAmount(contractId);
                if(!totalOpt.isPresent()){
                    req.setAttribute("error", "Không tìm thấy hợp đồng hoặc số tiền không hợp lệ");
                    req.getRequestDispatcher("/error.jsp").forward(req, resp);
                    return;
                }
                amountToPay = totalOpt.get();
                description = "Thanh toan hop dong " + contractId;
            }
if(amountToPay == null || amountToPay.compareTo(BigDecimal.ZERO) <= 0){
                req.setAttribute("error", "Số tiền thanh toán không hợp lệ");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            boolean completed = paymentService.hasCompleted(contractId);
            boolean depositPaid = paymentService.hasCompletedDeposit(contractId);

            req.setAttribute("contractId", contractId);
            req.setAttribute("totalAmount", amountToPay.intValue());

            if(completed || (depositPaid && amountToPay.compareTo(contract.getTotalAmount()) < 0)){
                req.setAttribute("initialStatus", "SUCCESS");
                req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);
                return;
            }

            Optional<PaymentDTO> existingPending = paymentService.findPendingPayment(contractId, amountToPay);
            if(!existingPending.isPresent()){
                if(amountToPay.compareTo(contract.getDepositAmount()) == 0){
                    paymentService.markDepositPaid(contractId, amountToPay);
                } else {
                    paymentService.createPendingPayment(contractId, amountToPay);
                }
            }

            String qr = "https://img.vietqr.io/image/" + BANK_ID + "-" + ACCOUNT_NO + "-" + TEMPLATE + ".jpg"
                    + "?amount=" + amountToPay.intValue()
                    + "&addInfo=" + URLEncoder.encode(description, StandardCharsets.UTF_8)
                    + "&accountName=" + URLEncoder.encode(ACCOUNT_NAME, StandardCharsets.UTF_8);

            req.setAttribute("qrUrl", qr);
            req.setAttribute("initialStatus", "PENDING");
            req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);

        } catch(Exception e){
            req.setAttribute("error", MessageUtil.getError("error.system.payment.processing"));
            req.getRequestDispatcher("/payment/payment.jsp").forward(req, resp);
        }
    }
}