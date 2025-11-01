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
            // Tìm user theo username
            Optional<Users> userOpt = usersDAO.getUserByUsername(username);
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }

            Users user = userOpt.get();

            // Kiểm tra mật khẩu
            boolean valid = PasswordUtil.verifyPassword(
                    password,
                    user.getPasswordHash(),
                    user.getPasswordSalt()
            );

            if (!valid) {
                return Optional.empty();
            }

            // Chuyển sang DTO
            UserDTO dto = userMapper.toDTO(user);

            // Nếu bạn chỉ muốn đăng nhập admin:
            // if (!hasAdminRole(user.getUserId())) return Optional.empty();
            return Optional.of(dto);

        } catch (Exception e) {
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
            // 
            if (usersDAO.getUserByUsername(userDTO.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Tên đăng nhập '" + userDTO.getUsername() + "' đã tồn tại!");
            }
            if (usersDAO.existsEmail(userDTO.getEmail())) {
                throw new IllegalArgumentException("Email '" + userDTO.getEmail() + "' đã tồn tại!");
            }

            if (usersDAO.existsPhone(userDTO.getPhone())) {
                throw new IllegalArgumentException("Phone '" + userDTO.getPhone() + "' đã tồn tại!");
            }

            // 2️⃣ Xử lý locationId
            Integer locationId = userDTO.getLocationId();
            if (locationId == null && userDTO.getCity() != null) {
                locationId = locationsDAO.getOrCreateIdByCity(userDTO.getCity());
            }

            // 3️⃣ Hash mật khẩu với salt ngẫu nhiên
            byte[] salt = PasswordUtil.generateSalt();
            byte[][] hashResult = PasswordUtil.hashPassword(password, salt);
            byte[] passwordHash = hashResult[0];
            byte[] passwordSalt = hashResult[1];

            // 4️⃣ Chuyển DTO → Model
            Users user = userMapper.toModel(userDTO);
            user.setPasswordHash(passwordHash);
            user.setPasswordSalt(passwordSalt);
            user.setLocationId(locationId);

            boolean created = usersDAO.createUser(user);
            if (!created) {
                throw new RuntimeException("Không thể thêm người dùng vào cơ sở dữ liệu.");
            }

            

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Đã xảy ra lỗi khi thêm người dùng.", e);
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        try {
            // Kiểm tra người dùng có tồn tại chưa
            Optional<Users> existingUser = usersDAO.getUserById(userDTO.getUserId());
            if (existingUser.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy người dùng ID = " + userDTO.getUserId());
            }

            // Kiểm tra email, phone (nếu có thay đổi)
            Users oldUser = existingUser.get();

            if (!oldUser.getEmail().equals(userDTO.getEmail())
                    && usersDAO.existsEmail(userDTO.getEmail())) {
                throw new IllegalArgumentException("Email '" + userDTO.getEmail() + "' đã được sử dụng!");
            }

            if (!oldUser.getPhone().equals(userDTO.getPhone())
                    && usersDAO.existsPhone(userDTO.getPhone())) {
                throw new IllegalArgumentException("Số điện thoại '" + userDTO.getPhone() + "' đã tồn tại!");
            }

            // Resolve locationId
            Integer locationId = userDTO.getLocationId();
            if (locationId == null && userDTO.getCity() != null) {
                locationId = locationsDAO.getOrCreateIdByCity(userDTO.getCity());
            }

            // Chuyển đổi sang model
            Users user = userMapper.toModel(userDTO);
            user.setLocationId(locationId);

            boolean success = usersDAO.updateUser(user);
            if (!success) {
                throw new RuntimeException("Không thể cập nhật thông tin người dùng (ID = " + userDTO.getUserId() + ")");
            }

            

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Đã xảy ra lỗi khi cập nhật người dùng.", e);
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
            throw new RuntimeException("Lỗi khi xóa user: " + e.getMessage());
        }
    }

    @Override
    public List<LocationDTO> getAllLocation() {
        try {
            // Lấy toàn bộ danh sách Location từ Database
            var locations = locationsDAO.getAllLocations();

            // Chuyển sang DTO
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
            // Trả về danh sách rỗng nếu lỗi
            return List.of();
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

    @Override
    public boolean changeUserPassword(Integer userId, String oldPassword, String newPassword) {
        try {
            // Không cho phép đặt mật khẩu mới giống mật khẩu cũ
            if (oldPassword.equals(newPassword)) {
                return false;
            }

            // Lấy thông tin user từ database
            Optional<Users> ou = usersDAO.getUserById(userId);
            if (!ou.isPresent()) {
                // Không tìm thấy user
                return false;
            }

            Users user = ou.get();

            // Xác thực mật khẩu cũ
            if (!PasswordUtil.verifyPassword(oldPassword,
                    user.getPasswordHash(), user.getPasswordSalt())) {
                // Mật khẩu cũ không đúng
                return false; 
            }

            // Tạo salt mới và hash mật khẩu mới
            byte[] newSalt = PasswordUtil.generateSalt(); // Tạo salt mới
            byte[][] newHash = PasswordUtil.hashPassword(newPassword, newSalt); // Hash mật khẩu mới
            byte[] newPasswordHash = newHash[0]; // Lấy hash
            byte[] newPasswordSalt = newHash[1]; // Lấy salt

            // Cập nhật mật khẩu mới trong database
            return usersDAO.changePassword(user.getUserId(), newPasswordHash, newPasswordSalt);

        } catch (Exception e) {
            e.printStackTrace();
            // Có lỗi xảy ra
            return false; 
        }
    }

}
