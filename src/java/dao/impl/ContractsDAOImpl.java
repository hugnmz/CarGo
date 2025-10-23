package dao.impl;

import dao.ContractsDAO;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Contracts;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 * ContractsDAOImpl - Implementation cho quản lý hợp đồng
 */
@Repository
public class ContractsDAOImpl implements ContractsDAO {

    @Override
    public List<Contracts> getAllContracts() {
        String sql = "SELECT * FROM Contracts ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class);
    }

    @Override
    public Optional<Contracts> getContractById(Integer contractId) {
        String sql = "SELECT * FROM Contracts WHERE contractId = ?";
        Contracts contract = JdbcTemplateUtil.queryOne(sql, Contracts.class, contractId);
        return Optional.ofNullable(contract);
    }

    @Override
    public boolean addContract(Contracts contract) {
        String sql = "INSERT INTO Contracts(customerId, staffId, startDate, endDate, status, createAt, totalAmount, depositAmount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int result = JdbcTemplateUtil.update(sql,
                contract.getCustomerId(),
                contract.getStaffId(),
                contract.getStartDate() != null ? Timestamp.valueOf(contract.getStartDate()) : null,
                contract.getEndDate() != null ? Timestamp.valueOf(contract.getEndDate()) : null,
                contract.getStatus(),
                contract.getCreateAt() != null ? Timestamp.valueOf(contract.getCreateAt()) : null,
                contract.getTotalAmount(),
                contract.getDepositAmount()
        );
        return result > 0;
    }

    @Override
    public boolean updateContract(Contracts contract) {
        String sql = "UPDATE Contracts SET customerId = ?, staffId = ?, startDate = ?, endDate = ?, status = ?, totalAmount = ?, depositAmount = ? WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql,
                contract.getCustomerId(),
                contract.getStaffId(),
                contract.getStartDate() != null ? Timestamp.valueOf(contract.getStartDate()) : null,
                contract.getEndDate() != null ? Timestamp.valueOf(contract.getEndDate()) : null,
                contract.getStatus(),
                contract.getTotalAmount(),
                contract.getDepositAmount(),
                contract.getContractId()
        );
        return result > 0;
    }

    @Override
    public boolean deleteContract(Integer contractId) {
        String sql = "DELETE FROM Contracts WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql, contractId);
        return result > 0;
    }

    @Override
    public boolean updateContractStatus(Integer contractId, String status) {
        String sql = "UPDATE Contracts SET status = ? WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql, status, contractId);
        return result > 0;
    }

    @Override
    public List<Contracts> getContractByCustomer(Integer customerId) {
        String sql = "SELECT * FROM Contracts WHERE customerId = ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class, customerId);
    }

    @Override
    public List<Contracts> getContractByStaff(Integer staffId) {
        String sql = "SELECT * FROM Contracts WHERE staffId = ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class, staffId);
    }

    @Override
    public List<Contracts> getContractByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM Contracts WHERE startDate >= ? AND endDate <= ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class,
                startDate != null ? Timestamp.valueOf(startDate) : null,
                endDate != null ? Timestamp.valueOf(endDate) : null);
    }

    @Override
    public List<Contracts> getContractByStatus(String status) {
        String sql = "SELECT * FROM Contracts WHERE status = ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class, status);
    }

    @Override
    public boolean calculateTotalAmout(Integer contractId) {
        // TODO: Implement calculation logic
        return false;
    }
}
