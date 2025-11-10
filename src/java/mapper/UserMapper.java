
package mapper;

import dto.UserDTO;
import model.User;
import model.Location;
import model.Roles;
import util.di.annotation.Component;

/**
 * UserMapper - Chuyển đổi giữa UserDTO và User Model
 */
@Component
public class UserMapper {

    // ===== Chuyển từ Model -> DTO =====
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();

        // Thông tin cơ bản
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setCreateAt(user.getCreateAt());

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
    public User toModel(UserDTO dto) {
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
        user.setCreateAt(dto.getCreateAt());

        // Gán trực tiếp FK
        user.setLocationId(dto.getLocationId());
        user.setRoleId(dto.getRoleId());

        // Địa điểm (optional)
        if (dto.getLocationId() != null || dto.getCity() != null || dto.getAddress() != null) {
            Location location = new Location();
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
