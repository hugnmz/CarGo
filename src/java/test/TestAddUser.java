package test;

import dto.UseDTO;
import util.di.DIContainer;
import service.UseService;

public class TestAddUser {
    public static void main(String[] args) {
        try {
            // Lấy instance UseService từ DI Container
            UseService userService = DIContainer.get(UseService.class);

            // Tạo DTO user mới
            UseDTO newUser = new UseDTO();
            newUser.setUsername("admin_test");
            newUser.setFullName("Nguyễn Văn Test");
            newUser.setEmail("testadmin@example.com");
            newUser.setPhone("0987654321");
            newUser.setRoleId(1);        // phải khớp roleId có trong bảng Roles
            newUser.setLocationId(2);    // phải khớp locationId có trong bảng Locations

            String password = "123456";

            // Gọi hàm addUser
            userService.addUser(newUser, password);

            // In kết quả
            System.out.println("✅ Đã thêm user thành công!");
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi thêm user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
