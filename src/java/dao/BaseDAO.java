/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import util.PasswordUtil;
import model.Users;
import dao.impl.UserDaoImp;
import java.util.Optional;

/**
 *
 * @author admin
 */
public class BaseDAO {

    public static void main(String[] args) {
//        try {
//            UserDaoImp userDao = new UserDaoImp();
//
//            Users admin = new Users();
//            admin.setUsername("admin");
//            admin.setFullName("System Administrator");
//            admin.setPhone("0111111111");
//            admin.setEmail("admin@system.com");
//
//            // Mã hoá mật khẩu
//            String password = "admin123";
//            byte[] salt = PasswordUtil.generateSalt();
//            byte[][] result = PasswordUtil.hashPassword(password, salt);
//            admin.setPasswordHash(result[0]);
//            admin.setPasswordSalt(result[1]);
//
//            boolean created = userDao.createUser(admin);
//            if (created) {
//                System.out.println("✅ Admin created successfully with ID: " + admin.getUserId());
//
//                // Gán role ADMIN (kiểm tra roleId thật trong bảng Roles)
//                userDao.assignRole(admin.getUserId(), 3);
//                System.out.println("✅ Role ADMIN assigned successfully!");
//            } else {
//                System.out.println("❌ Failed to create admin.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        UserDaoImp userDao = new UserDaoImp();
        Optional<Users> adminOpt = userDao.getUserByUsername("admin");

        if (adminOpt.isPresent()) {
            Users admin = adminOpt.get();
            System.out.println("✅ Found user:");
            System.out.println("ID: " + admin.getUserId());
            System.out.println("Username: " + admin.getUsername());
            System.out.println("Hash (length): " + admin.getPasswordHash().length);
            System.out.println("Salt (length): " + admin.getPasswordSalt().length);
        } else {
            System.out.println("❌ User 'admin' not found");
        }
    }
}
