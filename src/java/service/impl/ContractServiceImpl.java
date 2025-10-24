/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dto.ContractDTO;
import java.util.List;
import java.util.Optional;
import service.ContractService;
import mapper.ContractMapper;
import util.di.annotation.Autowired;
import dao.ContractsDAO;

/**
 *
 * @author Admin
 */
public class ContractServiceImpl implements ContractService{

    @Autowired
    private ContractsDAO contractDAO;
    
    @Autowired
    private ContractMapper contractMapper;
    
    @Override
    public List<ContractDTO> getAllContracts() {
        
        
  }

    @Override
    public Optional<ContractDTO> getContractById(Integer contractId) {
        
        
  }
    
}
