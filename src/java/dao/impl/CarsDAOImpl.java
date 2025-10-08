package dao.impl;

import dao.CarsDAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import model.Cars;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

/**
 * CarsDAOImpl - Implementation cho quản lý thông tin xe
 * 
 * MỤC ĐÍCH:
 * - Xử lý các thao tác CRUD với bảng Cars
 * - Lấy danh sách xe để hiển thị trên trang chủ
 * - Tìm kiếm xe theo các tiêu chí khác nhau
 * - Quản lý thông tin chi tiết của từng loại xe
 */
@Repository
public class CarsDAOImpl implements CarsDAO {

    /**
     * LẤY TẤT CẢ XE
     * 
     * MỤC ĐÍCH:
     * - Lấy danh sách tất cả xe có trong hệ thống
     * - Dùng để hiển thị trên trang chủ
     * - Bao gồm thông tin xe và giá hiện tại
     * 
     * HOẠT ĐỘNG:
     * 1. Query database để lấy tất cả xe
     * 2. Join với bảng CarPrices để lấy giá hiện tại
     * 3. MapResultSet tự động mapping thành List<Cars>
     * 
     * @return List<Cars> - Danh sách tất cả xe
     */
    @Override
    public List<Cars> getAllCars() {
        String sql = "SELECT c.*, cp.dailyPrice, cp.depositAmount " +
                    "FROM dbo.Cars c " +
                    "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "WHERE cp.endDate IS NULL OR cp.endDate > GETDATE() " +
                    "ORDER BY c.carId";
        return JdbcTemplateUtil.query(sql, Cars.class);
    }

    /**
     * LẤY XE THEO ID
     * 
     * MỤC ĐÍCH:
     * - Lấy thông tin chi tiết của một xe cụ thể
     * - Dùng khi user click vào xe để xem chi tiết
     * 
     * @param carId - ID của xe
     * @return Optional<Cars> - Thông tin xe nếu tìm thấy
     */
    @Override
    public Optional<Cars> getCarById(Integer carId) {
        String sql = "SELECT c.*, cp.dailyPrice, cp.depositAmount " +
                    "FROM dbo.Cars c " +
                    "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "WHERE c.carId = ? AND (cp.endDate IS NULL OR cp.endDate > GETDATE())";
        Cars car = JdbcTemplateUtil.queryOne(sql, Cars.class, carId);
        return Optional.ofNullable(car);
    }

