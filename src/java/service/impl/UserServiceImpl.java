package service.impl;

import dao.LocationsDAO;
import dao.UsersDAO;
import dto.UserDTO;
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

    // ✅ Nếu bạn cần kiểm tra quyền admin
    private boolean hasAdminRole(Integer userId) {
        // bạn có thể viết thêm query ở UsersDAO để kiểm tra role admin
        // ví dụ SELECT COUNT(*) FROM UserRoles ur 
        // JOIN Roles r ON ur.roleId = r.roleId 
        // WHERE ur.userId = ? AND r.roleName = 'ADMIN'
        return true; // tạm thời giả định là admin
    }
}
