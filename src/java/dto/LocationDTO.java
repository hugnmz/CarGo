package dto;

import java.util.List;

public class LocationDTO {
    private Integer locationId;      // ID địa điểm
    private String city;             // Thành phố
    private String address;          // Địa chỉ
    
    
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
    
}
