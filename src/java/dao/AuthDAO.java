package dao;

import model.Users;
import model.Customers;
import java.util.List;

public interface AuthDAO {
    
//    // Xác thực user
//    Users authenticateUser(String username, byte[] passwordHash);
//    
//    // Xác thực customer
//    Customers authenticateCustomer(String username, byte[] passwordHash);
//    
//    // Kiểm tra username có tồn tại không (Users)
//    boolean isUsernameExistsInUsers(String username);
//    
//    // Kiểm tra username có tồn tại không (Customers)
//    boolean isUsernameExistsInCustomers(String username);
//    
//    // Kiểm tra email có tồn tại không (Users)
//    boolean isEmailExistsInUsers(String email);
//    
//    // Kiểm tra email có tồn tại không (Customers)
//    boolean isEmailExistsInCustomers(String email);
//    
//    // Kiểm tra phone có tồn tại không (Users)
//    boolean isPhoneExistsInUsers(String phone);
//    
//    // Kiểm tra phone có tồn tại không (Customers)
//    boolean isPhoneExistsInCustomers(String phone);
//    
//    // Lấy user theo username
//    Users getUserByUsername(String username);
//    
//    // Lấy customer theo username
//    Customers getCustomerByUsername(String username);
//    
//    // Cập nhật mật khẩu user
//    boolean updateUserPassword(Integer userId, byte[] newPasswordHash, byte[] newPasswordSalt);
//    
//    // Cập nhật mật khẩu customer
//    boolean updateCustomerPassword(Integer customerId, byte[] newPasswordHash, byte[] newPasswordSalt);
//    
//    List<Users> getActiveUsers();
//    
//    List<Customers> getActiveCustomers();
//    
//    boolean isAccountLocked(Integer userId, String userType);
//    
//    boolean lockAccount(Integer userId, String userType);
//    
//    boolean unlockAccount(Integer userId, String userType);
}
