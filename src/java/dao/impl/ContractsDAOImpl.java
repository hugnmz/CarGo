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
import model.Contracts;
import util.DB;
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
        // Lấy tất cả staff với số lượng hợp đồng PENDING
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
        
        List<StaffContractCount> staffList = new ArrayList<>();
        
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer userId = rs.getInt("userId");
                    int contractCount = rs.getInt("contractCount");
                    staffList.add(new StaffContractCount(userId, contractCount));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
        
        if (staffList.isEmpty()) {
            return null;
        }
        
        // Tìm số lượng hợp đồng ít nhất
        int minCount = staffList.get(0).contractCount;
        List<Integer> staffIdsWithMinCount = new ArrayList<>();
        
        for (StaffContractCount staff : staffList) {
            if (staff.contractCount == minCount) {
                staffIdsWithMinCount.add(staff.userId);
            } else {
                // Đã vượt quá minCount, dừng lại
                break;
            }
        }
        
        // Random chọn một trong số các staff có cùng số lượng hợp đồng ít nhất
        if (staffIdsWithMinCount.size() == 1) {
            return staffIdsWithMinCount.get(0);
        }
        
        Random random = new Random();
        return staffIdsWithMinCount.get(random.nextInt(staffIdsWithMinCount.size()));
    }
    
    // Inner class để lưu thông tin staff và số lượng hợp đồng
    private static class StaffContractCount {
        Integer userId;
        int contractCount;
        
        StaffContractCount(Integer userId, int contractCount) {
            this.userId = userId;
            this.contractCount = contractCount;
        }
    }
    
        @Override
    public BigDecimal getTotalAmount(Integer contractId) {
        String sql = "SELECT totalAmount FROM Contracts WHERE contractId = ?";
        Contracts contract = JdbcTemplateUtil.queryOne(sql, Contracts.class, contractId);
        return contract != null ? contract.getTotalAmount() : null;
    }

    @Override
    public boolean addPaymentLog(Integer contractId, String message) {
        String sql = "INSERT INTO paymentLogs (contractId, message, createdAt) VALUES (?, ?, NOW())";
        return JdbcTemplateUtil.update(sql, contractId, message) > 0;
    }
}
