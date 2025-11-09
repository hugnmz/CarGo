package controller.pay;

import constant.ConstractStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.Optional;
import org.json.JSONObject;
import service.ContractService;
import service.PaymentService;
import util.di.DIContainer;
import util.MessageUtil;
import dto.ContractDTO;
import dto.PaymentDTO;

@WebServlet("/paymentCallback")
public class PaymentCallbackServlet extends HttpServlet {

    private ContractService contractService;
    private PaymentService paymentService;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        JSONObject responseJson = new JSONObject();

        try {
            JSONObject json = new JSONObject(sb.toString());
            int errorCode = json.optInt("error", 1);
            if (errorCode != 0) {
                responseJson.put("status", "error");
                responseJson.put("message", "Casso returned error");
                resp.getWriter().write(responseJson.toString());
                return;
            }

            JSONObject data = json.getJSONObject("data");
            BigDecimal amount = new BigDecimal(String.valueOf(data.opt("amount")));
            String description = data.optString("description", "").toLowerCase();
            int contractId = extractContractId(description);

            if(contractId == -1) {
                responseJson.put("status", "error");
                responseJson.put("message", "Could not extract contractId");
                resp.getWriter().write(responseJson.toString());
                return;
            }

            Optional<ContractDTO> contractOpt = contractService.getContractById(contractId);
            if(!contractOpt.isPresent()){
                responseJson.put("status", "error");
                responseJson.put("message", "Contract not found");
                resp.getWriter().write(responseJson.toString());
                return;
            }
            ContractDTO contract = contractOpt.get();
            BigDecimal depositAmount = contract.getDepositAmount();
            BigDecimal totalAmount = contract.getTotalAmount();

// ===== Xử lý deposit riêng =====
            if(amount.compareTo(depositAmount) == 0 && contract.getStatus().equals("ACCEPTED")) {
                boolean updated = paymentService.markDepositPaid(contractId, amount);
                responseJson.put("status", updated ? "ok" : "warning");
                responseJson.put("message", updated ? "Deposit updated" : "Deposit already paid");
            } 
            // ===== Full payment =====
            else if(amount.compareTo(totalAmount) >= 0) {
                Optional<PaymentDTO> pendingPayment = paymentService.findPendingPayment(contractId, amount);
                if(pendingPayment.isPresent() && !paymentService.isPaymentCompleted(pendingPayment.get().getPaymentId())) {
                    boolean updatedPayment = paymentService.completePaymentById(pendingPayment.get().getPaymentId());
                    if(updatedPayment) contractService.updateContractStatus(contractId, ConstractStatus.COMPLETED.name());
                    responseJson.put("status", "ok");
                    responseJson.put("message", "Full payment completed");
                } else {
                    responseJson.put("status", "warning");
                    responseJson.put("message", "No pending payment or already completed");
                }
            } else {
                responseJson.put("status", "error");
                responseJson.put("message", "Invalid payment amount");
            }

            resp.getWriter().write(responseJson.toString());
        } catch (Exception e) {
            responseJson.put("status", "error");
            responseJson.put("message", MessageUtil.getError("error.system.payment.callback"));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(responseJson.toString());
        }
    }

    private int extractContractId(String description) {
        try {
            return Integer.parseInt(description.replaceAll("\\D+", ""));
        } catch(Exception e) {
            return -1;
        }
    }
}