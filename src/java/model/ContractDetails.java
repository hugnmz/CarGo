package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import util.di.annotation.Column;
import util.di.annotation.Nested;

public class ContractDetails {
    
    @Column()
    private Integer contractDetailId; // ID chi tiết hợp đồng
    @Column()
    private Integer contractId;      // ID hợp đồng
    @Column()
    private Integer vehicleId;       // ID xe thuê
    @Column()
    private BigDecimal price;        // Giá thuê
    @Column()
    private LocalDateTime rentStartDate; // Ngày bắt đầu thuê
    @Column()
    private LocalDateTime rentEndDate;   // Ngày kết thúc thuê
    @Column()
    private String note;             // Ghi chú
    @Nested
    // Các đối tượng liên quan
    private Contracts contract;      // Hợp đồng
    @Nested
    private Vehicles vehicle;        // Xe thuê
    private List<Incidents> incidents; // Sự cố
    
    // Constructors
    public ContractDetails() {}
    
    public ContractDetails(Integer contractId, Integer vehicleId, BigDecimal price, 
                          LocalDateTime rentStartDate, LocalDateTime rentEndDate) {
        this.contractId = contractId;
        this.vehicleId = vehicleId;
        this.price = price;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
    }
    
    // Getters and Setters
    public Integer getContractDetailId() { return contractDetailId; }
    public void setContractDetailId(Integer contractDetailId) { this.contractDetailId = contractDetailId; }
    
    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }
    
    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public LocalDateTime getRentStartDate() { return rentStartDate; }
    public void setRentStartDate(LocalDateTime rentStartDate) { this.rentStartDate = rentStartDate; }
    
    public LocalDateTime getRentEndDate() { return rentEndDate; }
    public void setRentEndDate(LocalDateTime rentEndDate) { this.rentEndDate = rentEndDate; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    public Contracts getContract() { return contract; }
    public void setContract(Contracts contract) { this.contract = contract; }
    
    public Vehicles getVehicle() { return vehicle; }
    public void setVehicle(Vehicles vehicle) { this.vehicle = vehicle; }
    
    public List<Incidents> getIncidents() { return incidents; }
    public void setIncidents(List<Incidents> incidents) { this.incidents = incidents; }
}
