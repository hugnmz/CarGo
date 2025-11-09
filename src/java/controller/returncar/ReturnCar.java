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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.RequestReturnCar;
import service.ContractService;
import service.ReturnCarService;
import util.di.DIContainer;
import util.AuthUtil;
import util.MessageUtil;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import java.util.UUID;

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
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy danh sách tạm thời từ queue 
        List<RequestReturnCar> requests = new ArrayList<>(returnCarService.all());
        req.setAttribute("requests", requests);

        //tạo mã csrf để bảo mật
        String csrf = UUID.randomUUID().toString();
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
        // kiem tra dang nhap, neu chua dang nhap thi chuyen den trang dang nhap
        if (!AuthUtil.requireLogin(req, resp)) {
            return;
        }
        
        try {
            String contractIdParam = req.getParameter("contractId");

            // Xóa tất cả check - đã chuyển vào service
            // Parse contractId
            int id = (contractIdParam != null && !contractIdParam.trim().isEmpty()) 
                ? Integer.parseInt(contractIdParam.trim()) : 0;

            // Gọi service - service sẽ check và throw WebException
            Optional<ContractDTO> dtoOpt = contractService.getContractById(id);
            if (dtoOpt.isPresent()) {
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
            }

            // Redirect về trang khách để tránh submit lại khi F5
            resp.sendRedirect(req.getContextPath() + "/view-contract?contractId=" + id);
        } catch (ValidationException | BusinessException | DataAccessException e) {
            req.getSession().setAttribute("error", MessageUtil.getErrorFromException(e));
            req.getRequestDispatcher("/customer/contract-view.jsp").forward(req, resp);
        } catch (Exception e) {
            req.getSession().setAttribute("error", MessageUtil.getError("error.system.return.car"));
            req.getRequestDispatcher("/customer/contract-view.jsp").forward(req, resp);
        }
    }


}
