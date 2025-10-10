package dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CarPriceDTO {
    private Integer priceId;          // ID giá
    private Integer carId;            // ID xe
    private BigDecimal dailyPrice;    // Giá theo ngày
    private BigDecimal depositAmount; // Số tiền cọc
    private LocalDate startDate;      // Ngày bắt đầu áp dụng
    private LocalDate endDate;        // Ngày kết thúc áp dụng (NULL = hiện hành)
    private LocalDateTime createAt;   // Ngày tạo
    
    // Thông tin xe
    private String carName;           // Tên xe
    private String carImage;          // Hình ảnh xe
    private Integer year;             // Năm sản xuất
    private String categoryName;      // Tên danh mục
    private String fuelType;          // Loại nhiên liệu
    private Integer seatingType;       // Số chỗ ngồi
    
    // Trạng thái
    private Boolean isCurrent;        // Có phải giá hiện tại không
    private Boolean isActive;         // Có đang áp dụng không
    
    // Constructors
    public CarPriceDTO() {}
    
    public CarPriceDTO(Integer carId, BigDecimal dailyPrice, LocalDate startDate) {
        this.carId = carId;
        this.dailyPrice = dailyPrice;
        this.startDate = startDate;
        this.createAt = LocalDateTime.now();
    }
    
    public CarPriceDTO(Integer priceId, Integer carId, BigDecimal dailyPrice, 
                      BigDecimal depositAmount, LocalDate startDate, LocalDate endDate) {
        this.priceId = priceId;
        this.carId = carId;
        this.dailyPrice = dailyPrice;
        this.depositAmount = depositAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getPriceId() { return priceId; }
    public void setPriceId(Integer priceId) { this.priceId = priceId; }
    
    public Integer getCarId() { return carId; }
    public void setCarId(Integer carId) { this.carId = carId; }
    
    public BigDecimal getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }
    
    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
    
    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }
    
    public String getCarImage() { return carImage; }
    public void setCarImage(String carImage) { this.carImage = carImage; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    
    public Integer getSeatingType() { return seatingType; }
    public void setSeatingType(Integer seatingType) { this.seatingType = seatingType; }
    
    public Boolean getIsCurrent() { return isCurrent; }
    public void setIsCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
