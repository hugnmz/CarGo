package controller.pay;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CalculateTotalAmount", urlPatterns = {"/calculateTotalAmount"})
public class CalculateTotalAmount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contractIdRaw = request.getParameter("contractId");
        if (contractIdRaw == null || contractIdRaw.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Không tìm thấy contractId.");
            request.getRequestDispatcher("/incident.jsp").forward(request, response);
            return;
        }

        try {
            int contractId = Integer.parseInt(contractIdRaw.trim());
            request.setAttribute("contractId", contractId); // Đặt attribute đúng tên
            request.getRequestDispatcher("/incident.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "contractId không hợp lệ: " + contractIdRaw);
            request.getRequestDispatcher("/incident.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi server: " + e.getMessage());
            request.getRequestDispatcher("/incident.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}