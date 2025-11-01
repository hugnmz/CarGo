/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.RolesDao;
import java.util.List;
import java.util.Optional;
import model.Roles;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 *
 * @author DELL
 */
@Repository
public class RolesDaoImpl implements RolesDao{

    @Override
    public List<Roles> getAllRoles() {
        String sql = "SELECT * FROM Roles WHERE roleId = 1 OR roleId = 2";
        return JdbcTemplateUtil.query(sql, Roles.class);
    }
    
    public static void main(String[] args) {
        RolesDaoImpl r = new RolesDaoImpl();
        List<Roles> list = r.getAllRoles();
        for (Roles roles : list) {
            
        }
    }

    @Override
    public Optional<Roles> getRoleById(Integer roleId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Roles> getRoleByName(String roleName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean createRole(Roles role) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateRole(Roles role) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteRole(Integer roleId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int countUsersWithRole(Integer roleId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
