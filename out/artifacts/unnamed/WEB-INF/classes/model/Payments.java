package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Payments {
    private Integer paymentId;       // ID thanh toán
    private Integer contractId;      // ID hợp đồng
    private BigDecimal amount;       // So tien thanh toan
    private Integer methodId;        // ID phương thức thanh toán
    private String status;           // Trạng thái thanh toán (pending, completed, failed)
    private LocalDateTime paymentDate; // Ngày thanh toán
    
    // Các đối tượng liên quan
    private Contracts contract;      // Hợp đồng
    private PaymentMethods paymentMethod; // Phương thức thanh toán
    
    // Constructors
    public Payments() {}
    
    public Payments(Integer contractId, BigDecimal amount, Integer methodId) {
        this.contractId = contractId;
        this.amount = amount;
        this.methodId = methodId;
        this.status = "pending";
        this.paymentDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    
    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public Integer getMethodId() { return methodId; }
    public void setMethodId(Integer methodId) { this.methodId = methodId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public Contracts getContract() { return contract; }
    public void setContract(Contracts contract) { this.contract = contract; }
    
    public PaymentMethods getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethods paymentMethod) { this.paymentMethod = paymentMethod; }
}
