/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

public class Locations {
    private Integer locationId;      // ID địa điểm
    private String city;             // Thành phố
    private String address;          // Dia chi
    
    // Các đối tượng liên quan
    private List<Users> users;       // Danh sach nguoi dung
    private List<Customers> customers; // Danh sách khách hàng
    private List<Cars> cars;         // Danh sách xe
    
    // Constructors
    public Locations() {}
    
    public Locations(String city, String address) {
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
    
    public List<Users> getUsers() { return users; }
    public void setUsers(List<Users> users) { this.users = users; }
    
    public List<Customers> getCustomers() { return customers; }
    public void setCustomers(List<Customers> customers) { this.customers = customers; }
    
    public List<Cars> getCars() { return cars; }
    public void setCars(List<Cars> cars) { this.cars = cars; }
}
