/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.ContractDetails;

/**
 *
 * @author admin
 */
public interface ContractDetailsDAO {

    List<ContractDetails> getAllContractDetails();

    Optional<ContractDetails> getContractDetailById(Integer contractDetailId);

    boolean deleteContractDetail(Integer contractDetailId);

    List<ContractDetails> getContractDetailsByVehicle(Integer vehicleId);

    boolean checkVehicleAvailability(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);
}
