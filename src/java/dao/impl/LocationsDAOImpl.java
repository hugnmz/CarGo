/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import java.sql.Connection;
import dao.LocationsDAO;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import util.DB;
import util.di.annotation.Repository;

/**
 *
 * @author admin
 */
@Repository
public class LocationsDAOImpl implements LocationsDAO {

    @Override
    public Integer findIdByCity(String city) {

        String sql = "select locationId from Locations"
                + "where UPPER(LTRIM(RTRIM(city))) = UPPER(LTRIM(RTRIM(?)))";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // tra ve key sau khi insert
    @Override
    public int insertCity(String city) {
        String sql = "INSERT INTO Locations(city, address) VALUES(?, ?)";

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, city);
            ps.setNull(2, java.sql.Types.NVARCHAR);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public int getOrCreateIdByCity(String city) {
        Integer id = findIdByCity(city);
        if (id != null) {
            return id;
        }
        // Nếu chưa có, insert mới
        return insertCity(city);

    }

}
