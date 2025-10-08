package dao;

import model.PaymentMethods;
import java.util.List;

public interface PaymentMethodsDAO {
    
    // Lấy tất cả phương thức thanh toán
    List<PaymentMethods> getAllPaymentMethods();
    
    // Lấy phương thức thanh toán theo ID
    PaymentMethods getPaymentMethodById(Integer methodId);
    
    // Lấy phương thức thanh toán theo tên
    PaymentMethods getPaymentMethodByName(String methodName);
    
    // Thêm phương thức thanh toán mới
    boolean addPaymentMethod(PaymentMethods paymentMethod);
    
    // Cập nhật phương thức thanh toán
    boolean updatePaymentMethod(PaymentMethods paymentMethod);
    
    // Xóa phương thức thanh toán
    boolean deletePaymentMethod(Integer methodId);
    
    // Kiểm tra phương thức thanh toán có đang được sử dụng không
    boolean isPaymentMethodInUse(Integer methodId);
}
