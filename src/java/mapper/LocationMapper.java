package mapper;

import dto.LocationDTO;
import model.Location;
import util.di.annotation.Component;

@Component
public class LocationMapper {

    // Chuyen tu Model sang DTO
    public LocationDTO toDTO(Location location) {
        // Kiem tra null
        if (location == null) {
            return null;
        }

        LocationDTO dto = new LocationDTO();

        // Gan cac truong co ban cua dia diem
        dto.setLocationId(location.getLocationId());
        dto.setCity(location.getCity());
        dto.setAddress(location.getAddress());

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Location toModel(LocationDTO dto) {
        // Kiem tra null
        if (dto == null) {
            return null;
        }

        Location location = new Location();

        // Gan cac truong co ban cua dia diem
        location.setLocationId(dto.getLocationId());
        location.setCity(dto.getCity());
        location.setAddress(dto.getAddress());

        return location;
    }
}
