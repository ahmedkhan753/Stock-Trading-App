package stockapp.src;

import java.sql.Connection;

/**
 * A temporary utility class to test the database connectivity 
 * before implementing the full application logic.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("--- Starting Oracle Connection Test ---");
        
        // 1. Call the static method from the DatabaseConnection utility class
        Connection conn = DatabaseConnection.getConnection();

        // 2. Check if the connection was successful
        if (conn != null) {
            System.out.println(" SUCCESS: Connection to Oracle is live!");
            System.out.println("   You are now ready to build your DAOs.");
            
            // 3. Safely close the connection after the test
            DatabaseConnection.closeConnection(conn);
            System.out.println("Connection closed.");
        } else {
            System.err.println(" FAILURE: Could not connect to the database.");
            System.err.println("   Please check the following:");
            System.err.println("   - Oracle TNS Listener is running (check command line).");
            System.err.println("   - The URL in DatabaseConnection.java (try changing /FREEPDB1 to /XE or /XEPDB1).");
            System.err.println("   - The ojdbc11.jar file is correctly added to your project's Referenced Libraries.");
        }
    }
}
