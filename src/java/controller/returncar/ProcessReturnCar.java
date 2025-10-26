package controller.returncar;

import dto.ContractDTO;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import model.RequestReturnCar;
import service.ContractService;
import service.ReturnCarService;
import util.di.DIContainer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ProcessReturnCar", urlPatterns = {"/processreturncar"})
public class ProcessReturnCar extends HttpServlet {

    private ContractService contractService;
    private ReturnCarService returnCarService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

        try {
            contractService = DIContainer.get(ContractService.class);
            returnCarService = DIContainer.get(ReturnCarService.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ContractService", e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST");
    }

    /**
     * Xử lý xác nhận trả xe từ form.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/returncar");
            return;
        }

        //  Kiểm tra CSRF
        String csrfSession = (String) session.getAttribute("csrf");
        String csrfParam = req.getParameter("csrf");
        if (csrfSession != null && !csrfSession.equals(csrfParam)) {
            session.setAttribute("flash", "Phiên làm việc không hợp lệ (CSRF).");
            resp.sendRedirect(req.getContextPath() + "/returncar");
            return;
        }

        // Hợp đồng
        Integer contractId;
        try {
            contractId = Integer.valueOf(req.getParameter("contractId"));
        } catch (Exception e) {
            session.setAttribute("flash", "contractId không hợp lệ.");
            resp.sendRedirect(req.getContextPath() + "/returncar");
            return;
        }

        // Whitelist theo phiên (được set ở /returncar GET)
        Map<Integer, RequestReturnCar> pendingMap
                = (Map<Integer, RequestReturnCar>) session.getAttribute("pendingReturnMap");
        if (pendingMap == null || !pendingMap.containsKey(contractId)) {
            session.setAttribute("flash", "Hợp đồng không nằm trong danh sách cần xử lý.");
            resp.sendRedirect(req.getContextPath() + "/returncar");
            return;
        }

        // Phân nhánh theo tham số confirm:
        boolean confirm = "1".equals(req.getParameter("confirm"));
        if (!confirm) {
            // ========== POST #1: HIỂN THỊ CHI TIẾT ==========
            RequestReturnCar currentRequest = pendingMap.get(contractId);

            // (khuyến nghị) kiểm tra còn nằm trong hàng chờ service không
            if (!returnCarService.contains(contractId)) {
                pendingMap.remove(contractId);
                session.setAttribute("pendingReturnMap", pendingMap);
                session.setAttribute("flash", "Yêu cầu đã rời khỏi hàng chờ, vui lòng tải lại.");
                resp.sendRedirect(req.getContextPath() + "/returncar");
                return;
            }

            // Đẩy object sang JSP chi tiết
            req.setAttribute("currentRequest", currentRequest);
            req.getRequestDispatcher("/staff/process_returncar.jsp").forward(req, resp);
            return;
        }

        // ========== POST #2: XÁC NHẬN & CẬP NHẬT ==========
        try {
            // Lấy dữ liệu form xác nhận (phí muộn + hư hại)
            BigDecimal lateFee = parseMoney(req.getParameter("lateFee"), "0");
            String damageFeeParam = req.getParameter("damageFee");
            BigDecimal damageFee = "custom".equalsIgnoreCase(damageFeeParam)
                    ? parseMoney(req.getParameter("customAmount"), "0")
                    : parseMoney(damageFeeParam, "0");
            

            String note = req.getParameter("note");
            
            
            // Lấy hợp đồng từ DB
            Optional<ContractDTO> contractOpt = contractService.getContractById(contractId);
            if (contractOpt.isEmpty()) {
                session.setAttribute("flash", "Không tìm thấy hợp đồng mã" + contractId);
                resp.sendRedirect(req.getContextPath() + "/returncar");
                return;
            }
            ContractDTO contract = contractOpt.get();
            BigDecimal totalAmount= safe(lateFee).add(safe(damageFee)).add(contract.getTotalAmount());
            
            //update tổng tiền sau khi trả xe
            contractService.updateContractTotalAmount(contractId, totalAmount);
            
            
            // Dọn dẹp: bỏ khỏi whitelist theo phiên + hàng chờ service
            pendingMap.remove(contractId);
            session.setAttribute("pendingReturnMap", pendingMap);
            returnCarService.remove(contractId);

            session.setAttribute("flash",
                    "Đã hoàn tất trả xe #" + contractId );
            resp.sendRedirect(req.getContextPath() + "/returncar");
        } catch (Exception ex) {
            ex.printStackTrace();
            session.setAttribute("flash", "Có lỗi khi cập nhật. Vui lòng thử lại.");
            resp.sendRedirect(req.getContextPath() + "/returncar");
        }
    }


    private BigDecimal parseMoney(String raw, String defaultVal) {
        try {
            String v = (raw == null || raw.isBlank()) ? defaultVal : raw.trim();
            return new BigDecimal(v);
        } catch (Exception e) {
            return new BigDecimal(defaultVal);
        }
    }

    private BigDecimal safe(BigDecimal x) {
        return x == null ? BigDecimal.ZERO : x;
    }
}
