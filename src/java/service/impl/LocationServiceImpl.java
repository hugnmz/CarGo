package service.impl;

import dao.LocationsDAO;
import dto.LocationDTO;
import java.util.ArrayList;
import java.util.List;
import mapper.LocationMapper;
import model.Locations;
import service.LocationService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import util.MessageUtil;
import util.exception.DataAccessException;

@Service
public class LocationServiceImpl implements LocationService {
    
    @Autowired
    private LocationsDAO locationsDAO;
    
    @Autowired
    private LocationMapper locationMapper;
    
    @Override
    public List<LocationDTO> getAllLocations() {
        try {
            List<Locations> locations = locationsDAO.getAllLocations();
            List<LocationDTO> dtoList = new ArrayList<>();
            
            for (Locations location : locations) {
                dtoList.add(locationMapper.toDTO(location));
            }
            
            return dtoList;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system.location.load"), e);
        }
    }
}