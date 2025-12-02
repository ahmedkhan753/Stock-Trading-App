package stockapp.src.services;

import stockapp.src.dao.UserDAO;
import stockapp.src.models.User;
import java.math.BigDecimal;

/**
 * Business logic layer for wallet and balance operations.
 * Manages user account balance and fund transfers.
 */
public class WalletService {
    
    private final UserDAO userDAO;
    
    // Constructor
    public WalletService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Gets the current balance for a user.
     */
    public BigDecimal getUserBalance(int userID) {
        User user = userDAO.getUserById(userID);
        if (user != null) {
            return user.getBalance();
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Deducts funds from user's wallet (for buying stocks).
     */
    public boolean deductBalance(int userID, double amount) {
        if (amount <= 0) {
            System.err.println("ERROR: Deduction amount must be positive");
            return false;
        }
        
        User user = userDAO.getUserById(userID);
        if (user == null) {
            System.err.println("ERROR: User not found");
            return false;
        }
        
        BigDecimal currentBalance = user.getBalance();
        BigDecimal deductionAmount = new BigDecimal(amount);
        
        if (currentBalance.compareTo(deductionAmount) < 0) {
            System.err.println("ERROR: Insufficient balance");
            return false;
        }
        
        BigDecimal newBalance = currentBalance.subtract(deductionAmount);
        user.setBalance(newBalance);
        
        return userDAO.updateUserBalance(userID, newBalance);
    }
    
    /**
     * Adds funds to user's wallet (for selling stocks).
     */
    public boolean addBalance(int userID, double amount) {
        if (amount <= 0) {
            System.err.println("ERROR: Addition amount must be positive");
            return false;
        }
        
        User user = userDAO.getUserById(userID);
        if (user == null) {
            System.err.println("ERROR: User not found");
            return false;
        }
        
        BigDecimal currentBalance = user.getBalance();
        BigDecimal additionAmount = new BigDecimal(amount);
        BigDecimal newBalance = currentBalance.add(additionAmount);
        
        user.setBalance(newBalance);
        
        return userDAO.updateUserBalance(userID, newBalance);
    }
    
    /**
     * Checks if user has sufficient balance for a transaction.
     */
    public boolean hassufficientBalance(int userID, double requiredAmount) {
        User user = userDAO.getUserById(userID);
        if (user == null) {
            return false;
        }
        
        BigDecimal currentBalance = user.getBalance();
        BigDecimal requiredBalance = new BigDecimal(requiredAmount);
        
        return currentBalance.compareTo(requiredBalance) >= 0;
    }
    
    /**
     * Gets formatted balance string for display.
     */
    public String getFormattedBalance(int userID) {
        BigDecimal balance = getUserBalance(userID);
        return String.format("$%.2f", balance);
    }
    
    /**
     * Deposits money into user's account.
     */
    public boolean depositFunds(int userID, double amount) {
        if (amount <= 0) {
            System.err.println("ERROR: Deposit amount must be positive");
            return false;
        }
        
        return addBalance(userID, amount);
    }
    
    /**
     * Withdraws money from user's account.
     */
    public boolean withdrawFunds(int userID, double amount) {
        if (amount <= 0) {
            System.err.println("ERROR: Withdrawal amount must be positive");
            return false;
        }
        
        return deductBalance(userID, amount);
    }
}
