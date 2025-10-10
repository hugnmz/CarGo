package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IncidentDTO {
    private Integer incidentId;      // ID sự cố
    private String description;      // Mô tả sự cố
    private BigDecimal fineAmount;   // Số tiền phạt
    private LocalDateTime incidentDate; // Ngày xảy ra sự cố
    private String status;           // Trạng thái sự cố (pending, resolved, cancelled)
    private Integer contractDetailId; // ID chi tiết hợp đồng
    private Integer incidentTypeId;  // ID loại sự cố
    
    // Thông tin hợp đồng
    private Integer contractId;      // ID hợp đồng
    private String customerName;     // Tên khách hàng
    private String customerPhone;   // Số điện thoại khách hàng
    
    // Thông tin xe
    private Integer vehicleId;       // ID xe
    private String plateNumber;      // Biển số xe
    private String carName;        // Tên xe
    
    // Thông tin loại sự cố
    private String incidentTypeName; // Tên loại sự cố
    
    // Constructors
    public IncidentDTO() {}
    
    public IncidentDTO(String description, BigDecimal fineAmount, LocalDateTime incidentDate, 
                      Integer contractDetailId, Integer incidentTypeId) {
        this.description = description;
        this.fineAmount = fineAmount;
        this.incidentDate = incidentDate;
        this.contractDetailId = contractDetailId;
        this.incidentTypeId = incidentTypeId;
        this.status = "pending";
    }
    
    // Getters and Setters
    public Integer getIncidentId() { return incidentId; }
    public void setIncidentId(Integer incidentId) { this.incidentId = incidentId; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    
    public LocalDateTime getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDateTime incidentDate) { this.incidentDate = incidentDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getContractDetailId() { return contractDetailId; }
    public void setContractDetailId(Integer contractDetailId) { this.contractDetailId = contractDetailId; }
    
    public Integer getIncidentTypeId() { return incidentTypeId; }
    public void setIncidentTypeId(Integer incidentTypeId) { this.incidentTypeId = incidentTypeId; }
    
    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    
    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    
    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }
    
    public String getIncidentTypeName() { return incidentTypeName; }
    public void setIncidentTypeName(String incidentTypeName) { this.incidentTypeName = incidentTypeName; }
}
