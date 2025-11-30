package stockapp.services;

import stockapp.dao.StockDAO;
import stockapp.models.Stock;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic layer for stock operations.
 * Manages market data and stock-related operations.
 */
public class StockService {
    
    private final StockDAO stockDAO;
    
    // Constructor
    public StockService() {
        this.stockDAO = new StockDAO();
    }
    
    /**
     * Retrieves all stocks from the market.
     */
    public List<Stock> getAllStocks() {
        return stockDAO.getAllStocks();
    }
    
    /**
     * Searches for stocks by ticker symbol (case-insensitive).
     */
    public List<Stock> searchStocksByTicker(String ticker) {
        if (ticker == null || ticker.trim().isEmpty()) {
            return getAllStocks();
        }
        
        String searchTerm = ticker.toLowerCase().trim();
        return stockDAO.getAllStocks().stream()
            .filter(stock -> stock.getTicker().toLowerCase().contains(searchTerm))
            .collect(Collectors.toList());
    }
    
    /**
     * Searches for stocks by name (case-insensitive).
     */
    public List<Stock> searchStocksByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllStocks();
        }
        
        String searchTerm = name.toLowerCase().trim();
        return stockDAO.getAllStocks().stream()
            .filter(stock -> stock.getName().toLowerCase().contains(searchTerm))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets a specific stock by ID.
     */
    public Stock getStockById(int stockID) {
        return stockDAO.getAllStocks().stream()
            .filter(stock -> stock.getId() == stockID)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets a specific stock by ticker.
     */
    public Stock getStockByTicker(String ticker) {
        if (ticker == null || ticker.trim().isEmpty()) {
            return null;
        }
        
        return stockDAO.getAllStocks().stream()
            .filter(stock -> stock.getTicker().equalsIgnoreCase(ticker))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets stocks sorted by price (ascending).
     */
    public List<Stock> getStocksSortedByPrice() {
        return stockDAO.getAllStocks().stream()
            .sorted((s1, s2) -> Double.compare(s1.getPrice(), s2.getPrice()))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets stocks sorted by price change percentage (descending).
     */
    public List<Stock> getStocksSortedByChange() {
        return stockDAO.getAllStocks().stream()
            .sorted((s1, s2) -> Double.compare(s2.getChangePercent(), s1.getChangePercent()))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets top performers (gainers) based on change percent.
     */
    public List<Stock> getTopGainers(int limit) {
        return getStocksSortedByChange().stream()
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Gets worst performers (losers) based on change percent.
     */
    public List<Stock> getTopLosers(int limit) {
        return stockDAO.getAllStocks().stream()
            .sorted((s1, s2) -> Double.compare(s1.getChangePercent(), s2.getChangePercent()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Validates if a user can buy shares at given price.
     */
    public boolean canAffordStock(double walletBalance, int quantity, double stockPrice) {
        return walletBalance >= (quantity * stockPrice);
    }
    
    /**
     * Calculates transaction cost.
     */
    public double calculateTransactionCost(int quantity, double stockPrice) {
        return quantity * stockPrice;
    }
}
