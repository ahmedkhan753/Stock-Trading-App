package stockapp.src.services;

import stockapp.src.dao.PortfolioDAO;
import stockapp.src.dao.StockDAO;
import stockapp.src.models.Portfolio;
import stockapp.src.models.Stock;
import java.util.List;

/**
 * Business logic layer for portfolio operations.
 * Manages user stock portfolios and calculations.
 */
public class PortfolioService {
    
    private final PortfolioDAO portfolioDAO;
    private final StockDAO stockDAO;
    
    // Constructor
    public PortfolioService() {
        this.portfolioDAO = new PortfolioDAO();
        this.stockDAO = new StockDAO();
    }
    
    /**
     * Adds a stock to user's portfolio or updates existing entry.
     */
    public boolean addStockToPortfolio(int userID, int stockID, int quantity, double price) {
        // Validate inputs
        if (quantity <= 0 || price <= 0) {
            System.err.println("ERROR: Quantity and price must be positive");
            return false;
        }
        
        // Check if user already has this stock
        Portfolio existingPortfolio = portfolioDAO.getPortfolioByUserAndStock(userID, stockID);
        
        if (existingPortfolio != null) {
            // Update existing entry with new average price
            int totalQuantity = existingPortfolio.getQuantity() + quantity;
            double avgPrice = calculateAveragePrice(
                existingPortfolio.getQuantity(), 
                existingPortfolio.getAvgPrice(), 
                quantity, 
                price
            );
            return portfolioDAO.updatePortfolio(existingPortfolio.getId(), totalQuantity, avgPrice);
        } else {
            // Add new entry
            return portfolioDAO.addToPortfolio(userID, stockID, quantity, price);
        }
    }
    
    /**
     * Sells stock from user's portfolio.
     */
    public boolean sellStockFromPortfolio(int userID, int stockID, int quantityToSell) {
        // Validate inputs
        if (quantityToSell <= 0) {
            System.err.println("ERROR: Quantity to sell must be positive");
            return false;
        }
        
        // Get current portfolio
        Portfolio portfolio = portfolioDAO.getPortfolioByUserAndStock(userID, stockID);
        
        if (portfolio == null) {
            System.err.println("ERROR: User does not own this stock");
            return false;
        }
        
        int currentQuantity = portfolio.getQuantity();
        
        if (quantityToSell > currentQuantity) {
            System.err.println("ERROR: Insufficient shares to sell");
            return false;
        }
        
        if (quantityToSell == currentQuantity) {
            // Delete the entire entry
            return portfolioDAO.deletePortfolio(portfolio.getId());
        } else {
            // Update with reduced quantity
            int newQuantity = currentQuantity - quantityToSell;
            return portfolioDAO.updatePortfolio(portfolio.getId(), newQuantity, portfolio.getAvgPrice());
        }
    }
    
    /**
     * Retrieves user's complete portfolio.
     */
    public List<Portfolio> getUserPortfolio(int userID) {
        return portfolioDAO.getPortfolioByUser(userID);
    }
    
    /**
     * Calculates total portfolio value based on current stock prices.
     */
    public double calculatePortfolioValue(int userID) {
        List<Portfolio> portfolio = portfolioDAO.getPortfolioByUser(userID);
        List<Stock> allStocks = stockDAO.getAllStocks();
        
        double totalValue = 0;
        
        for (Portfolio p : portfolio) {
            // Find the stock in the market data
            Stock stock = allStocks.stream()
                .filter(s -> s.getId() == p.getStockID())
                .findFirst()
                .orElse(null);
            
            if (stock != null) {
                totalValue += stock.getPrice() * p.getQuantity();
            }
        }
        
        return totalValue;
    }
    
    /**
     * Calculates profit/loss for the entire portfolio.
     */
    public double calculatePortfolioProfitLoss(int userID) {
        List<Portfolio> portfolio = portfolioDAO.getPortfolioByUser(userID);
        List<Stock> allStocks = stockDAO.getAllStocks();
        
        double totalCost = 0;
        double totalValue = 0;
        
        for (Portfolio p : portfolio) {
            // Cost basis
            totalCost += p.getAvgPrice() * p.getQuantity();
            
            // Current value
            Stock stock = allStocks.stream()
                .filter(s -> s.getId() == p.getStockID())
                .findFirst()
                .orElse(null);
            
            if (stock != null) {
                totalValue += stock.getPrice() * p.getQuantity();
            }
        }
        
        return totalValue - totalCost;
    }
    
    /**
     * Helper method to calculate average price when adding to existing position.
     */
    private double calculateAveragePrice(int existingQty, double existingPrice, 
                                        int newQty, double newPrice) {
        return ((existingQty * existingPrice) + (newQty * newPrice)) / (existingQty + newQty);
    }
}
