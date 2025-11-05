package service;

import dto.ContractDTO;
import dto.ContractDetailDTO;
import java.math.BigDecimal;
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
    // Tính tổng tiền hợp đồng

    boolean calculateTotalAmount(Integer contractId);

    // Xóa hợp đồng
    boolean deleteContract(Integer contractId);

    List<ContractDTO> getContractsByStaff(Integer staffId);

    List<ContractDTO> getAllContracts();

    boolean createContract(ContractDTO contractDTO);
    
    List<ContractDTO> createContractsFromCart(Integer customerId, Integer[] selectedOrderIds);

        void updateContractTotalAmount(Integer contractId, BigDecimal totalAmount);

    void updateStaffId(Integer staffId, Integer contractId);

    void updateNote(String note, Integer contractId);    boolean updateContractStatus(Integer contractId, String status, String reason);

    //dem so hop dong dang co
    int countContract();
}
