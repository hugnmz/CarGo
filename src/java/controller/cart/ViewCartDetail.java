package controller.cart;

import dto.OrderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import service.CartService;
import util.di.DIContainer;

// Servlet hien thi chi tiet gio hang
@WebServlet(name = "ViewCartDetail", urlPatterns = {"/ViewCartDetail"})
public class ViewCartDetail extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        // Khoi tao CartService tu DI Container
        try {
            cartService = DIContainer.get(CartService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiem tra trang thai dang nhap
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerId") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }
        
        // Lay thong tin customer va danh sach san pham trong gio hang
        Integer customerId = (Integer) session.getAttribute("customerId");
        List<OrderDTO> items = cartService.getCartItems(customerId);
        
        // Truyen danh sach san pham xuong JSP
        request.setAttribute("cartItems", items);
        
        // Truyen carId va vehicleId xuong JSP de hien thi link "Quay lai xem xe"
        String carId = request.getParameter("carId");
        String vehicleId = request.getParameter("vehicleId");
        if (carId != null && !carId.trim().isEmpty()) {
            request.setAttribute("carId", carId);
        }
        if (vehicleId != null && !vehicleId.trim().isEmpty()) {
            request.setAttribute("vehicleId", vehicleId);
        }
        
        // Them cache control headers de tranh cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        // Forward den trang cart.jsp
        request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiem tra trang thai dang nhap
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerId") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }
        
        // Lay thong tin customer va cac tham so
        Integer customerId = (Integer) session.getAttribute("customerId");
        String action = request.getParameter("action");
        String[] selected = request.getParameterValues("selectedIds");

        boolean success = false;
        
        // Xu ly cac hanh dong xoa san pham
        if ("clear".equals(action)) {
            // Xoa tat ca san pham trong gio hang
            success = cartService.clearCart(customerId);
        } else if ("remove".equals(action) && selected != null && selected.length > 0) {
            // Xoa cac san pham da chon
            for (String id : selected) {
                try {
                    boolean result = cartService.removeFromCart(customerId, Integer.parseInt(id));
                    if (result) success = true;
                } catch (NumberFormatException e) {
                    // Bo qua cac ID khong hop le
                }
            }
        }

        // Sau khi xoa: lay lai danh sach gio hang moi
        List<OrderDTO> items = cartService.getCartItems(customerId);
        request.setAttribute("cartItems", items);
        
        // Truyen carId va vehicleId xuong JSP
        String carId = request.getParameter("carId");
        String vehicleId = request.getParameter("vehicleId");
        if (carId != null && !carId.trim().isEmpty()) {
            request.setAttribute("carId", carId);
        }
        if (vehicleId != null && !vehicleId.trim().isEmpty()) {
            request.setAttribute("vehicleId", vehicleId);
        }
        
        // Them cache control headers de tranh cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        // Forward lai trang cart
        request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
    }
}


