package com.stockapp.dao;

import com.stockapp.models.Stock;
import com.stockapp.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "SELECT id, symbol, name, price, change_pct FROM stocks";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("Database connection failed in getAllStocks.");
                return stocks;
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String symbol = rs.getString("symbol");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");

                    // User Request: Calculate P&L based on reference price of $200
                    double referencePrice = 200.0;
                    double changePercent = (price - referencePrice) / referencePrice;

                    stocks.add(new Stock(id, symbol, name, price, changePercent));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching stocks: " + e.getMessage());
            e.printStackTrace();
        }

        if (stocks.isEmpty()) {
            System.out.println("Stocks table is empty. seeding default data...");
            seedDefaultStocks();
            return getAllStocksRec();
        }

        return stocks;
    }

    private List<Stock> getAllStocksRec() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT id, symbol, name, price, change_pct FROM stocks";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                double price = rs.getDouble("price");
                double referencePrice = 200.0;
                double chg = (price - referencePrice) / referencePrice;

                stocks.add(new Stock(rs.getInt("id"), rs.getString("symbol"), rs.getString("name"),
                        price, chg));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    private void seedDefaultStocks() {
        String insertSQL = "INSERT INTO stocks (symbol, name, price, change_pct) VALUES (?, ?, ?, ?)";
        Object[][] defaults = {
                { "AAPL", "Apple Inc.", 150.00, 0.015 },
                { "GOOGL", "Alphabet Inc.", 2800.00, -0.005 },
                { "MSFT", "Microsoft Corp.", 300.00, 0.008 },
                { "TSLA", "Tesla Inc.", 750.00, 0.03 },
                { "AMZN", "Amazon.com Inc.", 3400.00, -0.012 }
        };

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            if (conn == null)
                return;

            for (Object[] stock : defaults) {
                stmt.setString(1, (String) stock[0]);
                stmt.setString(2, (String) stock[1]);
                stmt.setDouble(3, (Double) stock[2]);
                stmt.setDouble(4, (Double) stock[3]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("Seeded default stocks.");
        } catch (SQLException e) {
            System.err.println("Error planting seeds: " + e.getMessage());
        }
    }

    /**
     * Updates the price of a stock.
     */
    public boolean updatePrice(int stockID, double newPrice) {
        String sql = "UPDATE stocks SET price = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setInt(2, stockID);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock price: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}