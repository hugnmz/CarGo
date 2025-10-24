package controller.cart;

import dao.OrdersDAO;
import dto.OrderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.Customers;
import model.Orders;
import util.JdbcTemplateUtil;
import util.di.DIContainer;
import util.AuthUtil;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private OrdersDAO ordersDAO;

    // Make a proper bean for JSP EL
    public static class CreatedContract {
        private int contractId;
        private String customerName; // Tên khách hàng
        private LocalDateTime start;
        private LocalDateTime end;
        private BigDecimal total;
        private BigDecimal deposit; // Tiền đặt cọc
        private List<OrderDTO> details = new ArrayList<>();

        public int getContractId() { return contractId; }
        public void setContractId(int contractId) { this.contractId = contractId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public LocalDateTime getStart() { return start; }
        public void setStart(LocalDateTime start) { this.start = start; }
        public LocalDateTime getEnd() { return end; }
        public void setEnd(LocalDateTime end) { this.end = end; }
        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
        public BigDecimal getDeposit() { return deposit; }
        public void setDeposit(BigDecimal deposit) { this.deposit = deposit; }
        public List<OrderDTO> getDetails() { return details; }
        public void setDetails(List<OrderDTO> details) { this.details = details; }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ordersDAO = DIContainer.get(OrdersDAO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthUtil.requireLogin(request, response)) {
            return;
        }
        Integer customerId = AuthUtil.getCustomerId(request);
        
        // Lấy tên khách hàng từ database
        String customerName = "Khách hàng"; // default
        try {
            String sqlCustomer = "SELECT fullName FROM dbo.Customers WHERE customerId=?";
            List<Customers> customers = JdbcTemplateUtil.query(sqlCustomer, Customers.class, customerId);
            if (!customers.isEmpty() && customers.get(0).getFullName() != null) {
                customerName = customers.get(0).getFullName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        final String finalCustomerName = customerName; // Make it effectively final for lambda

        String[] selectedIds = request.getParameterValues("selectedIds");

        List<OrderDTO> selectedOrders = new ArrayList<>();
        try {
            if (selectedIds != null && selectedIds.length > 0) {
                for (String sid : selectedIds) {
                    try {
                        Integer id = Integer.valueOf(sid);
                        Optional<Orders> o = ordersDAO.getOrderById(id);
                        if (o.isPresent()) {
                            Orders ord = o.get();
                            OrderDTO dto = new OrderDTO();
                            dto.setCartDetailId(ord.getCartDetailId());
                            dto.setCartId(ord.getCartId());
                            dto.setVehicleId(ord.getVehicleId());
                            dto.setRentStartDate(ord.getRentStartDate());
                            dto.setRentEndDate(ord.getRentEndDate());
                            dto.setPrice(ord.getPrice());
                            selectedOrders.add(dto);
                        }
                    } catch (NumberFormatException ignore) {}
                }
            } else {
                // nếu không chọn gì: lấy toàn bộ trong giỏ
                // tái sử dụng ViewCartDetail logic: getCartItems
                // để tránh phụ thuộc circular, truy vấn trực tiếp bằng SQL đơn giản
                String sql = "SELECT o.* FROM dbo.Orders o JOIN dbo.Carts c ON c.cartId=o.cartId WHERE c.customerId=?";
                List<Orders> ords = JdbcTemplateUtil.query(sql, Orders.class, customerId);
                for (Orders ord : ords) {
                    OrderDTO dto = new OrderDTO();
                    dto.setCartDetailId(ord.getCartDetailId());
                    dto.setCartId(ord.getCartId());
                    dto.setVehicleId(ord.getVehicleId());
                    dto.setRentStartDate(ord.getRentStartDate());
                    dto.setRentEndDate(ord.getRentEndDate());
                    dto.setPrice(ord.getPrice());
                    selectedOrders.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (selectedOrders.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ViewCartDetail");
            return;
        }

        // Nhóm theo (startDate, endDate)
        Map<String, List<OrderDTO>> groups = new HashMap<>();
        for (OrderDTO o : selectedOrders) {
            String key = o.getRentStartDate().toString() + "|" + o.getRentEndDate().toString();
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(o);
        }

        // Tạo hợp đồng cho từng nhóm
        List<CreatedContract> created = new ArrayList<>();

        for (Map.Entry<String, List<OrderDTO>> e : groups.entrySet()) {
            List<OrderDTO> orders = e.getValue();
            if (orders.isEmpty()) continue;
            LocalDateTime start = orders.get(0).getRentStartDate();
            LocalDateTime end = orders.get(0).getRentEndDate();
            
            // Tính tổng tiền thuê
            BigDecimal total = BigDecimal.ZERO;
            for (OrderDTO o : orders) {
                if (o.getPrice() != null) total = total.add(o.getPrice());
            }
            
            // Tính tiền đặt cọc (30% tổng tiền thuê, tối thiểu 500k)
            BigDecimal depositPercent = new BigDecimal("0.30");
            BigDecimal depositAmount = total.multiply(depositPercent);
            BigDecimal minDeposit = new BigDecimal("500000");
            if (depositAmount.compareTo(minDeposit) < 0) {
                depositAmount = minDeposit;
            }
            // Làm tròn đến hàng nghìn
            depositAmount = depositAmount.divide(new BigDecimal("1000"), 0, RoundingMode.UP)
                                       .multiply(new BigDecimal("1000"));

            // insert Contracts
            String insC = "INSERT INTO dbo.Contracts(customerId,startDate,endDate,status,totalAmount,depositAmount) VALUES (?,?,?,?,?,?)";
            int contractId = JdbcTemplateUtil.insertAndReturnKey(insC,
                    customerId,
                    start,
                    end,
                    "PENDING",
                    total,
                    depositAmount
            );

            // insert each ContractDetails
            String insD = "INSERT INTO dbo.ContractDetails(contractId,vehicleId,price,rentStartDate,rentEndDate,note) VALUES (?,?,?,?,?,NULL)";
            for (OrderDTO o : orders) {
                JdbcTemplateUtil.insertAndReturnKey(insD,
                        contractId,
                        o.getVehicleId(),
                        o.getPrice(),
                        o.getRentStartDate(),
                        o.getRentEndDate());
                // xóa order khỏi giỏ
                ordersDAO.deleteOrder(o.getCartDetailId());
            }

            CreatedContract cc = new CreatedContract();
            cc.setContractId(contractId);
            cc.setCustomerName(finalCustomerName);
            cc.setStart(start);
            cc.setEnd(end);
            cc.setTotal(total);
            cc.setDeposit(depositAmount);
            cc.getDetails().addAll(orders);
            created.add(cc);
        }

        request.setAttribute("created", created);
        request.getRequestDispatcher("/customer/checkout-result.jsp").forward(request, response);
    }
}
