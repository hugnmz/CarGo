/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Users {
    private Integer userId;          // ID nguoi dung
    private String username;         // Tên đăng nhập
    private byte[] passwordHash;    // Hash mật khẩu
    private byte[] passwordSalt;    // Salt mật khẩu
    private String fullName;        // Ho ten
    private String phone;           // Số điện thoại
    private String email;           // Email
    private LocalDate dateOfBirth;  // Ngày sinh
    private LocalDateTime createAt; // Ngày tạo
    private Integer locationId;     // ID địa điểm
    
    // Các đối tượng liên quan
    private Locations location;     // Dia diem
    private List<Roles> roles;      // Danh sách vai trò
    private List<UserRoles> userRoles; // Quan hệ với vai trò
    private List<Contracts> contracts; // Danh sách hợp đồng xử lý
    
    // Constructors
    public Users() {}
    
    public Users(String username, String fullName, String phone, String email) {
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.createAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public byte[] getPasswordHash() { return passwordHash; }
    public void setPasswordHash(byte[] passwordHash) { this.passwordHash = passwordHash; }
    
    public byte[] getPasswordSalt() { return passwordSalt; }
    public void setPasswordSalt(byte[] passwordSalt) { this.passwordSalt = passwordSalt; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
    
    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }
    
    public Locations getLocation() { return location; }
    public void setLocation(Locations location) { this.location = location; }
    
    public List<Roles> getRoles() { return roles; }
    public void setRoles(List<Roles> roles) { this.roles = roles; }
    
    public List<UserRoles> getUserRoles() { return userRoles; }
    public void setUserRoles(List<UserRoles> userRoles) { this.userRoles = userRoles; }
    
    public List<Contracts> getContracts() { return contracts; }
    public void setContracts(List<Contracts> contracts) { this.contracts = contracts; }
}
