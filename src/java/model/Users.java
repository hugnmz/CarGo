/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import util.di.annotation.Column;
import util.di.annotation.Nested;

public class Users {

    @Column
    private Integer userId;

    @Column()
    private String username;

    @Column(name = "password_hash")
    private byte[] passwordHash;

    @Column(name = "password_salt")
    private byte[] passwordSalt;

    @Column(name = "full_name")
    private String fullName;

    @Column()
    private String phone;

    @Column()
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "location_id")
    private Integer locationId;
    
    // THÊM MỚI: Thay thế UserRoles bằng roleId trực tiếp
    @Column(name = "roleId")
    private Integer roleId;

    @Nested
    private Locations location;

    // XÓA: Không còn sử dụng UserRoles
    // @Nested
    // private List<Roles> roles;
    // @Nested
    // private List<UserRoles> userRoles;

    // THÊM MỚI: Thay thế bằng single role
    @Nested
    private Roles role;

    @Nested
    private List<Contracts> contracts;

    // Constructors
    public Users() {
    }

    public Users(String username, String fullName, String phone, String email) {
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }

    // THÊM MỚI: Getters và setters cho roleId và role
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }


    public List<Contracts> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contracts> contracts) {
        this.contracts = contracts;
    }
    
    
    
}
