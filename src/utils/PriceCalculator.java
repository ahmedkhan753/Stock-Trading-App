package stockapp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for price and financial calculations.
 * Provides methods for transaction costs, profit/loss, and other financial computations.
 */
public class PriceCalculator {
    
    // Precision for decimal calculations
    private static final int PRECISION = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    /**
     * Calculates total transaction cost (quantity * price).
     */
    public static double calculateTransactionCost(int quantity, double pricePerShare) {
        if (quantity < 0 || pricePerShare < 0) {
            throw new IllegalArgumentException("Quantity and price must be non-negative");
        }
        
        BigDecimal qty = new BigDecimal(quantity);
        BigDecimal price = new BigDecimal(pricePerShare);
        
        return qty.multiply(price)
            .setScale(PRECISION, ROUNDING_MODE)
            .doubleValue();
    }
    
    /**
     * Calculates profit or loss from a transaction.
     */
    public static double calculateProfitLoss(double buyPrice, double sellPrice, int quantity) {
        double buyCost = calculateTransactionCost(quantity, buyPrice);
        double sellRevenue = calculateTransactionCost(quantity, sellPrice);
        
        BigDecimal profit = new BigDecimal(sellRevenue - buyCost);
        return profit.setScale(PRECISION, ROUNDING_MODE).doubleValue();
    }
    
    /**
     * Calculates profit/loss percentage.
     */
    public static double calculateProfitLossPercentage(double buyPrice, double sellPrice) {
        if (buyPrice <= 0) {
            throw new IllegalArgumentException("Buy price must be positive");
        }
        
        double percentageChange = ((sellPrice - buyPrice) / buyPrice) * 100;
        BigDecimal percentage = new BigDecimal(percentageChange);
        
        return percentage.setScale(2, ROUNDING_MODE).doubleValue();
    }
    
    /**
     * Calculates average price from multiple purchases.
     */
    public static double calculateAveragePrice(double existingPrice, int existingQuantity, 
                                               double newPrice, int newQuantity) {
        if (existingQuantity < 0 || newQuantity < 0) {
            throw new IllegalArgumentException("Quantities must be non-negative");
        }
        
        if (existingQuantity + newQuantity == 0) {
            return 0.0;
        }
        
        BigDecimal existingCost = new BigDecimal(existingPrice).multiply(new BigDecimal(existingQuantity));
        BigDecimal newCost = new BigDecimal(newPrice).multiply(new BigDecimal(newQuantity));
        BigDecimal totalCost = existingCost.add(newCost);
        BigDecimal totalQuantity = new BigDecimal(existingQuantity + newQuantity);
        
        return totalCost.divide(totalQuantity, PRECISION, ROUNDING_MODE).doubleValue();
    }
    
    /**
     * Calculates portfolio value.
     */
    public static double calculatePortfolioValue(double[] prices, int[] quantities) {
        if (prices.length != quantities.length) {
            throw new IllegalArgumentException("Prices and quantities arrays must have same length");
        }
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (int i = 0; i < prices.length; i++) {
            BigDecimal value = new BigDecimal(prices[i]).multiply(new BigDecimal(quantities[i]));
            total = total.add(value);
        }
        
        return total.setScale(PRECISION, ROUNDING_MODE).doubleValue();
    }
    
    /**
     * Calculates unrealized profit/loss.
     */
    public static double calculateUnrealizedProfitLoss(double costBasis, double currentValue) {
        BigDecimal pnl = new BigDecimal(currentValue - costBasis);
        return pnl.setScale(PRECISION, ROUNDING_MODE).doubleValue();
    }

    /**
     * Calculates unrealized profit/loss percentage.
     */
    public static double calculateUnrealizedProfitLossPercentage(double costBasis, double currentValue) {
        if (costBasis <= 0) {
            throw new IllegalArgumentException("Cost basis must be positive");
        }
        
        double percentage = ((currentValue - costBasis) / costBasis) * 100;
        BigDecimal pnlPercentage = new BigDecimal(percentage);
        
        return pnlPercentage.setScale(2, ROUNDING_MODE).doubleValue();
    }

    /**
     * Rounds a price to 2 decimal places.
     */
    public static double roundPrice(double price) {
        BigDecimal rounded = new BigDecimal(price).setScale(PRECISION, ROUNDING_MODE);
        return rounded.doubleValue();
    }

    /**
     * Formats price as currency string.
     */
    public static String formatPrice(double price) {
        return String.format("$%.2f", roundPrice(price));
    }

    /**
     * Validates if a transaction is affordable.
     */
    public static boolean isAffordable(double walletBalance, double transactionCost) {
        BigDecimal balance = new BigDecimal(walletBalance);
        BigDecimal cost = new BigDecimal(transactionCost);
        
        return balance.compareTo(cost) >= 0;
    }
}