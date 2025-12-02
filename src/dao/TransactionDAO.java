package stockapp.src.dao;

import stockapp.src.DatabaseConnection;
import stockapp.src.models.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Transaction entity.
 * Handles CRUD operations for buy/sell transactions.
 */
public class TransactionDAO {
    
    private static final String INSERT_TRANSACTION_SQL = 
        "INSERT INTO transactions (user_id, stock_id, quantity, price, timestamp, type) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_TRANSACTIONS_BY_USER_SQL = 
        "SELECT id, user_id stock_id, quantity, price, timestamp, type FROM transactions WHERE user_id = ? ORDER BY timestamp DESC";
    
    private static final String SELECT_ALL_TRANSACTIONS_SQL = 
        "SELECT id, user_id, stock_id, quantity, price, timestamp, type FROM transactions ORDER BY timestamp DESC";
    
    /**
     * Maps a ResultSet row to a Transaction object.
     */
    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userID = rs.getInt("userID");
        int stockID = rs.getInt("stockID");
        int quantity = rs.getInt("quantity");
        double price = rs.getDouble("price");
        Timestamp timestamp = rs.getTimestamp("timestamp");
        String type = rs.getString("type");
        
        return new Transaction(id, userID, stockID, quantity, price, timestamp, type);
    }
    
    /**
     * Records a new transaction (buy or sell).
     */
    public boolean recordTransaction(int userID, int stockID, int quantity, double price, String type) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_TRANSACTION_SQL)) {
            
            stmt.setInt(1, userID);
            stmt.setInt(2, stockID);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            stmt.setString(6, type.toLowerCase()); // "buy" or "sell"
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to record transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves all transactions for a specific user.
     */
    public List<Transaction> getTransactionsByUser(int userID) {
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TRANSACTIONS_BY_USER_SQL)) {
            
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to retrieve transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }
    
    /**
     * Retrieves all transactions in the system.
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TRANSACTIONS_SQL)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to retrieve all transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }
}
