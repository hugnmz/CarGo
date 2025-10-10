package dto;

import java.math.BigDecimal;
import java.util.List;

public class CarDTO {
    private Integer carId;           // ID xe
    private String name;              // Tên xe
    private Integer year;             // Năm sản xuất
    private String description;      // Mô tả xe
    private String image;             // Hình ảnh xe
    private String categoryName;      // Tên danh mục
    private String fuelType;          // Loại nhiên liệu
    private Integer seatingType;      // Số chỗ ngồi
    private String city;              // Thành phố
    private String address;           // Địa chỉ
    private BigDecimal currentPrice;  // Giá hiện tại
    private BigDecimal depositAmount; // Số tiền cọc
    private List<String> availableVehicles; // Danh sách xe có sẵn
    
    // Constructors
    public CarDTO() {}

    public CarDTO(Integer carId, String name, Integer year, String description, String image, String categoryName, String fuelType, Integer seatingType, String city, String address, BigDecimal currentPrice, BigDecimal depositAmount, List<String> availableVehicles) {
        this.carId = carId;
        this.name = name;
        this.year = year;
        this.description = description;
        this.image = image;
        this.categoryName = categoryName;
        this.fuelType = fuelType;
        this.seatingType = seatingType;
        this.city = city;
        this.address = address;
        this.currentPrice = currentPrice;
        this.depositAmount = depositAmount;
        this.availableVehicles = availableVehicles;
    }
    
    
    
    // Getters and Setters
    public Integer getCarId() { return carId; }
    public void setCarId(Integer carId) { this.carId = carId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
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
    
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    
    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    
    public List<String> getAvailableVehicles() { return availableVehicles; }
    public void setAvailableVehicles(List<String> availableVehicles) { this.availableVehicles = availableVehicles; }
}
