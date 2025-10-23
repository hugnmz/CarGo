package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DB {

    private static final Properties p = new Properties();

    static {
        // 1) Load file db.properties
        try (InputStream in = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties not found on classpath!");
            }
            p.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load db.properties", e);
        }

        // 2) Nap driver JDBC SQL Server
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("[DB] SQLServerDriver loaded.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLServerDriver NOT found on classpath", e);
        }
    }

    // Ham tra ve Connection
    public static Connection get() throws Exception {
        return DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));
    }

    // ===== Ham main de test ket noi =====
    public static void main(String[] args) {
        try (Connection conn = DB.get()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Ket noi database thanh cong!");
            } else {
                System.out.println("❌ Ket noi database that bai!");
            }
        } catch (Exception e) {
            System.out.println("❌ Loi khi ket noi database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
