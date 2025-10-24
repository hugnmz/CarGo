/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dto.ContractDTO;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Admin
 */
public interface ContractService {
    //lay tat ca hop dong thue xe
    List<ContractDTO> getAllContracts();
    
    //tim hop dong theo id
    Optional<ContractDTO> getContractById(Integer contractId);
}
