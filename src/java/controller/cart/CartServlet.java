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

            // kiem tra tham so bat buoc, neu thieu thi chuyen ve trang chi tiet xe voi thong bao loi
            if (vehicleIdStr == null || carIdStr == null) {
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + (carIdStr != null ? carIdStr : "")
                        + "&error=missing_params");
                return;
            }

            // chuyen doi tham so tu string sang integer
            Integer carId;
            Integer vehicleId;
            try {
                carId = Integer.valueOf(carIdStr);
                vehicleId = Integer.valueOf(vehicleIdStr);
            } catch (NumberFormatException nfe) {
                // neu chuyen doi that bai thi chuyen ve trang chi tiet xe voi thong bao loi
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carIdStr + "&error=invalid_params");
                return;
            }

            // tao danh sach de luu cac loi validation
            List<String> errors = new ArrayList<>();

            // kiem tra ngay bat dau, neu thieu thi them loi vao danh sach
            if (startDateStr == null || startDateStr.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.start.date.required"));
            }
            
            // kiem tra ngay ket thuc, neu thieu thi them loi vao danh sach
            if (endDateStr == null || endDateStr.trim().isEmpty()) {
                errors.add(MessageUtil.getError("error.end.date.required"));
            }

            // chuyen doi ngay tu string sang localdate
            // Filter da validate format, nen parse an toan
            LocalDate startDate = null, endDate = null;
            if (startDateStr != null && !startDateStr.trim().isEmpty() 
                && endDateStr != null && !endDateStr.trim().isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
                endDate = LocalDate.parse(endDateStr);
            }

            // kiem tra ngay bat dau phai trong tuong lai
            if (startDate != null && startDate.isBefore(LocalDate.now())) {
                errors.add(MessageUtil.getError("error.date.past"));
            }

            // kiem tra thoi gian thue toi thieu 1 ngay
            if (startDate != null && endDate != null && (startDate.isAfter(endDate) || startDate.isEqual(endDate))) {
                errors.add(MessageUtil.getError("error.rental.minimum"));
            }

            // neu co loi thi hien thi tat ca loi va chuyen ve trang booking form
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("pickupLocation", request.getParameter("pickupLocation"));
                request.setAttribute("returnLocation", request.getParameter("returnLocation"));
                request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
                return;
            }

            // chuyen doi sang localdatetime voi gio mac dinh (6h-22h)
            LocalDateTime startDateTime = startDate.atTime(6, 0);   // 6:00 sang
            LocalDateTime endDateTime = endDate.atTime(22, 0);      // 22:00 dem
            // them vao gio hang bang service
            boolean success = cartService.addToCart(customerId, vehicleId, startDateTime, endDateTime);

            if (success) {
                // neu them thanh cong thi chuyen ve trang chi tiet xe voi thong bao thanh cong
                response.sendRedirect(request.getContextPath() + "/car-detail?carId=" + carId + "&vehicleId=" + vehicleId + "&add=true");
            } else {
                // neu them that bai thi them loi vao danh sach va hien thi lai form
                errors.add(MessageUtil.getError("error.vehicle.unavailable"));
                request.setAttribute("errors", errors);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("pickupLocation", request.getParameter("pickupLocation"));
                request.setAttribute("returnLocation", request.getParameter("returnLocation"));
                request.getRequestDispatcher("/customer/booking-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            // xu ly loi he thong
            throw new RuntimeException("error.system.cart", e);
        }
    }

}