    /**
     * THÊM XE MỚI
     * 
     * MỤC ĐÍCH:
     * - Thêm một loại xe mới vào hệ thống
     * - Dùng cho admin quản lý xe
     * 
     * @param car - Object Cars chứa thông tin xe mới
     * @return boolean - true nếu thêm thành công
     */
    @Override
    public boolean addCar(Cars car) {
        String sql = "INSERT INTO dbo.Cars(name, year, description, image, categoryId, fuelId, seatingId) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql,
                car.getName(),
                car.getYear(),
                car.getDescription(),
                car.getImage(),
                car.getCategoryId(),
                car.getFuelId(),
                car.getSeatingId());
        return result > 0;
    }

    /**
     * CẬP NHẬT THÔNG TIN XE
     * 
     * MỤC ĐÍCH:
     * - Cập nhật thông tin của một xe đã có
     * - Dùng khi admin chỉnh sửa thông tin xe
     * 
     * @param car - Object Cars với thông tin mới
     * @return boolean - true nếu cập nhật thành công
     */
    @Override
    public boolean updateCar(Cars car) {
        String sql = "UPDATE dbo.Cars SET name = ?, year = ?, description = ?, image = ?, " +
                    "categoryId = ?, fuelId = ?, seatingId = ? WHERE carId = ?";
        int result = JdbcTemplateUtil.update(sql,
                car.getName(),
                car.getYear(),
                car.getDescription(),
                car.getImage(),
                car.getCategoryId(),
                car.getFuelId(),
                car.getSeatingId(),
                car.getCarId());
        return result > 0;
    }

    /**
     * XÓA XE
     * 
     * MỤC ĐÍCH:
     * - Xóa một xe khỏi hệ thống
     * - Dùng khi xe không còn được sử dụng
     * 
     * @param carId - ID của xe cần xóa
     * @return boolean - true nếu xóa thành công
     */
    @Override
    public boolean deleteCar(Integer carId) {
        String sql = "DELETE FROM dbo.Cars WHERE carId = ?";
        int result = JdbcTemplateUtil.update(sql, carId);
        return result > 0;
    }

    /**
     * LẤY XE THEO DANH MỤC
     * 
     * MỤC ĐÍCH:
     * - Lọc xe theo loại (sedan, SUV, hatchback, etc.)
     * - Dùng cho tính năng filter trên trang web
     * 
     * @param categoryId - ID của danh mục
     * @return List<Cars> - Danh sách xe thuộc danh mục
     */
    @Override
    public List<Cars> getCarByCategory(Integer categoryId) {
        String sql = "SELECT c.*, cp.dailyPrice, cp.depositAmount " +
                    "FROM dbo.Cars c " +
                    "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "WHERE c.categoryId = ? AND (cp.endDate IS NULL OR cp.endDate > GETDATE()) " +
                    "ORDER BY c.carId";
        return JdbcTemplateUtil.query(sql, Cars.class, categoryId);
    }

    /**
     * LẤY XE THEO LOẠI NHIÊN LIỆU
     * 
     * MỤC ĐÍCH:
     * - Lọc xe theo loại nhiên liệu (xăng, diesel, hybrid, electric)
     * - Dùng cho tính năng filter
     * 
     * @param fuelId - ID của loại nhiên liệu
     * @return List<Cars> - Danh sách xe theo nhiên liệu
     */
    @Override
    public List<Cars> getCarByFuel(Integer fuelId) {
        String sql = "SELECT c.*, cp.dailyPrice, cp.depositAmount " +
                    "FROM dbo.Cars c " +
                    "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "WHERE c.fuelId = ? AND (cp.endDate IS NULL OR cp.endDate > GETDATE()) " +
                    "ORDER BY c.carId";
        return JdbcTemplateUtil.query(sql, Cars.class, fuelId);
    }

    /**
     * LẤY XE THEO SỐ CHỖ NGỒI
     * 
     * MỤC ĐÍCH:
     * - Lọc xe theo số chỗ ngồi (4 chỗ, 5 chỗ, 7 chỗ, etc.)
     * - Dùng cho tính năng filter
     * 
     * @param seatingId - ID của số chỗ ngồi
     * @return List<Cars> - Danh sách xe theo số chỗ
     */
    @Override
    public List<Cars> getCarBySeating(Integer seatingId) {
        String sql = "SELECT c.*, cp.dailyPrice, cp.depositAmount " +
                    "FROM dbo.Cars c " +
                    "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "WHERE c.seatingId = ? AND (cp.endDate IS NULL OR cp.endDate > GETDATE()) " +
                    "ORDER BY c.carId";
        return JdbcTemplateUtil.query(sql, Cars.class, seatingId);
    }

    /**
     * LẤY XE THEO ĐỊA ĐIỂM
     * 
     * MỤC ĐÍCH:
     * - Lọc xe theo địa điểm có sẵn
     * - Dùng cho tính năng tìm xe gần vị trí
     * 
     * @param locationId - ID của địa điểm
     * @return List<Cars> - Danh sách xe tại địa điểm
     */
    @Override
    public List<Cars> getCarByLocation(Integer locationId) {
        String sql = "SELECT DISTINCT c.*, cp.dailyPrice, cp.depositAmount " +
                    "FROM dbo.Cars c " +
                    "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "INNER JOIN dbo.Vehicles v ON c.carId = v.carId " +
                    "WHERE v.locationId = ? AND (cp.endDate IS NULL OR cp.endDate > GETDATE()) " +
                    "ORDER BY c.carId";
        return JdbcTemplateUtil.query(sql, Cars.class, locationId);
    }

    /**
     * TÌM KIẾM XE THEO TỪ KHÓA
     * 
     * MỤC ĐÍCH:
     * - Tìm xe theo tên, mô tả
     * - Dùng cho tính năng search trên trang web
     * 
     * @param keyword - Từ khóa tìm kiếm
     * @return List<Cars> - Danh sách xe phù hợp
     */
    @Override
    public List<Cars> searchCars(String keyword) {
        String sql = "SELECT c.*, cp.dailyPrice, cp.depositAmount " +
                    "FROM dbo.Cars c " +
                    "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "WHERE (c.name LIKE ? OR c.description LIKE ?) " +
                    "AND (cp.endDate IS NULL OR cp.endDate > GETDATE()) " +
                    "ORDER BY c.carId";
        String searchPattern = "%" + keyword + "%";
        return JdbcTemplateUtil.query(sql, Cars.class, searchPattern, searchPattern);
    }

    /**
     * LẤY XE THEO KHOẢNG GIÁ
     * 
     * MỤC ĐÍCH:
     * - Lọc xe theo khoảng giá thuê
     * - Dùng cho tính năng filter giá
     * 
     * @param minPrice - Giá tối thiểu
     * @param maxPrice - Giá tối đa
     * @return List<Cars> - Danh sách xe trong khoảng giá
     */
    @Override
    public List<Cars> getCarWithCurrentPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT c.*, cp.dailyPrice, cp.depositAmount " +
                    "FROM dbo.Cars c " +
                    "LEFT JOIN dbo.CarPrices cp ON c.carId = cp.carId " +
                    "WHERE cp.dailyPrice BETWEEN ? AND ? " +
                    "AND (cp.endDate IS NULL OR cp.endDate > GETDATE()) " +
                    "ORDER BY cp.dailyPrice";
        return JdbcTemplateUtil.query(sql, Cars.class, minPrice, maxPrice);
    }
}
