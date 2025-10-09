package dao.impl;

import dao.VehiclesDAO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.Vehicles;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 * VehiclesDAOImpl - Implementation cho quản lý xe thực tế
 *
 * MỤC ĐÍCH: - Xử lý các thao tác CRUD với bảng Vehicles - Quản lý thông tin xe
 * thực tế (biển số, trạng thái, vị trí) - Kiểm tra tính khả dụng của xe - Lấy
 * danh sách xe có sẵn để thuê
 */
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
        String sql = " SELECT v.*, c.name, l.city FROM dbo.Vehicles v LEFT JOIN dbo.Cars c ON c.carId = v.carId "
                + " LEFT JOIN dbo.Locations l ON l.locationId = v.locationId WHERE v.carId = ?";
        
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
}
