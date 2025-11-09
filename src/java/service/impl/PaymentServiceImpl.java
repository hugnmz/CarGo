package service.impl;

import dao.ContractsDAO;
import dao.PaymentsDAO;
import dto.PaymentDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.PaymentsMapper;
import model.Payments;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentsDAO paymentsDAO;

    @Autowired
    private ContractsDAO contractsDAO;

    @Autowired
    private PaymentsMapper paymentsMapper;

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payments> payments = paymentsDAO.getAllPayments();
        List<PaymentDTO> paymentDTOs = new ArrayList<>();
        for (Payments p : payments) {
            paymentDTOs.add(paymentsMapper.toDTO(p));
        }
        return paymentDTOs;
    }

    @Override
    public Optional<PaymentDTO> getPaymentById(Integer paymentId) {
        Payments payment = paymentsDAO.getPaymentById(paymentId);
        return payment != null ? Optional.of(paymentsMapper.toDTO(payment)) : Optional.empty();
    }

    @Override
    public List<PaymentDTO> getPaymentsByContract(Integer contractId) {
        List<Payments> payments = paymentsDAO.getPaymentsByContract(contractId);
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payments p : payments) dtos.add(paymentsMapper.toDTO(p));
        return dtos;
    }

    @Override
    public boolean addPayment(PaymentDTO paymentDTO) {
        Payments payment = paymentsMapper.toModel(paymentDTO);
        if ("DEPOSIT_PAID".equals(payment.getStatus())) {
            // thêm deposit
            int id = paymentsDAO.insertDepositPayment(payment.getContractId(), payment.getAmount());
            return id > 0;
        } else {
            // thêm thanh toán bình thường
            return paymentsDAO.addPayment(payment);
        }
    }

    @Override
    public boolean updatePaymentStatus(Integer paymentId, String status) {
        return paymentsDAO.updatePaymentStatus(paymentId, status);
    }

    @Override
    public Optional<PaymentDTO> findPendingPayment(Integer contractId, BigDecimal amount) {
        Payments payment = paymentsDAO.findPendingPayment(contractId, amount);
        return payment != null ? Optional.of(paymentsMapper.toDTO(payment)) : Optional.empty();
    }

    @Override
    public boolean isPaymentCompleted(Integer paymentId) {
        return paymentsDAO.isPaymentCompleted(paymentId);
    }

    @Override
    public String getPaymentStatus(Integer contractId) {
        String status = paymentsDAO.getPaymentStatus(contractId);
        if ("NONE".equals(status)) {
            BigDecimal total = paymentsDAO.getTotalPaidAmount(contractId);
            BigDecimal contractTotal = contractsDAO.getTotalAmount(contractId);
            if (total != null && contractTotal != null && total.compareTo(contractTotal) >= 0) {
return "COMPLETED";
            } else if (total != null && total.compareTo(BigDecimal.ZERO) > 0) {
                return "DEPOSIT_PAID";
            } else {
                return "PENDING";
            }
        }
        return status;
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
        int id = paymentsDAO.insertPendingPayment(contractId, amount);
        if (id > 0) {
            Payments payment = new Payments();
            payment.setContractId(contractId);
            payment.setAmount(amount);
            payment.setStatus("PENDING");
            payment.setPaymentDate(null);
            return paymentsMapper.toDTO(payment);
        }
        return null;
    }
}