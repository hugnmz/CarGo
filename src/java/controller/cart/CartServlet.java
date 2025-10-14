/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import service.CartService;
import util.di.DIContainer;

/**
 *
 * @author admin
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/Cart"})
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        try {
            cartService = DIContainer.get(CartService.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize CartService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /*
    ktra dang nhap
    lay validate parameter
    add san pham vao gio hang
    redirect voi thong bao ket qua
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // ktra login
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("customerId") == null) {

                String ctx = request.getContextPath();                     // "/CarGo"
                String path = request.getRequestURI().substring(ctx.length()); // "/booking"
                String qs = request.getQueryString();                      // "vehicleId=5"
                String carId = request.getParameter("carId");
                String currURL = "/car-detail?carId=" + carId;
                session = request.getSession(true);
                session.setAttribute("redirectAfterLogin", currURL);
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            //lay customer tu session
            Integer customerId = (Integer) session.getAttribute("customerId");

            // lay cac them so
            String vehicleIdStr = request.getParameter("vehicleId");
            String carIdStr = request.getParameter("carId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            if (vehicleIdStr == null || carIdStr == null) {
                response.sendRedirect(request.getContextPath() + "car-detail?carId=" + carIdStr
                        + "&erorr=missing_params");
                return;
            }

            // parse
            Integer carId = Integer.parseInt(carIdStr);
            Integer vehicleId = Integer.parseInt(vehicleIdStr);

            LocalDateTime startDate, endDate;
            if (startDateStr != null && endDateStr != null
                    && !startDateStr.trim().isEmpty() && !endDateStr.trim().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                startDate = LocalDateTime.parse(startDateStr + "T00:00:00");
                endDate = LocalDateTime.parse(endDateStr + "T23:59:59");
            } else {

                // ko co date dung defaule
                startDate = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0);
                endDate = startDate.plusDays(1).withHour(17).withMinute(0).withSecond(0);
            }

            // them vao gio hang
            boolean ok = cartService.addToCart(customerId, vehicleId, startDate, endDate);

            if (ok) {
                // Thành công -> redirect với thông báo success
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carId + "&added=true");
            } else {
                // Thất bại -> redirect với thông báo error
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carId + "&error=add_failed");
            }

        } catch (Exception e) {
            // Lỗi khác
            e.printStackTrace();
            String carId = request.getContextPath() + "/car-detail?carId=" + request.getParameter("carId") + "&error=system_error";
        }
    }

}
