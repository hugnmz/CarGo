/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.CustomerDTO;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author admin
 */
public interface CustomerService {

    //login
    Optional<CustomerDTO> loginCustomer(String username, String password);

    // register
    boolean registerCustomer(CustomerDTO customerDTO, String password);

    //quản lí customer
    Optional<CustomerDTO> getCustomerByUsername(String username);

    List<CustomerDTO> getAllCustomers();

    boolean addCustomer(CustomerDTO customerDTO);

    boolean updateCustomer(CustomerDTO customerDTO);

    boolean deleteCustomer(Integer customerId);

    // validation method
    boolean isUsernameExists(String username);

    boolean isEmailExists(String email);

    boolean isPhoneExists(String phone);

    // password method
    boolean changeCustomerPassword(Integer customerId, String oldPassword, String newPassword);

    boolean resetCustomerPassword(String username, String newPassword);

    boolean verifyAccount(String username, String code);

    Optional<String> generateAndStoreVerificationCode(String username);
}
