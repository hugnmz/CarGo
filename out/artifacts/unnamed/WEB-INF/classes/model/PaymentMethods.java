package model;

import java.util.List;


public class PaymentMethods {
    private Integer methodId;        // ID phương thức thanh toán
    private String methodName;       // Tên phương thức thanh toán
    private List<Payments> payments; // Danh sách thanh toán
    
    // Constructors
    public PaymentMethods() {}
    
    public PaymentMethods(String methodName) {
        this.methodName = methodName;
    }
    
    // Getters and Setters
    public Integer getMethodId() { return methodId; }
    public void setMethodId(Integer methodId) { this.methodId = methodId; }
    
    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    
    public List<Payments> getPayments() { return payments; }
    public void setPayments(List<Payments> payments) { this.payments = payments; }
}
