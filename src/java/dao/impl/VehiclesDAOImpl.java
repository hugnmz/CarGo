package dao.impl;

import dao.VehiclesDAO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Vehicles;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class VehiclesDAOImpl implements VehiclesDAO {

    @Override
    public List<Vehicles> getAllVehicles() {
        String sql = "SELECT v.*, l.city, c.name, c.year FROM dbo.Vehicles v LEFT JOIN dbo.Locations l ON l.locationId = v.locationId LEFT JOIN dbo.Cars c ON c.carId = v.carId";
        return JdbcTemplateUtil.query(sql, Vehicles.class);
    }

    @Override
    public Optional<Vehicles> getVehicleById(Integer vehicleId) {
        String sql = "SELECT v.*, l.city, c.name, c.year FROM dbo.Vehicles v "
                + "	LEFT JOIN dbo.Locations l ON l.locationId = v.locationId LEFT JOIN dbo.Cars c ON c.carId = v.carId "
                + "	WHERE v.vehicleId = ?";

        Vehicles v = JdbcTemplateUtil.queryOne(sql, Vehicles.class, vehicleId);
        return Optional.ofNullable(v);
    }

    @Override
    public Optional<Vehicles> getVehicleyPlateNumber(String plateNumber) {
        String sql = "SELECT v.*, l.city, c.name, c.year FROM dbo.Vehicles v "
                + "LEFT JOIN dbo.Locations l ON l.locationId = v.locationId LEFT JOIN dbo.Cars c ON c.carId = v.carId "
                + "WHERE v.plateNumber = ?";

        Vehicles v = JdbcTemplateUtil.queryOne(sql, Vehicles.class, plateNumber);
        return Optional.ofNullable(v);
    }

    @Override
    public boolean addVehicle(Vehicles vehicle) {
        String sql = "INSERT INTO dbo.Vehicles(carId,plateNumber,isActive,locationId) VALUES (?,?,?,?)";

        int result = JdbcTemplateUtil.insertAndReturnKey(sql,
                vehicle.getCarId(),
                vehicle.getPlateNumber(),
                vehicle.getIsActive(),
                vehicle.getLocationId());
        return result > 0;
    }

    @Override
    public boolean updateVehicle(Vehicles vehicle) {
        String sql = "UPDATE dbo.Vehicles "
                + "SET carId = ?, plateNumber = ?, isActive = ?, locationId = ? "
                + "WHERE vehicleId = ?";

        int result = JdbcTemplateUtil.update(sql, vehicle.getCarId(),
                vehicle.getPlateNumber(),
                vehicle.getIsActive(),
                vehicle.getLocationId(),
                vehicle.getVehicleId());

        return result > 0;
    }

    @Override
    public boolean deleteVehicle(Integer vehicleId) {
        String sql = " DELETE FROM dbo.Vehicles WHERE vehicleId = ?";

        int result = JdbcTemplateUtil.update(sql, vehicleId);
        return result >= 0;
    }

    @Override
    public List<Vehicles> getVehiclesByCar(Integer CarId) {
        String sql = "SELECT "
                + "v.vehicleId, v.carId, v.plateNumber, v.isActive, v.locationId, "
                + "c.carId as c_carId, c.name as c_name, c.year as c_year, c.description as c_description, c.image as c_image, "
                + "c.categoryId as c_categoryId, c.fuelId as c_fuelId, c.seatingId as c_seatingId, "
                + "cat.categoryId as cat_categoryId, cat.categoryName as cat_categoryName, "
                + "f.fuelId as f_fuelId, f.fuelType as f_fuelType, "
                + "s.seatingId as s_seatingId, s.seatingType as s_seatingType, "
                + "l.locationId as l_locationId, l.city as l_city, l.address as l_address, "
                + "cp.priceId as cp_priceId, cp.dailyPrice as cp_dailyPrice, cp.depositAmount as cp_depositAmount, "
                + "cp.startDate as cp_startDate, cp.endDate as cp_endDate, cp.createAt as cp_createAt "
                + "FROM dbo.Vehicles v "
                + "INNER JOIN dbo.Cars c ON c.carId = v.carId "
                + "LEFT JOIN dbo.Categories cat ON c.categoryId = cat.categoryId "
                + "LEFT JOIN dbo.Fuels f ON c.fuelId = f.fuelId "
                + "LEFT JOIN dbo.Seatings s ON c.seatingId = s.seatingId "
                + "LEFT JOIN dbo.Locations l ON l.locationId = v.locationId "
                + "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId AND cp.endDate IS NULL "
                + "WHERE v.carId = ?";

        return JdbcTemplateUtil.query(sql, Vehicles.class, CarId);
    }

    @Override
    public List<Vehicles> getAvailableVehiclesByCar(Integer carId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT v.*, l.city, c.name, c.year FROM dbo.Vehicles v "
                + "LEFT JOIN dbo.Locations l ON l.locationId = v.locationId "
                + "LEFT JOIN dbo.Cars c ON c.carId = v.carId "
                + "WHERE v.carId = ? AND v.isActive = 1 "
                + "AND v.vehicleId NOT IN ("
                + "    SELECT DISTINCT cd.vehicleId FROM dbo.ContractDetails cd "
                + "    INNER JOIN dbo.Contracts ct ON ct.contractId = cd.contractId "
                + "    WHERE ct.status = 'ACTIVE' "
                + "    AND ("
                + "        (ct.startDate <= ? AND ct.endDate >= ?) OR "
                + "        (ct.startDate <= ? AND ct.endDate >= ?) OR "
                + "        (ct.startDate >= ? AND ct.endDate <= ?)"
                + "    )"
                + ")";

        return JdbcTemplateUtil.query(sql, Vehicles.class, carId, startDate, startDate, endDate, endDate, startDate, endDate);
    }

    @Override
    public boolean isVehicleAvailable(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM dbo.Vehicles v "
                + "WHERE v.vehicleId = ? AND v.isActive = 1 "
                + "AND v.vehicleId NOT IN ("
                + "    SELECT DISTINCT cd.vehicleId FROM dbo.ContractDetails cd "
                + "    INNER JOIN dbo.Contracts ct ON ct.contractId = cd.contractId "
                + "    WHERE ct.status = 'ACTIVE' "
                + "    AND ("
                + "        (ct.startDate <= ? AND ct.endDate >= ?) OR "
                + "        (ct.startDate <= ? AND ct.endDate >= ?) OR "
                + "        (ct.startDate >= ? AND ct.endDate <= ?)"
                + "    )"
                + ")";

        int count = JdbcTemplateUtil.count(sql, vehicleId, startDate, startDate, endDate, endDate, startDate, endDate);
        return count > 0;
    }
}
