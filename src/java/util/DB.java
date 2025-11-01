package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Properties;

// class quan ly ket noi database
public class DB {

    // properties chua cau hinh database
    private static final Properties p = new Properties();

    // khoi tao cau hinh database
    static {
        // 1) load db.properties tu classpath
        try (InputStream in = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties not found on classpath!");
            }
            p.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load db.properties", e);
        }

        // 2) nap driver jdbc sql server (ep nap de chac chan)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLServerDriver NOT found on classpath", e);
        }
    }

    // lay ket noi database
    public static Connection get() throws Exception {
        return DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));
    }
}
