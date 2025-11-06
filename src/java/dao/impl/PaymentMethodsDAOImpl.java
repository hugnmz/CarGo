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
        String sql = "SELECT methodId, methodName FROM PaymentMethods";
        return JdbcTemplateUtil.query(sql, PaymentMethods.class);
    }

    @Override
    public PaymentMethods getPaymentMethodById(Integer methodId) {
        String sql = "SELECT methodId, methodName FROM PaymentMethods WHERE methodId = ?";
        return JdbcTemplateUtil.queryOne(sql, PaymentMethods.class, methodId);
    }

    @Override
    public PaymentMethods getPaymentMethodByName(String methodName) {
        String sql = "SELECT methodId, methodName FROM PaymentMethods WHERE methodName = ?";
        return JdbcTemplateUtil.queryOne(sql, PaymentMethods.class, methodName);
    }

    @Override
    public boolean addPaymentMethod(PaymentMethods paymentMethod) {
        String sql = "INSERT INTO PaymentMethods (methodId, methodName) VALUES (?, ?)";
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
        String sql = "UPDATE PaymentMethods SET methodName = ? WHERE methodId = ?";
        int affected = JdbcTemplateUtil.update(
            sql,
            paymentMethod.getMethodName(),
            paymentMethod.getMethodId()
        );
        return affected > 0;
    }

    @Override
    public boolean deletePaymentMethod(Integer methodId) {
        String sql = "DELETE FROM PaymentMethods WHERE methodId = ?";
        int affected = JdbcTemplateUtil.update(sql, methodId);
        return affected > 0;
    }

    @Override
    public boolean isPaymentMethodInUse(Integer methodId) {
        String sql = "SELECT COUNT(*) FROM Payments WHERE methodId = ?";
        int count = JdbcTemplateUtil.count(sql, methodId);
        return count > 0;
    }
}