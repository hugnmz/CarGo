package controller.payment;

import dao.impl.PaymentsDAOImpl;
import dao.impl.ContractsDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

@WebServlet("/paymentCallback")
public class PaymentCallbackServlet extends HttpServlet {

    private final PaymentsDAOImpl paymentsDAO = new PaymentsDAOImpl();
    private final ContractsDAOImpl contractsDAO = new ContractsDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        // Đọc dữ liệu JSON từ body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JSONObject responseJson = new JSONObject();

        try {
            System.out.println("=== CALLBACK START ===");
            System.out.println("Raw JSON: " + sb.toString());

            JSONObject json = new JSONObject(sb.toString());
            

            // Kiểm tra error
            int errorCode = json.optInt("error", 1);
            System.out.println("Casso error code: " + errorCode);
            if (errorCode != 0) {
                responseJson.put("status", "error");
                responseJson.put("message", "Casso returned error");
                resp.getWriter().write(responseJson.toString());
                return;
            }

            JSONObject data = json.getJSONObject("data");
            BigDecimal amount = new BigDecimal(String.valueOf(data.opt("amount")));
            String description = data.optString("description", "").toLowerCase();
            String toAccount = data.optString("accountNumber", "");

            System.out.println("Parsed callback: description=" + description + ", amount=" + amount + ", toAccount=" + toAccount);

            // Lấy contractId từ description
            int contractId = extractContractId(description);
            System.out.println("Extracted contractId: " + contractId);
            if (contractId == -1) {
                responseJson.put("status", "error");
                responseJson.put("message", "Could not extract contractId");
                resp.getWriter().write(responseJson.toString());
                return;
            }

            // Kiểm tra pending payment
            var pendingPayment = paymentsDAO.findPendingPayment(contractId, amount);
            System.out.println("Pending payment found: " + (pendingPayment != null));

            if (pendingPayment != null && !paymentsDAO.isPaymentCompleted(pendingPayment.getPaymentId())) {
                boolean updatedPayment = paymentsDAO.updatePaymentStatus(pendingPayment.getPaymentId(), "COMPLETED");
                boolean updatedContract = contractsDAO.updateContractStatus(contractId, "ACCEPTED");
                System.out.println("[CALLBACK] UpdatedPayment=" + updatedPayment + ", UpdatedContract=" + updatedContract);

                System.out.println("Payment updated: " + updatedPayment);
                System.out.println("Contract status updated: " + updatedContract);
                
                String statusNow = paymentsDAO.getPaymentStatus(contractId);
                System.out.println("[CALLBACK] Recheck DB status after update: " + statusNow);

                responseJson.put("status", "ok");
                responseJson.put("message", "Payment updated successfully for contract " + contractId);
            } else {
                System.out.println("No matching pending payment or already completed.");
                responseJson.put("status", "warning");
                responseJson.put("message", "No matching pending payment found or already completed for contract " + contractId);
            }

            resp.getWriter().write(responseJson.toString());
            System.out.println("=== CALLBACK END ===");

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put("status", "error");
            responseJson.put("message", "Internal server error while processing callback");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(responseJson.toString());
        }
    }

    private int extractContractId(String description) {
        try {
            // Chỉ match "Thanh toan hop dong <số>"
            Pattern p = Pattern.compile("Thanh toan hop dong (\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(description);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        } catch (Exception e) {
            System.out.println("Error extracting contractId: " + e.getMessage());
        }
        return -1;
    }
}
