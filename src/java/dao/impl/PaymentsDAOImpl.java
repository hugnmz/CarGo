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
        String sql = "SELECT payment_id, contract_id, amount, method_id, status, payment_date " +
                     "FROM payments";
        return JdbcTemplateUtil.query(sql, Payments.class);
    }

    @Override
    public Payments getPaymentById(Integer paymentId) {
        String sql = "SELECT payment_id, contract_id, amount, method_id, status, payment_date " +
                     "FROM payments WHERE payment_id = ?";
        return JdbcTemplateUtil.queryOne(sql, Payments.class, paymentId);
    }

    @Override
    public List<Payments> getPaymentsByContract(Integer contractId) {
        String sql = "SELECT payment_id, contract_id, amount, method_id, status, payment_date " +
                     "FROM payments WHERE contract_id = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, contractId);
    }

    @Override
    public List<Payments> getPaymentsByStatus(String status) {
        String sql = "SELECT payment_id, contract_id, amount, method_id, status, payment_date " +
                     "FROM payments WHERE status = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, status);
    }

    @Override
    public List<Payments> getPaymentsByMethod(Integer methodId) {
        String sql = "SELECT payment_id, contract_id, amount, method_id, status, payment_date " +
                     "FROM payments WHERE method_id = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, methodId);
    }

    @Override
    public boolean addPayment(Payments payment) {
        String sql = "INSERT INTO payments (contract_id, amount, method_id, status, payment_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
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
        String sql = "UPDATE payments SET contract_id = ?, amount = ?, method_id = ?, status = ?, payment_date = ? " +
                     "WHERE payment_id = ?";
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
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        int affected = JdbcTemplateUtil.update(sql, status, paymentId);
        return affected > 0;
    }

    @Override
    public boolean deletePayment(Integer paymentId) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        int affected = JdbcTemplateUtil.update(sql, paymentId);
        return affected > 0;
    }

    @Override
    public BigDecimal getTotalPaidAmount(Integer contractId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM payments WHERE contract_id = ? AND status = 'completed'";
        return JdbcTemplateUtil.queryOne(sql, BigDecimal.class, contractId);
    }

    @Override
    public BigDecimal getRemainingAmount(Integer contractId) {
        // Giả sử có bảng contracts chứa total_amount
        String sql = "SELECT (c.total_amount - COALESCE((SELECT SUM(amount) FROM payments WHERE contract_id = ? AND status = 'completed'), 0)) AS remaining " +
                     "FROM contracts c WHERE c.contract_id = ?";
        return JdbcTemplateUtil.queryOne(sql, BigDecimal.class, contractId, contractId);
    }

    @Override
    public boolean isPaymentCompleted(Integer paymentId) {
        String sql = "SELECT COUNT(*) FROM payments WHERE payment_id = ? AND status = 'completed'";
        int count = JdbcTemplateUtil.count(sql, paymentId);
        return count > 0;
    }
}