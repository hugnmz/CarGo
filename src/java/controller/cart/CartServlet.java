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
import util.AuthUtil;

// Servlet xu ly them san pham vao gio hang
@WebServlet(name = "CartServlet", urlPatterns = {"/Cart"})
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Khoi tao CartService tu DI Container
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

    // Xu ly them san pham vao gio hang
    // Kiem tra dang nhap, lay va validate tham so, them san pham vao gio hang, redirect voi thong bao ket qua
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Kiem tra trang thai dang nhap
            if (!AuthUtil.requireLogin(request, response)) {
                return;
            }

            // Lay thong tin customer tu session
            Integer customerId = AuthUtil.getCustomerId(request);

            // Lay cac tham so tu request
            String vehicleIdStr = request.getParameter("vehicleId");
            String carIdStr = request.getParameter("carId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");

            // Kiem tra tham so bat buoc
            if (vehicleIdStr == null || carIdStr == null) {
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + (carIdStr != null ? carIdStr : "")
                        + "&error=missing_params");
                return;
            }

            // Chuyen doi tham so sang Integer
            Integer carId;
            Integer vehicleId;
            try {
                carId = Integer.valueOf(carIdStr);
                vehicleId = Integer.valueOf(vehicleIdStr);
            } catch (NumberFormatException nfe) {
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carIdStr + "&error=invalid_params");
                return;
            }

            // Xu ly thoi gian thue xe
            LocalDateTime startDate, endDate;
            if (startDateStr != null && endDateStr != null
                    && !startDateStr.trim().isEmpty() && !endDateStr.trim().isEmpty()) {
                // Neu co gio:phut thi dung, nguoc lai mac dinh 09:00 - 17:00
                String startTime = (startTimeStr != null && !startTimeStr.trim().isEmpty()) ? startTimeStr : "09:00";
                String endTime = (endTimeStr != null && !endTimeStr.trim().isEmpty()) ? endTimeStr : "17:00";
                startDate = LocalDateTime.parse(startDateStr + "T" + startTime + ":00");
                endDate = LocalDateTime.parse(endDateStr + "T" + endTime + ":00");
            } else {
                // Khong co date: dung thoi gian mac dinh
                startDate = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0);
                endDate = startDate.plusDays(1).withHour(17).withMinute(0).withSecond(0);
            }

            // Kiem tra thoi gian thue toi thieu 1 gio
            if (java.time.Duration.between(startDate, endDate).toMinutes() < 60) {
                String back = request.getContextPath() + "/customer/booking-form.jsp?carId=" + carId
                        + "&vehicleId=" + vehicleId + "&error=min_1h"
                        + "&startDate=" + startDateStr + "&endDate=" + endDateStr
                        + "&startTime=" + startTimeStr + "&endTime=" + endTimeStr
                        + "&pickupLocation=" + request.getParameter("pickupLocation")
                        + "&returnLocation=" + request.getParameter("returnLocation");
                response.sendRedirect(back);
                return;
            }
            
            // Kiem tra thoi gian dat phai trong tuong lai
            LocalDateTime now = LocalDateTime.now();
            if (startDate.isBefore(now)) {
                String back = request.getContextPath() + "/customer/booking-form.jsp?carId=" + carId
                        + "&vehicleId=" + vehicleId + "&error=past_time"
                        + "&startDate=" + startDateStr + "&endDate=" + endDateStr
                        + "&startTime=" + startTimeStr + "&endTime=" + endTimeStr
                        + "&pickupLocation=" + request.getParameter("pickupLocation")
                        + "&returnLocation=" + request.getParameter("returnLocation");
                response.sendRedirect(back);
                return;
            }

            // Kiem tra trung lap cung vehicle trong gio hang cua user truoc khi them
            java.util.List<dto.OrderDTO> items = cartService.getCartItems(customerId);
            boolean overlapInCart = false;
            for (dto.OrderDTO it : items) {
                if (it.getVehicleId().equals(vehicleId)) {
                    boolean noOverlap = endDate.isEqual(it.getRentStartDate()) || endDate.isBefore(it.getRentStartDate())
                            || startDate.isEqual(it.getRentEndDate()) || startDate.isAfter(it.getRentEndDate());
                    if (!noOverlap) { overlapInCart = true; break; }
                }
            }
            if (overlapInCart) {
                // Co trung lap: redirect ve booking form voi thong bao loi
                String back = request.getContextPath() + "/customer/booking-form.jsp?carId=" + carId
                        + "&vehicleId=" + vehicleId + "&error=overlap"
                        + "&startDate=" + startDateStr + "&endDate=" + endDateStr
                        + "&startTime=" + startTimeStr + "&endTime=" + endTimeStr
                        + "&pickupLocation=" + request.getParameter("pickupLocation")
                        + "&returnLocation=" + request.getParameter("returnLocation");
                response.sendRedirect(back);
                return;
            }

            // Them san pham vao gio hang
            boolean ok = cartService.addToCart(customerId, vehicleId, startDate, endDate);

            if (ok) {
                // Thanh cong: quay lai car-detail voi thong bao success
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carId + "&vehicleId=" + vehicleId + "&add=true");
            } else {
                // That bai chung (khong khang dinh overlap)
                String back = request.getContextPath() + "/customer/booking-form.jsp?carId=" + carId
                        + "&vehicleId=" + vehicleId + "&error=add_failed"
                        + "&startDate=" + startDateStr + "&endDate=" + endDateStr
                        + "&startTime=" + startTimeStr + "&endTime=" + endTimeStr
                        + "&pickupLocation=" + request.getParameter("pickupLocation")
                        + "&returnLocation=" + request.getParameter("returnLocation");
                response.sendRedirect(back);
            }

        } catch (Exception e) {
            // Xu ly loi he thong
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + request.getParameter("carId") + "&error=system_error");
        }
    }

}
