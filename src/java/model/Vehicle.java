package model;

import java.util.List;
import util.di.annotation.Column;
import util.di.annotation.Nested;

public class Vehicle {

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
    @Nested(prefix = "c")
    private Cars car;
    
    @Nested(prefix = "l")                                   // Model xe
    private Location location;
    private List<ContractDetail> contractDetails; // Chi tiết hợp đồng
    private List<Orders> orders;      // Don hang trong gio
    private List<Feedback> feedbacks; // Phản hồi

    // Constructors
    public Vehicle() {
    }

    public Vehicle(Integer vehicleId, Integer carId, String plateNumber, Boolean isActive, Integer locationId, Cars car, Location location, List<ContractDetail> contractDetails, List<Orders> orders, List<Feedback> feedbacks) {
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

    public List<ContractDetail> getContractDetails() {
        return contractDetails;
    }

    public void setContractDetails(List<ContractDetail> contractDetails) {
        this.contractDetails = contractDetails;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
