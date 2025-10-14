/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import java.util.Optional;
import model.Roles;

public interface RolesDao {
    
    // Lấy tất cả role trong hệ thống
    List<Roles> getAllRoles();

    // Lấy role theo ID
    Optional<Roles> getRoleById(Integer roleId);

    // Lấy role theo tên (ví dụ: "ADMIN")
    Optional<Roles> getRoleByName(String roleName);

    // Tạo role mới
    boolean createRole(Roles role);

    // Cập nhật thông tin role
    boolean updateRole(Roles role);

    // Xóa role
    boolean deleteRole(Integer roleId);

    // Đếm số lượng user có role này
    int countUsersWithRole(Integer roleId);
}
