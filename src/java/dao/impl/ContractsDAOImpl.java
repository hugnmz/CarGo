/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

/**
 *
 * @author laptop lenovo
 */
import dao.ContractsDAO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Contracts;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class ContractsDAOImpl implements ContractsDAO {

    @Override
    public BigDecimal getTotalAmount(Integer contractId) {
        String sql = "SELECT totalAmount FROM Contracts WHERE contractId = ?";
        Contracts contract = JdbcTemplateUtil.queryOne(sql, Contracts.class, contractId);
        return contract != null ? contract.getTotalAmount() : null;
    }

    @Override
    public boolean addPaymentLog(Integer contractId, String message) {
        String sql = "INSERT INTO paymentLogs (contractId, message, createdAt) VALUES (?, ?, NOW())";
        return JdbcTemplateUtil.update(sql, contractId, message) > 0;
    }

    @Override
    public List<Contracts> getAllContracts() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Contracts> getContractById(Integer contractId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean addContract(Contracts contract) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateContract(Contracts contract) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteContract(Integer contractId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateContractStatus(Integer contractId, String status) {
        String sql = "UPDATE Contracts SET status = ? WHERE contractId = ?";
        int affected = JdbcTemplateUtil.update(sql, status, contractId);
        return affected > 0;
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
