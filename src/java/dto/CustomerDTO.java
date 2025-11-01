package dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerDTO {

    private Integer customerId;      // ID khách hàng
    private String username;         // Tên đăng nhập
    private String fullName;         // Ho ten
    private String phone;            // Số điện thoại
    private String email;            // Email
    private LocalDate dateOfBirth;   // Ngày sinh
    private LocalDateTime createAt;  // Ngày tạo
    private String city;             // Thành phố
    private Boolean isVerified;
    private Integer locationId;

    
    
    // Constructors
    public CustomerDTO() {
    }

    public CustomerDTO(Integer customerId, String username, String fullName, String phone, String email, LocalDate dateOfBirth, LocalDateTime createAt, String city, Boolean isVerified, Integer locationId) {
        this.customerId = customerId;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.createAt = createAt;
        this.city = city;
        this.isVerified = isVerified;
        this.locationId = locationId;
    }

    // Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

}
