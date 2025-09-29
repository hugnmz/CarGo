/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

public class Roles {
    private Integer roleId;          // ID vai trò
    private String roleName;         // Tên vai trò (STAFF, MANAGER, ADMIN)
    
    // Các đối tượng liên quan
    private List<Users> users;       // Danh sách người dùng có vai trò này
    private List<UserRoles> userRoles; // Quan hệ với người dùng
    
    // Constructors
    public Roles() {}
    
    public Roles(String roleName) {
        this.roleName = roleName;
    }
    
    // Getters and Setters
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    public List<Users> getUsers() { return users; }
    public void setUsers(List<Users> users) { this.users = users; }
    
    public List<UserRoles> getUserRoles() { return userRoles; }
    public void setUserRoles(List<UserRoles> userRoles) { this.userRoles = userRoles; }
}
