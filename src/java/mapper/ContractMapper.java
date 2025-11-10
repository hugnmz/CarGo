package mapper;

import dto.ContractDTO;
import model.Contract;
import util.di.annotation.Component;

@Component
public class ContractMapper {

    public ContractDTO toDTO(Contract contract) {
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

    public Contract toModel(ContractDTO dto) {
        if (dto == null) {
            return null;
        }

        Contract contract = new Contract();
        
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