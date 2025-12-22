package stockapp.src.dao;

import stockapp.src.models.Stock;
import stockapp.src.DatabaseConnection;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Data Access Object for managing Stock market data.
 * Handles database operations related to the 'stocks' table.
 */
@Repository
public class StockDAO {

    /**
     * @return A list of Stock objects with fetched or simulated change percent.
     */
    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT id, symbol, name, price, change_percent, is_suspended FROM stocks";

        // Generate a random seed based on the current day for stable simulation
        long seed = System.currentTimeMillis() / (1000 * 60 * 60 * 24);

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            Random random = new Random(seed);

            while (rs.next()) {
                int id = rs.getInt("id");
                String symbol = rs.getString("symbol");
                String name = rs.getString("name");
                BigDecimal price = rs.getBigDecimal("price");

                // Fetch the change_percent from the database.
                double changePercent = rs.getDouble("change_percent");

                // Simulate change if the database returns the default (0.0)
                if (changePercent == 0.0) {
                    // Simulate a random value between -5% and +5%
                    changePercent = (random.nextDouble() * 0.10) - 0.05;
                }

                boolean suspended = rs.getBoolean("is_suspended");

                stocks.add(new Stock(id, symbol, name, price, changePercent, suspended));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all stocks from database: " + e.getMessage());
            e.printStackTrace();
        }
        return stocks;
    }

    /**
     * Get a stock by ID
     */
    public Stock getStockById(int stockId) {
        String sql = "SELECT id, symbol, name, price, change_percent, is_suspended FROM stocks WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, stockId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String symbol = rs.getString("symbol");
                    String name = rs.getString("name");
                    BigDecimal price = rs.getBigDecimal("price");
                    double changePercent = rs.getDouble("change_percent");
                    boolean suspended = rs.getBoolean("is_suspended");

                    return new Stock(id, symbol, name, price, changePercent, suspended);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching stock by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update stock price and other details
     */
    public boolean updateStock(Stock stock) {
        String sql = "UPDATE stocks SET symbol = ?, name = ?, price = ?, change_percent = ?, is_suspended = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, stock.getSymbol());
            stmt.setString(2, stock.getName());
            stmt.setBigDecimal(3, stock.getPrice());
            stmt.setDouble(4, stock.getChangePercent());
            stmt.setBoolean(5, stock.isSuspended());
            stmt.setInt(6, stock.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if a stock is suspended
     */
    public boolean isStockSuspended(int stockId) {
        String sql = "SELECT is_suspended FROM stocks WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, stockId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_suspended");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking stock suspension: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get a stock by symbol
     */
    public Stock getStockBySymbol(String symbol) {
        String sql = "SELECT id, symbol, name, price, change_percent, is_suspended FROM stocks WHERE symbol = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, symbol);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String sym = rs.getString("symbol");
                    String name = rs.getString("name");
                    BigDecimal price = rs.getBigDecimal("price");
                    double changePercent = rs.getDouble("change_percent");
                    boolean suspended = rs.getBoolean("is_suspended");

                    return new Stock(id, sym, name, price, changePercent, suspended);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching stock by symbol: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}