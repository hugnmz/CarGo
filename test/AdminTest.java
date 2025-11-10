/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package test;

import util.PasswordUtil;
import model.User;
import dao.impl.UserDaoImpl;
import java.util.Optional;

/**
 *
 * @author DELL
 */
public class AdminTest {

    public static void main(String[] args) {
        try {
            // Khởi tạo DAO
            UserDaoImpl userDao = new UserDaoImpl();

            // Tạo đối tượng admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setFullName("System Administrator");

            // Mã hóa mật khẩu
            String password = "111";
            byte[] salt = PasswordUtil.generateSalt();
            byte[][] result = PasswordUtil.hashPassword(password, salt);

            admin.setPasswordHash(result[0]);
            admin.setPasswordSalt(result[1]);

            // Gán roleId cho admin
            admin.setRoleId(3);

            // Nếu có locationId
            admin.setLocationId(null);

            // Tạo user
            boolean created = userDao.createUser(admin);
            if (created) {
                System.out.println("Admin created successfully with ID: " + admin.getUserId());
                System.out.println("Role ADMIN assigned (roleId = " + admin.getRoleId() + ")");
            } else {
                System.out.println("Failed to create admin.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        UseDaoImp userDao = new UseDaoImp();
//        Optional<Users> adminOpt = userDao.getUserByUsername("admin");
//
//        if (adminOpt.isPresent()) {
//            User admin = adminOpt.get();
//            System.out.println("✅ Found user:");
//            System.out.println("ID: " + admin.getUserId());
//            System.out.println("Username: " + admin.getUsername());
//            System.out.println("Hash (length): " + admin.getPasswordHash().length);
//            System.out.println("Salt (length): " + admin.getPasswordSalt().length);
//        } else {
//            System.out.println("❌ User 'admin' not found");
//        }
    }
}
