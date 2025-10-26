package dao.impl;

import dao.UsersDAO;
import model.Users;
import java.util.List;
import java.util.Optional;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class UserDaoImp implements UsersDAO {

    @Override
    public List<Users> getAllUsers() {
        String sql = "SELECT * FROM Users";
        return JdbcTemplateUtil.query(sql, Users.class);
    }

    @Override
    public Optional<Users> getUserById(Integer userId) {
        String sql = """
            SELECT u.*, l.city
            FROM Users u
            LEFT JOIN Locations l ON u.locationId = l.locationId
            WHERE u.userId = ?
        """;
        Users one = JdbcTemplateUtil.queryOne(sql, Users.class, userId);
        return Optional.ofNullable(one);
    }

    @Override
    public Optional<Users> getUserByUsername(String username) {
        // SỬA: Cập nhật query để include role information
        String sql = """
            SELECT u.*, l.city, l.address, r.roleId, r.roleName
            FROM Users u
            LEFT JOIN Locations l ON u.locationId = l.locationId
            LEFT JOIN Roles r ON u.roleId = r.roleId
            WHERE u.username = ?
        """;
        Users one = JdbcTemplateUtil.queryOne(sql, Users.class, username);
        return Optional.ofNullable(one);
    }

//    public static void main(String[] args) {
//        UserDaoImp user = new UserDaoImp();
//        Optional<Users> list = user.getUserByUsername("admin");
//        
//
//        if (list.isPresent()) {
//            System.out.println("Tìm thấy user:");
//            System.out.println("ID: " + list.get().getUserId());
//            System.out.println("Username: " + list.get().getUsername());
//            System.out.println("Role: " + list.get().getRoles());
//        } else { 
//            System.out.println("Không tìm thấy user 'admin'");
//        }
//    }
    @Override
    public boolean createUser(Users user) {
        // SỬA: Thêm roleId vào INSERT statement
        String sql = """
        INSERT INTO Users (
            username, password_hash, password_salt,
            fullName, phone, email, dateOfBirth, locationId, roleId
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        int id = JdbcTemplateUtil.insertAndReturnKey(
                sql,
                user.getUsername(),
                user.getPasswordHash(),
                user.getPasswordSalt(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getDateOfBirth() != null ? java.sql.Date.valueOf(user.getDateOfBirth()) : null,
                user.getLocationId(),
                user.getRoleId() // THÊM MỚI: roleId parameter
        );

        if (id > 0) {
            user.setUserId(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUser(Users user) {
        // SỬA: Thêm roleId vào UPDATE statement
        String sql = """
            UPDATE Users SET
                fullName = ?, phone = ?, email = ?, dateOfBirth = ?,
                locationId = ?, roleId = ?
            WHERE userId = ?
        """;

        int affected = JdbcTemplateUtil.update(
                sql,
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getDateOfBirth() != null ? java.sql.Date.valueOf(user.getDateOfBirth()) : null,
                user.getLocationId(),
                user.getRoleId(), // THÊM MỚI: roleId parameter
                user.getUserId()
        );

        return affected > 0;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        String sql = "DELETE FROM Users WHERE userId = ?";
        int affected = JdbcTemplateUtil.update(sql, userId);
        return affected > 0;
    }

    @Override
    public boolean changePassword(Integer userId, byte[] passwordHash, byte[] passwordSalt) {
        String sql = """
            UPDATE Users SET password_hash = ?, password_salt = ?
            WHERE userId = ?
        """;
        int affected = JdbcTemplateUtil.update(sql, passwordHash, passwordSalt, userId);
        return affected > 0;
    }

    @Override
    public boolean assignRole(Integer userId, Integer roleId) {
        // SỬA: Thay đổi logic vì không còn bảng UserRoles
        String sql = "UPDATE Users SET roleId = ? WHERE userId = ?";
        int affected = JdbcTemplateUtil.update(sql, roleId, userId);
        return affected > 0;
    }

    @Override
    public boolean removeRole(Integer userId, Integer roleId) {
        // SỬA: Thay đổi logic vì không còn bảng UserRoles
        // Với database mới, không thể remove role (mỗi user phải có 1 role)
        return false;
    }
}
