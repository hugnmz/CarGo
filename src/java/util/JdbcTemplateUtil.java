/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 *
 * @author admin
 */

// trong dao ae chỉ cần viết câu sql rồi nhét tham só placeholder vào trong mấy hàm này
// thiếu cái nào viết thêm vào

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
            e.printStackTrace();
            return 0;
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

            // lay ket qua sau khi thu hien cau lenh select
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(MapResultSet.mapResultSet(rs, type));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // thuc hien cac cau lenh select va tra ve 1 dong duy nhat(dong dau)
    public static <T> T queryOne(String sql, Class<T> type, Object... params) {
        List<T> list = query(sql, type, params);
        return list.isEmpty() ? null : list.get(0); // Lấy phần tử đầu tiên
    }

    // thuc hien insert va tra ve key vua dc insert
    public static int insertAndReturnKey(String sql, Object... param) {
        try (Connection conn = DB.get(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }

            ps.executeUpdate();
            //lay key vua tao
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //thu hien select count(*) va tra ve 1 so nguyen
    public static int count(String sql, Object... params) {
        try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            // Bind tham số
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
  
    // sau to viet them
//    public static <T> T inTransaction(SQLFunction<Connection, T> work) { ... }

}
