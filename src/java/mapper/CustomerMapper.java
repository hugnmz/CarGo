/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import dto.CustomerDTO;
import model.Customers;
import model.Locations;
import util.di.annotation.Component;

/**
 *
 * @author admin
 */
// class này để chuyển đối giữa DTO và Model và gược lại
@Component
public class CustomerMapper {

    // chuyển từ model sang DTO
    public CustomerDTO toDTO(Customers customer) {

        if (customer == null) {
            return null;
        }
        CustomerDTO dto = new CustomerDTO();

        dto.setCustomerId(customer.getCustomerId());
        dto.setUsername(customer.getUsername());
        dto.setFullName(customer.getFullName());
        dto.setPhone(customer.getPhone());
        dto.setEmail(customer.getEmail());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setCreateAt(customer.getCreateAt());
        dto.setIsVerified(customer.isIsVerified());

        if (customer.getLocationId() != null && customer.getLocation() != null) {
            dto.setLocationId(customer.getLocationId());
            dto.setCity(customer.getLocation().getCity());
        }

        return dto;
    }

    // từ dto sang model
    public Customers toUsers(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }

        // tạo mới Customer
        Customers user = new Customers();
        user.setCustomerId(dto.getCustomerId());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setIsVerified(dto.getIsVerified());
        user.setCreateAt(dto.getCreateAt());

        if (dto.getLocationId() != null) {
            user.setLocationId(dto.getLocationId());
        } else if (dto.getCity() != null) {
            Locations location = new Locations();
            location.setCity(dto.getCity());
            location.setAddress(dto.getAddress());
            user.setLocation(location);
        }
        //tra ve user
        return user;
    }
}
