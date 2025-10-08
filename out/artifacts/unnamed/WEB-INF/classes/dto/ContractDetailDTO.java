package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class ContractDetailDTO {
    private Integer contractDetailId; // ID chi tiết hợp đồng
    private Integer contractId;      // ID hợp đồng
    private Integer vehicleId;       // ID xe
    private String plateNumber;       // Biển số xe
    private String carName;          // Tên xe
    private BigDecimal price;        // Giá thuê
    private LocalDateTime rentStartDate; // Ngày bắt đầu thuê
    private LocalDateTime rentEndDate;   // Ngày kết thúc thuê
    private String note;            // Ghi chú
    
    // Constructors
    public ContractDetailDTO() {}
    
    // Getters and Setters
    public Integer getContractDetailId() { return contractDetailId; }
    public void setContractDetailId(Integer contractDetailId) { this.contractDetailId = contractDetailId; }
    
    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }
    
    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    
    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public LocalDateTime getRentStartDate() { return rentStartDate; }
    public void setRentStartDate(LocalDateTime rentStartDate) { this.rentStartDate = rentStartDate; }
    
    public LocalDateTime getRentEndDate() { return rentEndDate; }
    public void setRentEndDate(LocalDateTime rentEndDate) { this.rentEndDate = rentEndDate; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
