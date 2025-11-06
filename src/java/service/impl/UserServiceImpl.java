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
import util.MessageUtil;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import util.exception.ApplicationException;
import util.exception.DataAccessException;
import util.exception.ValidationException;
import util.exception.BusinessException;

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
        Optional<Users> userOpt = usersDAO.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            throw new ValidationException(MessageUtil.getError("error.login.invalid"));
        }

        Users user = userOpt.get();

        boolean valid = PasswordUtil.verifyPassword(
                password,
                user.getPasswordHash(),
                user.getPasswordSalt()
        );

        if (!valid) {
            throw new ValidationException(MessageUtil.getError("error.login.invalid"));
        }

        UserDTO dto = userMapper.toDTO(user);
        return Optional.of(dto);
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
            // Kiểm tra trùng username
            if (usersDAO.getUserByUsername(userDTO.getUsername()).isPresent()) {
                throw new ValidationException(MessageUtil.getError("error.username.exists"));
            }
            // Kiểm tra trùng email
            if (usersDAO.existsEmail(userDTO.getEmail())) {
                String emailError = MessageUtil.getError("error.email.exists").replace("{0}", userDTO.getEmail());
                throw new ValidationException(emailError);
            }
            // Kiểm tra trùng phone
            if (usersDAO.existsPhone(userDTO.getPhone())) {
                String phoneError = MessageUtil.getError("error.phone.exists").replace("{0}", userDTO.getPhone());
                throw new ValidationException(phoneError);
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
                throw new BusinessException(MessageUtil.getError("error.dataaccess.user.add.failed"));
            }

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.user.add.error"), e);
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        try {
            // Kiểm tra người dùng có tồn tại chưa
            Optional<Users> existingUser = usersDAO.getUserById(userDTO.getUserId());
            if (existingUser.isEmpty()) {
                throw new ValidationException(MessageUtil.getError("error.validation.user.not.found"));
            }

            // Kiểm tra email, phone (nếu có thay đổi)
            Users oldUser = existingUser.get();

            // Kiểm tra trùng email nếu có thay đổi
            if (!oldUser.getEmail().equals(userDTO.getEmail())
                    && usersDAO.existsEmail(userDTO.getEmail())) {
                String emailError = MessageUtil.getError("error.email.exists").replace("{0}", userDTO.getEmail());
                throw new ValidationException(emailError);
            }
            // Kiểm tra trùng phone nếu có thay đổi
            if (!oldUser.getPhone().equals(userDTO.getPhone())
                    && usersDAO.existsPhone(userDTO.getPhone())) {
                String phoneError = MessageUtil.getError("error.phone.exists").replace("{0}", userDTO.getPhone());
                throw new ValidationException(phoneError);
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
                throw new BusinessException(MessageUtil.getError("error.dataaccess.user.update.failed"));
            }

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.user.update.error"), e);
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        try {
            boolean success = usersDAO.deleteUser(userId);
            if (!success) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.user.delete.failed"));
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.user.delete.error"), e);
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

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }

    }

    @Override
    public UserDTO getUserById(Integer userId) {
        try {
            Optional<Users> optionalUser = usersDAO.getUserById(userId);
            if (optionalUser.isEmpty()) {
                throw new ValidationException(MessageUtil.getError("error.validation.user.not.found"));
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
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }
    }

    @Override
    public boolean changeUserPassword(Integer userId, String oldPassword, String newPassword) {
        try {
            // Không cho phép đặt mật khẩu mới giống mật khẩu cũ
            if (oldPassword.equals(newPassword)) {
                throw new ValidationException(MessageUtil.getError("error.password.change.failed"));
            }

            // Lấy thông tin user từ database
            Optional<Users> ou = usersDAO.getUserById(userId);
            if (!ou.isPresent()) {
                throw new ValidationException(MessageUtil.getError("error.validation.user.not.found"));
            }

            Users user = ou.get();

            // Xác thực mật khẩu cũ
            if (!PasswordUtil.verifyPassword(oldPassword,
                    user.getPasswordHash(), user.getPasswordSalt())) {
                throw new ValidationException(MessageUtil.getError("error.password.change.failed"));
            }

            // Tạo salt mới và hash mật khẩu mới
            byte[] newSalt = PasswordUtil.generateSalt(); // Tạo salt mới
            byte[][] newHash = PasswordUtil.hashPassword(newPassword, newSalt); // Hash mật khẩu mới
            byte[] newPasswordHash = newHash[0]; // Lấy hash
            byte[] newPasswordSalt = newHash[1]; // Lấy salt

            // Cập nhật mật khẩu mới trong database
            boolean result = usersDAO.changePassword(user.getUserId(), newPasswordHash, newPasswordSalt);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.password.change.failed"));
            }
            return true;

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.password.change.failed"), e);
        }
    }

}
