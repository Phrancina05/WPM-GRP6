package nust.na.MonitoringSystem;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBConnection.java
 * Handles MySQL database connection for all servlets.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/farmtrack";
    private static final String USER     = "root";
    private static final String PASSWORD = "";  // XAMPP default is empty

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}