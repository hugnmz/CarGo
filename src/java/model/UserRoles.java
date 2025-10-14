package model;

import util.di.annotation.Column;
import util.di.annotation.Nested;


public class UserRoles {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Nested
    private Users user;

    @Nested
    private Roles role;
    
    // Constructors
    public UserRoles() {}
    
    public UserRoles(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    
    public Roles getRole() { return role; }
    public void setRole(Roles role) { this.role = role; }
}
