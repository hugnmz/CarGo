/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.*;
import dto.RoleDTO;
import java.util.List;
import mapper.*;
import service.RoleService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

/**
 *
 * @author DELL
 */
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RolesDao roleDao ;

    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public List<RoleDTO> getAllRole() {
        return roleDao.getAllRoles()
                .stream()
                .map(roleMapper::toDTO)
                .toList();
    }
    
}
