package service.impl;

import dao.ContractsDAO;
import dao.PaymentsDAO;
import dto.PaymentDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.PaymentsMapper;
import model.Payments;
import service.PaymentService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

/**
 * PaymentServiceImpl - Implementation của PaymentService
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentsDAO paymentsDAO;

    @Autowired
    private ContractsDAO contractsDAO; // Dùng để lấy totalAmount cho payment flow

    @Autowired
    private PaymentsMapper paymentsMapper;

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payments> payments = paymentsDAO.getAllPayments();
        List<PaymentDTO> paymentDTOs = new ArrayList<>();
        for (Payments payment : payments) {
            PaymentDTO dto = paymentsMapper.toDTO(payment);
            paymentDTOs.add(dto);
        }

        return paymentDTOs;
    }

    @Override
    public Optional<PaymentDTO> getPaymentById(Integer paymentId) {
        Payments payment = paymentsDAO.getPaymentById(paymentId);
        if (payment != null) {
            PaymentDTO dto = paymentsMapper.toDTO(payment);
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public List<PaymentDTO> getPaymentsByContract(Integer contractId) {
        List<Payments> payments = paymentsDAO.getPaymentsByContract(contractId);
        List<PaymentDTO> paymentDTOs = new ArrayList<>();

        for (Payments payment : payments) {
            PaymentDTO dto = paymentsMapper.toDTO(payment);
            paymentDTOs.add(dto);
        }

        return paymentDTOs;
    }

    @Override
    public boolean addPayment(PaymentDTO paymentDTO) {
        Payments payment = paymentsMapper.toModel(paymentDTO);
        return paymentsDAO.addPayment(payment);
    }

    @Override
    public boolean updatePaymentStatus(Integer paymentId, String status) {
        return paymentsDAO.updatePaymentStatus(paymentId, status);
    }

    @Override
    public Optional<PaymentDTO> findPendingPayment(Integer contractId, BigDecimal amount) {
        Payments payment = paymentsDAO.findPendingPayment(contractId, amount);
        if (payment != null) {
            PaymentDTO dto = paymentsMapper.toDTO(payment);
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public boolean completePaymentById(Integer paymentId) {
        // Cast to implementation để gọi method không có trong interface
        dao.impl.PaymentsDAOImpl impl = (dao.impl.PaymentsDAOImpl) paymentsDAO;
        return impl.completePaymentById(paymentId);
    }

    @Override
    public boolean isPaymentCompleted(Integer paymentId) {
        return paymentsDAO.isPaymentCompleted(paymentId);
    }

    @Override
    public boolean hasCompleted(Integer contractId) {
        // Cast to implementation để gọi method không có trong interface
        dao.impl.PaymentsDAOImpl impl = (dao.impl.PaymentsDAOImpl) paymentsDAO;
        return impl.hasCompleted(contractId);
    }

    @Override
    public String getPaymentStatus(Integer contractId) {
        // Cast to implementation để gọi method không có trong interface
        dao.impl.PaymentsDAOImpl impl = (dao.impl.PaymentsDAOImpl) paymentsDAO;
        return impl.getPaymentStatus(contractId);
    }

    @Override
    public BigDecimal getTotalPaidAmount(Integer contractId) {
        return paymentsDAO.getTotalPaidAmount(contractId);
    }

    @Override
    public BigDecimal getRemainingAmount(Integer contractId) {
        return paymentsDAO.getRemainingAmount(contractId);
    }

    @Override
    public Optional<BigDecimal> getContractTotalAmount(Integer contractId) {
        BigDecimal total = contractsDAO.getTotalAmount(contractId);
        return total != null ? Optional.of(total) : Optional.empty();
    }

    @Override
    public PaymentDTO createPendingPayment(int contractId, BigDecimal amount) {
        int affected = paymentsDAO.insertPendingPayment(contractId, amount);
        if (affected > 0) {
            Payments payment = new Payments();
            payment.setContractId(contractId);
            payment.setAmount(amount);
            payment.setStatus("pending");
            payment.setPaymentDate(null);
            return paymentsMapper.toDTO(payment);
        } else {
            return null;
        }
    }
    @Override
public int markDepositPaid(int contractId, BigDecimal amount) {
    return paymentsDAO.insertDepositPayment(contractId, amount);
}
}