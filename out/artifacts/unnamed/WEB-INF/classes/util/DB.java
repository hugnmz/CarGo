package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Properties;

public class DB {

    private static final Properties p = new Properties();

    static {
        // 1) Load db.properties từ classpath
        try (InputStream in = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties not found on classpath!");
            }
            p.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load db.properties", e);
        }

        // 2) Nạp driver JDBC SQL Server (ép nạp để chắc chắn)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("[DB] SQLServerDriver loaded.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLServerDriver NOT found on classpath", e);
        }
    }

    public static Connection get() throws Exception {
        return DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));
    }
}
