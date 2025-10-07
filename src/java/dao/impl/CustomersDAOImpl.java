/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import java.sql.Connection;
import dao.CustomersDAO;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Customers;
import java.sql.SQLException;
import java.sql.Date;
import util.DB;
import java.sql.Timestamp;
import util.di.annotation.Repository;
import util.reflection.ReflectionMapper;

/**
 *
 * @author admin
 */
@Repository
public class CustomersDAOImpl implements CustomersDAO {

    // chuyển đổi từ ResultSet sang model
    private Customers mapResultSet(ResultSet rs) throws SQLException {
        return ReflectionMapper.mapResultSet(rs, Customers.class);
    }

    @Override
    public List<Customers> getAllCustomers() {
        String sql = "select * from Customers";

        List<Customers> customersList = new ArrayList<>();

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customers customers = mapResultSet(rs);
                customersList.add(customers);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customersList;
    }

    @Override
    public Optional<Customers> getCustomerById(Integer customerId) {
        String sql = "select c.*, l.city "
                + "from Customers c left join Locations l on "
                + "c.locationId = l.locationId where c.customerId = ?";

        Optional<Customers> customer;
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean addCustomer(Customers c) {
        String sql = "INSERT INTO Customers("
                + "username, password_hash, password_salt, fullName, phone, email, dateOfBirth, "
                + "isVerified, verifyCode, verifyCodeExpire, locationId"
                + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getUsername());
            ps.setBytes(2, c.getPasswordHash());
            ps.setBytes(3, c.getPasswordSalt());
            ps.setString(4, c.getFullName());
            ps.setString(5, c.getPhone());
            ps.setString(6, c.getEmail());
            ps.setDate(7, c.getDateOfBirth() != null ? java.sql.Date.valueOf(c.getDateOfBirth()) : null);

            // thêm 3 cột xác minh (PLAIN TEXT code)
            ps.setBoolean(8, c.isIsVerified());
            ps.setString(9, c.getVerifyCode());
            ps.setTimestamp(10, c.getVerifyCodeExpire() != null
                    ? Timestamp.valueOf(c.getVerifyCodeExpire()) : null);

            ps.setObject(11, c.getLocationId());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setCustomerId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCustomer(Customers customer) {
        String sql = "update Customers set fullName = ?, phone = ?, email = ?, dateOfBirth = ?,"
                + "locationId = ?, verifyCode = ?, verifyCodeExpire = ?, isVerified=?"
                + " where customerId = ?";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setDate(4, customer.getDateOfBirth() != null
                    ? Date.valueOf(customer.getDateOfBirth()) : null);
            ps.setObject(5, customer.getLocationId());
            ps.setString(6, customer.getVerifyCode());
            ps.setTimestamp(7, customer.getVerifyCodeExpire() != null 
                    ? Timestamp.valueOf(customer.getVerifyCodeExpire()) : null);
            ps.setBoolean(8, customer.isIsVerified());
            ps.setInt(9, customer.getCustomerId());
            
            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(Integer customerId) {
        String sql = "delete from Customers where customerId = ?";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changePassword(Integer customerId, byte[] newHash, byte[] newSalt) {
        String sql = "update Customers set password_hash = ?, password_salt = ?"
                + "where customerId = ?";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBytes(1, newHash);
            ps.setBytes(2, newSalt);
            ps.setInt(3, customerId);

            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsUsername(String username) {
        String sql = "SELECT COUNT(*) FROM Customers WHERE username = ?";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean existEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Customers WHERE email = ?";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Optional<Customers> getCustomerByUserName(String username) {
        String sql = "select c.*, l.city "
                + "from Customers c left join Locations l on "
                + "c.locationId = l.locationId where c.username = ?";

        Optional<Customers> customer;
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();

    }

    @Override
    public boolean existPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM Customers WHERE phone = ?";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
