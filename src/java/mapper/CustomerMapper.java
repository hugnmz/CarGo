
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import dto.CustomerDTO;
import model.Customer;
import model.Location;
import util.di.annotation.Component;

/**
 *
 * @author admin
 */
// class này để chuyển đối giữa DTO và Model và gược lại
@Component
public class CustomerMapper {

    // chuyển từ model sang DTO
    public CustomerDTO toDTO(Customer customer) {

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
        
        dto.setLocationId(customer.getLocationId());


        if (customer.getLocationId() != null && customer.getLocation() != null) {
            dto.setCity(customer.getLocation().getCity());
        }

        return dto;
    }

    // từ dto sang model
    public Customer toUsers(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }

        // tạo mới Customer
        Customer user = new Customer();
        user.setCustomerId(dto.getCustomerId());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setCreateAt(dto.getCreateAt());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setIsVerified(dto.getIsVerified()); // Thêm mapping cho isVerified
        if (dto.getCity() != null) {
            Location location = new Location();
            location.setCity(dto.getCity());
            user.setLocation(location);
        }
        //tra ve user
        if (dto.getLocationId() != null) {
            Location location = new Location();
            location.setLocationId(dto.getLocationId()); // Set locationId
            location.setCity(dto.getCity());
            user.setLocation(location);
            user.setLocationId(dto.getLocationId()); // Set cho field locationId trong Customer
        }
        return user;
    }
}
