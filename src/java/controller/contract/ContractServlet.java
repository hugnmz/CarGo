package controller.contract;

import dto.ContractDTO;
import dto.ContractDetailDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ContractService;
import util.di.DIContainer;
import util.MessageUtil;
import util.exception.BusinessException;
import util.exception.DataAccessException;
import util.exception.ValidationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/ContractServlet")
public class ContractServlet extends HttpServlet {

    private ContractService contractService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            throw new ServletException(MessageUtil.getError("error.system.dependency.injection"), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("list".equals(action)) {
            // Hiển thị danh sách hợp đồng
            List<ContractDTO> contracts = contractService.getAllContracts();
            request.setAttribute("contracts", contracts);
            request.getRequestDispatcher("/staff/staff.jsp").forward(request, response);

        } else if ("view".equals(action)) {
            String contractIdStr = request.getParameter("contractId");
            if (contractIdStr != null) {
                try {
                    Integer contractId = Integer.valueOf(contractIdStr);

                    var opt = contractService.getContractById(contractId);
                    if (opt.isPresent()) {
                        request.setAttribute("contract", opt.get());

                        List<ContractDetailDTO> details = contractService.getContractDetails(contractId);
                        request.setAttribute("details", details);

                        request.getRequestDispatcher("/customer/contract-view.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/staff?error=contract_not_found");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/staff");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/staff?error=missing_contract_id");
            }

        } else {
            // Mặc định hiển thị form tạo hợp đồng
            request.getRequestDispatcher("/staff/create-contract.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createContract(request, response);
        } else if ("update_status".equals(action)) {
            updateContractStatus(request, response);
        } else if ("create_after_payment".equals(action)) {
            createContractAfterPayment(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/staff?error=invalid_action");
        }
    }

    private void createContract(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy thông tin từ form
            Integer customerId = Integer.parseInt(request.getParameter("customerId"));
            Integer staffId = (Integer) request.getSession().getAttribute("staffId");

            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String totalAmountStr = request.getParameter("totalAmount");
            String depositAmountStr = request.getParameter("depositAmount");

            // Parse dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

            // Parse amounts
            BigDecimal totalAmount = new BigDecimal(totalAmountStr);
            BigDecimal depositAmount = new BigDecimal(depositAmountStr);

            // Tạo ContractDTO
            ContractDTO contractDTO = new ContractDTO();
            contractDTO.setCustomerId(customerId);
            contractDTO.setStaffId(staffId);
            contractDTO.setStartDate(startDate);
            contractDTO.setEndDate(endDate);
            contractDTO.setTotalAmount(totalAmount);
            contractDTO.setDepositAmount(depositAmount);
            contractDTO.setStatus("PENDING");

            // Tạo hợp đồng
            boolean success = contractService.createContract(contractDTO);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/staff?success=contract_created");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff?error=contract_creation_failed");
            }

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getErrorFromException(e));
            response.sendRedirect(request.getContextPath() + "/staff?error=contract_creation_failed");
        } catch (Exception e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getError("error.system.contract.process"));
            response.sendRedirect(request.getContextPath() + "/staff?error=contract_creation_failed");
        }
    }

    private void updateContractStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Integer contractId = Integer.valueOf(request.getParameter("contractId"));
            String status = request.getParameter("status");
            String reason = request.getParameter("reason"); // có thể null

            boolean success = contractService.updateContractStatus(contractId, status, reason); 
            if (success) {
                response.sendRedirect(request.getContextPath() + "/staff?success=status_updated&contractId=" + contractId + "&status=" + status);
            } else {
                response.sendRedirect(request.getContextPath() + "/staff?error=status_update_failed&contractId=" + contractId + "&status=" + status);
            }

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getErrorFromException(e));
            response.sendRedirect(request.getContextPath() + "/staff?error=status_update_failed&contractId=" + request.getParameter("contractId"));
        } catch (Exception e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getError("error.system.contract.process"));
            response.sendRedirect(request.getContextPath() + "/staff?error=status_update_failed&contractId=" + request.getParameter("contractId"));
        }
    }

    private void createContractAfterPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin từ request sau khi customer thanh toán
            Integer customerId = Integer.valueOf(request.getParameter("customerId"));
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String totalAmountStr = request.getParameter("totalAmount");
            String depositAmountStr = request.getParameter("depositAmount");

            // Parse thời gian
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

            // Parse tiền
            BigDecimal totalAmount = new BigDecimal(totalAmountStr);
            BigDecimal depositAmount = new BigDecimal(depositAmountStr);

            // Tạo contract pending chờ staff duyệt
            ContractDTO contractDTO = new ContractDTO();
            contractDTO.setCustomerId(customerId);
            contractDTO.setStaffId(null); // Chưa assign staff
            contractDTO.setStartDate(startDate);
            contractDTO.setEndDate(endDate);
            contractDTO.setTotalAmount(totalAmount);
            contractDTO.setDepositAmount(depositAmount);
            contractDTO.setStatus("PENDING"); // Dùng chữ hoa để thống nhất

            // Lưu contract
            boolean success = contractService.createContract(contractDTO);

            if (success) {
                // Chuyển sang trang thông báo chờ duyệt
                response.sendRedirect(request.getContextPath() + "/customer/contract-pending.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/customer/payment-failed.jsp");
            }

        } catch (ValidationException | BusinessException | DataAccessException e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getErrorFromException(e));
            response.sendRedirect(request.getContextPath() + "/customer/payment-failed.jsp");
        } catch (Exception e) {
            request.getSession().setAttribute("flash_error", MessageUtil.getError("error.system.contract.process"));
            response.sendRedirect(request.getContextPath() + "/customer/payment-failed.jsp");
        }
    }

}