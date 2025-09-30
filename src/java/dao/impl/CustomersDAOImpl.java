/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import java.sql.Connection;
import dao.CustomersDAO;
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
import model.Locations;

/**
 *
 * @author admin
 */
public class CustomersDAOImpl implements CustomersDAO {

    // chuyển đổi từ ResultSet sang model
    private Customers mapResultSet(ResultSet rs) throws SQLException {
        Customers customer = new Customers();

        //map cac field co ban
        customer.setCustomerId(rs.getInt("customerId"));
        customer.setUsername(rs.getString("username"));
        customer.setPasswordHash(rs.getBytes("password_hash"));
        customer.setPasswordSalt(rs.getBytes("password_salt"));
        customer.setFullName(rs.getString("fullName"));
        customer.setPhone(rs.getString("phone"));
        customer.setEmail(rs.getString("email"));

        Date dateOfBirth = rs.getDate("dateOfBirth");
        if (dateOfBirth != null) {
            customer.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        Timestamp createAt = rs.getTimestamp("createAt");
        if (createAt != null) {
            customer.setCreateAt(createAt.toLocalDateTime());
        }

        Integer locationId = rs.getObject("locationId", Integer.class);
        customer.setLocationId(locationId);

        if (locationId != null) {
            Locations location = new Locations();
            location.setLocationId(locationId);
            location.setCity(rs.getString("city"));
            customer.setLocation(location);
        }

        return customer;
    }

    @Override
    public List<Customers> getAllCustomers() {
        String sql = "select c.*, l.city"
                + " from Customers c left join Locations l on"
                + "c.locationId = l.locationId";

        List<Customers> customersList = new ArrayList<>();

        try (Connection conn = (Connection) DB.get(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
    public boolean addCustomer(Customers customer) {
        String sql = "insert into Customers(username, password_hash, password_salt"
                + ", fullName, phone, email, dateOfBirth, locationId)"
                + "values(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getUsername());
            ps.setBytes(2, customer.getPasswordHash());
            ps.setBytes(3, customer.getPasswordSalt());
            ps.setString(4, customer.getFullName());
            ps.setString(5, customer.getPhone());
            ps.setString(6, customer.getEmail());
            ps.setDate(7, customer.getDateOfBirth() != null
                    ? Date.valueOf(customer.getDateOfBirth()) : null);
            ps.setObject(8, customer.getLocationId());

            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean updateCustomer(Customers customer) {
        String sql = "update Customers set fullName = ?, phone = ?, email = ?, dateOfBirth = ?"
                + "locationId = ? where customerId = ?";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setDate(4, customer.getDateOfBirth() != null
                    ? Date.valueOf(customer.getDateOfBirth()) : null);
            ps.setObject(5, customer.getLocationId());
            ps.setInt(6, customer.getCustomerId());

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
