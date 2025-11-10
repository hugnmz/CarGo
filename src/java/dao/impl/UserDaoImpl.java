package dao.impl;

import model.User;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;
import dao.UserDAO;

@Repository
public class UserDaoImpl implements UserDAO {

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT u.*, l.city, r.roleName\n"
                + "        FROM Users u\n"
                + "        LEFT JOIN Locations l ON u.locationId = l.locationId\n"
                + "        LEFT JOIN Roles r ON u.roleId = r.roleId";
        return JdbcTemplateUtil.query(sql, User.class);
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        String sql = """
            SELECT u.*, l.city
            FROM Users u
            LEFT JOIN Locations l ON u.locationId = l.locationId
            WHERE u.userId = ?
        """;
        User one = JdbcTemplateUtil.queryOne(sql, User.class, userId);
        return Optional.ofNullable(one);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        String sql = """
            SELECT u.*, l.city, r.roleName
                    FROM Users u
                    LEFT JOIN Locations l ON u.locationId = l.locationId
                    LEFT JOIN Roles r ON u.roleId = r.roleId
                    WHERE u.username = ?
        """;
        User one = JdbcTemplateUtil.queryOne(sql, User.class, username);
        return Optional.ofNullable(one);
    }

    @Override
    public boolean createUser(User user) {
        String sql = """
        INSERT INTO Users (
            username, password_hash, password_salt,
            fullName, phone, email,
            locationId, roleId, createAt
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
                user.getLocationId(),
                user.getRoleId(),
                user.getCreateAt() != null ? Timestamp.valueOf(user.getCreateAt()) : new Timestamp(System.currentTimeMillis())
        );

        if (id > 0) {
            user.setUserId(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = """
            UPDATE Users SET
                fullName = ?, phone = ?, email = ?, locationId = ?
            WHERE userId = ?
        """;

        int affected = JdbcTemplateUtil.update(
                sql,
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getLocationId(),
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
        String sql = "INSERT INTO UserRoles (userId, roleId) VALUES (?, ?)";
        int affected = JdbcTemplateUtil.update(sql, userId, roleId);
        return affected > 0;
    }

    @Override
    public boolean removeRole(Integer userId, Integer roleId) {
        String sql = "DELETE FROM UserRoles WHERE userId = ? AND roleId = ?";
        int affected = JdbcTemplateUtil.update(sql, userId, roleId);
        return affected > 0;
    }

    @Override
    public boolean existsUsername(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        int count = JdbcTemplateUtil.count(sql, username);
        return count > 0;
    }

    @Override
    public boolean existsEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        int count = JdbcTemplateUtil.count(sql, email);
        return count > 0;
    }

    @Override
    public boolean existsPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM Users WHERE phone = ?";
        int count = JdbcTemplateUtil.count(sql, phone);
        return count > 0;
    }
}