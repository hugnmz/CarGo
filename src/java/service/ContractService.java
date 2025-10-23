package service;

import dto.ContractDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContractService {
    
    // Tạo hợp đồng mới
    boolean createContract(ContractDTO contractDTO);
    
    // Lấy hợp đồng theo ID
    Optional<ContractDTO> getContractById(Integer contractId);
    
    // Lấy tất cả hợp đồng
    List<ContractDTO> getAllContracts();
    
    // Lấy hợp đồng theo khách hàng
    List<ContractDTO> getContractsByCustomer(Integer customerId);
    
    // Lấy hợp đồng theo nhân viên
    List<ContractDTO> getContractsByStaff(Integer staffId);
    
    // Cập nhật trạng thái hợp đồng
    boolean updateContractStatus(Integer contractId, String status);
    
    // Tính tổng tiền hợp đồng
    boolean calculateTotalAmount(Integer contractId);
    
    // Xóa hợp đồng
    boolean deleteContract(Integer contractId);
}