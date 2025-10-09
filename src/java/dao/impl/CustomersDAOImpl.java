/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.CustomersDAO;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import model.Customers;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;
import util.reflection.ReflectionMapper;

/**
 *
 * @author admin
 */
@Repository
public class CustomersDAOImpl implements CustomersDAO {
<<<<<<< HEAD
=======

    // chuyển đổi từ ResultSet sang model
    private Customers mapResultSet(ResultSet rs) throws SQLException {
        return ReflectionMapper.mapResultSet(rs, Customers.class);
    }

>>>>>>> main
    @Override
    public List<Customers> getAllCustomers() {
        String sql = "select * from Customers";
        return JdbcTemplateUtil.query(sql, Customers.class);
    }

    @Override
    public Optional<Customers> getCustomerById(Integer customerId) {
        String sql = "select c.*, l.city "
                + "from Customers c left join Locations l on "
                + "c.locationId = l.locationId where c.customerId = ?";
        Customers one = JdbcTemplateUtil.queryOne(sql, Customers.class, customerId);
        return Optional.ofNullable(one);
    }

    @Override
    public boolean addCustomer(Customers c) {
        String sql = "INSERT INTO Customers("
                + "username, password_hash, password_salt, fullName, phone, email, dateOfBirth, "
                + "isVerified, verifyCode, verifyCodeExpire, locationId"
                + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                c.getLocationId()
        );
        if (id > 0) {
            c.setCustomerId(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCustomer(Customers customer) {
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
    public Optional<Customers> getCustomerByUserName(String username) {
        String sql = "select c.*, l.city "
                + "from Customers c left join Locations l on "
                + "c.locationId = l.locationId where c.username = ?";
        Customers one = JdbcTemplateUtil.queryOne(sql, Customers.class, username);
        return Optional.ofNullable(one);

    }

    @Override
    public boolean existPhone(String phone) {
        String sql = "select COUNT(*) FROM Customers WHERE phone = ?";
        int count = JdbcTemplateUtil.count(sql, phone);
        return count > 0;
    }

}
