package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/CreateContractServlet")
public class CreateContractServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy dữ liệu từ form
        request.setCharacterEncoding("UTF-8");
        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String customerAddress = request.getParameter("customerAddress");
        String carName = request.getParameter("carName");
        String plateNumber = request.getParameter("plateNumber");
        String rentStart = request.getParameter("rentStartDate");
        String rentEnd = request.getParameter("rentEndDate");
        String pricePerDay = request.getParameter("pricePerDay");
        String deposit = request.getParameter("deposit");
        String staffName = "Nguyen Van A"; // tạm thời, có thể lấy từ session

        // Truyền sang JSP hiển thị hợp đồng
        request.setAttribute("customerName", customerName);
        request.setAttribute("customerPhone", customerPhone);
        request.setAttribute("customerAddress", customerAddress);
        request.setAttribute("carName", carName);
        request.setAttribute("plateNumber", plateNumber);
        request.setAttribute("rentStart", rentStart);
        request.setAttribute("rentEnd", rentEnd);
        request.setAttribute("pricePerDay", pricePerDay);
        request.setAttribute("deposit", deposit);
        request.setAttribute("staffName", staffName);

        // Chuyển hướng sang trang hiển thị hợp đồng chi tiết
        RequestDispatcher dispatcher = request.getRequestDispatcher("contractDetail.jsp");
        dispatcher.forward(request, response);
    }
}
