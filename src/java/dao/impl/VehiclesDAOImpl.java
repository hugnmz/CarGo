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
 * MỤC ĐÍCH:
 * - Xử lý các thao tác CRUD với bảng Vehicles
 * - Quản lý thông tin xe thực tế (biển số, trạng thái, vị trí)
 * - Kiểm tra tính khả dụng của xe
 * - Lấy danh sách xe có sẵn để thuê
 */
@Repository
public class VehiclesDAOImpl implements VehiclesDAO {

    /**
     * LẤY TẤT CẢ XE THỰC TẾ
     * 
     * MỤC ĐÍCH:
     * - Lấy danh sách tất cả xe thực tế trong hệ thống
     * - Bao gồm thông tin xe, vị trí, trạng thái
     * 
     * HOẠT ĐỘNG:
     * 1. Query database với JOIN để lấy thông tin đầy đủ
     * 2. MapResultSet mapping thành List<Vehicles>
     * 3. Mỗi Vehicles object chứa thông tin Cars và Locations
     * 
     * @return List<Vehicles> - Danh sách tất cả xe thực tế
     */
    @Override
    public List<Vehicles> getAllVehicles() {
        String sql = "SELECT v.*, c.name as carName, c.year, c.description, c.image, " +
                    "c.categoryId, c.fuelId, c.seatingId, " +
                    "l.city, l.address " +
                    "FROM dbo.Vehicles v " +
                    "LEFT JOIN dbo.Cars c ON v.carId = c.carId " +
                    "LEFT JOIN dbo.Locations l ON v.locationId = l.locationId " +
                    "WHERE v.isActive = 1 " +
                    "ORDER BY v.vehicleId";
        return JdbcTemplateUtil.query(sql, Vehicles.class);
    }

    /**
     * LẤY XE THEO ID
     * 
     * MỤC ĐÍCH:
     * - Lấy thông tin chi tiết của một xe thực tế
     * - Dùng khi user chọn xe để thuê
     * 
     * @param vehicleId - ID của xe thực tế
     * @return Optional<Vehicles> - Thông tin xe nếu tìm thấy
     */
    @Override
    public Optional<Vehicles> getVehicleById(Integer vehicleId) {
        String sql = "SELECT v.*, c.name as carName, c.year, c.description, c.image, " +
                    "c.categoryId, c.fuelId, c.seatingId, " +
                    "l.city, l.address " +
                    "FROM dbo.Vehicles v " +
                    "LEFT JOIN dbo.Cars c ON v.carId = c.carId " +
                    "LEFT JOIN dbo.Locations l ON v.locationId = l.locationId " +
                    "WHERE v.vehicleId = ? AND v.isActive = 1";
        Vehicles vehicle = JdbcTemplateUtil.queryOne(sql, Vehicles.class, vehicleId);
        return Optional.ofNullable(vehicle);
    }

    /**
     * LẤY XE THEO BIỂN SỐ
     * 
     * MỤC ĐÍCH:
     * - Tìm xe theo biển số
     * - Dùng để kiểm tra xe có tồn tại không
     * 
     * @param plateNumber - Biển số xe
     * @return Optional<Vehicles> - Thông tin xe nếu tìm thấy
     */
    @Override
    public Optional<Vehicles> getVehicleyPlateNumber(String plateNumber) {
        String sql = "SELECT v.*, c.name as carName, c.year, c.description, c.image, " +
                    "c.categoryId, c.fuelId, c.seatingId, " +
                    "l.city, l.address " +
                    "FROM dbo.Vehicles v " +
                    "LEFT JOIN dbo.Cars c ON v.carId = c.carId " +
                    "LEFT JOIN dbo.Locations l ON v.locationId = l.locationId " +
                    "WHERE v.plateNumber = ? AND v.isActive = 1";
        Vehicles vehicle = JdbcTemplateUtil.queryOne(sql, Vehicles.class, plateNumber);
        return Optional.ofNullable(vehicle);
    }

