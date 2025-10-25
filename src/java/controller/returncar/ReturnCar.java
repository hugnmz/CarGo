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
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import model.RequestReturnCar;
import service.ContractService;
import util.di.DIContainer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ReturnCar", urlPatterns = {"/returncar"})
public class ReturnCar extends HttpServlet {

    // Hàng đợi yêu cầu theo contractId để chống trùng lặp
    private final ConcurrentMap<Integer, RequestReturnCar> queue = new ConcurrentHashMap<>();

    private ContractService contractService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

        try {
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ContractService", e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Render danh sách cho staff
        req.setAttribute("requests", new ArrayList<>(queue.values()));
        req.getRequestDispatcher("staff/returncar.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
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

        // Nếu có lỗi: trả lại cho KH (customer.jsp), không đẩy sang trang staff
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            // Ở đây forward để KH thấy lỗi ngay trên trang của họ
            req.getRequestDispatcher("/staff/staff.jsp").forward(req, resp);
            return;
        }

        // Không lỗi → thêm/ cập nhật vào queue theo contractId (chống trùng)
        ContractDTO dto = dtoOpt.get();
        int cid = dto.getContractId();

        queue.compute(cid, (k, existing) -> {
            if (existing == null) {
                return new RequestReturnCar(dto, LocalDateTime.now());
            }
            return existing;
        });

        // Flash message & PRG
        req.getSession().setAttribute("message", "Gửi yêu cầu trả xe thành công");
        // Redirect về trang khách để tránh submit lại khi F5
        resp.sendRedirect(req.getContextPath() + "/staff/staff.jsp");
    }

}
