package controller.payment;



import dao.impl.PaymentsDAOImpl;
import dao.impl.ContractsDAOImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Payments;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/checkPayment")
public class CheckPaymentServlet extends HttpServlet {

    // DÁN API KEY CỦA BẠN
    private static final String CASSO_API_KEY = "AK_CS.d5c4e4e0b41c11f098dc7bf09fa7a682.DLw7n0mvI3dX3ymsSdpxaCGzVPGLjPCLgKsTYnvC2oajuPkfZfZqyZQng15D2r6lrHdphFCp";
    private static final String CASSO_API_URL = "https://oauth.casso.vn/v2/transactions";
    private static final String MY_ACCOUNT = "0862671682";

    private final PaymentsDAOImpl paymentsDAO = new PaymentsDAOImpl();
    private final ContractsDAOImpl contractsDAO = new ContractsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String contractIdStr = req.getParameter("contractId");
        String amountStr = req.getParameter("amount");

        if (contractIdStr == null || amountStr == null) {
            out.print("{\"status\":\"ERROR\"}");
            return;
        }

        int contractId = Integer.parseInt(contractIdStr);
        BigDecimal requiredAmount = new BigDecimal(amountStr);

        boolean paid = checkCassoAPI(requiredAmount);

        if (paid) {
            Payments payment = paymentsDAO.findPendingPayment(contractId, requiredAmount);
            if (payment != null) {
                paymentsDAO.updatePaymentStatus(payment.getPaymentId(), "completed");
                contractsDAO.updateContractStatus(contractId, "ACCEPTED");
                System.out.println("CẬP NHẬT DB: Hợp đồng " + contractId);
            }
        }

        out.print("{\"status\":\"" + (paid ? "SUCCESS" : "PENDING") + "\"}");
    }

    private boolean checkCassoAPI(BigDecimal requiredAmount) {
        try {
            URL url = new URL(CASSO_API_URL + "?pageSize=5");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Apikey " + CASSO_API_KEY);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            int code = conn.getResponseCode();
            System.out.println("Casso API Code: " + code);
            if (code != 200) return false;

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            String json = sb.toString();
            System.out.println("Casso JSON: " + json);

            JSONObject root = new JSONObject(json);
            if (!root.has("data")) return false;

            JSONArray records = root.getJSONArray("data");
            for (int i = 0; i < records.length(); i++) {
                JSONObject rec = records.getJSONObject(i);
                String amountStr = rec.optString("amount", "0").replaceAll("[^0-9]", "");
                String account = rec.optString("accountNumber", "");

                BigDecimal amount = amountStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(amountStr);

                if (account.equals(MY_ACCOUNT) && amount.compareTo(requiredAmount) == 0) {
                    System.out.println("THÀNH CÔNG: " + amount + " VNĐ → " + account);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}