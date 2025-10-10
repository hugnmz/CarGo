package mapper;

import dto.LocationDTO;
import model.Locations;
import util.di.annotation.Component;

/**
 * LocationMapper - Chuyển đổi giữa LocationDTO và Locations Model
 */
@Component
public class LocationMapper {

    // Chuyen tu Model sang DTO
    public LocationDTO toDTO(Locations location) {
        // Kiem tra null
        if (location == null) {
            return null;
        }

        LocationDTO dto = new LocationDTO();
        
        // Gan cac truong co ban cua dia diem
        dto.setLocationId(location.getLocationId());
        dto.setCity(location.getCity());
        dto.setAddress(location.getAddress());

        // Statistics (optional - can be set separately)
        if (location.getUsers() != null) {
            dto.setUserCount(location.getUsers().size());
        }
        if (location.getCustomers() != null) {
            dto.setCustomerCount(location.getCustomers().size());
        }
        if (location.getCars() != null) {
            dto.setCarCount(location.getCars().size());
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Locations toModel(LocationDTO dto) {
        // Kiem tra null
        if (dto == null) {
            return null;
        }

        Locations location = new Locations();
        
        // Gan cac truong co ban cua dia diem
        location.setLocationId(dto.getLocationId());
        location.setCity(dto.getCity());
        location.setAddress(dto.getAddress());

        return location;
    }
}
