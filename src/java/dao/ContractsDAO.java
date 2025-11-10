/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Contract;

/**
 *
 * @author admin
 */
public interface ContractsDAO {

    List<Contract> getAllContracts();

    Optional<Contract> getContractById(Integer contractId);

    boolean addContract(Contract contract);

    boolean updateContract(Contract contract);

    boolean deleteContract(Integer contractId);

    boolean updateContractStatus(Integer contractId, String status);

    List<Contract> getContractByCustomer(Integer customerId);

    List<Contract> getContractByStaff(Integer staffId);

    List<Contract> getContractByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Contract> getContractByStatus(String status);

    public boolean updateContractTotalAmount(Integer contractId, BigDecimal totalAmount);

    boolean calculateTotalAmout(Integer contractId);

    boolean updateStaffId(Integer staffId, Integer contractId);

    boolean updateNote(String note, Integer contractId);

    boolean updateContractStatus(Integer contractId, String status, String reason);

    int countContract();

    Integer findLeastLoadedStaffId();

    BigDecimal getTotalAmount(Integer contractId);

    boolean addPaymentLog(Integer contractId, String message);
}
