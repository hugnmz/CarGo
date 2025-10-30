package dao;

import java.util.List;
import model.Locations;

public interface LocationsDAO {

    List<Locations> getAllLocations();

    Integer findIdByCity(String city);

    int insertCity(String city);

    int getOrCreateIdByCity(String city);

}
