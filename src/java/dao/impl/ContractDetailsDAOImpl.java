package dao.impl;

import dao.ContractDetailsDAO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.ContractDetail;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class ContractDetailsDAOImpl implements ContractDetailsDAO {

    @Override
    public List<ContractDetail> getAllContractDetails() {
        String sql = "SELECT * FROM dbo.ContractDetails";
        return JdbcTemplateUtil.query(sql, ContractDetail.class);
    }

    @Override
    public Optional<ContractDetail> getContractDetailById(Integer contractDetailId) {
        String sql = "SELECT * FROM dbo.ContractDetails WHERE contractDetailId = ?";
        ContractDetail detail = JdbcTemplateUtil.queryOne(sql, ContractDetail.class, contractDetailId);
        return Optional.ofNullable(detail);
    }

    @Override
    public boolean deleteContractDetail(Integer contractDetailId) {
        String sql = "DELETE FROM dbo.ContractDetails WHERE contractDetailId = ?";
        int result = JdbcTemplateUtil.update(sql, contractDetailId);
        return result > 0;
    }

    @Override
    public List<ContractDetail> getContractDetailsByVehicle(Integer vehicleId) {
        String sql = "SELECT * FROM dbo.ContractDetails WHERE vehicleId = ?";
        return JdbcTemplateUtil.query(sql, ContractDetail.class, vehicleId);
    }
    
    @Override
    public List<ContractDetail> getContractDetailsByContractId(Integer contractId) {
        String sql = "SELECT ct.*, v.plateNumber, c.name FROM dbo.ContractDetails ct JOIN dbo.Vehicles v ON v.vehicleId = ct.vehicleId JOIN dbo.Cars c ON c.carId = v.carId WHERE  ct.contractId = ? ORDER BY contractDetailId";
        return JdbcTemplateUtil.query(sql, ContractDetail.class, contractId);
    }

    @Override
    public boolean checkVehicleAvailability(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM dbo.ContractDetails cd " +
                    "JOIN dbo.Contracts c ON cd.contractId = c.contractId " +
                    "WHERE cd.vehicleId = ? AND c.status IN ('PENDING', 'ACCEPTED', 'IN_PROGRESS') " +
                    "AND (cd.rentStartDate < ? AND cd.rentEndDate > ?)";
        int count = JdbcTemplateUtil.count(sql, vehicleId, endDate, startDate);
        return count == 0;
    }

    @Override
    public boolean addContractDetail(ContractDetail contractDetail) {
        String sql = "INSERT INTO dbo.ContractDetails(contractId, vehicleId, price, rentStartDate, rentEndDate, note) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql,
                contractDetail.getContractId(),
                contractDetail.getVehicleId(),
                contractDetail.getPrice(),
                contractDetail.getRentStartDate(),
                contractDetail.getRentEndDate(),
                contractDetail.getNote());
        return result > 0;
    }

    @Override
    public boolean deleteContractDetailByContractId(Integer contractId) {
        String sql = "DELETE FROM dbo.ContractDetails WHERE contractId = ?";
        int result = JdbcTemplateUtil.update(sql, contractId);
        return result > 0;

    }
    
}
