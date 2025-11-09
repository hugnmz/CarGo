package controller.pay;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.json.JSONObject;
import service.PaymentService;
import util.di.DIContainer;
import util.MessageUtil;

@WebServlet("/checkPayment")
public class CheckPaymentServlet extends HttpServlet {

    private PaymentService paymentService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            paymentService = DIContainer.get(PaymentService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        // no-cache
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        JSONObject result = new JSONObject();

        try {
            int contractId = Integer.parseInt(request.getParameter("contractId"));

            //Kiểm tra có payment COMPLETED chưa
            String status = paymentService.getPaymentStatus(contractId);

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
