package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import util.di.annotation.Column;
import util.di.annotation.Nested;

public class Contract {

    @Column()
    private Integer contractId;       // ID hợp đồng
    @Column()
    private Integer customerId;      // ID khách hàng
    @Column()
    private Integer staffId;         // ID nhân viên xử lý
    @Column()
    private LocalDateTime startDate; // Ngày bắt đầu hợp đồng
    @Column()
    private LocalDateTime endDate;   // Ngày kết thúc hợp đồng
    @Column()
    private String status;           // Trạng thái hợp đồng (pending, accepted, rejected)
    @Column()
    private LocalDateTime createAt; // Ngày tạo hợp đồng
    @Column()
    private BigDecimal totalAmount;  // Tong tien
    @Column()
    private BigDecimal depositAmount; // Tien coc

    @Nested
    // Các đối tượng liên quan
    private Customer customer;      // Khách hàng
    @Nested
    private User staff;            // Nhân viên
    private List<ContractDetail> contractDetails; // Chi tiết hợp đồng
    private List<Payments> payments; // Thanh toán
    
    // Constructors
    public Contract() {
    }

    public Contract(Integer customerId, LocalDateTime startDate, LocalDateTime endDate) {
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "pending";
        this.createAt = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
        this.depositAmount = BigDecimal.ZERO;
    }

    // Getters and Setters
    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getStaff() {
        return staff;
    }

    public void setStaff(User staff) {
        this.staff = staff;
    }

    public List<ContractDetail> getContractDetails() {
        return contractDetails;
    }

    public void setContractDetails(List<ContractDetail> contractDetails) {
        this.contractDetails = contractDetails;
    }

    public List<Payments> getPayments() {
        return payments;
    }

    public void setPayments(List<Payments> payments) {
        this.payments = payments;
    }
    // fields
@Column()
private String rejectionReason; // Lý do từ chối (nếu có)

// getters/setters
public String getRejectionReason() {
    return rejectionReason;
}

public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
}
}