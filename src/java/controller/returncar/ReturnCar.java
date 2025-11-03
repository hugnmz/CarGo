/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
@WebServlet(name = "ReturnCar", urlPatterns = {"/returncar"})
public class ReturnCar extends HttpServlet {

    private ReturnCarService returnCarService;
    private ContractService contractService;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy danh sách tạm thời từ queue (state toàn ứng dụng)
        List<RequestReturnCar> requests = new ArrayList<>(returnCarService.all());
        req.setAttribute("requests", requests);
        
        //giả định mã nhân viên
        req.getSession().setAttribute("staffId", 1);
        //tạo mã csrf để bảo mật
        String csrf = java.util.UUID.randomUUID().toString();
        req.getSession().setAttribute("csrf", csrf);
        // Tạo session whitelist: chỉ những contractId này mới được phép xử lý
        Map<Integer, RequestReturnCar> pendingMap = new LinkedHashMap<>();
        for (RequestReturnCar r : requests) {
            pendingMap.put(r.getContract().getContractId(), r);
        }
        req.getSession().setAttribute("pendingReturnMap", pendingMap);

        // Forward tới trang danh sách
        req.getRequestDispatcher("staff/returncar.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        HttpSession session = req.getSession();
        String contractIdParam = req.getParameter("contractId");

        // 1) Validate input
        if (contractIdParam == null || contractIdParam.trim().isEmpty()) {
            errors.add("Không nhận được mã hợp đồng");
        }

        int id = 0;
        if (errors.isEmpty()) {
            try {
                id = Integer.parseInt(contractIdParam.trim());
            } catch (NumberFormatException ex) {
                errors.add("Mã hợp đồng không hợp lệ");
            }
        }

        Optional<ContractDTO> dtoOpt = Optional.empty();
        if (errors.isEmpty()) {
            try {
                dtoOpt = contractService.getContractById(id);
                if (dtoOpt.isEmpty()) {
                    errors.add("Không tồn tại hợp đồng");
                } else {
                    if ("PENDING".equals(dtoOpt.get().getStatus()) || "REJECTED".equals(dtoOpt.get().getStatus())) {
                        errors.add("Hợp đồng không hợp lệ");
                    } else if ("COMPLETED".equals(dtoOpt.get().getStatus())) {
                        errors.add("Hợp đồng đã hoàn thành");
                    }
                }
            } catch (Exception e) {
                errors.add("Lỗi hệ thống: " + e.getMessage());
            }
        }

        // Nếu có lỗi: trả lại cho KH 
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            // Ở đây forward để KH thấy lỗi ngay trên trang của họ
            req.getRequestDispatcher("/staff/staff.jsp").forward(req, resp);
            return;
        }

        // Không lỗi → thêm/ cập nhật vào queue theo contractId (chống trùng)
        ContractDTO dto = dtoOpt.get();
        dto.setContractDetails(contractService.getContractDetails(id));
        int cid = dto.getContractId();

        RequestReturnCar existing = returnCarService.get(cid);
        if (existing == null) {
            // Chưa có → thêm mới
            RequestReturnCar newReq = new RequestReturnCar(dto, LocalDateTime.now());
            returnCarService.putIfAbsentOrKeep(cid, newReq);
            req.getSession().setAttribute("flash", "Gửi yêu cầu trả xe thành công");
        } else {
            // Đã có trong hàng chờ
            req.getSession().setAttribute("flash", "Hợp đồng đang chờ xử lí");
        }

        // Redirect về trang khách để tránh submit lại khi F5
        resp.sendRedirect(req.getContextPath() + "/staff/staff.jsp");
    }

}
