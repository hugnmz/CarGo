/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import util.di.annotation.Column;
import util.di.annotation.Nested;

/**
 *
 * @author admin
 */
public class MapResultSet {

    public static <T> T mapResultSet(ResultSet rs, Class<T> clazz) throws SQLException {
        try {

            // tao instance tu clazz
            T instance = clazz.getDeclaredConstructor().newInstance();

            //duyet qua cac field cua class do
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);

                // lay column dc gan annotation
                Column column = f.getAnnotation(Column.class);
                // neu ko dc gan bo qua
                if (column == null) {
                    continue;
                }

                // lay ten tu column
                // ko co thi lay ten cua field
                String columnName = column.name().isEmpty() ? f.getName() : column.name();

                // chi set neu ResultSet thuc su co cot nay
                if (!columnExists(rs, columnName)) {
                    continue;
                }

                try {
                    Object obj = readValue(rs, columnName, f.getType());
                    f.set(instance, obj);
                } catch (Exception ignore) {
                    // bo qua cot ko dc set
                }

            }

            // cac field co @Nested( 1- 1) list thi chiu nhé ae
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getAnnotation(Column.class) != null) {
                    continue;
                }

                Nested nested = f.getAnnotation(Nested.class);
                if (nested == null) {
                    continue; // ko dc gan annotation
                }
                String prefix = nested.prefix(); // default la dung chi ten cot cua db
                Class<?> childType = f.getType();
                Object child = childType.getDeclaredConstructor().newInstance();
                boolean check = false;

                for (Field cf : childType.getDeclaredFields()) {
                    cf.setAccessible(true);

                    Column c = cf.getAnnotation(Column.class);
                    if (c == null) {
                        continue; // chi nap nhung field dc gan annotation
                    }

                    String base = c.name().isEmpty() ? cf.getName() : c.name();
                    String label = prefix.isEmpty() ? base : (prefix + "_" + base);

                    try {
                        Object val = readValue(rs, label, cf.getType());
                        cf.set(child, val);
                        check = true;  //da dc set it nhat 1 field
                    } catch (Exception ignore) {
                    }

                }

                // chi gan object neu thuc su da map it nhat 1 field
                if (check) {
                    f.set(instance, child);
                }

            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Mapping error: " + e.getMessage(), e);

        }

    }

    // ktra xem ResultSet có cột này ko 
    private static boolean columnExists(ResultSet rs, String label) {
        try {
            // Thử lấy giá trị từ column để kiểm tra xem có tồn tại không
            rs.getObject(label);
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }

    private static Object readValue(ResultSet rs, String label, Class<?> type) throws SQLException {
        if (type.isEnum()) {
            String s = rs.getString(label);
            return (s == null) ? null : Enum.valueOf((Class<Enum>) type, s);
        }
        if (type == LocalDate.class) {
            Date d = rs.getDate(label);
            return (d != null) ? d.toLocalDate() : null;
        }
        if (type == LocalDateTime.class) {
            Timestamp ts = rs.getTimestamp(label);
            return (ts != null) ? ts.toLocalDateTime() : null;
        }
        if (type == LocalTime.class) {
            Time t = rs.getTime(label);
            return (t != null) ? t.toLocalTime() : null;
        }
        if (type == byte[].class) {
            return rs.getBytes(label);
        }
        try {
            return rs.getObject(label, type);
        } catch (Exception ignore) {
            return rs.getObject(label);
        }
    }

}
