package dao.impl;

import dao.ContractsDAO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import model.Contract;
import util.DB;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class ContractsDAOImpl implements ContractsDAO {

    @Override
    public List<Contract> getAllContracts() {
        String sql = "SELECT * FROM dbo.Contracts";
        return JdbcTemplateUtil.query(sql, Contract.class);
    }

    @Override
    public Optional<Contract> getContractById(Integer contractId) {
        String sql = "SELECT * FROM dbo.Contracts WHERE contractId = ?";
        Contract contract = JdbcTemplateUtil.queryOne(sql, Contract.class, contractId);
        return Optional.ofNullable(contract);
    }

    @Override
    public boolean addContract(Contract contract) {
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
    public boolean updateContract(Contract contract) {
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
    public List<Contract> getContractByCustomer(Integer customerId) {
        String sql = "SELECT * FROM dbo.Contracts WHERE customerId = ? ORDER BY contractId DESC";
        return JdbcTemplateUtil.query(sql, Contract.class, customerId);
    }

    @Override
    public List<Contract> getContractByStaff(Integer staffId) {
        String sql = "SELECT * FROM dbo.Contracts WHERE staffId = ? ORDER BY contractId DESC";
        return JdbcTemplateUtil.query(sql, Contract.class, staffId);
    }

    @Override
    public List<Contract> getContractByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM dbo.Contracts WHERE startDate >= ? AND endDate <= ? ORDER BY contractId DESC";
        return JdbcTemplateUtil.query(sql, Contract.class, startDate, endDate);
    }

    @Override
    public List<Contract> getContractByStatus(String status) {
        String sql = "SELECT * FROM dbo.Contracts WHERE status = ? ORDER BY contractId DESC";
        return JdbcTemplateUtil.query(sql, Contract.class, status);
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

    @Override
    public int countContract() {
        String sql = "SELECT COUNT(*) FROM dbo.Contracts";
        return JdbcTemplateUtil.count(sql);
    }

    @Override
    public boolean calculateTotalAmout(Integer contractId) {
        return true;
    }

    @Override
    public boolean updateContractStatus(Integer contractId, String status, String reason) {
        String sql = "UPDATE dbo.Contracts SET status = ?, rejectionReason = ? WHERE contractId = ?";
        String reasonToSave = "REJECTED".equalsIgnoreCase(status) ? reason : null;
        int result = JdbcTemplateUtil.update(sql, status, reasonToSave, contractId);
        return result > 0;
    }

    @Override
    public Integer findLeastLoadedStaffId() {
        String sql = """
        SELECT u.userId, COUNT(c.contractId) as contractCount
        FROM Users u
        JOIN Roles r ON r.roleId = u.roleId AND r.roleName = 'STAFF'
        LEFT JOIN Contracts c
            ON c.staffId = u.userId
            AND c.status = 'PENDING'
        GROUP BY u.userId
        ORDER BY COUNT(c.contractId) ASC
        """;

        List<Integer> staffIds = new ArrayList<>();
        Integer minCount = null;

        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer userId = rs.getInt(1);
                    int contractCount = rs.getInt(2);

                    if (minCount == null) {
                        minCount = contractCount;
                        staffIds.add(userId);
                    } else if (contractCount == minCount) {
                        staffIds.add(userId);
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }

        if (staffIds.isEmpty()) {
            return null;
        }

        if (staffIds.size() == 1) {
            return staffIds.get(0);
        }

        Random random = new Random();
        return staffIds.get(random.nextInt(staffIds.size()));
    }

    @Override
    public BigDecimal getTotalAmount(Integer contractId) {
        String sql = "SELECT totalAmount FROM Contracts WHERE contractId = ?";
        Contract contract = JdbcTemplateUtil.queryOne(sql, Contract.class, contractId);
        return contract != null ? contract.getTotalAmount() : null;
    }

    @Override
    public boolean addPaymentLog(Integer contractId, String message) {
        String sql = "INSERT INTO paymentLogs (contractId, message, createdAt) VALUES (?, ?, NOW())";
        return JdbcTemplateUtil.update(sql, contractId, message) > 0;
    }
}
