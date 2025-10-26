package controller.Payment;

import dto.PaymentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.PaymentService;
import dao.PaymentMethodsDAO;
import dto.ContractDTO;
import model.PaymentMethods;
import util.di.DIContainer;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import service.ContractService;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {

    private PaymentService paymentService;
    private PaymentMethodsDAO paymentMethodsDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            paymentService = DIContainer.get(PaymentService.class);
            paymentMethodsDAO = DIContainer.get(PaymentMethodsDAO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize services", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("list".equals(action)) {
            // Hiển thị danh sách thanh toán
            List<PaymentDTO> payments = paymentService.getAllPayments();
            request.setAttribute("payments", payments);
            request.getRequestDispatcher("/staff/payments.jsp").forward(request, response);

        } else if ("by_contract".equals(action)) {
            // Lấy thanh toán theo hợp đồng
            String contractIdStr = request.getParameter("contractId");
            if (contractIdStr != null) {
                try {
                    Integer contractId = Integer.parseInt(contractIdStr);
                    List<PaymentDTO> payments = paymentService.getPaymentsByContract(contractId);
                    request.setAttribute("payments", payments);
                    request.setAttribute("contractId", contractId);
                    request.getRequestDispatcher("/staff/contract-payments.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/staff?error=invalid_contract_id");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/staff?error=missing_contract_id");
            }
        } else {
            // Mặc định hiển thị form tạo thanh toán
            request.getRequestDispatcher("/staff/create-payment.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("staffId") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/admin_login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createPayment(request, response);
        } else if ("update_status".equals(action)) {
            updatePaymentStatus(request, response);
        } else if ("create_after_payment".equals(action)) {
            createPaymentAndContract(request, response); // mới
        } else {
            response.sendRedirect(request.getContextPath() + "/staff?error=invalid_action");
        }

    }

    private void createPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy thông tin từ form
            Integer contractId = Integer.parseInt(request.getParameter("contractId"));
            String amountStr = request.getParameter("amount");
            Integer methodId = Integer.parseInt(request.getParameter("methodId"));

            BigDecimal amount = new BigDecimal(amountStr);

            // Lấy tên phương thức thanh toán từ methodId
            PaymentMethods paymentMethod = paymentMethodsDAO.getPaymentMethodById(methodId);
            if (paymentMethod == null) {
                response.sendRedirect(request.getContextPath() + "/staff?error=invalid_payment_method");
                return;
            }

            // Tạo PaymentDTO
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setContractId(contractId);
            paymentDTO.setAmount(amount);
            paymentDTO.setMethodName(paymentMethod.getMethodName());
            paymentDTO.setStatus("pending");

            // Tạo thanh toán
            boolean success = paymentService.createPayment(paymentDTO);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/staff?success=payment_created");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff?error=payment_creation_failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/staff?error=payment_creation_error");
        }
    }

    private void updatePaymentStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Integer paymentId = Integer.parseInt(request.getParameter("paymentId"));
            String status = request.getParameter("status");

            boolean success = paymentService.updatePaymentStatus(paymentId, status);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/PaymentServlet?action=list&success=status_updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/PaymentServlet?action=list&error=status_update_failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/staff?error=status_update_error");
        }
    }

    private void createPaymentAndContract(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("customerId") == null) {
                response.sendRedirect(request.getContextPath() + "/customer/login.jsp");
                return;
            }

            Integer customerId = (Integer) session.getAttribute("customerId");
            BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));
            BigDecimal depositAmount = new BigDecimal(request.getParameter("depositAmount"));
            LocalDateTime startDate = LocalDateTime.parse(request.getParameter("startDate"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            LocalDateTime endDate = LocalDateTime.parse(request.getParameter("endDate"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            Integer methodId = Integer.parseInt(request.getParameter("methodId"));

            // 1. Tạo Payment
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setAmount(totalAmount);
            paymentDTO.setMethodName(paymentMethodsDAO.getPaymentMethodById(methodId).getMethodName());
            paymentDTO.setStatus("completed");
            boolean paymentCreated = paymentService.createPayment(paymentDTO);

            if (!paymentCreated) {
                response.sendRedirect(request.getContextPath() + "/customer/cart.jsp?error=payment_failed");
                return;
            }

            // 2. Tạo Contract
            ContractService contractService = DIContainer.get(ContractService.class);
            ContractDTO contractDTO = new ContractDTO();
            contractDTO.setCustomerId(customerId);
            contractDTO.setTotalAmount(totalAmount);
            contractDTO.setDepositAmount(depositAmount);
            contractDTO.setStartDate(startDate);
            contractDTO.setEndDate(endDate);
            contractDTO.setStatus("pending"); // staff sẽ xử lý sau

            boolean contractCreated = contractService.createContract(contractDTO);

            if (contractCreated) {
                // Xóa giỏ hàng (nếu cần)
                // cartService.clearCart(customerId);

                request.setAttribute("message", "Thanh toán và tạo hợp đồng thành công! Đang chờ nhân viên xử lý.");
                request.getRequestDispatcher("/customer/success.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Thanh toán thành công nhưng không tạo được hợp đồng.");
                request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/customer/cart.jsp?error=payment_contract_error");
        }
    }

}
