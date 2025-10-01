/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.InputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author admin
 */
public class DB {

    // chứa các key-value load từ db.properties lên
    private static final Properties p = new Properties();

    // đoạn code dc khởi tạo ngay khi project dc deploy
    static {

        // tìm file db.properties trong classpathkhi deploy
        try (InputStream inputStream = DB.class.getClassLoader().getResourceAsStream("db.properties")) {

            // nếu ko thấy file thì dừng web sớm
            if (inputStream == null) {
                throw new RuntimeException("db.prorerties not found!");
            }

            // load cả key và value vào trong db.properties vào p
            p.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // tạo 1 connection mới mỗi khi mn gọi DB.get()
    public static Connection get() throws Exception {
        return DriverManager.getConnection(p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));
    }
}
