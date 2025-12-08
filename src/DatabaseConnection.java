package stockapp.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Standard utility class for managing the connection to the MySQL database.
 * This class provides a static method to obtain a database Connection object,
 * which is required by all DAO classes (like UserDAO).
 */
public class DatabaseConnection {

    // --- 1. JDBC Driver Class ---
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // --- 2. Connection Details (Set as final static constants) ---
    private static final String URL = "jdbc:mysql://localhost:3306/stockapp?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "12345678";
    /**
     * Attempts to establish a connection to the MySQL database.
     * @return A valid Connection object, or null if connection fails.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            // Establish the connection using the static credentials
            connection = DriverManager.getConnection(URL, USER, PASS);
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("FATAL ERROR: MySQL JDBC Driver not found. Check your classpath!");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("ERROR: Database connection failed.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}