package service;

import dto.ContractDTO;
import dto.ContractDetailDTO;
import java.util.List;
import java.util.Optional;

/**
 * ContractService - Service layer cho Contract business logic
 */
public interface ContractService {
    
    /**
     * Lấy danh sách hợp đồng của khách hàng
     * @param customerId - ID khách hàng
     * @return List<ContractDTO> - Danh sách hợp đồng
     */
    List<ContractDTO> getContractsByCustomer(Integer customerId);
    
    /**
     * Lấy chi tiết một hợp đồng
     * @param contractId - ID hợp đồng
     * @return Optional<ContractDTO> - Hợp đồng tìm được hoặc empty
     */
    Optional<ContractDTO> getContractById(Integer contractId);
    
    /**
     * Lấy chi tiết hợp đồng (contract details)
     * @param contractId - ID hợp đồng
     * @return List<ContractDetailDTO> - Danh sách chi tiết
     */
    List<ContractDetailDTO> getContractDetails(Integer contractId);
}
