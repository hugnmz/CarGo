package dao.impl;

import dao.PaymentsDAO;
import java.math.BigDecimal;
import java.util.List;
import model.Payments;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class PaymentsDAOImpl implements PaymentsDAO {

    @Override
    public List<Payments> getAllPayments() {
        String sql = "SELECT * FROM Payments";
        return JdbcTemplateUtil.query(sql, Payments.class);
    }

    @Override
    public Payments getPaymentById(Integer paymentId) {
        String sql = "SELECT * FROM Payments WHERE paymentId = ?";
        return JdbcTemplateUtil.queryOne(sql, Payments.class, paymentId);
    }

    @Override
    public List<Payments> getPaymentsByContract(Integer contractId) {
        String sql = "SELECT * FROM Payments WHERE contractId = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, contractId);
    }

    @Override
    public boolean addPayment(Payments payment) {
        String sql = "INSERT INTO Payments (contractId, amount, methodId, status, paymentDate) VALUES (?, ?, ?, ?, ?)";
        int id = JdbcTemplateUtil.insertAndReturnKey(sql,
                payment.getContractId(),
                payment.getAmount(),
                payment.getMethodId(),
                payment.getStatus(),
                payment.getPaymentDate()
        );
        return id > 0;
    }

    @Override
    public boolean updatePaymentStatus(Integer paymentId, String status) {
        String sql = "UPDATE Payments SET status = ? WHERE paymentId = ?";
        int affected = JdbcTemplateUtil.update(sql, status, paymentId);
        return affected > 0;
    }

    @Override
    public boolean isPaymentCompleted(Integer paymentId) {
        String sql = "SELECT COUNT(*) FROM Payments WHERE paymentId = ? AND status = 'COMPLETED'";
        int count = JdbcTemplateUtil.count(sql, paymentId);
        return count > 0;
    }

    @Override
    public int insertPendingPayment(int contractId, BigDecimal amount) {
        String sql = "INSERT INTO Payments (contractId, amount, methodId, status) VALUES (?, ?, 1, 'PENDING')";
        return JdbcTemplateUtil.insertAndReturnKey(sql, contractId, amount);
    }

    @Override
    public Payments findPendingPayment(int contractId, BigDecimal amount) {
        String sql = "SELECT * FROM Payments WHERE contractId = ? AND amount = ? AND status = 'PENDING'";
        return JdbcTemplateUtil.queryOne(sql, Payments.class, contractId, amount);
    }

    @Override
    public BigDecimal getTotalPaidAmount(int contractId) {
        String sql = "SELECT SUM(amount) FROM Payments WHERE contractId = ? AND status IN ('DEPOSIT_PAID','COMPLETED')";
        BigDecimal total = JdbcTemplateUtil.querySingle(sql, BigDecimal.class, contractId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getRemainingAmount(int contractId) {
        String sql = "SELECT totalAmount FROM Contracts WHERE contractId = ?";
        BigDecimal total = JdbcTemplateUtil.querySingle(sql, BigDecimal.class, contractId);
        BigDecimal paid = getTotalPaidAmount(contractId);
        return total != null ? total.subtract(paid) : BigDecimal.ZERO;
    }

    @Override
    public int insertDepositPayment(int contractId, BigDecimal amount) {
        String sql = "INSERT INTO Payments (contractId, amount, methodId, status) VALUES (?, ?, 1, 'DEPOSIT_PAID')";
        return JdbcTemplateUtil.insertAndReturnKey(sql, contractId, amount);
    }

    @Override
    public boolean hasDepositPaid(int contractId) {
        String sql = "SELECT COUNT(*) FROM Payments WHERE contractId = ? AND status = 'DEPOSIT_PAID'";
        int count = JdbcTemplateUtil.count(sql, contractId);
        return count > 0;
    }

    @Override
    public String getPaymentStatus(int contractId) {
        String sql = "SELECT TOP 1 status FROM Payments WHERE contractId = ? ORDER BY paymentDate DESC";
        String status = JdbcTemplateUtil.querySingle(sql, String.class, contractId);
        return status != null ? status : "NONE";
    }
}