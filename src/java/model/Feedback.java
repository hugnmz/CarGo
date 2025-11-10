package model;

import java.time.LocalDateTime;
import util.di.annotation.Column;
import util.di.annotation.Nested;

public class Feedback {

    @Column
    private Integer feedbackId;    // ID phản hồi
    @Column

    private Integer customerId;       // ID khách hàng
    @Column

    private Integer vehicleId;        // ID xe
    @Column

    private String comment;           // Nội dung phản hồi

    @Column
    private LocalDateTime createAt;   // Ngày tạo phản hồi

    @Nested
    // Các đối tượng liên quan
    private Customer customer;       // Khách hàng
    @Nested
    private Vehicle vehicle;        // Xe

    // Constructors
    public Feedback() {
    }

    public Feedback(Integer customerId, Integer vehicleId, String comment) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.comment = comment;
        this.createAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
