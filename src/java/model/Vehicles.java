package model;

import java.util.List;
import util.di.annotation.Column;
import util.di.annotation.Nested;

public class Vehicles {

    @Column()
    private Integer vehicleId;       // ID xe thực tế
    @Column()
    private Integer carId;           // ID model xe
    @Column()
    private String plateNumber;      // Biển số xe
    @Column()
    private Boolean isActive;        // Trạng thái hoạt động
    @Column()
    private Integer locationId;
    // Các đối tượng liên quan
    @Nested
    private Cars car;
    
    @Nested                                   // Model xe
    private Locations location;
    private List<ContractDetails> contractDetails; // Chi tiết hợp đồng
    private List<Orders> orders;      // Don hang trong gio
    private List<Feedbacks> feedbacks; // Phản hồi

    // Constructors
    public Vehicles() {
    }

    public Vehicles(Integer vehicleId, Integer carId, String plateNumber, Boolean isActive, Integer locationId, Cars car, Locations location, List<ContractDetails> contractDetails, List<Orders> orders, List<Feedbacks> feedbacks) {
        this.vehicleId = vehicleId;
        this.carId = carId;
        this.plateNumber = plateNumber;
        this.isActive = isActive;
        this.locationId = locationId;
        this.car = car;
        this.location = location;
        this.contractDetails = contractDetails;
        this.orders = orders;
        this.feedbacks = feedbacks;
    }


    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    // Getters and Setters
    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Cars getCar() {
        return car;
    }

    public void setCar(Cars car) {
        this.car = car;
    }

    public List<ContractDetails> getContractDetails() {
        return contractDetails;
    }

    public void setContractDetails(List<ContractDetails> contractDetails) {
        this.contractDetails = contractDetails;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Feedbacks> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedbacks> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }
}
