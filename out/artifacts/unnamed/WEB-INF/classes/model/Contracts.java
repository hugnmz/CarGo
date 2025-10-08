package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class Contracts {
    private Integer contractId;       // ID hợp đồng
    private Integer customerId;      // ID khách hàng
    private Integer staffId;         // ID nhân viên xử lý
    private LocalDateTime startDate; // Ngày bắt đầu hợp đồng
    private LocalDateTime endDate;   // Ngày kết thúc hợp đồng
    private String status;           // Trạng thái hợp đồng (pending, accepted, rejected)
    private LocalDateTime createAt; // Ngày tạo hợp đồng
    private BigDecimal totalAmount;  // Tong tien
    private BigDecimal depositAmount; // Tien coc
    
    // Các đối tượng liên quan
    private Customers customer;      // Khách hàng
    private Users staff;            // Nhân viên
    private List<ContractDetails> contractDetails; // Chi tiết hợp đồng
    private List<Payments> payments; // Thanh toán
    
    // Constructors
    public Contracts() {}
    
    public Contracts(Integer customerId, LocalDateTime startDate, LocalDateTime endDate) {
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "pending";
        this.createAt = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
        this.depositAmount = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }
    
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    
    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    
    public Customers getCustomer() { return customer; }
    public void setCustomer(Customers customer) { this.customer = customer; }
    
    public Users getStaff() { return staff; }
    public void setStaff(Users staff) { this.staff = staff; }
    
    public List<ContractDetails> getContractDetails() { return contractDetails; }
    public void setContractDetails(List<ContractDetails> contractDetails) { this.contractDetails = contractDetails; }
    
    public List<Payments> getPayments() { return payments; }
    public void setPayments(List<Payments> payments) { this.payments = payments; }
}
