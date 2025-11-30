package stockapp.services;

import stockapp.dao.UserDAO;
import stockapp.models.User;
import java.math.BigDecimal;

/**
 * Service layer for authentication operations.
 * Handles registration and login logic by delegating to UserDAO.
 */
public class AuthService {
    
    private final UserDAO userDAO;
    
    // Constructor
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Registers a new user with the provided username, password, and initial balance.
     * @param username The username to register
     * @param password The password (plaintext)
     * @param initialBalance The initial account balance
     * @return The registered User object with ID set, or null if registration failed
     */
    public User registerUser(String username, String password, BigDecimal initialBalance) {
        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            System.err.println("ERROR: Username cannot be empty");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("ERROR: Password cannot be empty");
            return null;
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("ERROR: Initial balance must be greater than 0");
            return null;
        }
        
        // Check if user already exists
        User existingUser = userDAO.getUserByUsername(username);
        if (existingUser != null) {
            System.err.println("ERROR: Username already exists");
            return null;
        }
        
        // Create new user and register
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setBalance(initialBalance);
        
        boolean registered = userDAO.registerUser(newUser);
        if (registered) {
            // Retrieve the registered user to get the ID
            return userDAO.getUserByUsername(username);
        }
        return null;
    }
    
    /**
     * Authenticates a user with username and password.
     * @param username The username
     * @param password The password (plaintext)
     * @return The authenticated User object, or null if authentication failed
     */
    public User loginUser(String username, String password) {
        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            System.err.println("ERROR: Username cannot be empty");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("ERROR: Password cannot be empty");
            return null;
        }
        
        // Retrieve user from database
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.err.println("ERROR: User not found");
            return null;
        }
        
        // Check password
        if (password.equals(user.getPassword())) {
            return user;
        } else {
            System.err.println("ERROR: Invalid password");
            return null;
        }
    }
    
    /**
     * Gets a user by username.
     * @param username The username to search for
     * @return The User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
}
