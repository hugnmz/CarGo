/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import model.Customer;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;
import dao.CustomerDAO;

/**
 *
 * @author admin
 */
@Repository
public class CustomersDAOImpl implements CustomerDAO {

    @Override
    public List<Customer> getAllCustomers() {
        String sql = "select * from Customers c JOIN Locations l ON c.locationId = l.locationId";
        return JdbcTemplateUtil.query(sql, Customer.class);
    }

    @Override
    public Optional<Customer> getCustomerById(Integer customerId) {
        String sql = "select c.*, l.city "
                + "from Customers c left join Locations l on "
                + "c.locationId = l.locationId where c.customerId = ?";
        Customer one = JdbcTemplateUtil.queryOne(sql, Customer.class, customerId);
        return Optional.ofNullable(one);
    }

    @Override
    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO Customers("
                + "username, password_hash, password_salt, fullName, phone, email, dateOfBirth, "
                + "isVerified, verifyCode, verifyCodeExpire, locationId, createAt"
                + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int id = JdbcTemplateUtil.insertAndReturnKey(
                sql,
                c.getUsername(),
                c.getPasswordHash(),
                c.getPasswordSalt(),
                c.getFullName(),
                c.getPhone(),
                c.getEmail(),
                c.getDateOfBirth() != null ? java.sql.Date.valueOf(c.getDateOfBirth()) : null,
                c.isIsVerified(),
                c.getVerifyCode(),
                c.getVerifyCodeExpire() != null ? Timestamp.valueOf(c.getVerifyCodeExpire()) : null,
                c.getLocationId(),
                c.getCreateAt()
        );
        if (id > 0) {
            c.setCustomerId(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        String sql = "update Customers set fullName = ?, phone = ?, email = ?, dateOfBirth = ?,"
                + "locationId = ?, verifyCode = ?, verifyCodeExpire = ?, isVerified=?"
                + " where customerId = ?";
        int affected = JdbcTemplateUtil.update(
                sql,
                customer.getFullName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getDateOfBirth() != null ? java.sql.Date.valueOf(customer.getDateOfBirth()) : null,
                customer.getLocationId(),
                customer.getVerifyCode(),
                customer.getVerifyCodeExpire() != null ? Timestamp.valueOf(customer.getVerifyCodeExpire()) : null,
                customer.isIsVerified(),
                customer.getCustomerId()
        );
        return affected > 0;
    }

    @Override
    public boolean deleteCustomer(Integer customerId) {
        String sql = "delete from Customers where customerId = ?";
        int affected = JdbcTemplateUtil.update(sql, customerId);
        return affected > 0;
    }

    @Override
    public boolean changePassword(Integer customerId, byte[] newHash, byte[] newSalt) {
        String sql = "update Customers set password_hash = ?, password_salt = ?"
                + "where customerId = ?";
        int affected = JdbcTemplateUtil.update(sql, newHash, newSalt, customerId);
        return affected > 0;
    }

    @Override
    public boolean existsUsername(String username) {
        String sql = "SELECT COUNT(*) FROM Customers WHERE username = ?";
        int count = JdbcTemplateUtil.count(sql, username);
        return count > 0;
    }

    @Override
    public boolean existEmail(String email) {
        String sql = "select COUNT(*) FROM Customers WHERE email = ?";
        int count = JdbcTemplateUtil.count(sql, email);
        return count > 0;
    }

    @Override
    public Optional<Customer> getCustomerByUserName(String username) {
        String sql = "select c.*, l.city "
                + "from Customers c left join Locations l on "
                + "c.locationId = l.locationId where c.username = ?";
        Customer one = JdbcTemplateUtil.queryOne(sql, Customer.class, username);
        return Optional.ofNullable(one);

    }

    @Override
    public boolean existPhone(String phone) {
        String sql = "select COUNT(*) FROM Customers WHERE phone = ?";
        int count = JdbcTemplateUtil.count(sql, phone);
        return count > 0;
    }

    @Override
    public Optional<Customer> getCustomerByUsernameAndEmail(String username, String email) {
        String sql = "select c.*, l.city "
                + "from Customers c left join Locations l on "
                + "c.locationId = l.locationId where c.username = ? AND c.email = ?";
        Customer one = JdbcTemplateUtil.queryOne(sql, Customer.class, username, email);
        return Optional.ofNullable(one);
    }

    @Override
    public int countCustomer() {
        String sql = "SELECT COUNT(*) FROM dbo.Customers";
        return JdbcTemplateUtil.count(sql);
    }

}
