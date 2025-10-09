
package util.reflection;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Lớp tiện ích dùng Reflection để map tự động giữa các đối tượng (DTO, Model)
 * và từ ResultSet sang Model.
 */
public class ReflectionMapper {
    /**
     * Map từ đối tượng nguồn (src) sang đối tượng đích (dest)
     * Nếu tên field giống nhau và kiểu dữ liệu tương thích → tự động gán.
     */
    public static <S, D> D map(S src, Class<D> destClass) {
        if (src == null) return null;

        try {
            D dest = destClass.getDeclaredConstructor().newInstance();

            Field[] srcFields = src.getClass().getDeclaredFields();
            Field[] destFields = destClass.getDeclaredFields();

            for (Field srcField : srcFields) {
                srcField.setAccessible(true);
                Object value = srcField.get(src);

                for (Field destField : destFields) {
                    if (destField.getName().equalsIgnoreCase(srcField.getName())) {
                        destField.setAccessible(true);
                        destField.set(dest, value);
                        break;
                    }
                }
            }

            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Map từ ResultSet sang đối tượng Model
     * Tên cột trong bảng phải trùng với tên field trong model (hoặc trùng một phần).
     */
    public static <T> T mapResultSet(ResultSet rs, Class<T> clazz) throws SQLException {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object value = rs.getObject(i);

                try {
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(instance, value);
                } catch (NoSuchFieldException ignore) {
                    // bỏ qua nếu model không có field này
                }
            }

            return instance;
        } catch (Exception e) {
            throw new SQLException("Lỗi khi map ResultSet sang " + clazz.getSimpleName(), e);
        }
    }
}
