package service.impl;

import dao.LocationsDAO;
import dao.UsersDAO;
import dto.LocationDTO;
import dto.UserDTO;
import java.util.List;
import java.util.Optional;
import mapper.UserMapper;
import model.Users;
import service.UserService;
import util.PasswordUtil;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LocationsDAO locationsDAO;

    @Override
    public Optional<UserDTO> loginUser(String username, String password) {
        try {
            // 1️⃣ Tìm user theo username
            Optional<Users> userOpt = usersDAO.getUserByUsername(username);
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }

            Users user = userOpt.get();

            // 2️⃣ Kiểm tra mật khẩu
            boolean valid = PasswordUtil.verifyPassword(
                    password,
                    user.getPasswordHash(),
                    user.getPasswordSalt()
            );

            if (!valid) {
                return Optional.empty();
            }

            // 3️⃣ Chuyển sang DTO
            UserDTO dto = userMapper.toDTO(user);

            // 4️⃣ (Tùy chọn) Kiểm tra nếu là admin
            // Nếu bạn chỉ muốn đăng nhập admin:
            // if (!hasAdminRole(user.getUserId())) return Optional.empty();
            return Optional.of(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Nếu bạn cần kiểm tra quyền admin
    private boolean hasAdminRole(Integer userId) {
        // bạn có thể viết thêm query ở UsersDAO để kiểm tra role admin
        // ví dụ SELECT COUNT(*) FROM UserRoles ur 
        // JOIN Roles r ON ur.roleId = r.roleId 
        // WHERE ur.userId = ? AND r.roleName = 'ADMIN'
        return true; // tạm thời giả định là admin
    }

    @Override
    public List<UserDTO> getAllUser() {
        return usersDAO.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public void addUser(UserDTO userDTO, String password) {
        try {
            // resolve locationId
            Integer locationId = userDTO.getLocationId();
            if (locationId == null && userDTO.getCity() != null) {
                locationId = locationsDAO.getOrCreateIdByCity(userDTO.getCity());
            }
            //Mã hóa mật khẩu với salt ngẫu nhiên
            byte[] hashSalt = PasswordUtil.generateSalt(); // Tạo salt ngẫu nhiên
            byte[][] hashPassword = PasswordUtil.hashPassword(password, hashSalt); // Hash password
            byte[] passwordHash = hashPassword[0]; // Lấy hash
            byte[] passwordSalt = hashPassword[1]; // Lấy salt

            //Chuyển đổi từ DTO sang Model
            Users user = userMapper.toModel(userDTO);
            user.setPasswordHash(passwordHash); // Lưu hash password (byte[])
            user.setPasswordSalt(passwordSalt); // Lưu salt (byte[])
            user.setLocationId(locationId);

            usersDAO.createUser(user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        try {
            // 1️⃣ Tìm locationId nếu chỉ nhập city
            Integer locationId = userDTO.getLocationId();
            if (locationId == null && userDTO.getCity() != null) {
                locationId = locationsDAO.getOrCreateIdByCity(userDTO.getCity());
            }

            // 2️⃣ Chuyển DTO sang Model
            Users user = userMapper.toModel(userDTO);
            user.setLocationId(locationId);

            // 3️⃣ Gọi DAO để update
            boolean success = usersDAO.updateUser(user);
            if (!success) {
                throw new RuntimeException("Không thể cập nhật user ID = " + userDTO.getUserId());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật user: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        try {
            boolean success = usersDAO.deleteUser(userId);
            if (!success) {
                throw new RuntimeException("Không thể xóa user ID = " + userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa user: " + e.getMessage());
        }
    }

    @Override
    public List<LocationDTO> getAllLocation() {
        try {
            // 1️⃣ Lấy toàn bộ danh sách location từ DAO
            var locations = locationsDAO.getAllLocations();

            // 2️⃣ Chuyển sang DTO (nếu bạn chưa có LocationMapper)
            return locations.stream()
                    .map(loc -> {
                        LocationDTO dto = new LocationDTO();
                        dto.setLocationId(loc.getLocationId());
                        dto.setCity(loc.getCity());
                        dto.setAddress(loc.getAddress());
                        return dto;
                    })
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Trả về danh sách rỗng nếu lỗi
        }

    }

    @Override
    public UserDTO getUserById(Integer userId) {
        Optional<Users> optionalUser = usersDAO.getUserById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }

        Users user = optionalUser.get();
        UserDTO dto = new UserDTO();

        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setCreateAt(user.getCreateAt());
        dto.setRoleId(user.getRoleId());
        dto.setLocationId(user.getLocationId());

        return dto;
    }

}
