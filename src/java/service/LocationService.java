package service;

import dto.LocationDTO;
import java.util.List;

public interface LocationService {
    List<LocationDTO> getAllLocations();
}