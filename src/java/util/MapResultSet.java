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
                Object obj;

                try {

                    // neu la enum la lay gia tri cua enum
                    if (f.getType().isEnum()) {
                        String s = rs.getString(columnName);
                        obj = (s == null) ? null : Enum.valueOf((Class<Enum>) f.getType(), s);
                    } else {
                        try {

                            // de cho driver tu map
                            obj = rs.getObject(columnName, f.getType());
                        } catch (Exception e) {

                            // nhung th driver ko the tu map dc
                            if (f.getType() == LocalDate.class) {
                                Date d = rs.getDate(columnName);
                                obj = (d != null) ? d.toLocalDate() : null;
                            } else if (f.getType() == LocalDateTime.class) {
                                Timestamp ts = rs.getTimestamp(columnName);
                                obj = (ts != null) ? ts.toLocalDateTime() : null;
                            } else if (f.getType() == LocalTime.class) {
                                Time t = rs.getTime(columnName);
                                obj = (t != null) ? t.toLocalTime() : null;
                            } else if (f.getType() == byte[].class) {
                                obj = rs.getBytes(columnName);
                            } else {
                                obj = rs.getObject(columnName);
                            }
                        }

                        f.set(instance, obj);
                    }
                } catch (Exception ignore) {
                    // ko co thi bo qua
                }

            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Mapping error: " + e.getMessage(), e);

        }

    }

}
