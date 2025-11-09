
package service;

import dto.PaymentDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * PaymentService - Service layer cho Payment business logic
 */
public interface PaymentService {

    /**
     * Lấy tất cả thanh toán và chuyển thành DTO
     */
    List<PaymentDTO> getAllPayments();

    /**
     * Lấy thanh toán theo ID và chuyển thành DTO
     */
    Optional<PaymentDTO> getPaymentById(Integer paymentId);

    /**
     * Lấy thanh toán theo hợp đồng
     */
    List<PaymentDTO> getPaymentsByContract(Integer contractId);

    /**
     * Thêm thanh toán mới
     */
    boolean addPayment(PaymentDTO paymentDTO);

    /**
     * Cập nhật trạng thái thanh toán
     */
    boolean updatePaymentStatus(Integer paymentId, String status);

    /**
     * Tìm thanh toán đang pending
     */
    Optional<PaymentDTO> findPendingPayment(Integer contractId, BigDecimal amount);

    /**
     * Hoàn thành thanh toán theo ID
     */
    boolean completePaymentById(Integer paymentId);

    /**
     * Kiểm tra thanh toán đã hoàn thành chưa
     */
    boolean isPaymentCompleted(Integer paymentId);

    /**
     * Kiểm tra hợp đồng đã có thanh toán hoàn thành chưa
     */
    boolean hasCompleted(Integer contractId);

    /**
     * Lấy trạng thái thanh toán của hợp đồng
     */
    String getPaymentStatus(Integer contractId);

    /**
     * Tính tổng tiền đã thanh toán của hợp đồng
     */
    BigDecimal getTotalPaidAmount(Integer contractId);

    /**
     * Tính số tiền còn lại cần thanh toán
     */
    BigDecimal getRemainingAmount(Integer contractId);

    /**
     * Lấy tổng tiền của hợp đồng (để hiển thị payment) Method này gọi
     * ContractsDAO bên trong để không cần tạo ContractService
     */
    Optional<BigDecimal> getContractTotalAmount(Integer contractId);

    public PaymentDTO createPendingPayment(int contractId, BigDecimal amount);
    
    
int markDepositPaid(int contractId, BigDecimal amount); // trả về paymentId
}
