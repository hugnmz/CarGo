package controller.cart;

import dto.OrderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import service.CartService;
import util.di.DIContainer;
import util.AuthUtil;

@WebServlet(name = "ViewCartDetail", urlPatterns = {"/ViewCartDetail"})
public class ViewCartDetail extends HttpServlet {

    // service xu ly gio hang
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        try {
            // khoi tao cart service tu di container
            cartService = DIContainer.get(CartService.class);
        } catch (Exception e) {
            // nem loi neu khoi tao service that bai
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // kiem tra trang thai dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }
        
        // lay thong tin customer tu session
        Integer customerId = AuthUtil.getCustomerId(request);
        // lay danh sach san pham trong gio hang tu database
        List<OrderDTO> items = cartService.getCartItems(customerId);
        
        // dat danh sach san pham vao request de truyen sang jsp
        request.setAttribute("cartItems", items);
        
        String carId = request.getParameter("carId");
        String vehicleId = request.getParameter("vehicleId");
        
        // Nếu không có carId từ parameter, thử lấy từ referer (nếu từ trang car-detail)
        if (carId == null || carId.trim().isEmpty()) {
            String referer = request.getHeader("Referer");
            if (referer != null && referer.contains("/car-detail")) {
                // Trích xuất carId từ URL: /car-detail?carId=1
                int carIdIndex = referer.indexOf("carId=");
                if (carIdIndex != -1) {
                    String carIdFromUrl = referer.substring(carIdIndex + 6);
                    int ampIndex = carIdFromUrl.indexOf("&");
                    if (ampIndex != -1) {
                        carIdFromUrl = carIdFromUrl.substring(0, ampIndex);
                    }
                    carId = carIdFromUrl;
                }
            }
        }
        
        if (carId != null && !carId.trim().isEmpty()) {
            request.setAttribute("carId", carId);
        }
        if (vehicleId != null && !vehicleId.trim().isEmpty()) {
            request.setAttribute("vehicleId", vehicleId);
        }
        
        // chuyen huong den trang cart.jsp
        request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // kiem tra dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }
        
        // lay thong tin customer tu session
        Integer customerId = AuthUtil.getCustomerId(request);
        // lay tham so action de xac dinh hanh dong can thuc hien
        String action = request.getParameter("action");
        // lay danh sach id cac san pham duoc chon de xoa
        String[] selected = request.getParameterValues("selectedIds");

        boolean success = false;
        
        // xu ly cac hanh dong xoa san pham
        if ("clear".equals(action)) {
            // xoa tat ca san pham trong gio hang
            success = cartService.clearCart(customerId);
        } else if ("remove".equals(action) && selected != null && selected.length > 0) {
            // xoa cac san pham da chon
            for (String id : selected) {
                try {
                    // chuyen doi id tu string sang integer va xoa san pham
                    boolean result = cartService.removeFromCart(customerId, Integer.valueOf(id));
                    if (result) success = true;
                } catch (NumberFormatException e) {
                    // bo qua cac id khong hop le
                }
            }
        }

        // sau khi xoa: lay lai danh sach gio hang moi tu database
        List<OrderDTO> items = cartService.getCartItems(customerId);
        request.setAttribute("cartItems", items);
        
        String carId = request.getParameter("carId");
        String vehicleId = request.getParameter("vehicleId");
        if (carId != null && !carId.trim().isEmpty()) {
            request.setAttribute("carId", carId);
        }
        if (vehicleId != null && !vehicleId.trim().isEmpty()) {
            request.setAttribute("vehicleId", vehicleId);
        }
        
        // chuyen huong lai trang cart.jsp
        request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
    }
}


