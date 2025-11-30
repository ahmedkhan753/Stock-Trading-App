package stockapp.dao;

import stockapp.models.Stock;
import stockapp.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Data Access Object for managing Stock market data.
 * Handles database operations related to the 'stocks' table.
 */
public class StockDAO {

    /**
     * @return A list of Stock objects with fetched or simulated change percent.
     */
    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT id, symbol, name, price, change_percent FROM stocks";

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
                double price = rs.getDouble("price");

                // Fetch the change_percent from the database.
                double changePercent = rs.getDouble("change_percent");

                // Simulate change if the database returns the default (0.0)
                if (changePercent == 0.0) {
                    // Simulate a random value between -5% and +5%
                    changePercent = (random.nextDouble() * 0.10) - 0.05;
                }
                stocks.add(new Stock(id, symbol, name, price, changePercent));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all stocks from database: " + e.getMessage());
            e.printStackTrace();
        }
        return stocks;
    }
}