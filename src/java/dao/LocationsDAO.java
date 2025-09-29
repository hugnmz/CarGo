package dao;

import java.util.List;
import java.util.Optional;
import model.Locations;

public interface LocationsDAO {
    List<Locations> getAllLocations();
    Optional<Locations> getLocationById(Integer id);
    boolean createLocations(Locations location);
    boolean updateLocations(Locations location);
    boolean deleteLocations(Integer id);
    List<Locations> getLocationsByCity(String city);
}
