package dao;

import java.util.List;
import java.util.Optional;
import model.Users;

public interface UsersDAO {
    List<Users> getAllUsers();
    Optional<Users> getUserById(Integer userId);
    Optional<Users> getUserByUsername(String username);
    boolean createUser(Users user);
    boolean updateUser(Users user);
    boolean deleteUser(Integer userId);
    boolean changePassword(Integer userId, byte[] passwordHash, byte[] passwordSalt);
    boolean assignRole(Integer userId, Integer roleId);
    boolean removeRole(Integer userId, Integer roleId);
}
