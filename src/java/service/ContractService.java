package service;

import dto.ContractDTO;
import dto.ContractDetailDTO;
import java.util.List;
import java.util.Optional;

/**
 * ContractService - Service layer cho Contract business logic
 */
public interface ContractService {

    List<ContractDTO> getContractsByCustomer(Integer customerId);

    Optional<ContractDTO> getContractById(Integer contractId);

    List<ContractDetailDTO> getContractDetails(Integer contractId);

    boolean updateContractStatus(Integer contractId, String status);

}
