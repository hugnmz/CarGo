package dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * UserDTO - Dùng để trao đổi dữ liệu người dùng giữa tầng Controller / View.
 * Tương thích hoàn toàn với model Users và database hiện tại.
 */
public class UserDTO {

    private Integer userId;          // ID người dùng
    private String username;         // Tên đăng nhập
    private String fullName;         // Họ và tên
    private String phone;            // Số điện thoại
    private String email;            // Email
    private LocalDate dateOfBirth;   // Ngày sinh
    private LocalDateTime createAt;  // Ngày tạo
    private String city;             // Thành phố (từ bảng Locations)
    private String address;          // Địa chỉ (từ bảng Locations)
    private String roleName;         // Tên vai trò (từ bảng Roles)
    private Integer roleId;          // ID vai trò (FK)
    private Integer locationId;      // ID địa điểm (FK)

    public UserDTO() {
    }

    // ===== GETTERS & SETTERS =====
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getCreatedAtFormatted() {
        if (createAt == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return createAt.format(formatter);
    }
}
