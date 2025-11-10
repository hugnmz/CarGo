/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import util.di.annotation.Column;

public class Location {
    @Column()
    private Integer locationId;      // ID địa điểm
    @Column()
    private String city;             // Thành phố
    @Column()
    private String address;          // Dia chi
    
    // Các đối tượng liên quan
    private List<User> users;       // Danh sach nguoi dung
    private List<Customer> customers; // Danh sách khách hàng
    private List<Cars> cars;         // Danh sách xe
    
    // Constructors
    public Location() {}
    
    public Location(String city, String address) {
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
    
    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
    
    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }
    
    public List<Cars> getCars() { return cars; }
    public void setCars(List<Cars> cars) { this.cars = cars; }

    @Override
    public String toString() {
        return "Locations{" + "locationId=" + locationId + ", city=" + city + ", address=" + address + ", users=" + users + ", customers=" + customers + ", cars=" + cars + '}';
    }
    
    
}
