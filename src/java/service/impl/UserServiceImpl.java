package service.impl;

import dto.LocationDTO;
import dto.UseDTO;
import java.util.List;
import java.util.Optional;
import mapper.UseMapper;
import model.User;
import util.PasswordUtil;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import dao.LocDAO;
import dao.UseDAO;
import service.UseService;

@Service
public class UserServiceImpl implements UseService {

    @Autowired
    private UseDAO usersDAO;

    @Autowired
    private UseMapper userMapper;

    @Autowired
    private LocDAO locationsDAO;

    @Override
    public Optional<UseDTO> loginUser(String username, String password) {
        try {
            // Tìm user theo username
            Optional<User> userOpt = usersDAO.getUserByUsername(username);
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }

            User user = userOpt.get();

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
            UseDTO dto = userMapper.toDTO(user);

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
        // bạn có thể viết thêm query ở UseDAO để kiểm tra role admin
        // ví dụ SELECT COUNT(*) FROM UserRoles ur 
        // JOIN Roles r ON ur.roleId = r.roleId 
        // WHERE ur.userId = ? AND r.roleName = 'ADMIN'
        return true; // tạm thời giả định là admin
    }

    @Override
    public List<UseDTO> getAllUser() {
        return usersDAO.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public void addUser(UseDTO userDTO, String password) {
        try {
            // check lỗi
            if (usersDAO.getUserByUsername(userDTO.getUsername()).isPresent()) {
                throw new IllegalArgumentException(
                        String.format("Tên đăng nhập '%s' đã tồn tại!", userDTO.getUsername())
                );
            }

            if (usersDAO.existsEmail(userDTO.getEmail())) {
                throw new IllegalArgumentException(
                        String.format("Email '%s' đã tồn tại!", userDTO.getEmail())
                );
            }

            if (usersDAO.existsPhone(userDTO.getPhone())) {
                throw new IllegalArgumentException(
                        String.format("Số điện thoại '%s' đã tồn tại!", userDTO.getPhone())
                );
            }

            // Xử lí LocationId
            Integer locationId = userDTO.getLocationId();
            if (locationId == null && userDTO.getCity() != null) {
                locationId = locationsDAO.getOrCreateIdByCity(userDTO.getCity());
            }

            // Hash mật khẩu với salt ngẫu nhiên
            byte[] salt = PasswordUtil.generateSalt();
            byte[][] hashResult = PasswordUtil.hashPassword(password, salt);
            byte[] passwordHash = hashResult[0];
            byte[] passwordSalt = hashResult[1];

            // Chuyển DTO -> Model
            User user = userMapper.toModel(userDTO);
            user.setPasswordHash(passwordHash);
            user.setPasswordSalt(passwordSalt);
            user.setLocationId(locationId);

            //Gọi dao để thực hiện insert
            boolean created = usersDAO.createUser(user);
            if (!created) {
                throw new RuntimeException("Không thể thêm người dùng vào cơ sở dữ liệu.");
            }

            System.out.println("Thêm user thành công: " + user.getUsername());

        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
            throw new RuntimeException("Đã xảy ra lỗi khi thêm người dùng.", e);
        }
    }

    @Override
    public void updateUser(UseDTO userDTO) {
        try {
            // Kiểm tra người dùng có tồn tại chưa
            Optional<User> existingUser = usersDAO.getUserById(userDTO.getUserId());
            if (existingUser.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy người dùng ID = " + userDTO.getUserId());
            }

            // Kiểm tra email, phone (nếu có thay đổi)
            User oldUser = existingUser.get();

            if (!oldUser.getEmail().equals(userDTO.getEmail())
                    && usersDAO.existsEmail(userDTO.getEmail())) {
                throw new IllegalArgumentException(String.format("Email '%s' đã được sử dụng!", userDTO.getEmail()));
            }

            if (!oldUser.getPhone().equals(userDTO.getPhone())
                    && usersDAO.existsPhone(userDTO.getPhone())) {
                throw new IllegalArgumentException(String.format("Số điện thoại '%s' đã tồn tại!", userDTO.getPhone()));
            }

            // Resolve locationId
            Integer locationId = userDTO.getLocationId();
            if (locationId == null && userDTO.getCity() != null) {
                locationId = locationsDAO.getOrCreateIdByCity(userDTO.getCity());
            }

            // Chuyển đổi sang model
            User user = userMapper.toModel(userDTO);
            user.setLocationId(locationId);

            boolean success = usersDAO.updateUser(user);
            if (!success) {
                throw new RuntimeException(String.format("Không thể cập nhật thông tin người dùng (ID = %d)", userDTO.getUserId()));
            }

            System.out.println("Cập nhật user thành công: " + user.getUsername());

        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi dữ liệu: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
            throw new RuntimeException("Đã xảy ra lỗi khi cập nhật người dùng.", e);
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        try {
            //Gọi dao đề truyền dữ liệu
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
            e.printStackTrace();
            // Trả về danh sách rỗng nếu lỗi
            return List.of();
        }

    }

    @Override
    public UseDTO getUserById(Integer userId) {
        
        Optional<User> optionalUser = usersDAO.getUserById(userId);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        UseDTO dto = new UseDTO();

        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setCreateAt(user.getCreateTime());
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
            Optional<User> ou = usersDAO.getUserById(userId);
            if (!ou.isPresent()) {
                // Không tìm thấy user
                return false;
            }

            User user = ou.get();

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
