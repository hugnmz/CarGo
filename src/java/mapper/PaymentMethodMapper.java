package mapper;

import dto.PaymentMethodDTO;
import model.PaymentMethods;
import model.Payments;
import util.di.annotation.Component;

/**
 * PaymentMethodMapper - Chuyển đổi giữa PaymentMethodDTO và PaymentMethods Model
 */
@Component
public class PaymentMethodMapper {

    // Chuyen tu Model sang DTO
    public PaymentMethodDTO toDTO(PaymentMethods paymentMethod) {
        // Kiem tra null
        if (paymentMethod == null) {
            return null;
        }

        PaymentMethodDTO dto = new PaymentMethodDTO();
        
        // Gan cac truong co ban cua phuong thuc thanh toan
        dto.setMethodId(paymentMethod.getMethodId());
        dto.setMethodName(paymentMethod.getMethodName());

        // Thong ke (tuy chon - co the dat rieng)
        if (paymentMethod.getPayments() != null) {
            dto.setPaymentCount(paymentMethod.getPayments().size());
            
            // Tinh tong tien
            double totalAmount = 0.0;
            for (Payments payment : paymentMethod.getPayments()) {
                if ("completed".equals(payment.getStatus())) {
                    totalAmount += payment.getAmount().doubleValue();
                }
            }
            dto.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));
        }

        return dto;
    }

    // Chuyen tu DTO sang Model
    public PaymentMethods toModel(PaymentMethodDTO dto) {
        // Kiem tra null
        if (dto == null) {
            return null;
        }

        PaymentMethods paymentMethod = new PaymentMethods();
        
        // Gan cac truong co ban cua phuong thuc thanh toan
        paymentMethod.setMethodId(dto.getMethodId());
        paymentMethod.setMethodName(dto.getMethodName());

        return paymentMethod;
    }
}
