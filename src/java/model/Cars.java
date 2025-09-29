/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;


public class Cars {
    private Integer carId;           // ID xe
    private String name;              // Tên xe
    private Integer year;             // Năm sản xuất
    private String description;      // Mô tả xe
    private String image;             // Hình ảnh xe
    private Integer categoryId;        // ID danh mục
    private Integer fuelId;           // ID loại nhiên liệu
    private Integer seatingId;        // ID số chỗ ngồi
    private Integer locationId;       // ID địa điểm
    
    // Các đối tượng liên quan
    private Categories category;      // Danh mục xe
    private Fuels fuel;              // Loại nhiên liệu
    private Seatings seating;        // Số chỗ ngồi
    private Locations location;       // Địa điểm
    private List<Vehicles> vehicles; // Danh sách xe thực tế
    private List<CarPrices> carPrices; // Danh sách giá xe
    
    // Constructors
    public Cars() {}
    
    public Cars(String name, Integer year, String description) {
        this.name = name;
        this.year = year;
        this.description = description;
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
    
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    
    public Integer getFuelId() { return fuelId; }
    public void setFuelId(Integer fuelId) { this.fuelId = fuelId; }
    
    public Integer getSeatingId() { return seatingId; }
    public void setSeatingId(Integer seatingId) { this.seatingId = seatingId; }
    
    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }
    
    public Categories getCategory() { return category; }
    public void setCategory(Categories category) { this.category = category; }
    
    public Fuels getFuel() { return fuel; }
    public void setFuel(Fuels fuel) { this.fuel = fuel; }
    
    public Seatings getSeating() { return seating; }
    public void setSeating(Seatings seating) { this.seating = seating; }
    
    public Locations getLocation() { return location; }
    public void setLocation(Locations location) { this.location = location; }
    
    public List<Vehicles> getVehicles() { return vehicles; }
    public void setVehicles(List<Vehicles> vehicles) { this.vehicles = vehicles; }
    
    public List<CarPrices> getCarPrices() { return carPrices; }
    public void setCarPrices(List<CarPrices> carPrices) { this.carPrices = carPrices; }
}
