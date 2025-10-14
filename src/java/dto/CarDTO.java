package dto;

/**
 * CarDTO - Data Transfer Object cho Car
 * 
 * MỤC ĐÍCH:
 * - Chuyển đổi từ Entity sang DTO để hiển thị
 * - Chỉ chứa các field cần thiết cho UI
 * - Tránh expose toàn bộ Entity ra ngoài
 */
public class CarDTO {
    
    private Integer carId;
    private String name;
    private Integer year;
    private String description;
    private String image;
    private String categoryName;
    private String fuelType;
    private Integer seatingType;
    private String locationCity;
    private Double dailyPrice;
    
    // Constructors
    public CarDTO() {}
    
    public CarDTO(Integer carId, String name, Integer year, String description, String image,
                  String categoryName, String fuelType, Integer seatingType, String locationCity, Double dailyPrice) {
        this.carId = carId;
        this.name = name;
        this.year = year;
        this.description = description;
        this.image = image;
        this.categoryName = categoryName;
        this.fuelType = fuelType;
        this.seatingType = seatingType;
        this.locationCity = locationCity;
        this.dailyPrice = dailyPrice;
    }
    
    // Getters and Setters
    public Integer getCarId() {
        return carId;
    }
    
    public void setCarId(Integer carId) {
        this.carId = carId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    
    public Integer getSeatingType() {
        return seatingType;
    }
    
    public void setSeatingType(Integer seatingType) {
        this.seatingType = seatingType;
    }
    
    public String getLocationCity() {
        return locationCity;
    }
    
    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }
    
    public Double getDailyPrice() {
        return dailyPrice;
    }
    
    public void setDailyPrice(Double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }
    
    @Override
    public String toString() {
        return "CarDTO{" +
                "carId=" + carId +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", seatingType=" + seatingType +
                ", locationCity='" + locationCity + '\'' +
                ", dailyPrice=" + dailyPrice +
                '}';
    }
}