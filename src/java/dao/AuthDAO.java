package dao;

import model.User;
import model.Customer;
import java.util.List;

public interface AuthDAO {
    
//    // Xác thực user
//    User authenticateUser(String username, byte[] passwordHash);
//    
//    // Xác thực customer
//    Customer authenticateCustomer(String username, byte[] passwordHash);
//    
//    // Kiểm tra username có tồn tại không (User)
//    boolean isUsernameExistsInUsers(String username);
//    
//    // Kiểm tra username có tồn tại không (Customer)
//    boolean isUsernameExistsInCustomers(String username);
//    
//    // Kiểm tra email có tồn tại không (User)
//    boolean isEmailExistsInUsers(String email);
//    
//    // Kiểm tra email có tồn tại không (Customer)
//    boolean isEmailExistsInCustomers(String email);
//    
//    // Kiểm tra phone có tồn tại không (User)
//    boolean isPhoneExistsInUsers(String phone);
//    
//    // Kiểm tra phone có tồn tại không (Customer)
//    boolean isPhoneExistsInCustomers(String phone);
//    
//    // Lấy user theo username
//    User getUserByUsername(String username);
//    
//    // Lấy customer theo username
//    Customer getCustomerByUsername(String username);
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
