package stockapp.src.dao;

// ALL NECESSARY IMPORTS
import stockapp.src.DatabaseConnection;
import stockapp.src.models.User; // Import the new User Model
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;

/**
 * Data Access Object for the User entity.
 * Handles all CRUD operations related to the 'users' table.
 */
public class UserDAO {

    private static final String INSERT_USER_SQL =
            "INSERT INTO users (username, password, balance) VALUES (?, ?, ?)";

    private static final String SELECT_USER_BY_USERNAME_SQL =
            "SELECT id, username, password, balance FROM users WHERE username = ?";

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
        return user;
    }

    /**
     * Inserts a new user into the database.
     * @param user The User object containing plaintext password and initial balance.
     * @return true if insertion was successful, false otherwise.
     */
    public boolean registerUser(User user) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword()); // Plaintext password
            preparedStatement.setBigDecimal(3, user.getBalance());

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
     * Checks if the provided credentials are valid for login by comparing plaintext passwords.
     * @param username The username provided by the user.
     * @param password The plaintext password provided by the user.
     * @return The authenticated User object if valid, otherwise null.
     */
    public User loginUser(String username, String password) {
        User user = findUserByUsername(username);

        if (user != null) {
            // Compare the plaintext password from the user against the plaintext password stored in the DB
            if (password.equals(user.getPassword())) {
                return user; // Authentication successful
            }
        }
        return null; // Login failed (user not found or password mismatch)
    }
}