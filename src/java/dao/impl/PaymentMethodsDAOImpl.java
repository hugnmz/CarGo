package dao.impl;

import dao.PaymentMethodsDAO;
import java.util.List;
import model.PaymentMethods;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 *
 * @author admin
 */
@Repository
public class PaymentMethodsDAOImpl implements PaymentMethodsDAO {

    @Override
    public List<PaymentMethods> getAllPaymentMethods() {
        String sql = "SELECT method_id, method_name FROM payment_methods";
        return JdbcTemplateUtil.query(sql, PaymentMethods.class);
    }

    @Override
    public PaymentMethods getPaymentMethodById(Integer methodId) {
        String sql = "SELECT method_id, method_name FROM payment_methods WHERE method_id = ?";
        return JdbcTemplateUtil.queryOne(sql, PaymentMethods.class, methodId);
    }

    @Override
    public PaymentMethods getPaymentMethodByName(String methodName) {
        String sql = "SELECT method_id, method_name FROM payment_methods WHERE method_name = ?";
        return JdbcTemplateUtil.queryOne(sql, PaymentMethods.class, methodName);
    }

    @Override
    public boolean addPaymentMethod(PaymentMethods paymentMethod) {
        String sql = "INSERT INTO payment_methods (method_id, method_name) VALUES (?, ?)";
        int id = JdbcTemplateUtil.insertAndReturnKey(
            sql,
            paymentMethod.getMethodId(),
            paymentMethod.getMethodName()
        );
        if (id > 0) {
            paymentMethod.setMethodId(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePaymentMethod(PaymentMethods paymentMethod) {
        String sql = "UPDATE payment_methods SET method_name = ? WHERE method_id = ?";
        int affected = JdbcTemplateUtil.update(
            sql,
            paymentMethod.getMethodName(),
            paymentMethod.getMethodId()
        );
        return affected > 0;
    }

    @Override
    public boolean deletePaymentMethod(Integer methodId) {
        String sql = "DELETE FROM payment_methods WHERE method_id = ?";
        int affected = JdbcTemplateUtil.update(sql, methodId);
        return affected > 0;
    }

    @Override
    public boolean isPaymentMethodInUse(Integer methodId) {
        String sql = "SELECT COUNT(*) FROM payments WHERE method_id = ?";
        int count = JdbcTemplateUtil.count(sql, methodId);
        return count > 0;
    }
}