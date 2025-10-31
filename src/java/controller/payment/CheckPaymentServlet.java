package controller.payment;

import dao.impl.PaymentsDAOImpl;
import dao.impl.ContractsDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

@WebServlet("/checkPayment")
public class CheckPaymentServlet extends HttpServlet {

   private final PaymentsDAOImpl paymentsDAO = new PaymentsDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        JSONObject result = new JSONObject();

        try {
            int contractId = Integer.parseInt(request.getParameter("contractId"));

            // 🔍 Kiểm tra có payment COMPLETED chưa
            String sql = "SELECT TOP 1 status FROM Payments WHERE contractId = ? ORDER BY paymentDate DESC";
            String status = paymentsDAO.getPaymentStatus(contractId);

            if ("COMPLETED".equalsIgnoreCase(status)) {
                result.put("status", "SUCCESS");
                result.put("message", "Thanh toán thành công.");
            } else {
                result.put("status", "PENDING");
                result.put("message", "Đang chờ thanh toán...");
            }

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("message", e.getMessage());
        }

        response.getWriter().write(result.toString());
    }
}
