/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import util.di.annotation.Column;
import util.di.annotation.Nested;


public class Cars {
    
    @Column()
    private Integer carId;           // ID xe
    @Column()
    private String name;              // Tên xe
    @Column()
    private Integer year;             // Năm sản xuất
    @Column()
    private String description;      // Mô tả xe
    @Column()
    private String image;             // Hình ảnh xe
    @Column()
    private Integer categoryId;        // ID danh mục
    @Column()
    private Integer fuelId;           // ID loại nhiên liệu
    @Column()
    private Integer seatingId;        // ID số chỗ ngồi
    @Column()
    private Integer locationId;       // ID địa điểm
    
    // Các đối tượng liên quan
    @Nested
    private Categories category;      // Danh mục xe
    @Nested
    private Fuels fuel;              // Loại nhiên liệu
    @Nested
    private Seatings seating;        // Số chỗ ngồi
    @Nested
    private Locations location;       // Dia diem
    private List<Vehicles> vehicles; // Danh sách xe thực tế
    private List<CarPrices> carPrices; // Danh sách giá xe
    
    // Constructors
    public Cars() {}

    public Cars(String name, Integer year, String description, String image, Integer categoryId, Integer fuelId, Integer seatingId) {
        this.name = name;
        this.year = year;
        this.description = description;
        this.image = image;
        this.categoryId = categoryId;
        this.fuelId = fuelId;
        this.seatingId = seatingId;
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
