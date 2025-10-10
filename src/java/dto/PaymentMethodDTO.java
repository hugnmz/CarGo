package dto;

import java.util.List;

public class PaymentMethodDTO {
    private Integer methodId;        // ID phương thức thanh toán
    private String methodName;       // Tên phương thức thanh toán
    
    // Thống kê (optional)
    private Integer paymentCount;    // Số lượng thanh toán
    private java.math.BigDecimal totalAmount; // Tổng số tiền
    
    // Danh sách thanh toán (optional)
    private List<PaymentDTO> payments; // Danh sách thanh toán
    
    // Constructors
    public PaymentMethodDTO() {}
    
    public PaymentMethodDTO(String methodName) {
        this.methodName = methodName;
    }
    
    public PaymentMethodDTO(Integer methodId, String methodName) {
        this.methodId = methodId;
        this.methodName = methodName;
    }
    
    // Getters and Setters
    public Integer getMethodId() { return methodId; }
    public void setMethodId(Integer methodId) { this.methodId = methodId; }
    
    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    
    public Integer getPaymentCount() { return paymentCount; }
    public void setPaymentCount(Integer paymentCount) { this.paymentCount = paymentCount; }
    
    public java.math.BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public List<PaymentDTO> getPayments() { return payments; }
    public void setPayments(List<PaymentDTO> payments) { this.payments = payments; }
}
