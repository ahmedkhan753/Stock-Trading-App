package stockapp.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing the connection to the Oracle 23ai database.
 * This class handles the JDBC driver loading and connection lifecycle.
 */
public class DatabaseConnection {

    // --- 1. JDBC Driver Class ---
    // The Oracle driver class name
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

    // --- 2. Connection Details (Customize these for your Oracle 23ai setup) ---

    // Standard URL format for Oracle 23ai/XE:
    // "jdbc:oracle:thin:@//<host>:<port>/<service_name>"
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
    // If you are using Oracle Free (23c), the service name might be 'FREE' or 'XEPDB1'. Check your DBeaver connection settings.

    private static final String USER = "system"; 
    private static final String PASS = "newpass123";

    /**
     * Attempts to establish a connection to the Oracle database.
     * @return A valid Connection object, or null if connection fails.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the JDBC driver (legacy step, but good practice)
            Class.forName(DRIVER);

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connection to Oracle successful!");
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Oracle JDBC Driver not found in the classpath!");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("ERROR: Database connection failed!");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Safely closes a database connection.
     * @param conn The Connection object to close.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                // Don't rethrow, just log the error
            }
        }
    }
}
