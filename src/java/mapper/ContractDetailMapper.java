package mapper;

import dto.ContractDetailDTO;
import model.ContractDetails;
import util.di.annotation.Component;

@Component
public class ContractDetailMapper {

    public ContractDetailDTO toDTO(ContractDetails detail) {
        if (detail == null) {
            return null;
        }

        ContractDetailDTO dto = new ContractDetailDTO();

        // Map basic fields
        dto.setContractDetailId(detail.getContractDetailId());
        dto.setContractId(detail.getContractId());
        dto.setVehicleId(detail.getVehicleId());
        dto.setPrice(detail.getPrice());
        dto.setRentStartDate(detail.getRentStartDate());
        dto.setRentEndDate(detail.getRentEndDate());
        dto.setNote(detail.getNote());
        if (detail.getVehicle() != null) {
            dto.setPlateNumber(detail.getVehicle().getPlateNumber());
            
            if (detail.getVehicle().getCar() != null) {
                dto.setName(detail.getVehicle().getCar().getName());
            }
        }

        return dto;
    }

    public ContractDetails toModel(ContractDetailDTO dto) {
        if (dto == null) {
            return null;
        }

        ContractDetails detail = new ContractDetails();

        // Map basic fields
        detail.setContractDetailId(dto.getContractDetailId());
        detail.setContractId(dto.getContractId());
        detail.setPrice(dto.getPrice());
        detail.setRentStartDate(dto.getRentStartDate());
        detail.setRentEndDate(dto.getRentEndDate());
        detail.setNote(dto.getNote());
        if (dto.getVehicleId() != null) {
            detail.setVehicleId(dto.getVehicleId());
        }

        return detail;
    }
}
