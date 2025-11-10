package dao;

import model.Incident;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IncidentsDAO {
    
    List<Incident> getAllIncidents();
    
    Incident getIncidentById(Integer incidentId);
    
    List<Incident> getIncidentsByContractDetail(Integer contractDetailId);
    
    List<Incident> getIncidentsByStatus(String status);
    
    List<Incident> getIncidentsByType(Integer incidentTypeId);
    
    List<Incident> getIncidentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    boolean addIncident(Incident incident);
    
    boolean updateIncident(Incident incident);
    
    boolean updateIncidentStatus(Integer incidentId, String status);
    
    boolean deleteIncident(Integer incidentId);
    
    BigDecimal getTotalFineAmount(Integer contractDetailId);
    
    List<Incident> getPendingIncidents();
    
    List<Incident> getResolvedIncidents();
    
    List<Incident> getIncidentsByFineRange(BigDecimal minFine, BigDecimal maxFine);
}
