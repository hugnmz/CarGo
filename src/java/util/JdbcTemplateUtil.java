package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Location;

// class xu ly cac cau lenh sql
// trong dao chi can viet cau sql roi nhet tham so placeholder vao trong may ham nay
// thieu cai nao viet them vao

public class JdbcTemplateUtil {

    // thuc hien cau lenh update hay delete va tra ve so hang da bi thay doi
    public static int update(String sql, Object... param) {
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // bind cac tham so vao placeholder trong sql theo thu tu tu 1
            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }

            // thuc hien cau lenh
            return ps.executeUpdate();

        } catch (Exception e) {
            // log loi neu co
            throw new RuntimeException("error.system", e);
        }
    }

    // thuc hien cac cau lenh select va tra ve list entity
    public static <T> List<T> query(String sql, Class<T> type, Object... param) {
        List<T> result = new ArrayList<>();
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // set cac param vao placeholder trong sql
            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }

            // lay ket qua sau khi thuc hien cau lenh select
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(MapResultSet.mapResultSet(rs, type));
                }

            }
        } catch (Exception e) {
            // log loi neu co
            throw new RuntimeException("error.system", e);
        }

        return result;
    }

    // thuc hien cac cau lenh select va tra ve 1 dong duy nhat (dong dau)
    public static <T> T queryOne(String sql, Class<T> type, Object... params) {
        List<T> list = query(sql, type, params);
        return list.isEmpty() ? null : list.get(0); // lay phan tu dau tien
    }

    // thuc hien insert va tra ve key vua duoc insert
    public static int insertAndReturnKey(String sql, Object... param) {
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }

            ps.executeUpdate();
            // lay key vua tao
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            // log loi neu co
            throw new RuntimeException("error.system", e);
        }
    }

    // thuc hien select count(*) va tra ve 1 so nguyen
    public static int count(String sql, Object... params) {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            // bind tham so
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0; 
            }
        } catch (Exception e) {
            // log loi neu co
            throw new RuntimeException("error.system", e);
        }
    }
  
    // sau to viet them
//    public static <T> T inTransaction(SQLFunction<Connection, T> work) { ... }

     //format dieu kien query search car
    public static String formatConditionSearchCar(Integer locationId, String name, Integer categoryId, Double price) {
        StringBuilder condition = new StringBuilder(" WHERE 1=1 ");

        if (locationId != null) {
            condition.append(" AND")
                    .append(" EXISTS (SELECT 1 FROM dbo.Vehicles v WHERE v.carId = c.carId AND v.locationId = ")
                    .append(locationId)
                    .append(")");
        }

        if (name != null && !name.trim().isEmpty()) {
            // thêm dấu nháy đơn vì là chuỗi
            condition.append(" AND LOWER(c.name) LIKE LOWER('%")
                    .append(name.trim())
                    .append("%')");
        }

        if (categoryId != null) {
            condition.append(" AND c.categoryId = ").append(categoryId);
        }

        if (price != null && price > 0) {
            condition.append(" AND cp.dailyPrice <= ").append(price);
        }

        return condition.toString();
    
    }


}
