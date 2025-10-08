package dao;

import model.Incidents;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IncidentsDAO {
    
    List<Incidents> getAllIncidents();
    
    Incidents getIncidentById(Integer incidentId);
    
    List<Incidents> getIncidentsByContractDetail(Integer contractDetailId);
    
    List<Incidents> getIncidentsByStatus(String status);
    
    List<Incidents> getIncidentsByType(Integer incidentTypeId);
    
    List<Incidents> getIncidentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    boolean addIncident(Incidents incident);
    
    boolean updateIncident(Incidents incident);
    
    boolean updateIncidentStatus(Integer incidentId, String status);
    
    boolean deleteIncident(Integer incidentId);
    
    BigDecimal getTotalFineAmount(Integer contractDetailId);
    
    List<Incidents> getPendingIncidents();
    
    List<Incidents> getResolvedIncidents();
    
    List<Incidents> getIncidentsByFineRange(BigDecimal minFine, BigDecimal maxFine);
}
