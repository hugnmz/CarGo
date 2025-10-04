package model;

import java.util.List;

public class Vehicles {
    private Integer vehicleId;       // ID xe thực tế
    private Integer carId;           // ID model xe
    private String plateNumber;      // Biển số xe
    private Boolean isActive;        // Trạng thái hoạt động
    
    // Các đối tượng liên quan
    private Cars car;                // Model xe
    private List<ContractDetails> contractDetails; // Chi tiết hợp đồng
    private List<Orders> orders;      // Don hang trong gio
    private List<Feedbacks> feedbacks; // Phản hồi
    
    // Constructors
    public Vehicles() {}
    
    public Vehicles(Integer carId, String plateNumber) {
        this.carId = carId;
        this.plateNumber = plateNumber;
        this.isActive = true;
    }
    
    // Getters and Setters
    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    public Integer getCarId() { return carId; }
    public void setCarId(Integer carId) { this.carId = carId; }
    
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Cars getCar() { return car; }
    public void setCar(Cars car) { this.car = car; }
    
    public List<ContractDetails> getContractDetails() { return contractDetails; }
    public void setContractDetails(List<ContractDetails> contractDetails) { this.contractDetails = contractDetails; }
    
    public List<Orders> getOrders() { return orders; }
    public void setOrders(List<Orders> orders) { this.orders = orders; }
    
    public List<Feedbacks> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(List<Feedbacks> feedbacks) { this.feedbacks = feedbacks; }
}
