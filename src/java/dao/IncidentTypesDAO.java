package dao;

import model.IncidentTypes;
import java.util.List;

public interface IncidentTypesDAO {
    
    List<IncidentTypes> getAllIncidentTypes();
    
    IncidentTypes getIncidentTypeById(Integer incidentTypeId);
    
    IncidentTypes getIncidentTypeByName(String typeName);
    
    boolean addIncidentType(IncidentTypes incidentType);
    
    boolean updateIncidentType(IncidentTypes incidentType);
    
    boolean deleteIncidentType(Integer incidentTypeId);
    
    boolean isIncidentTypeInUse(Integer incidentTypeId);
    
    List<IncidentTypes> getMostCommonIncidentTypes(int limit);
}
