/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.ContractsDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Contracts;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class ContractsDAOImpl implements ContractsDAO {

    @Override
    public List<Contracts> getAllContracts() {
        String sql = "select c.createAt, c.contractId, pm.methodName, cus.fullName, car.name, v.plateNumber,c.startDate,c.endDate,c.status    from Contracts c\n"
                + "join Payments p on p.contractId = c.contractId\n"
                + "join PaymentMethods pm on pm.methodId = p.methodId\n"
                + "join Customers cus on cus.customerId = c.customerId\n"
                + "join ContractDetails cd on cd.contractId = c.contractId\n"
                + "join Vehicles v on v.vehicleId = cd.vehicleId\n"
                + "join Cars car on car.carId = v.carId";
        return JdbcTemplateUtil.query(sql, Contracts.class);
    }

    @Override
    public Optional<Contracts> getContractById(Integer contractId) {
        String sql = "select c.createAt, c.contractId, pm.methodName, cus.fullName, car.name, v.plateNumber,c.startDate,c.endDate,c.status    from Contracts c\n"
                + "join Payments p on p.contractId = c.contractId\n"
                + "join PaymentMethods pm on pm.methodId = p.methodId\n"
                + "join Customers cus on cus.customerId = c.customerId\n"
                + "join ContractDetails cd on cd.contractId = c.contractId\n"
                + "join Vehicles v on v.vehicleId = cd.vehicleId\n"
                + "join Cars car on car.carId = v.carId\n"
                + "where c.contractId = ?";
        Contracts c = JdbcTemplateUtil.queryOne(sql, Contracts.class, contractId);
        return Optional.ofNullable(c);
    }

    @Override
    public boolean addContract(Contracts contract) {
        String sql = "INSERT INTO Contracts (customerId,staffId,startDate,endDate,status,createAt,totalAmount,depositAmount)\n"
                + "values (?,?,?,?,?,?,?,?)";
        int result = JdbcTemplateUtil.update(sql,
                contract.getCustomerId(),
                contract.getStaffId(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getStatus(),
                contract.getCreateAt(),
                contract.getTotalAmount(),
                contract.getDepositAmount());
        return result > 0;

    }

    @Override
    public boolean updateContract(Contracts contract) {
        
    }

    @Override
    public boolean deleteContract(Integer contractId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateContractStatus(Integer contractId, String status) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Contracts> getContractByCustomer(Integer customerId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Contracts> getContractByStaff(Integer staffId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Contracts> getContractByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Contracts> getContractByStatus(String status) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean calculateTotalAmout(Integer contractId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   
}
