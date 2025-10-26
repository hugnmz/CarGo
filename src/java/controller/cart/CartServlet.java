package controller.cart;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import util.MessageUtil;
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

            // Tạo list để thu thập lỗi
            List<String> errors = new ArrayList<>();

            // Kiểm tra ngày
            if (startDateStr == null || startDateStr.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.start.date.required"));
            }
            
            if (endDateStr == null || endDateStr.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.end.date.required"));
            }

            LocalDate startDate = null, endDate = null;
            if (startDateStr != null && endDateStr != null) {
                try {
                    startDate = LocalDate.parse(startDateStr);
                    endDate = LocalDate.parse(endDateStr);
                } catch (Exception e) {
                    errors.add(MessageUtil.getError("error.date.format.invalid"));
                }
            }

            // Kiểm tra ngày trong tương lai
            if (startDate != null && startDate.isBefore(LocalDate.now())) {
                errors.add(MessageUtil.getError("error.date.past"));
            }

            // Kiểm tra thời gian thuê tối thiểu 1 ngày
            if (startDate != null && endDate != null && (startDate.isAfter(endDate) || startDate.isEqual(endDate))) {
                errors.add(MessageUtil.getError("error.rental.minimum"));
            }

            // Nếu có lỗi, hiển thị tất cả lỗi
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("pickupLocation", request.getParameter("pickupLocation"));
                request.setAttribute("returnLocation", request.getParameter("returnLocation"));
                request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
                return;
            }

            // Chuyển đổi sang LocalDateTime với giờ mặc định (6h-22h)
            LocalDateTime startDateTime = startDate.atTime(6, 0);   // 6:00 sáng
            LocalDateTime endDateTime = endDate.atTime(22, 0);      // 22:00 đêm
            // Thêm vào giỏ hàng
            boolean success = cartService.addToCart(customerId, vehicleId, startDateTime, endDateTime);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carId + "&vehicleId=" + vehicleId + "&add=true");
            } else {
                errors.add(MessageUtil.getError("error.vehicle.unavailable"));
                request.setAttribute("errors", errors);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("pickupLocation", request.getParameter("pickupLocation"));
                request.setAttribute("returnLocation", request.getParameter("returnLocation"));
                request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            // Xu ly loi he thong
            e.printStackTrace();
            List<String> errors = new ArrayList<>();
            errors.add(MessageUtil.getError("error.system.cart"));
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
        }
    }

}
