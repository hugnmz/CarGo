package model;


public class UserRoles {
    private Integer userId;          // ID người dùng
    private Integer roleId;          // ID vai trò
    
    // Các đối tượng liên quan
    private Users user;              // Người dùng
    private Roles role;              // Vai trò
    
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
