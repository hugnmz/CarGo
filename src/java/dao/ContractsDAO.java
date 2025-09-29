/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Contracts;

/**
 *
 * @author admin
 */
public interface ContractsDAO {
    List<Contracts> getAllContracts();
    Optional<Contracts> getContractById(Integer contractId);
    boolean addContract(Contracts contract);
    boolean updateContract(Contracts contract);
    boolean deleteContract(Integer contractId);
    boolean updateContractStatus(Integer contractId, String status);
    List<Contracts> getContractByCustomer(Integer customerId);
    List<Contracts> getContractByStaff(Integer staffId);
    List<Contracts> getContractByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Contracts> getContractByStatus(String status);
    boolean calculateTotalAmout(Integer contractId);
}
