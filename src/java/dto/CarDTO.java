package dto;

public class CarDTO {

    private Integer carId;
    private String name;
    private Integer year;
    private String description;
    private String image;

    // 🔹 Dùng ID để map entity
    private Integer categoryId;
    private Integer fuelId;
    private Integer seatingId;

    // 🔹 Tên/type để hiển thị
    private String categoryName;
    private String fuelType;
    private Integer seatingType;

    private String locationCity;
    private Double dailyPrice;
    private Double depositAmount;

    public CarDTO() {
    }

    // Getters & Setters
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getFuelId() {
        return fuelId;
    }

    public void setFuelId(Integer fuelId) {
        this.fuelId = fuelId;
    }

    public Integer getSeatingId() {
        return seatingId;
    }

    public void setSeatingId(Integer seatingId) {
        this.seatingId = seatingId;
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

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }


}

