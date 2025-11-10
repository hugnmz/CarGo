package dao;

import java.util.List;
import model.Location;

public interface LocationDAO {

    List<Location> getAllLocations();

    Integer findIdByCity(String city);

    int insertCity(String city);

    int getOrCreateIdByCity(String city);

}
