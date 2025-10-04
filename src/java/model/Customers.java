/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Customers {
    private Integer customerId;      // ID khách hàng
    private String username;         // Tên đăng nhập
    private byte[] passwordHash;    // Hash mật khẩu (VARBINARY)
    private byte[] passwordSalt;    // Salt mật khẩu (VARBINARY)
    private String fullName;        // Ho ten
    private String phone;           // Số điện thoại
    private String email;           // Email
    private LocalDate dateOfBirth;  // Ngày sinh
    private LocalDateTime createAt; // Ngày tạo
    private Integer locationId;     // ID địa điểm
    private boolean isVerified;
    private String verifyCode;
    private LocalDateTime verifyCodeExpire;
    
    // Các đối tượng liên quan
    private Locations location;     // Dia diem
    private List<Contracts> contracts; // Danh sách hợp đồng
    private Carts cart;             // Gio hang
    
    // Constructors
    public Customers() {}
    
    public Customers(String username, String fullName, String phone, String email) {
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.createAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    
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
    
    public List<Contracts> getContracts() { return contracts; }
    public void setContracts(List<Contracts> contracts) { this.contracts = contracts; }
    
    public Carts getCart() { return cart; }
    public void setCart(Carts cart) { this.cart = cart; }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public LocalDateTime getVerifyCodeExpire() {
        return verifyCodeExpire;
    }

    public void setVerifyCodeExpire(LocalDateTime verifyCodeExpire) {
        this.verifyCodeExpire = verifyCodeExpire;
    }
    
    

    @Override
    public String toString() {
        return "Customers{" + "customerId=" + customerId + ", username=" + username + 
                ", passwordHash=" + passwordHash + ", passwordSalt=" + passwordSalt + 
                ", fullName=" + fullName + ", phone=" + phone + ", email=" + email + 
                ", dateOfBirth=" + dateOfBirth + ", createAt=" + createAt + ", locationId=" + 
                locationId + ", location=" + location + ", contracts=" + 
                contracts + ", cart=" + cart + '}';
    }
    
    
}
