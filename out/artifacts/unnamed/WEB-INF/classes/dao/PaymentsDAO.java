package dao;

import model.Payments;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentsDAO {
    
    // Lấy tất cả thanh toán
    List<Payments> getAllPayments();
    
    // Lấy thanh toán theo ID
    Payments getPaymentById(Integer paymentId);
    
    // Lấy thanh toán theo hợp đồng
    List<Payments> getPaymentsByContract(Integer contractId);
    
    // Lấy thanh toán theo trạng thái
    List<Payments> getPaymentsByStatus(String status);
    
    // Lấy thanh toán theo phương thức
    List<Payments> getPaymentsByMethod(Integer methodId);
    
    // Thêm thanh toán mới
    boolean addPayment(Payments payment);
    
    // Cập nhật thanh toán
    boolean updatePayment(Payments payment);
    
    // Cập nhật trạng thái thanh toán
    boolean updatePaymentStatus(Integer paymentId, String status);
    
    // Xóa thanh toán
    boolean deletePayment(Integer paymentId);
    
    // Tính tổng tiền đã thanh toán của hợp đồng
    BigDecimal getTotalPaidAmount(Integer contractId);
    
    // Tính số tiền còn lại cần thanh toán
    BigDecimal getRemainingAmount(Integer contractId);
    
    // Kiểm tra thanh toán có thành công không
    boolean isPaymentCompleted(Integer paymentId);
}
