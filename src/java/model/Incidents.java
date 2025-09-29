package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Incidents {
    private Integer incidentId;      // ID sự cố
    private String description;      // Mô tả sự cố
    private BigDecimal fineAmount;   // Số tiền phạt
    private LocalDateTime incidentDate; // Ngày xảy ra sự cố
    private String status;           // Trạng thái sự cố (pending, resolved, cancelled)
    private Integer contractDetailId; // ID chi tiết hợp đồng
    private Integer incidentTypeId;  // ID loại sự cố
    
    // Các đối tượng liên quan
    private ContractDetails contractDetail; // Chi tiết hợp đồng
    private IncidentTypes incidentType;     // Loại sự cố
    
    // Constructors
    public Incidents() {}
    
    public Incidents(String description, BigDecimal fineAmount, LocalDateTime incidentDate, 
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
    
    public ContractDetails getContractDetail() { return contractDetail; }
    public void setContractDetail(ContractDetails contractDetail) { this.contractDetail = contractDetail; }
    
    public IncidentTypes getIncidentType() { return incidentType; }
    public void setIncidentType(IncidentTypes incidentType) { this.incidentType = incidentType; }
}
