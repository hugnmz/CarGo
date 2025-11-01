package dao.impl;

import dao.FuelsDAO;
import java.util.List;
import java.util.Optional;
import model.Fuels;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class FuelsDAOImpl implements FuelsDAO {

    @Override
    public List<Fuels> getAllFuels() {
        String sql = "SELECT * FROM dbo.Fuels";
        return JdbcTemplateUtil.query(sql, Fuels.class);
    }
    
    public static void main(String[] args) {
        FuelsDAOImpl d = new FuelsDAOImpl();
        List<Fuels> list = d.getAllFuels();
        for (Fuels fuels : list) {
            
        }
    }

    @Override
    public Optional<Fuels> getFuelsById(Integer fuelId) {
        String sql = "SELECT * FROM dbo.Fuels WHERE fuelId = ?";
        Fuels fuel = JdbcTemplateUtil.queryOne(sql, Fuels.class, fuelId);
        return Optional.ofNullable(fuel);
    }

    @Override
    public boolean addFuels(Fuels fuel) {
        String sql = "INSERT INTO dbo.Fuels(fuelType) VALUES (?)";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql, fuel.getFuelType());
        if (result > 0) {
            fuel.setFuelId(result);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateFuels(Fuels fuel) {
        String sql = "UPDATE dbo.Fuels SET fuelType = ? WHERE fuelId = ?";
        int result = JdbcTemplateUtil.update(sql, fuel.getFuelType(), fuel.getFuelId());
        return result > 0;
    }

    @Override
    public boolean deleteFuels(Integer fuelId) {
        String sql = "DELETE FROM dbo.Fuels WHERE fuelId = ?";
        int result = JdbcTemplateUtil.update(sql, fuelId);
        return result > 0;
    }
}
