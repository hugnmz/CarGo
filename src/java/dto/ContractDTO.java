package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class ContractDTO {
    private Integer contractId;      // ID hợp đồng
    private Integer customerId;      // ID khách hàng
    private String customerName;     // Tên khách hàng
    private String customerPhone;     // Số điện thoại khách hàng
    private Integer staffId;         // ID nhân viên
    private String staffName;         // Tên nhân viên
    private LocalDateTime startDate; // Ngày bắt đầu
    private LocalDateTime endDate;   // Ngày kết thúc
    private String status;           // Trạng thái
    private LocalDateTime createAt;  // Ngày tạo
    private BigDecimal totalAmount;  // Tổng tiền
    private BigDecimal depositAmount; // Tiền cọc
    private List<ContractDetailDTO> contractDetails; // Chi tiết hợp đồng
    private List<PaymentDTO> payments; // Thanh toán
    
    // Constructors
    public ContractDTO() {}
    
    // Getters and Setters
    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }
    
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    
    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
    
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    
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
    
    public List<ContractDetailDTO> getContractDetails() { return contractDetails; }
    public void setContractDetails(List<ContractDetailDTO> contractDetails) { this.contractDetails = contractDetails; }
    
    public List<PaymentDTO> getPayments() { return payments; }
    public void setPayments(List<PaymentDTO> payments) { this.payments = payments; }
}
