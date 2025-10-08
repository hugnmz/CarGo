package model;

import java.time.LocalDateTime;
import util.di.annotation.Nested;


public class Feedbacks {
    private Integer feedbackId;    // ID phản hồi
    private Integer customerId;       // ID khách hàng
    private Integer vehicleId;        // ID xe
    private String comment;           // Nội dung phản hồi
    private LocalDateTime createAt;   // Ngày tạo phản hồi
    
    @Nested
    // Các đối tượng liên quan
    private Customers customer;       // Khách hàng
    @Nested
    private Vehicles vehicle;        // Xe
    
    // Constructors
    public Feedbacks() {}
    
    public Feedbacks(Integer customerId, Integer vehicleId, String comment) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.comment = comment;
        this.createAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Integer feedbackId) { this.feedbackId = feedbackId; }
    
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    
    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
    
    public Customers getCustomer() { return customer; }
    public void setCustomer(Customers customer) { this.customer = customer; }
    
    public Vehicles getVehicle() { return vehicle; }
    public void setVehicle(Vehicles vehicle) { this.vehicle = vehicle; }
}
