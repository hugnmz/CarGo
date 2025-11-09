package controller.cart;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import service.CartService;
import util.di.DIContainer;
import util.AuthUtil;
import util.MessageUtil;
import util.exception.*;

@WebServlet(name = "CartServlet", urlPatterns = {"/Cart"})
public class CartServlet extends HttpServlet {

    // Service xử lý nghiệp vụ giỏ hàng
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            cartService = DIContainer.get(CartService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward đến booking form (khi filter hoặc validate có lỗi)
        request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Kiểm tra trạng thái đăng nhập
            if (!AuthUtil.requireLogin(request, response)) {
                return;
            }

            Integer customerId = AuthUtil.getCustomerId(request);

            // Lấy tham số
            String vehicleIdStr = request.getParameter("vehicleId");
            String carIdStr = request.getParameter("carId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            // Parse dữ liệu
            Integer carId = (carIdStr != null) ? Integer.valueOf(carIdStr) : null;
            Integer vehicleId = (vehicleIdStr != null) ? Integer.valueOf(vehicleIdStr) : null;
            LocalDate startDate = (startDateStr != null && !startDateStr.trim().isEmpty()) ? LocalDate.parse(startDateStr) : null;
            LocalDate endDate = (endDateStr != null && !endDateStr.trim().isEmpty()) ? LocalDate.parse(endDateStr) : null;

            LocalDateTime startDateTime = (startDate != null) ? startDate.atTime(6, 0) : null;
            LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(22, 0) : null;

            // Gọi service xử lý thêm giỏ hàng
            boolean success = cartService.addToCart(customerId, vehicleId, startDateTime, endDateTime);

            if (success) {
                response.sendRedirect(request.getContextPath()
                        + "/car-detail?carId=" + carId
                        + "&vehicleId=" + vehicleId
                        + "&add=true");
            } else {
                response.sendRedirect(request.getContextPath()
                        + "/car-detail?carId=" + carId
                        + "&vehicleId=" + vehicleId
                        + "&error=add_failed");
            }

        } catch (ValidationException | BusinessException | DataAccessException e) {
            List<String> errors = new ArrayList<>();
            errors.add(MessageUtil.getErrorFromException(e));

            // Gọi hàm riêng để set lại giá trị người dùng
            setUserInputAttributes(request);

            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);

        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(MessageUtil.getError("error.system.cart"));

            // Gọi hàm riêng để set lại giá trị người dùng
            setUserInputAttributes(request);

            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
        }
    }

    private void setUserInputAttributes(HttpServletRequest request) {
        request.setAttribute("startDate", request.getParameter("startDate"));
        request.setAttribute("endDate", request.getParameter("endDate"));
        request.setAttribute("pickupLocation", request.getParameter("pickupLocation"));
        request.setAttribute("returnLocation", request.getParameter("returnLocation"));
    }
}
