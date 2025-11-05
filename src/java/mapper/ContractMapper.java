package mapper;

import dto.ContractDTO;
import model.Contracts;
import util.di.annotation.Component;

@Component
public class ContractMapper {

    public ContractDTO toDTO(Contracts contract) {
        if (contract == null) {
            return null;
        }

        ContractDTO dto = new ContractDTO();
        
        // Map basic fields
        dto.setContractId(contract.getContractId());
        dto.setCustomerId(contract.getCustomerId());
        dto.setStaffId(contract.getStaffId());
        dto.setStatus(contract.getStatus());
        dto.setStartDate(contract.getStartDate());
        dto.setEndDate(contract.getEndDate());
        dto.setCreateAt(contract.getCreateAt());
        dto.setTotalAmount(contract.getTotalAmount());
        dto.setDepositAmount(contract.getDepositAmount());
        dto.setRejectionReason(contract.getRejectionReason());
        return dto;
    }

    public Contracts toModel(ContractDTO dto) {
        if (dto == null) {
            return null;
        }

        Contracts contract = new Contracts();
        
        // Map basic fields
        contract.setContractId(dto.getContractId());
        contract.setCustomerId(dto.getCustomerId());
        contract.setStaffId(dto.getStaffId());
        contract.setStatus(dto.getStatus());
        contract.setStartDate(dto.getStartDate());
        contract.setEndDate(dto.getEndDate());
        contract.setCreateAt(dto.getCreateAt());
        contract.setTotalAmount(dto.getTotalAmount());
        contract.setDepositAmount(dto.getDepositAmount());
        contract.setRejectionReason(dto.getRejectionReason());
        return contract;
    }
}