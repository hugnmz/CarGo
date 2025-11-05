package dao.impl;

import dao.PaymentsDAO;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import model.Payments;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 *
 * @author admin
 */
@Repository
public class PaymentsDAOImpl implements PaymentsDAO {

    @Override
    public List<Payments> getAllPayments() {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate "
                + "FROM Payments";
        return JdbcTemplateUtil.query(sql, Payments.class);
    }

    @Override
    public Payments getPaymentById(Integer paymentId) {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate "
                + "FROM Payments WHERE paymentId = ?";
        return JdbcTemplateUtil.queryOne(sql, Payments.class, paymentId);
    }

    @Override
    public List<Payments> getPaymentsByContract(Integer contractId) {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate "
                + "FROM Payments WHERE contractId = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, contractId);
    }

    @Override
    public List<Payments> getPaymentsByStatus(String status) {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate "
                + "FROM Payments WHERE status = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, status);
    }

    @Override
    public List<Payments> getPaymentsByMethod(Integer methodId) {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate "
                + "FROM Payments WHERE methodId = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, methodId);
    }

    @Override
    public boolean addPayment(Payments payment) {
        String sql = "INSERT INTO Payments (contractId, amount, methodId, status, paymentDate) "
                + "VALUES (?, ?, ?, ?, ?)";
        int id = JdbcTemplateUtil.insertAndReturnKey(
                sql,
                payment.getContractId(),
                payment.getAmount(),
                payment.getMethodId(),
                payment.getStatus(),
                payment.getPaymentDate() != null ? Timestamp.valueOf(payment.getPaymentDate()) : null
        );
        if (id > 0) {
            payment.setPaymentId(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePayment(Payments payment) {
        String sql = "UPDATE Payments SET contractId = ?, amount = ?, methodId = ?, status = ?, paymentDate = ? "
                + "WHERE paymentId = ?";
        int affected = JdbcTemplateUtil.update(
                sql,
                payment.getContractId(),
                payment.getAmount(),
                payment.getMethodId(),
                payment.getStatus(),
                payment.getPaymentDate() != null ? Timestamp.valueOf(payment.getPaymentDate()) : null,
                payment.getPaymentId()
        );
        return affected > 0;
    }

    @Override
    public boolean updatePaymentStatus(Integer paymentId, String status) {
        String sql = "UPDATE Payments SET status = ? WHERE paymentId = ?";
        int affected = JdbcTemplateUtil.update(sql, status, paymentId);
        return affected > 0;
    }

    @Override
    public boolean deletePayment(Integer paymentId) {
        String sql = "DELETE FROM Payments WHERE paymentId = ?";
        int affected = JdbcTemplateUtil.update(sql, paymentId);
        return affected > 0;
    }

    @Override
    public BigDecimal getTotalPaidAmount(Integer contractId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM Payments WHERE contractId = ? AND status = 'COMPLETED'";
        return JdbcTemplateUtil.queryOne(sql, BigDecimal.class, contractId);
    }

    @Override
    public BigDecimal getRemainingAmount(Integer contractId) {
        // Giả sử có bảng contracts chứa total_amount
        String sql = "SELECT (c.totalAmount - COALESCE((SELECT SUM(amount) FROM Payments WHERE contractId = ? AND status = 'COMPLETED'), 0)) AS remaining "
                + "FROM Contracts c WHERE c.contractId = ?";
        return JdbcTemplateUtil.queryOne(sql, BigDecimal.class, contractId, contractId);
    }

    @Override
    public boolean isPaymentCompleted(Integer paymentId) {
        String sql = "SELECT COUNT(*) FROM Payments WHERE paymentId = ? AND status = 'COMPLETED'";
        int count = JdbcTemplateUtil.count(sql, paymentId);
        return count > 0;
    }

    @Override
    public Payments findPendingPayment(Integer contractId, BigDecimal amount) {
        String sql = "SELECT * FROM Payments WHERE contractId = ? AND amount = ? AND status = 'PENDING' ORDER BY paymentDate DESC LIMIT 1";
        List<Payments> list = JdbcTemplateUtil.query(sql, Payments.class, contractId, amount);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Payments findPendingPaymentByCode(Integer contractId, BigDecimal amount) {
        String sql = "SELECT * FROM Payments WHERE contractId = ? AND amount = ? AND status = 'PENDING' AND transactionCode IS NOT NULL ORDER BY paymentDate DESC LIMIT 1";
        List<Payments> list = JdbcTemplateUtil.query(sql, Payments.class, contractId, amount);
        return list.isEmpty() ? null : list.get(0);
    }

}
