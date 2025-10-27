package controller.contract;

import dto.ContractDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ContractService;
import util.di.DIContainer;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@WebServlet("/ContractServlet")
public class ContractServlet extends HttpServlet {
    
    // service xu ly hop dong
    private ContractService contractService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // khoi tao contract service tu di container
            contractService = DIContainer.get(ContractService.class);
        } catch (Exception e) {
            // nem loi neu khoi tao service that bai
            throw new RuntimeException("Failed to initialize ContractService", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // lay tham so action de xac dinh hanh dong can thuc hien
        String action = request.getParameter("action");
        
        if ("list".equals(action)) {
            // hien thi danh sach hop dong
            List<ContractDTO> contracts = contractService.getAllContracts();
            // dat danh sach hop dong vao request de truyen sang jsp
            request.setAttribute("contracts", contracts);
            // chuyen huong den trang danh sach hop dong
            request.getRequestDispatcher("/staff/contracts.jsp").forward(request, response);
            
        } else if ("view".equals(action)) {
            // xem chi tiet hop dong
            String contractIdStr = request.getParameter("contractId");
            if (contractIdStr != null) {
                try {
                    // chuyen doi contract id tu string sang integer
                    Integer contractId = Integer.parseInt(contractIdStr);
                    // lay thong tin hop dong tu database va dat vao request
                    contractService.getContractById(contractId).ifPresent(contract -> {
                        request.setAttribute("contract", contract);
                    });
                    // chuyen huong den trang chi tiet hop dong
                    request.getRequestDispatcher("/staff/contract-detail.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    // neu contract id khong hop le thi chuyen ve trang staff voi thong bao loi
                    response.sendRedirect(request.getContextPath() + "/staff/staff.jsp?error=invalid_contract_id");
                }
            } else {
                // neu khong co contract id thi chuyen ve trang staff voi thong bao loi
                response.sendRedirect(request.getContextPath() + "/staff/staff.jsp?error=missing_contract_id");
            }
        } else {
            // mac dinh hien thi form tao hop dong
            request.getRequestDispatcher("/staff/create-contract.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // lay session hien tai, neu khong co thi null
        HttpSession session = request.getSession(false);
        // kiem tra session va staff id, neu khong co thi chuyen den trang dang nhap admin
        if (session == null || session.getAttribute("staffId") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/admin_login.jsp");
            return;
        }
        
        // lay tham so action de xac dinh hanh dong can thuc hien
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            // neu action la create thi goi method tao hop dong
            createContract(request, response);
        } else if ("update_status".equals(action)) {
            // neu action la update_status thi goi method cap nhat trang thai hop dong
            updateContractStatus(request, response);
        } else {
            // neu action khong hop le thi chuyen ve trang staff voi thong bao loi
            response.sendRedirect(request.getContextPath() + "/staff/staff.jsp?error=invalid_action");
        }
    }
    
    private void createContract(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // lay thong tin tu form tao hop dong
            Integer customerId = Integer.parseInt(request.getParameter("customerId"));
            // lay staff id tu session
            Integer staffId = (Integer) request.getSession().getAttribute("staffId");
            
            // lay cac tham so ngay thang va tien tu form
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String totalAmountStr = request.getParameter("totalAmount");
            String depositAmountStr = request.getParameter("depositAmount");
            
            // chuyen doi ngay thang tu string sang localdatetime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);
            
            // chuyen doi tien tu string sang bigdecimal
            BigDecimal totalAmount = new BigDecimal(totalAmountStr);
            BigDecimal depositAmount = new BigDecimal(depositAmountStr);
            
            // tao doi tuong contract dto de luu thong tin hop dong
            ContractDTO contractDTO = new ContractDTO();
            contractDTO.setCustomerId(customerId);
            contractDTO.setStaffId(staffId);
            contractDTO.setStartDate(startDate);
            contractDTO.setEndDate(endDate);
            contractDTO.setTotalAmount(totalAmount);
            contractDTO.setDepositAmount(depositAmount);
            contractDTO.setStatus("pending");
            
            // goi service de tao hop dong trong database
            boolean success = contractService.createContract(contractDTO);
            
            if (success) {
                // neu tao hop dong thanh cong thi chuyen den trang staff voi thong bao thanh cong
                response.sendRedirect(request.getContextPath() + "/staff/staff.jsp?success=contract_created");
            } else {
                // neu tao hop dong that bai thi chuyen den trang staff voi thong bao loi
                response.sendRedirect(request.getContextPath() + "/staff/staff.jsp?error=contract_creation_failed");
            }
            
        } catch (Exception e) {
            // log loi ra console
            e.printStackTrace();
            // chuyen den trang staff voi thong bao loi
            response.sendRedirect(request.getContextPath() + "/staff/staff.jsp?error=contract_creation_error");
        }
    }
    
  
    private void updateContractStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // lay contract id tu tham so request
            Integer contractId = Integer.parseInt(request.getParameter("contractId"));
            // lay trang thai moi tu tham so request
            String status = request.getParameter("status");
            
            // goi service de cap nhat trang thai hop dong trong database
            boolean success = contractService.updateContractStatus(contractId, status);
            
            if (success) {
                // neu cap nhat thanh cong thi chuyen den trang xem chi tiet hop dong voi thong bao thanh cong
                response.sendRedirect(request.getContextPath() + "/ContractServlet?action=view&contractId=" + contractId + "&success=status_updated");
            } else {
                // neu cap nhat that bai thi chuyen den trang xem chi tiet hop dong voi thong bao loi
                response.sendRedirect(request.getContextPath() + "/ContractServlet?action=view&contractId=" + contractId + "&error=status_update_failed");
            }
            
        } catch (Exception e) {
            // log loi ra console
            e.printStackTrace();
            // chuyen den trang staff voi thong bao loi
            response.sendRedirect(request.getContextPath() + "/staff/staff.jsp?error=status_update_error");
        }
    }
}