package dao;

import java.util.List;
import java.util.Optional;
import model.User;

public interface UseDAO {
    List<User> getAllUsers();
    Optional<User> getUserById(Integer userId);
    Optional<User> getUserByUsername(String username);
    boolean createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(Integer userId);
    boolean changePassword(Integer userId, byte[] passwordHash, byte[] passwordSalt);
    boolean assignRole(Integer userId, Integer roleId);
    boolean removeRole(Integer userId, Integer roleId);
    public boolean existsUsername(String username);
    public boolean existsEmail(String email);
    public boolean existsPhone(String phone);
}
