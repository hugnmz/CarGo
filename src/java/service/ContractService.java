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
<<<<<<< Updated upstream
    // Tính tổng tiền hợp đồng

    boolean calculateTotalAmount(Integer contractId);

    // Xóa hợp đồng
    boolean deleteContract(Integer contractId);

    List<ContractDTO> getContractsByStaff(Integer staffId);

    List<ContractDTO> getAllContracts();

    boolean createContract(ContractDTO contractDTO);
    
    /**
     * Tạo hợp đồng từ giỏ hàng (checkout)
     * @param customerId - ID khách hàng
     * @param selectedOrderIds - Danh sách ID orders được chọn (null = tất cả)
     * @return List<ContractDTO> - Danh sách hợp đồng đã tạo
     */
    List<ContractDTO> createContractsFromCart(Integer customerId, Integer[] selectedOrderIds);

=======
    
    List<ContractDTO> getAllContracts();
>>>>>>> Stashed changes
}
