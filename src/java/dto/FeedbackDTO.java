package dto;

import java.time.LocalDateTime;

public class FeedbackDTO {
    private Integer feedbackId;     // ID phản hồi
    private Integer customerId;     // ID khách hàng
    private Integer vehicleId;      // ID xe
    private String comment;         // Nội dung phản hồi
    private LocalDateTime createAt; // Ngày tạo phản hồi
    
    // Thông tin khách hàng
    private String customerName;     // Tên khách hàng
    private String customerEmail;   // Email khách hàng
    
    // Thông tin xe
    private String plateNumber;      // Biển số xe
    private String carName;         // Tên xe
    private String carImage;         // Hình ảnh xe
    
    // Constructors
    public FeedbackDTO() {}
    
    public FeedbackDTO(Integer customerId, Integer vehicleId, String comment) {
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
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    
    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }
    
    public String getCarImage() { return carImage; }
    public void setCarImage(String carImage) { this.carImage = carImage; }
}
