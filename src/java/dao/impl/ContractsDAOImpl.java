package dao.impl;

import dao.ContractsDAO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Contracts;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class ContractsDAOImpl implements ContractsDAO {

    @Override
    public List<Contracts> getAllContracts() {
        String sql = "SELECT * FROM dbo.Contracts";
        return JdbcTemplateUtil.query(sql, Contracts.class);
    }

    @Override
    public Optional<Contracts> getContractById(Integer contractId) {
        String sql = "SELECT * FROM dbo.Contracts WHERE contractId = ?";
        Contracts contract = JdbcTemplateUtil.queryOne(sql, Contracts.class, contractId);
        return Optional.ofNullable(contract);
    }

    @Override
    public boolean addContract(Contracts contract) {
        String sql = "INSERT INTO dbo.Contracts (customerId, staffId, status, startDate, endDate, totalAmount, depositAmount, createAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql,
                contract.getCustomerId(),
                contract.getStaffId(),
                contract.getStatus(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getTotalAmount(),
                contract.getDepositAmount(),
                contract.getCreateAt());
        return result > 0;
    }

    @Override
    public boolean updateContract(Contracts contract) {
        String sql = "UPDATE dbo.Contracts SET customerId=?, staffId=?, status=?, startDate=?, endDate=?, totalAmount=?, depositAmount=? WHERE contractId=?";
        int result = JdbcTemplateUtil.update(sql,
                contract.getCustomerId(),
                contract.getStaffId(),
                contract.getStatus(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getTotalAmount(),
                contract.getDepositAmount(),
                contract.getContractId());
        return result > 0;
    }

    @Override
    public boolean deleteContract(Integer contractId) {
        String sql = "DELETE FROM dbo.Contracts WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql, contractId);
        return result > 0;
    }

    @Override
    public boolean updateContractStatus(Integer contractId, String status) {
        String sql = "UPDATE dbo.Contracts SET status = ? WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql, status, contractId);
        return result > 0;
    }

    @Override
    public List<Contracts> getContractByCustomer(Integer customerId) {
        String sql = "SELECT * FROM dbo.Contracts WHERE customerId = ? ORDER BY contractId DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class, customerId);
    }

    @Override
    public List<Contracts> getContractByStaff(Integer staffId) {
        String sql = "SELECT * FROM dbo.Contracts WHERE staffId = ? ORDER BY contractId DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class, staffId);
    }

    @Override
    public List<Contracts> getContractByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM dbo.Contracts WHERE startDate >= ? AND endDate <= ? ORDER BY contractId DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class, startDate, endDate);
    }

    @Override
    public List<Contracts> getContractByStatus(String status) {
        String sql = "SELECT * FROM dbo.Contracts WHERE status = ? ORDER BY contractId DESC";
        return JdbcTemplateUtil.query(sql, Contracts.class, status);
    }

    @Override
    public boolean calculateTotalAmout(Integer contractId) {
        return true;
    }

    @Override
    public boolean updateContractTotalAmount(Integer contractId, BigDecimal totalAmount) {
        String sql = "UPDATE dbo.Contracts SET totalAmount = ? WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql, totalAmount, contractId);
        return result > 0;
    }

    @Override
    public boolean updateStaffId(Integer staffId, Integer contractId) {
        String sql = "UPDATE dbo.Contracts SET staffId = ? WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql, staffId, contractId);
        return result > 0;
    }

    @Override
    public boolean updateNote(String note, Integer contractId) {
        String sql = "UPDATE dbo.Contracts SET note = ? WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql, note, contractId);
        return result > 0;
    }
;
}
