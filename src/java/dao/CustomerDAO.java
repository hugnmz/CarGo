package dao;

import java.util.List;
import java.util.Optional;
import model.Customer;

public interface CustomerDAO {
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Integer customerId);
    Optional<Customer> getCustomerByUserName(String username);
    boolean addCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(Integer customerId);
    boolean changePassword(Integer customerId, byte[] newHash, byte[] newSalt);
    boolean existsUsername(String username);
    boolean existEmail(String email);
    boolean existPhone(String phone);
    Optional<Customer> getCustomerByUsernameAndEmail(String username, String email);
    public int countCustomer();
}
