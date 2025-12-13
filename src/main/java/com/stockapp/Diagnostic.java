package com.stockapp;

import com.stockapp.dao.StockDAO;
import com.stockapp.models.Stock;
import java.sql.Connection;
import java.util.List;

public class Diagnostic {
    public static void main(String[] args) {
        System.out.println("=== STOCK APP DIAGNOSTIC TOOL ===");

        // 1. Test Database Connection
        System.out.println("[1] Testing Database Connection...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("    SUCCESS: Connected to Database.");
            } else {
                System.out.println("    FAILURE: Connection is NULL. Check JDBC settings.");
                return;
            }
        } catch (Exception e) {
            System.out.println("    FAILURE: Exception during connection: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 2. Test StockDAO
        System.out.println("[2] Testing StockDAO...");
        StockDAO stockDAO = new StockDAO();
        try {
            List<Stock> stocks = stockDAO.getAllStocks();
            System.out.println("    DAO Query Executed.");
            System.out.println("    Stock Count: " + stocks.size());

            if (stocks.isEmpty()) {
                System.out.println("    WARNING: Stock list is empty! Check database content.");
            } else {
                System.out.println("    Printing first 5 stocks:");
                for (int i = 0; i < Math.min(5, stocks.size()); i++) {
                    Stock s = stocks.get(i);
                    System.out.printf("    - %s (%s): $%.2f (Change: %.2f%%)%n",
                            s.getTicker(), s.getName(), s.getPrice(), s.getChangePercent());
                }
            }
        } catch (Exception e) {
            System.out.println("    FAILURE: StockDAO threw exception: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== DIAGNOSTIC COMPLETE ===");
    }
}
