/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.ContractDetail;

/**
 *
 * @author admin
 */
public interface ContractDetailsDAO {

    List<ContractDetail> getAllContractDetails();

    Optional<ContractDetail> getContractDetailById(Integer contractDetailId);

    boolean deleteContractDetail(Integer contractDetailId);

    boolean deleteContractDetailByContractId(Integer contractId);

    List<ContractDetail> getContractDetailsByVehicle(Integer vehicleId);

    List<ContractDetail> getContractDetailsByContractId(Integer contractId);

    boolean checkVehicleAvailability(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);

    boolean addContractDetail(ContractDetail contractDetail);
}
