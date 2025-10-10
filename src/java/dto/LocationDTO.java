package dto;

import java.util.List;

public class LocationDTO {
    private Integer locationId;      // ID địa điểm
    private String city;             // Thành phố
    private String address;          // Địa chỉ
    
    // Thống kê (optional)
    private Integer userCount;       // Số lượng người dùng
    private Integer customerCount;   // Số lượng khách hàng
    private Integer carCount;        // Số lượng xe
    private Integer vehicleCount;    // Số lượng xe thực tế
    
    // Danh sách liên quan (optional)
    private List<UserDTO> users;         // Danh sách người dùng
    private List<CustomerDTO> customers; // Danh sách khách hàng
    private List<CarDTO> cars;          // Danh sách xe
    private List<VehicleDTO> vehicles;  // Danh sách xe thực tế
    
    // Constructors
    public LocationDTO() {}
    
    public LocationDTO(String city, String address) {
        this.city = city;
        this.address = address;
    }
    
    public LocationDTO(Integer locationId, String city, String address) {
        this.locationId = locationId;
        this.city = city;
        this.address = address;
    }
    
    // Getters and Setters
    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public Integer getUserCount() { return userCount; }
    public void setUserCount(Integer userCount) { this.userCount = userCount; }
    
    public Integer getCustomerCount() { return customerCount; }
    public void setCustomerCount(Integer customerCount) { this.customerCount = customerCount; }
    
    public Integer getCarCount() { return carCount; }
    public void setCarCount(Integer carCount) { this.carCount = carCount; }
    
    public Integer getVehicleCount() { return vehicleCount; }
    public void setVehicleCount(Integer vehicleCount) { this.vehicleCount = vehicleCount; }
    
    public List<UserDTO> getUsers() { return users; }
    public void setUsers(List<UserDTO> users) { this.users = users; }
    
    public List<CustomerDTO> getCustomers() { return customers; }
    public void setCustomers(List<CustomerDTO> customers) { this.customers = customers; }
    
    public List<CarDTO> getCars() { return cars; }
    public void setCars(List<CarDTO> cars) { this.cars = cars; }
    
    public List<VehicleDTO> getVehicles() { return vehicles; }
    public void setVehicles(List<VehicleDTO> vehicles) { this.vehicles = vehicles; }
}
