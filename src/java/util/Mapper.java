/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import util.di.annotation.Component;

/**
 *
 * @author admin
 */
@Component
public class Mapper {
    public static <S, D> D map(S src, Class<D> destClass) {
        if (src == null) return null;
        try {
            D dest = destClass.getDeclaredConstructor().newInstance();

            Map<String, Field> destMap = new HashMap<>();
            for (Field df : destClass.getDeclaredFields()) {
                df.setAccessible(true);
                destMap.put(df.getName().toLowerCase(Locale.ROOT), df);
            }

            for (Field sf : src.getClass().getDeclaredFields()) {
                sf.setAccessible(true);
                Field df = destMap.get(sf.getName().toLowerCase(Locale.ROOT));
                if (df == null) continue;

                Object v = sf.get(src);
                if (v == null) { df.set(dest, null); continue; }

                Class<?> t = df.getType();

                // nhung type đặc biệt
                if (t == BigDecimal.class) {
                    if (v instanceof BigDecimal) df.set(dest, v);
                    else if (v instanceof Number) df.set(dest, BigDecimal.valueOf(((Number) v).doubleValue()));
                    else if (v instanceof CharSequence) df.set(dest, new BigDecimal(v.toString().trim()));
                    else df.set(dest, v); // let it fail if truly incompatible
                    continue;
                }
                if (t == LocalDate.class) {
                    if (v instanceof LocalDate) df.set(dest, v);
                    else if (v instanceof LocalDateTime) df.set(dest, ((LocalDateTime) v).toLocalDate());
                    else if (v instanceof CharSequence) df.set(dest, LocalDate.parse(v.toString().trim()));
                    else df.set(dest, v);
                    continue;
                }
                if (t == LocalDateTime.class) {
                    if (v instanceof LocalDateTime) df.set(dest, v);
                    else if (v instanceof LocalDate) df.set(dest, ((LocalDate) v).atStartOfDay());
                    else if (v instanceof CharSequence) df.set(dest, LocalDateTime.parse(v.toString().trim()));
                    else df.set(dest, v);
                    continue;
                }

                df.set(dest, v);
            }
            return dest;
        } catch (Exception e) {
            throw new RuntimeException("map failed: " + e.getMessage(), e);
        }
    }
}
