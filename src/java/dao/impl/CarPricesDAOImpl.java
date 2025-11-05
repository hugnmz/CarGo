package dao.impl;

import dao.CarPricesDAO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import model.CarPrices;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 * CarPricesDAOImpl - Implementation cho quan ly gia xe
 */
@Repository
public class CarPricesDAOImpl implements CarPricesDAO {

    @Override
    public List<CarPrices> getAllCarPrices() {
        String sql = "SELECT * FROM dbo.CarPrices ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, CarPrices.class);
    }

    @Override
    public Optional<CarPrices> getCurrentPriceByCar(Integer carId) {
        String sql = "SELECT * FROM dbo.CarPrices WHERE carId = ? AND endDate IS NULL ORDER BY createAt DESC";
        CarPrices price = JdbcTemplateUtil.queryOne(sql, CarPrices.class, carId);
        return Optional.ofNullable(price);
    }

    @Override
    public List<CarPrices> getPricesByCar(Integer carId) {
        String sql = "SELECT * FROM dbo.CarPrices WHERE carId = ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, CarPrices.class, carId);
    }

    @Override
    public boolean addCarPrice(CarPrices carPrice) {
        String sql = "INSERT INTO dbo.CarPrices(carId, dailyPrice, depositAmount, startDate, endDate, createAt)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        int result = JdbcTemplateUtil.update(sql,
                carPrice.getCarId(),
                carPrice.getDailyPrice(),
                carPrice.getDepositAmount(),
                carPrice.getStartDate(),
                carPrice.getEndDate(),
                carPrice.getCreateAt()
        );

        return result > 0;
    }

    @Override
    public boolean updateCarPrice(CarPrices carPrice) {
        String sql = "UPDATE dbo.CarPrices SET carId = ?, dailyPrice = ?, depositAmount = ?, startDate = ?, endDate = ? WHERE priceId = ?";

        int result = JdbcTemplateUtil.update(sql,
                carPrice.getCarId(),
                carPrice.getDailyPrice(),
                carPrice.getDepositAmount(),
                carPrice.getStartDate(),
                carPrice.getEndDate(),
                carPrice.getPriceId()
        );

        return result > 0;
    }

    @Override
    public boolean deleteCarPrice(Integer priceId) {
        String sql = "DELETE FROM dbo.CarPrices WHERE priceId = ?";
        int result = JdbcTemplateUtil.update(sql, priceId);
        return result > 0;
    }

    @Override
    public boolean deleteCarPricesByCarId(Integer carId) {
        String sql = "DELETE FROM dbo.CarPrices WHERE carId = ?";
        int result = JdbcTemplateUtil.update(sql, carId);
        return result >= 0;
    }

    @Override
    public boolean endCurrentPrice(Integer carId) {
        String sql = "UPDATE dbo.CarPrices SET endDate = GETDATE() WHERE carId = ? AND endDate IS NULL";
        int result = JdbcTemplateUtil.update(sql, carId);
        return result > 0;
    }

    @Override
    public Optional<BigDecimal> getCurrentDailyPrice(Integer carId) {
        String sql = "SELECT dailyPrice FROM dbo.CarPrices WHERE carId = ? AND endDate IS NULL ORDER BY createAt DESC";
        CarPrices price = JdbcTemplateUtil.queryOne(sql, CarPrices.class, carId);
        return price != null ? Optional.of(price.getDailyPrice()) : Optional.empty();
    }

    @Override
    public Optional<BigDecimal> getCurrentDepositAmount(Integer carId) {
        String sql = "SELECT depositAmount FROM dbo.CarPrices WHERE carId = ? AND endDate IS NULL ORDER BY createAt DESC";
        CarPrices price = JdbcTemplateUtil.queryOne(sql, CarPrices.class, carId);
        return price != null ? Optional.of(price.getDepositAmount()) : Optional.empty();
    }

}
