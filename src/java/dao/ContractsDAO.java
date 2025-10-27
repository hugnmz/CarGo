package dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Contracts;

/**
 * DAO interface cho Contracts
 */
public interface ContractsDAO {

    /**
     * Lấy tất cả hợp đồng, không filter staff
     * @return danh sách hợp đồng
     */
    List<Contracts> getAllContracts();

    /**
     * Lấy hợp đồng theo ID
     */
    Optional<Contracts> getContractById(Integer contractId);

    /**
     * Thêm hợp đồng mới
     */
    boolean addContract(Contracts contract);

    /**
     * Cập nhật thông tin hợp đồng
     */
    boolean updateContract(Contracts contract);

    /**
     * Xóa hợp đồng theo ID
     */
    boolean deleteContract(Integer contractId);

    /**
     * Cập nhật trạng thái hợp đồng
     */
    boolean updateContractStatus(Integer contractId, String status);

    /**
     * Lấy hợp đồng theo khách hàng
     */
    List<Contracts> getContractByCustomer(Integer customerId);

    /**
     * Lấy hợp đồng theo staff (nếu cần)
     */
    List<Contracts> getContractByStaff(Integer staffId);

    /**
     * Lấy hợp đồng theo khoảng thời gian
     */
    List<Contracts> getContractByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Lấy hợp đồng theo trạng thái (PENDING, ACCEPTED, REJECTED)
     */
    List<Contracts> getContractByStatus(String status);

    /**
     * Tính tổng tiền hợp đồng
     */
    boolean calculateTotalAmount(Integer contractId);
}
