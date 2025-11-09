package dao.impl;

import dao.PaymentsDAO;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import model.Payments;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class PaymentsDAOImpl implements PaymentsDAO {

    @Override
    public List<Payments> getAllPayments() {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate FROM Payments";
        return JdbcTemplateUtil.query(sql, Payments.class);
    }

    @Override
    public Payments getPaymentById(Integer paymentId) {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate FROM Payments WHERE paymentId = ?";
        return JdbcTemplateUtil.queryOne(sql, Payments.class, paymentId);
    }

    @Override
    public List<Payments> getPaymentsByContract(Integer contractId) {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate FROM Payments WHERE contractId = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, contractId);
    }

    @Override
    public List<Payments> getPaymentsByStatus(String status) {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate FROM Payments WHERE status = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, status);
    }

    @Override
    public List<Payments> getPaymentsByMethod(Integer methodId) {
        String sql = "SELECT paymentId, contractId, amount, methodId, status, paymentDate FROM Payments WHERE methodId = ?";
        return JdbcTemplateUtil.query(sql, Payments.class, methodId);
    }

    @Override
    public boolean addPayment(Payments payment) {
        String sql = "INSERT INTO Payments (contractId, amount, methodId, status, paymentDate) VALUES (?, ?, ?, ?, ?)";
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
        String sql = "UPDATE Payments SET contractId = ?, amount = ?, methodId = ?, status = ?, paymentDate = ? WHERE paymentId = ?";
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
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM Payments WHERE contractId = ? AND UPPER(LTRIM(RTRIM(status))) = 'COMPLETED'";
        return JdbcTemplateUtil.queryOne(sql, BigDecimal.class, contractId);
    }

    @Override
    public BigDecimal getRemainingAmount(Integer contractId) {
        String sql = """
            SELECT (c.totalAmount - COALESCE((SELECT SUM(amount)
                                              FROM Payments
                                              WHERE contractId = ?
                                              AND UPPER(LTRIM(RTRIM(status))) = 'COMPLETED'), 0)) AS remaining
            FROM Contracts c WHERE c.contractId = ?
            """;
        return JdbcTemplateUtil.queryOne(sql, BigDecimal.class, contractId, contractId);
    }

    @Override
    public boolean isPaymentCompleted(Integer paymentId) {
        String sql = "SELECT COUNT(*) FROM Payments WHERE paymentId = ? AND UPPER(LTRIM(RTRIM(status))) = 'COMPLETED'";
        int count = JdbcTemplateUtil.count(sql, paymentId);
        System.out.println("[isPaymentCompleted] paymentId=" + paymentId + " → count=" + count);
        return count > 0;
    }

    @Override
    public Payments findPendingPayment(Integer contractId, BigDecimal amount) {
        String sql;
        List<Payments> list;

        if (amount == null) {
            sql = "SELECT TOP 1 * FROM Payments WHERE contractId = ? AND UPPER(LTRIM(RTRIM(status))) = 'PENDING' ORDER BY paymentId DESC";
            list = JdbcTemplateUtil.query(sql, Payments.class, contractId);
        } else {
            sql = "SELECT TOP 1 * FROM Payments WHERE contractId = ? AND CAST(amount AS DECIMAL(10,2)) = CAST(? AS DECIMAL(10,2)) AND UPPER(LTRIM(RTRIM(status))) = 'PENDING' ORDER BY paymentId DESC";
            list = JdbcTemplateUtil.query(sql, Payments.class, contractId, amount);
        }

        System.out.println("[findPendingPayment] contractId=" + contractId + ", amount=" + amount + ", found=" + list.size());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Payments findPendingPaymentByCode(Integer contractId, BigDecimal amount) {
        String sql = """
            SELECT TOP 1 * FROM Payments
            WHERE contractId = ?
              AND CAST(amount AS DECIMAL(10,2)) = CAST(? AS DECIMAL(10,2))
              AND UPPER(LTRIM(RTRIM(status))) = 'PENDING'
              AND transactionCode IS NOT NULL
            ORDER BY paymentDate DESC
            """;
        List<Payments> list = JdbcTemplateUtil.query(sql, Payments.class, contractId, amount);
        return list.isEmpty() ? null : list.get(0);
    }

    // Guard tuyệt đối: đã có completed?
    public boolean hasCompleted(int contractId) {
        String sql = "SELECT COUNT(*) FROM Payments WHERE contractId = ? AND UPPER(LTRIM(RTRIM(status))) = 'COMPLETED'";
        int c = JdbcTemplateUtil.count(sql, contractId);
        System.out.println("[hasCompleted] contractId=" + contractId + " → " + c);
        return c > 0;
    }

    public String getPaymentStatus(int contractId) {
        String sql = """
            SELECT CASE 
                     WHEN EXISTS (SELECT 1 FROM Payments WHERE contractId = ? AND UPPER(LTRIM(RTRIM(status)))='COMPLETED') THEN 'COMPLETED'
                     WHEN EXISTS (SELECT 1 FROM Payments WHERE contractId = ? AND UPPER(LTRIM(RTRIM(status)))='PENDING') THEN 'PENDING'
                     ELSE 'NONE'
                   END AS status
            """;
        String status = JdbcTemplateUtil.queryOne(sql, String.class, contractId, contractId);
        System.out.println("[getPaymentStatus] contractId=" + contractId + " -> status=" + status);
        return status;
    }

    public boolean completePaymentById(Integer paymentId) {
        String sql = "UPDATE Payments SET status = 'COMPLETED', paymentDate = GETDATE() WHERE paymentId = ?";
        return JdbcTemplateUtil.update(sql, paymentId) > 0;
    }
   @Override
     public int insertPendingPayment(int contractId, BigDecimal amount) {
        String sql = "INSERT INTO Payments(contractId, amount, status, paymentDate, methodId) "
                   + "VALUES (?, ?, ?, NULL, 1)"; // methodId = 1 default
        return JdbcTemplateUtil.update(sql, contractId, amount, "PENDING");
    }
     
     @Override
public int insertDepositPayment(int contractId, BigDecimal amount) {
    String sql = "INSERT INTO Payments (contractId, amount, methodId, status, paymentDate) VALUES (?, ?, 1, 'DEPOSIT_PAID', GETDATE())";
    return JdbcTemplateUtil.insertAndReturnKey(sql, contractId, amount);
}
}