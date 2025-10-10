package mapper;

import dto.RoleDTO;
import model.Roles;
import util.di.annotation.Component;

/**
 * RoleMapper - Chuyển đổi giữa RoleDTO và Roles Model
 */
@Component
public class RoleMapper {

    // Chuyen tu Model sang DTO
    public RoleDTO toDTO(Roles role) {
        if (role == null) {
            return null;
        }

        RoleDTO dto = new RoleDTO();
        
        // Basic role fields
        dto.setRoleId(role.getRoleId());
        dto.setRoleName(role.getRoleName());

        // Statistics (optional - can be set separately)
        if (role.getUsers() != null) {
            dto.setUserCount(role.getUsers().size());
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Roles toModel(RoleDTO dto) {
        if (dto == null) {
            return null;
        }

        Roles role = new Roles();
        
        // Basic role fields
        role.setRoleId(dto.getRoleId());
        role.setRoleName(dto.getRoleName());

        return role;
    }

    /**
     * Chuyển từ Model sang DTO với danh sách users
     */
    public RoleDTO toDTOWithUsers(Roles role) {
        RoleDTO dto = toDTO(role);
        
        if (dto != null && role.getUsers() != null) {
            // Convert Users to UserDTOs (assuming UserMapper exists)
            // This would require UserMapper to be injected
            // For now, we'll just set the count
            dto.setUserCount(role.getUsers().size());
        }

        return dto;
    }
}
