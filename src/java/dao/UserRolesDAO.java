package dao;

import model.UserRoles;
import java.util.List;

public interface UserRolesDAO {
    
    List<UserRoles> getAllUserRoles();
    
    List<UserRoles> getUserRolesByUser(Integer userId);
    
    List<UserRoles> getUserRolesByRole(Integer roleId);
    
    UserRoles getUserRole(Integer userId, Integer roleId);
    
    boolean assignUserRole(Integer userId, Integer roleId);
    
    boolean removeUserRole(Integer userId, Integer roleId);
    
    boolean removeAllUserRoles(Integer userId);
    
    boolean removeAllUsersByRole(Integer roleId);
    
    boolean hasUserRole(Integer userId, Integer roleId);
    
    boolean hasUserRoleByName(Integer userId, String roleName);
    
    List<String> getUserRoleNames(Integer userId);
    
    List<Integer> getUserRoleIds(Integer userId);
    
    int countUsersByRole(Integer roleId);
    
    int countUserRoles(Integer userId);
}
