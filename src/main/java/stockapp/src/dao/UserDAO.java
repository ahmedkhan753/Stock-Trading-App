package stockapp.src.dao;

// ALL NECESSARY IMPORTS
import java.math.BigDecimal;
import java.sql.Connection; // Import the new User Model
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import stockapp.src.DatabaseConnection;
import stockapp.src.models.User;
import stockapp.src.models.UserRole;

/**
 * Data Access Object for the User entity.
 * Handles all CRUD operations related to the 'users' table.
 */
@Repository
public class UserDAO {

    private static final String INSERT_USER_SQL = "INSERT INTO users (username, password, balance, role, is_active) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_USER_BY_USERNAME_SQL = "SELECT id, username, password, balance, role, is_active FROM users WHERE username = ?";

    /**
     * Helper to map a ResultSet row to a User object.
     */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        // Retrieve balance as BigDecimal to match the model
        user.setBalance(rs.getBigDecimal("balance"));
        String roleStr = rs.getString("role");
        user.setRole(UserRole.fromString(roleStr));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }

    /**
     * Inserts a new user into the database.
     * 
     * @param user The User object containing plaintext password and initial
     *             balance.
     * @return true if insertion was successful, false otherwise.
     */
    public boolean registerUser(User user) {
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword()); // Plaintext password
            preparedStatement.setBigDecimal(3, user.getBalance());
            preparedStatement.setString(4, user.getRole().name());
            preparedStatement.setBoolean(5, user.isActive());

            int rowsAffected = preparedStatement.executeUpdate();

            // Optionally retrieve the generated ID here
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL duplicate entry error
                System.err.println("User registration failed: Username already exists.");
            } else {
                System.err.println("Database error during user registration: " + e.getMessage());
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a User object based on the username.
     * 
     * @param username The username to look up.
     * @return The User object if found, otherwise null.
     */
    public User findUserByUsername(String username) {
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME_SQL)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during user lookup: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if the provided credentials are valid for login by comparing plaintext
     * passwords.
     * 
     * @param username The username provided by the user.
     * @param password The plaintext password provided by the user.
     * @return The authenticated User object if valid, otherwise null.
     */
    public User loginUser(String username, String password) {
        User user = findUserByUsername(username);

        if (user != null) {
            // Compare the plaintext password from the user against the plaintext password
            // stored in the DB
            if (password.equals(user.getPassword())) {
                return user; // Authentication successful
            }
        }
        return null; // Login failed (user not found or password mismatch)
    }

    /**
     * Retrieves a User by ID.
     * 
     * @param userID The user's ID
     * @return The User object if found, otherwise null.
     */
    public User getUserById(int userID) {
        String sql = "SELECT id, username, password, balance, role, is_active FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates user's balance.
     * 
     * @param userID     The user's ID
     * @param newBalance The new balance
     * @return true if update successful, false otherwise
     */
    public boolean updateUserBalance(int userID, BigDecimal newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBigDecimal(1, newBalance);
            preparedStatement.setInt(2, userID);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error updating user balance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a user is an admin.
     */
    public boolean isAdmin(int userID) {
        String sql = "SELECT role FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return "ADMIN".equals(resultSet.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error checking admin role: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all admin users.
     */
    public java.util.List<User> getAllAdmins() {
        java.util.List<User> admins = new java.util.ArrayList<>();
        String sql = "SELECT id, username, password, balance, role, is_active FROM users WHERE role = 'ADMIN' AND is_active = TRUE";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    admins.add(mapRowToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving admins: " + e.getMessage());
            e.printStackTrace();
        }
        return admins;
    }

    /**
     * Get all users from the database.
     */
    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        String sql = "SELECT id, username, password, balance, role, is_active FROM users";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(mapRowToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Adapter for other code expecting getUserByUsername
     */
    public User getUserByUsername(String username) {
        return findUserByUsername(username);
    }

    /**
     * Updates a user record.
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, balance = ?, role = ?, is_active = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBigDecimal(3, user.getBalance());
            preparedStatement.setString(4, user.getRole().name());
            preparedStatement.setBoolean(5, user.isActive());
            preparedStatement.setInt(6, user.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a user by ID.
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new user and returns the generated ID.
     */
    public int createUser(User user) {
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBigDecimal(3, user.getBalance());
            preparedStatement.setString(4, user.getRole().name());
            preparedStatement.setBoolean(5, user.isActive());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("Database error during user creation: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}