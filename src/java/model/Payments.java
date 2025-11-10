package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import util.di.annotation.Column;
import util.di.annotation.Nested;


public class Payments {
    
    @Column()
    private Integer paymentId;       // ID thanh toán
    @Column()
    private Integer contractId;      // ID hợp đồng
    @Column()
    private BigDecimal amount;       // So tien thanh toan
    @Column()
    private Integer methodId;        // ID phương thức thanh toán
    @Column()
    private String status;           // Trạng thái thanh toán (pending, completed, failed)
    @Column()
    private LocalDateTime paymentDate; // Ngày thanh toán
    
    // Các đối tượng liên quan
    @Nested()
    private Contract contract;      // Hợp đồng
    @Nested()
    private PaymentMethods paymentMethod; // Phương thức thanh toán
    @Nested()
    private String transactionCode;
    // Constructors
    public Payments() {}
    
    public Payments(Integer contractId, BigDecimal amount, Integer methodId) {
        this.contractId = contractId;
        this.amount = amount;
        this.methodId = methodId;
        this.status = "pending";
        this.paymentDate = LocalDateTime.now();
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
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
    
    public Contract getContract() { return contract; }
    public void setContract(Contract contract) { this.contract = contract; }
    
    public PaymentMethods getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethods paymentMethod) { this.paymentMethod = paymentMethod; }
}
