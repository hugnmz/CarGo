package mapper;

import dto.PaymentDTO;
import model.Payments;
import util.di.annotation.Component;

@Component
public class PaymentsMapper {

    /**
     * Chuyển Payments (model) sang PaymentDTO (DTO)
     *
     * @param payment Payments object
     * @return PaymentDTO object
     */
    public PaymentDTO toDTO(Payments payment) {
        if (payment == null) {
            return null;
        }

        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setContractId(payment.getContractId());
        dto.setAmount(payment.getAmount());

        // Chuyển methodId sang methodName nếu PaymentMethods != null
        if (payment.getPaymentMethod() != null) {
            dto.setMethodName(payment.getPaymentMethod().getMethodName());
        }

        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());

        return dto;
    }
/**
     * Chuyển PaymentDTO (DTO) sang Payments (model)
     *
     * @param dto PaymentDTO object
     * @return Payments object
     */
    public Payments toModel(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }

        Payments payment = new Payments();
        payment.setPaymentId(dto.getPaymentId());
        payment.setContractId(dto.getContractId());
        payment.setAmount(dto.getAmount());

        // Lưu ý: PaymentMethods phải set riêng khi cần
        payment.setStatus(dto.getStatus());
        payment.setPaymentDate(dto.getPaymentDate());

        return payment;
    }
}