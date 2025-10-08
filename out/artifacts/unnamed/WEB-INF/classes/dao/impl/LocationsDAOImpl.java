/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.LocationsDAO;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 *
 * @author admin
 */
@Repository
public class LocationsDAOImpl implements LocationsDAO {

    @Override
    public Integer findIdByCity(String city) {

        String sql = "select locationId, city, address from Locations "
                + "where UPPER(LTRIM(RTRIM(city))) = UPPER(LTRIM(RTRIM(?)))";
        // Map to Locations entity then return id
        model.Locations loc = JdbcTemplateUtil.queryOne(sql, model.Locations.class, city);
        return (loc != null) ? loc.getLocationId() : null;
    }

    // tra ve key sau khi insert
    @Override
    public int insertCity(String city) {
        String sql = "INSERT INTO Locations(city, address) VALUES(?, ?)";
        int id = JdbcTemplateUtil.insertAndReturnKey(sql, city, null);
        return id;
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
