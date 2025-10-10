package mapper;

import dto.IncidentDTO;
import model.Incidents;
import model.ContractDetails;
import model.IncidentTypes;
import util.di.annotation.Component;

/**
 * IncidentMapper - Chuyển đổi giữa IncidentDTO và Incidents Model
 */
@Component
public class IncidentMapper {

    // Chuyen tu Model sang DTO
    public IncidentDTO toDTO(Incidents incident) {
        if (incident == null) {
            return null;
        }

        IncidentDTO dto = new IncidentDTO();
        
        // Basic incident fields
        dto.setIncidentId(incident.getIncidentId());
        dto.setDescription(incident.getDescription());
        dto.setFineAmount(incident.getFineAmount());
        dto.setIncidentDate(incident.getIncidentDate());
        dto.setStatus(incident.getStatus());
        dto.setContractDetailId(incident.getContractDetailId());
        dto.setIncidentTypeId(incident.getIncidentTypeId());

        // Contract information (nested)
        if (incident.getContractDetail() != null) {
            ContractDetails contractDetail = incident.getContractDetail();
            dto.setContractId(contractDetail.getContractId());
            
            // Vehicle information from contract detail
            if (contractDetail.getVehicle() != null) {
                dto.setVehicleId(contractDetail.getVehicle().getVehicleId());
                dto.setPlateNumber(contractDetail.getVehicle().getPlateNumber());
                
                // Car information from vehicle
                if (contractDetail.getVehicle().getCar() != null) {
                    dto.setCarName(contractDetail.getVehicle().getCar().getName());
                }
            }
            
            // Customer information from contract
            if (contractDetail.getContract() != null) {
                if (contractDetail.getContract().getCustomer() != null) {
                    dto.setCustomerName(contractDetail.getContract().getCustomer().getFullName());
                    dto.setCustomerPhone(contractDetail.getContract().getCustomer().getPhone());
                }
            }
        }

        // Incident type information (nested)
        if (incident.getIncidentType() != null) {
            IncidentTypes incidentType = incident.getIncidentType();
            dto.setIncidentTypeName(incidentType.getTypeName());
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public Incidents toModel(IncidentDTO dto) {
        if (dto == null) {
            return null;
        }

        Incidents incident = new Incidents();
        
        // Basic incident fields
        incident.setIncidentId(dto.getIncidentId());
        incident.setDescription(dto.getDescription());
        incident.setFineAmount(dto.getFineAmount());
        incident.setIncidentDate(dto.getIncidentDate());
        incident.setStatus(dto.getStatus());
        incident.setContractDetailId(dto.getContractDetailId());
        incident.setIncidentTypeId(dto.getIncidentTypeId());

        // Create nested ContractDetail object if contract info is provided
        if (dto.getContractId() != null || dto.getVehicleId() != null) {
            ContractDetails contractDetail = new ContractDetails();
            contractDetail.setContractDetailId(dto.getContractDetailId());
            contractDetail.setContractId(dto.getContractId());
            contractDetail.setVehicleId(dto.getVehicleId());
            incident.setContractDetail(contractDetail);
        }

        // Create nested IncidentType object if incident type info is provided
        if (dto.getIncidentTypeName() != null) {
            IncidentTypes incidentType = new IncidentTypes();
            incidentType.setIncidentTypeId(dto.getIncidentTypeId());
            incidentType.setTypeName(dto.getIncidentTypeName());
            incident.setIncidentType(incidentType);
        }

        return incident;
    }
}
