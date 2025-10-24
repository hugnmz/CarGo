package service;

import dto.PaymentDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    
    // Tạo thanh toán mới
    boolean createPayment(PaymentDTO paymentDTO);
    
    // Lấy thanh toán theo ID
    Optional<PaymentDTO> getPaymentById(Integer paymentId);
    
    // Lấy tất cả thanh toán
    List<PaymentDTO> getAllPayments();
    
    // Lấy thanh toán theo hợp đồng
    List<PaymentDTO> getPaymentsByContract(Integer contractId);
    
    // Cập nhật trạng thái thanh toán
    boolean updatePaymentStatus(Integer paymentId, String status);
    
    // Tính tổng tiền đã thanh toán
    BigDecimal getTotalPaidAmount(Integer contractId);
    
    // Tính số tiền còn lại
    BigDecimal getRemainingAmount(Integer contractId);
    
    // Xóa thanh toán
    boolean deletePayment(Integer paymentId);
}