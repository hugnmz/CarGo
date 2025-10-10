package mapper;

import dto.UserDTO;
import model.Users;
import model.Locations;
import model.Roles;
import util.di.annotation.Component;
import java.util.List;
import java.util.ArrayList;

/**
 * UserMapper - Chuyển đổi giữa UserDTO và Users Model
 */
@Component
public class UserMapper {

    // Chuyen tu Model sang DTO
    public UserDTO toDTO(Users user) {
        // Kiem tra null
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        
        // Gan cac truong co ban cua user
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setCreateAt(user.getCreateAt());

        // Thong tin dia diem (nested)
        if (user.getLocation() != null) {
            Locations location = user.getLocation();
            dto.setCity(location.getCity());
            dto.setAddress(location.getAddress());
        }

        // Thong tin vai tro (nested)
        if (user.getRoles() != null) {
            List<String> roleNames = new ArrayList<>();
            for (Roles role : user.getRoles()) {
                roleNames.add(role.getRoleName());
            }
            dto.setRoles(roleNames);
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Users toModel(UserDTO dto) {
        // Kiem tra null
        if (dto == null) {
            return null;
        }

        Users user = new Users();
        
        // Gan cac truong co ban cua user
        user.setUserId(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setCreateAt(dto.getCreateAt());

        // Tao doi tuong Location neu co thong tin dia diem
        if (dto.getCity() != null || dto.getAddress() != null) {
            Locations location = new Locations();
            location.setCity(dto.getCity());
            location.setAddress(dto.getAddress());
            user.setLocation(location);
        }

        return user;
    }
}
