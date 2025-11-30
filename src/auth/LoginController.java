package stockapp.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import stockapp.dao.UserDAO;
import stockapp.models.User;
import stockapp.auth.DashboardController; // Must be imported
import java.io.IOException;
import java.math.BigDecimal;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final UserDAO userDAO;

    // Constructor: Initialize DAO immediately
    public LoginController() {
        this.userDAO = new UserDAO();
    }

    // Initialization method (called after FXML elements are injected)
    @FXML
    public void initialize() {
        messageLabel.setTextFill(Color.BLACK);
        messageLabel.setText("Welcome! Please log in or register.");
    }

    // Helper method to switch scenes and pass the User object
    private void switchToDashboard(User authenticatedUser) {
        try {
            // Use the ClassLoader to load the FXML resource, ensuring it works when packaged
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard_view.fxml"));

            Parent root = loader.load();

            // Get the controller instance for the dashboard scene
            DashboardController dashboardController = loader.getController();

            // Pass the authenticated User object to the new controller
            dashboardController.initData(authenticatedUser);

            // Get the current stage (window) from the message label
            Stage stage = (Stage) messageLabel.getScene().getWindow();

            // Create the new scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Stock Trader: Dashboard");
            stage.show();

        } catch (Exception e) {
            // Display the error on the login screen if the dashboard fails to load
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Login Successful, but failed to load dashboard view.");
            System.err.println("Failed to load Dashboard FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Login Failed: Username and password are required.");
            return;
        }

        // 1. Call the DAO's secure login method, which returns the authenticated User object
        User authenticatedUser = userDAO.loginUser(username, password);

        if (authenticatedUser != null) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Login Successful! Switching to dashboard...");

            // ** 2. SUCCESS: SWITCH TO DASHBOARD **
            try {
                switchToDashboard(authenticatedUser);
            } catch (Exception e) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Login Successful, but failed to load dashboard view.");
                e.printStackTrace();
            }

        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Login Failed: Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Registration Failed: Username and password are required.");
            return;
        }

        try {
            // Default initial balance for new users
            final BigDecimal initialBalance = new BigDecimal("100000.00");

            // Create the User model
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password); // *** PASSING PLAINTEXT PASSWORD ***
            newUser.setBalance(initialBalance);

            // 2. Call the DAO's register method
            boolean success = userDAO.registerUser(newUser);

            if (success) {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText("Registration Successful! Initial balance: $" + initialBalance);
            } else {
                // Assumption: failure means duplicate username
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Registration Failed: Username '" + username + "' is already taken.");
            }
        } catch (Exception e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("An unexpected error occurred during registration.");
            e.printStackTrace();
        }
    }
}