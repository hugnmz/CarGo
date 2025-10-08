package dao;



public interface LocationsDAO {

    Integer findIdByCity(String city);

    int insertCity(String city);

    int getOrCreateIdByCity(String city);

}
