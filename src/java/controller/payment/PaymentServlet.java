package controller.payment;

import dao.ContractsDAO;
import dao.PaymentsDAO;
import dao.impl.ContractsDAOImpl;
import dao.impl.PaymentsDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Payments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;

@WebServlet("/paymentServlet")
public class PaymentServlet extends HttpServlet {

    private ContractsDAO contractsDAO = new ContractsDAOImpl();
    private PaymentsDAO paymentsDAO = new PaymentsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contractIdStr = request.getParameter("contractId");
        if (contractIdStr == null || contractIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Thiếu contractId!");
            request.getRequestDispatcher("/payment.jsp").forward(request, response);
            return;
        }

        Integer contractId = Integer.parseInt(contractIdStr);
        BigDecimal totalAmount = contractsDAO.getTotalAmount(contractId);
        request.setAttribute("contractId", contractId);
        request.setAttribute("totalAmount", totalAmount);
        request.getRequestDispatcher("/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contractIdStr = request.getParameter("contractId");
        String methodIdStr = request.getParameter("methodId");
        String amountStr = request.getParameter("amount");

        Integer contractId = contractIdStr != null ? Integer.parseInt(contractIdStr) : null;
        Integer methodId = methodIdStr != null ? Integer.parseInt(methodIdStr) : null;
        BigDecimal amount = amountStr != null ? new BigDecimal(amountStr) : null;

        request.setAttribute("contractId", contractId);
        request.setAttribute("totalAmount", amount);

        if (contractId == null || methodId == null || amount == null) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
            request.getRequestDispatcher("/payment.jsp").forward(request, response);
            return;
        }

        if (methodId == 2) {
            String cardNumber = request.getParameter("cardNumber");
            String expiryDate = request.getParameter("expiryDate");
            String cvv = request.getParameter("cvv");

            if (cardNumber == null || expiryDate == null || cvv == null
                    || !cardNumber.matches("\\d{16}") || !expiryDate.matches("\\d{2}/\\d{2}") || !cvv.matches("\\d{3}")) {
                request.setAttribute("errorMessage", "Thông tin thẻ không hợp lệ!");
                request.getRequestDispatcher("/payment.jsp").forward(request, response);
                return;
            }

            String error = validateCreditCard(cardNumber, expiryDate, cvv);
            if (error != null) {
                request.setAttribute("errorMessage", error);
                request.getRequestDispatcher("/payment.jsp").forward(request, response);
                return;
            }
        }

        Payments payment = new Payments(contractId, amount, methodId);
        boolean success = paymentsDAO.addPayment(payment);
        if (success) {
            paymentsDAO.updatePaymentStatus(payment.getPaymentId(), "completed");
            request.setAttribute("successMessage", "Thanh toán thành công!");
            request.setAttribute("paymentCompleted", true);
        } else {
            request.setAttribute("errorMessage", "Thanh toán thất bại!");
        }

        request.getRequestDispatcher("/payment.jsp").forward(request, response);
    }

    private String validateCreditCard(String cardNumber, String expiryDate, String cvv) {
        try {
            String[] parts = expiryDate.split("/");
            int expMonth = Integer.parseInt(parts[0]);
            int expYear = Integer.parseInt("20" + parts[1]);

            int currentYear = Year.now().getValue();
            int currentMonth = LocalDate.now().getMonthValue();
            if (expYear < currentYear || (expYear == currentYear && expMonth < currentMonth)) {
                return "Thẻ đã hết hạn!";
            }

            URL url = new URL("https://api.stripe.com/v1/tokens");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            String stripeKey = System.getenv("STRIPE_API_KEY");
            conn.setRequestProperty("Authorization", "Bearer " + stripeKey);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "card[number]=" + cardNumber
                    + "&card[exp_month]=" + expMonth
                    + "&card[exp_year]=" + expYear
                    + "&card[cvc]=" + cvv;

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = postData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return null; // Thẻ hợp lệ
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder error = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        error.append(line.trim());
                    }
                    return "Thẻ không hợp lệ: " + error.toString();
                }
            }

        } catch (Exception e) {
            return "Lỗi khi xác thực thẻ: " + e.getMessage();
        }
    }
}