    /**
     * THÊM XE THỰC TẾ MỚI
     * 
     * MỤC ĐÍCH:
     * - Thêm một xe thực tế mới vào hệ thống
     * - Dùng khi có xe mới được đưa vào fleet
     * 
     * @param vehicle - Object Vehicles chứa thông tin xe mới
     * @return boolean - true nếu thêm thành công
     */
    @Override
    public boolean addVehicle(Vehicles vehicle) {
        String sql = "INSERT INTO dbo.Vehicles(carId, plateNumber, locationId, isActive) " +
                    "VALUES (?, ?, ?, ?)";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql,
                vehicle.getCarId(),
                vehicle.getPlateNumber(),
                vehicle.getLocationId(),
                vehicle.getIsActive());
        return result > 0;
    }

    /**
     * CẬP NHẬT THÔNG TIN XE
     * 
     * MỤC ĐÍCH:
     * - Cập nhật thông tin của một xe thực tế
     * - Dùng khi thay đổi vị trí, trạng thái xe
     * 
     * @param vehicle - Object Vehicles với thông tin mới
     * @return boolean - true nếu cập nhật thành công
     */
    @Override
    public boolean updateVehicle(Vehicles vehicle) {
        String sql = "UPDATE dbo.Vehicles SET carId = ?, plateNumber = ?, locationId = ?, isActive = ? " +
                    "WHERE vehicleId = ?";
        int result = JdbcTemplateUtil.update(sql,
                vehicle.getCarId(),
                vehicle.getPlateNumber(),
                vehicle.getLocationId(),
                vehicle.getIsActive(),
                vehicle.getVehicleId());
        return result > 0;
    }

    /**
     * XÓA XE THỰC TẾ
     * 
     * MỤC ĐÍCH:
     * - Xóa một xe thực tế khỏi hệ thống
     * - Dùng khi xe không còn hoạt động
     * 
     * @param vehicleId - ID của xe cần xóa
     * @return boolean - true nếu xóa thành công
     */
    @Override
    public boolean deleteVehicle(Integer vehicleId) {
        String sql = "DELETE FROM dbo.Vehicles WHERE vehicleId = ?";
        int result = JdbcTemplateUtil.update(sql, vehicleId);
        return result > 0;
    }

    /**
     * LẤY XE THEO LOẠI XE
     * 
     * MỤC ĐÍCH:
     * - Lấy tất cả xe thực tế của một loại xe
     * - Dùng để hiển thị các xe có sẵn của một model
     * 
     * @param carId - ID của loại xe
     * @return List<Vehicles> - Danh sách xe thực tế của loại xe
     */
    @Override
    public List<Vehicles> getVehiclesByCar(Integer carId) {
        String sql = "SELECT v.*, c.name as carName, c.year, c.description, c.image, " +
                    "c.categoryId, c.fuelId, c.seatingId, " +
                    "l.city, l.address " +
                    "FROM dbo.Vehicles v " +
                    "LEFT JOIN dbo.Cars c ON v.carId = c.carId " +
                    "LEFT JOIN dbo.Locations l ON v.locationId = l.locationId " +
                    "WHERE v.carId = ? AND v.isActive = 1 " +
                    "ORDER BY v.vehicleId";
        return JdbcTemplateUtil.query(sql, Vehicles.class, carId);
    }

    /**
     * LẤY XE CÓ SẴN THEO LOẠI XE VÀ THỜI GIAN
     * 
     * MỤC ĐÍCH:
     * - Lấy xe có sẵn để thuê trong khoảng thời gian cụ thể
     * - Kiểm tra xe không bị thuê trong thời gian đó
     * - Dùng khi user chọn xe để thuê
     * 
     * HOẠT ĐỘNG:
     * 1. Lấy xe của loại xe cụ thể
     * 2. Loại trừ xe đã được thuê trong khoảng thời gian
     * 3. Chỉ trả về xe đang active và có sẵn
     * 
     * @param carId - ID của loại xe
     * @param startDate - Ngày bắt đầu thuê
     * @param endDate - Ngày kết thúc thuê
     * @return List<Vehicles> - Danh sách xe có sẵn
     */
    @Override
    public List<Vehicles> getAvailableVehiclesByCar(Integer carId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT v.*, c.name as carName, c.year, c.description, c.image, " +
                    "c.categoryId, c.fuelId, c.seatingId, " +
                    "l.city, l.address " +
                    "FROM dbo.Vehicles v " +
                    "LEFT JOIN dbo.Cars c ON v.carId = c.carId " +
                    "LEFT JOIN dbo.Locations l ON v.locationId = l.locationId " +
                    "WHERE v.carId = ? AND v.isActive = 1 " +
                    "AND v.vehicleId NOT IN (" +
                    "    SELECT DISTINCT o.vehicleId FROM dbo.Orders o " +
                    "    WHERE o.vehicleId = v.vehicleId " +
                    "    AND ((o.rentStartDate <= ? AND o.rentEndDate >= ?) " +
                    "         OR (o.rentStartDate <= ? AND o.rentEndDate >= ?) " +
                    "         OR (o.rentStartDate >= ? AND o.rentEndDate <= ?))" +
                    ") " +
                    "ORDER BY v.vehicleId";
        return JdbcTemplateUtil.query(sql, Vehicles.class, carId, startDate, startDate, endDate, endDate, startDate, endDate);
    }
}
