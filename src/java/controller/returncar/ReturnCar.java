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
import util.AuthUtil;
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
        // Lấy danh sách tạm thời từ queue 
        List<RequestReturnCar> requests = new ArrayList<>(returnCarService.all());
        req.setAttribute("requests", requests);

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
            req.getSession().setAttribute("error", "Không nhận được mã hợp đồng");
            req.getRequestDispatcher("/customer/contract-view.jsp").forward(req, resp);
            return;
        }

        int id = 0;
        if (errors.isEmpty()) {
            try {
                id = Integer.parseInt(contractIdParam.trim());
            } catch (NumberFormatException ex) {
                req.getSession().setAttribute("error", "Mã hợp đồng không hợp lệ");
                req.getRequestDispatcher("/customer/contract-view.jsp").forward(req, resp);
                return;
            }
        }

        Optional<ContractDTO> dtoOpt = Optional.empty();
        if (errors.isEmpty()) {
            try {
                dtoOpt = contractService.getContractById(id);
                if (dtoOpt.isEmpty()) {
                    req.getSession().setAttribute("error", "Không tồn tại hợp đồng");
                    resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + id);
                    return;
                } else {
                    if ("PENDING".equals(dtoOpt.get().getStatus()) || "REJECTED".equals(dtoOpt.get().getStatus())) {
                        req.getSession().setAttribute("error", "Hợp đồng không hợp lệ");
                        resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + id);
                        return;

                    } else if ("RETURNED".equals(dtoOpt.get().getStatus())) {
                        req.getSession().setAttribute("error", "Hợp đồng đã xử lí trả xe");
                        resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + id);
                        return;
                    } else if ("COMPLETED".equals(dtoOpt.get().getStatus())) {
                        req.getSession().setAttribute("error", "Hợp đồng đã hoàn thành");
                        resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + id);
                        return;
                    }
                }
            } catch (Exception e) {
                req.getSession().setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + id);
                return;
            }
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
            req.getSession().setAttribute("message", "Gửi yêu cầu trả xe thành công");
        } else {
            // Đã có trong hàng chờ
            req.getSession().setAttribute("message", "Hợp đồng đang chờ xử lí");
        }

        // Redirect về trang khách để tránh submit lại khi F5
        resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + id);
    }

}
