package dto;

import java.util.List;

public class VehicleDTO {
    private Integer vehicleId;       // ID xe thực tế
    private Integer carId;           // ID model xe
    private String plateNumber;       // Biển số xe
    private Boolean isActive;        // Trạng thái hoạt động
    private Integer locationId;      // ID địa điểm
    
    // Thông tin xe (từ Cars)
    private String carName;          // Tên xe
    private Integer year;            // Năm sản xuất
    private String description;      // Mô tả xe
    private String image;            // Hình ảnh xe
    private String categoryName;     // Tên danh mục
    private String fuelType;         // Loại nhiên liệu
    private Integer seatingType;     // Số chỗ ngồi
    
    // Thông tin địa điểm
    private String city;             // Thành phố
    private String address;          // Địa chỉ
    
    // Thông tin giá
    private java.math.BigDecimal currentPrice;  // Giá hiện tại
    private java.math.BigDecimal depositAmount; // Số tiền cọc
    
    // Danh sách liên quan
    private List<ContractDetailDTO> contractDetails; // Chi tiết hợp đồng
    private List<OrderDTO> orders;    // Đơn hàng trong giỏ
    private List<FeedbackDTO> feedbacks; // Phản hồi
    
    // Constructors
    public VehicleDTO() {}
    
    public VehicleDTO(Integer vehicleId, Integer carId, String plateNumber, Boolean isActive) {
        this.vehicleId = vehicleId;
        this.carId = carId;
        this.plateNumber = plateNumber;
        this.isActive = isActive;
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
    
    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }
    
    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    
    public Integer getSeatingType() { return seatingType; }
    public void setSeatingType(Integer seatingType) { this.seatingType = seatingType; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public java.math.BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(java.math.BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    
    public java.math.BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(java.math.BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    
    public List<ContractDetailDTO> getContractDetails() { return contractDetails; }
    public void setContractDetails(List<ContractDetailDTO> contractDetails) { this.contractDetails = contractDetails; }
    
    public List<OrderDTO> getOrders() { return orders; }
    public void setOrders(List<OrderDTO> orders) { this.orders = orders; }
    
    public List<FeedbackDTO> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(List<FeedbackDTO> feedbacks) { this.feedbacks = feedbacks; }
}
