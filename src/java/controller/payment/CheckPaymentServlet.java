package controller.payment;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;

@WebServlet("/checkPayment")
public class CheckPaymentServlet extends HttpServlet {
    private static final String API_URL = "https://oauth.casso.vn/v1/transactions";
    private static final String API_KEY = "AK_CS.555ba910b36311f098dc7bf09fa7a682.x1G9ufCXtajAd96OMHp3ZBWMt4edZIdOIaLqGXweRgBlwySgSNYhz8d9vRgfN3MNQTmdpKzY";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        var out = resp.getWriter();

        String contractId = req.getParameter("contractId");
        String amount = req.getParameter("amount");

        if (contractId == null || amount == null) {
            out.print("{\"status\":\"ERROR\"}");
            return;
        }

        boolean paid = checkWithCasso(contractId, amount);
        out.print("{\"status\":\"" + (paid ? "SUCCESS" : "PENDING") + "\"}");
    }

    private boolean checkWithCasso(String contractId, String amount) {
        try {
            String json = """
                {
                    "contractId": "%s",
                    "amount": %s
                }
                """.formatted(contractId, amount);

            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            System.out.println("Casso API: " + body);
            return body.contains("\"status\":\"PAID\"") || body.contains("\"status\":\"SUCCESS\"");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}