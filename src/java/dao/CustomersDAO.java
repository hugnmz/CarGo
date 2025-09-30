package dao;

import java.util.List;
import java.util.Optional;
import model.Customers;

public interface CustomersDAO {
    List<Customers> getAllCustomers();
    Optional<Customers> getCustomerById(Integer customerId);
    Optional<Customers> getCustomerByUserName(String username);
    boolean addCustomer(Customers customer);
    boolean updateCustomer(Customers customer);
    boolean deleteCustomer(Integer customerId);
    boolean changePassword(Integer customerId, byte[] newHash, byte[] newSalt);
    boolean existsUsername(String username);
    boolean existEmail(String email);
    boolean existPhone(String phone);
}
