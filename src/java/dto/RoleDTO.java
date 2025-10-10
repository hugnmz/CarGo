package dto;

import java.util.List;

public class RoleDTO {
    private Integer roleId;          // ID vai trò
    private String roleName;         // Tên vai trò (STAFF, MANAGER, ADMIN)
    
    // Thống kê (optional)
    private Integer userCount;       // Số lượng người dùng có vai trò này
    
    // Danh sách người dùng (optional)
    private List<UserDTO> users;     // Danh sách người dùng có vai trò này
    
    // Constructors
    public RoleDTO() {}
    
    public RoleDTO(String roleName) {
        this.roleName = roleName;
    }
    
    public RoleDTO(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
    
    // Getters and Setters
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    public Integer getUserCount() { return userCount; }
    public void setUserCount(Integer userCount) { this.userCount = userCount; }
    
    public List<UserDTO> getUsers() { return users; }
    public void setUsers(List<UserDTO> users) { this.users = users; }
}
