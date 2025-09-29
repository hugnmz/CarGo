package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class PaymentDTO {
    private Integer paymentId;       // ID thanh toán
    private Integer contractId;      // ID hợp đồng
    private BigDecimal amount;        // Số tiền thanh toán
    private String methodName;        // Tên phương thức thanh toán
    private String status;            // Trạng thái thanh toán
    private LocalDateTime paymentDate; // Ngày thanh toán
    
    // Constructors
    public PaymentDTO() {}
    
    // Getters and Setters
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    
    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
