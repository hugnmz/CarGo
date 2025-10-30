
package mapper;

import dto.UseDTO;
import java.time.LocalDateTime;
import model.User;
import model.Locations;
import model.Roles;
import util.di.annotation.Component;

/**
 * UseMapper - Chuyển đổi giữa UseDTO và User Model
 */
@Component
public class UseMapper {

    // ===== Chuyển từ Model -> DTO =====
    public UseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UseDTO dto = new UseDTO();

        // Thông tin cơ bản
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setCreateAt(user.getCreateTime());

        // Thông tin địa điểm
        if (user.getLocation() != null) {
            dto.setLocationId(user.getLocation().getLocationId());
            dto.setCity(user.getLocation().getCity());
            dto.setAddress(user.getLocation().getAddress());
        }

        // Thông tin vai trò
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getRoleId());
            dto.setRoleName(user.getRole().getRoleName());
        }

        return dto;
    }

    // ===== Chuyển từ DTO -> Model =====
    public User toModel(UseDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        // Thông tin cơ bản
        user.setUserId(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setCreateTime(dto.getCreateAt());

        // Gán trực tiếp FK
        user.setLocationId(dto.getLocationId());
        user.setRoleId(dto.getRoleId());

        // Địa điểm (optional)
        if (dto.getLocationId() != null || dto.getCity() != null || dto.getAddress() != null) {
            Locations location = new Locations();
            location.setLocationId(dto.getLocationId());
            location.setCity(dto.getCity());
            location.setAddress(dto.getAddress());
            user.setLocation(location);
        }

        // Vai trò (optional)
        if (dto.getRoleId() != null || dto.getRoleName() != null) {
            Roles role = new Roles();
            role.setRoleId(dto.getRoleId());
            role.setRoleName(dto.getRoleName());
            user.setRole(role);
        }

        return user;
    }
}
