package service.impl;

import dao.ContractsDAO;
import dao.ContractDetailsDAO;
import dao.CustomersDAO;
import dto.ContractDTO;
import dto.ContractDetailDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.ContractMapper;
import mapper.ContractDetailMapper;
import service.ContractService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractsDAO contractsDAO;
    
    @Autowired
    private ContractDetailsDAO contractDetailsDAO;
    
    @Autowired
    private CustomersDAO customersDAO;
    
    @Autowired
    private ContractMapper contractMapper;
    
    @Autowired
    private ContractDetailMapper contractDetailMapper;

    @Override
    public List<ContractDTO> getContractsByCustomer(Integer customerId) {
        List<ContractDTO> contractDTOs = new ArrayList<>();
        
        // Lấy danh sách contracts từ DAO
        List<model.Contracts> contracts = contractsDAO.getContractByCustomer(customerId);
        
        for (model.Contracts contract : contracts) {
            ContractDTO dto = contractMapper.toDTO(contract);
            contractDTOs.add(dto);
        }
        
        return contractDTOs;
    }

    @Override
    public Optional<ContractDTO> getContractById(Integer contractId) {
        Optional<model.Contracts> contract = contractsDAO.getContractById(contractId);
        if (contract.isPresent()) {
            ContractDTO dto = contractMapper.toDTO(contract.get());
            
            // Lấy tên khách hàng
            Optional<model.Customers> customer = customersDAO.getCustomerById(dto.getCustomerId());
            if (customer.isPresent() && customer.get().getFullName() != null) {
                dto.setCustomerName(customer.get().getFullName());
            }
            
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public List<ContractDetailDTO> getContractDetails(Integer contractId) {
        List<ContractDetailDTO> detailDTOs = new ArrayList<>();
        
        List<model.ContractDetails> details = contractDetailsDAO.getContractDetailsByContractId(contractId);
        
        for (model.ContractDetails detail : details) {
            ContractDetailDTO dto = contractDetailMapper.toDTO(detail);
            detailDTOs.add(dto);
        }
        
        return detailDTOs;
    }
}
