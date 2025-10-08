package model;

import java.util.List;


public class IncidentTypes {
    private Integer incidentTypeId;  // ID loại sự cố
    private String typeName;         // Tên loại sự cố
    private List<Incidents> incidents; // Danh sách sự cố
    
    // Constructors
    public IncidentTypes() {}
    
    public IncidentTypes(String typeName) {
        this.typeName = typeName;
    }
    
    // Getters and Setters
    public Integer getIncidentTypeId() { return incidentTypeId; }
    public void setIncidentTypeId(Integer incidentTypeId) { this.incidentTypeId = incidentTypeId; }
    
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    
    public List<Incidents> getIncidents() { return incidents; }
    public void setIncidents(List<Incidents> incidents) { this.incidents = incidents; }
}
