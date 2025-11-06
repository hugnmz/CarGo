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
import util.exception.WebException;


@WebServlet(name = "CartServlet", urlPatterns = {"/Cart"})
public class CartServlet extends HttpServlet {

    // service xu ly gio hang
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao cart service tu di container
            cartService = DIContainer.get(CartService.class);
        } catch (Exception e) {
            // nem loi neu khoi tao service that bai
            throw new RuntimeException("Failed to initialize CartService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward den booking form (duoc goi khi filter co loi validation)
        request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // kiem tra trang thai dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
            if (!AuthUtil.requireLogin(request, response)) {
                return;
            }

            // lay thong tin customer tu session
            Integer customerId = AuthUtil.getCustomerId(request);

            // lay cac tham so tu request
            String vehicleIdStr = request.getParameter("vehicleId");
            String carIdStr = request.getParameter("carId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            // Xóa tất cả check - đã chuyển vào service
            // Parse tham so
            Integer carId = (carIdStr != null) ? Integer.valueOf(carIdStr) : null;
            Integer vehicleId = (vehicleIdStr != null) ? Integer.valueOf(vehicleIdStr) : null;
            LocalDate startDate = (startDateStr != null && !startDateStr.trim().isEmpty()) ? LocalDate.parse(startDateStr) : null;
            LocalDate endDate = (endDateStr != null && !endDateStr.trim().isEmpty()) ? LocalDate.parse(endDateStr) : null;

            // chuyen doi sang localdatetime voi gio mac dinh (6h-22h)
            LocalDateTime startDateTime = (startDate != null) ? startDate.atTime(6, 0) : null;   // 6:00 sang
            LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(22, 0) : null;      // 22:00 dem
            
            // them vao gio hang bang service - service sẽ check và throw WebException
            boolean success = cartService.addToCart(customerId, vehicleId, startDateTime, endDateTime);

            if (success) {
                // neu them thanh cong thi chuyen ve trang chi tiet xe voi thong bao thanh cong
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carId + "&vehicleId=" + vehicleId + "&add=true");
            } else {
                // neu them that bai thi chuyen ve trang chi tiet xe voi thong bao loi
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carId + "&vehicleId=" + vehicleId + "&error=add_failed");
            }

        } catch (WebException.AppException ex) {
            // Bắt WebException
            List<String> errors = new ArrayList<>();
            errors.add(ex.getMessage());
            request.setAttribute("errors", errors);
            request.setAttribute("startDate", request.getParameter("startDate"));
            request.setAttribute("endDate", request.getParameter("endDate"));
            request.setAttribute("pickupLocation", request.getParameter("pickupLocation"));
            request.setAttribute("returnLocation", request.getParameter("returnLocation"));
            request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
        } catch (Exception e) {
            // xu ly loi he thong
            throw new RuntimeException("error.system.cart", e);
        }
    }

}
