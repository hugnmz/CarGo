/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import dto.CustomerDTO;
import model.Customers;
import model.Locations;
import util.di.annotation.Component;
import util.reflection.ReflectionMapper;

/**
 *
 * @author admin
 */
// class này để chuyển đối giữa DTO và Model và gược lại
@Component
public class CustomerMapper {

    public CustomerDTO toDTO(Customers customer) {
        return ReflectionMapper.map(customer, CustomerDTO.class);
    }

    public Customers toUsers(CustomerDTO dto) {
        return ReflectionMapper.map(dto, Customers.class);
    }
}
