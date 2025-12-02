package stockapp.src.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.math.BigDecimal;

/**
 * UI component for displaying user wallet and account balance information.
 * Shows current balance and provides wallet action options.
 */
public class Walletview extends VBox {
    
    private Label balanceLabel;
    private Label portfolioValueLabel;
    private Label profitLossLabel;
    private Label accountStatusLabel;
    private Button depositButton;
    private Button withdrawButton;
    private Label titleLabel;
    
    // Constructor
    public Walletview() {
        initializeWallet();
    }
    
    /**
     * Initializes wallet UI components.
     */
    private void initializeWallet() {
        // Create title
        titleLabel = new Label("Wallet & Balance");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        
        // Create balance display section
        VBox balanceSection = new VBox();
        balanceSection.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 15;");
        balanceSection.setSpacing(10);
        
        Label balanceTitleLabel = new Label("Available Balance");
        balanceTitleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666666;");
        
        balanceLabel = new Label("$0.00");
        balanceLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        
        balanceSection.getChildren().addAll(balanceTitleLabel, balanceLabel);
        balanceSection.setAlignment(Pos.CENTER_LEFT);
        
        // Create portfolio section
        VBox portfolioSection = new VBox();
        portfolioSection.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 15;");
        portfolioSection.setSpacing(10);
        
        Label portfolioTitleLabel = new Label("Portfolio Value");
        portfolioTitleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666666;");
        
        portfolioValueLabel = new Label("$0.00");
        portfolioValueLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1565c0;");
        
        portfolioSection.getChildren().addAll(portfolioTitleLabel, portfolioValueLabel);
        portfolioSection.setAlignment(Pos.CENTER_LEFT);
        
        // Create profit/loss section
        VBox profitLossSection = new VBox();
        profitLossSection.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 15;");
        profitLossSection.setSpacing(10);
        
        Label profitLossTitleLabel = new Label("Profit / Loss");
        profitLossTitleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666666;");
        
        profitLossLabel = new Label("$0.00");
        profitLossLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        
        profitLossSection.getChildren().addAll(profitLossTitleLabel, profitLossLabel);
        profitLossSection.setAlignment(Pos.CENTER_LEFT);
        
        // Create account status section
        VBox statusSection = new VBox();
        statusSection.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 15;");
        statusSection.setSpacing(10);
        
        Label statusTitleLabel = new Label("Account Status");
        statusTitleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666666;");
        
        accountStatusLabel = new Label("Active");
        accountStatusLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        
        statusSection.getChildren().addAll(statusTitleLabel, accountStatusLabel);
        statusSection.setAlignment(Pos.CENTER_LEFT);
        
        // Create buttons section
        HBox buttonSection = new HBox();
        buttonSection.setSpacing(10);
        buttonSection.setAlignment(Pos.CENTER);
        
        depositButton = new Button("Deposit Funds");
        depositButton.setStyle("-fx-font-size: 12; -fx-padding: 8 20; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        withdrawButton = new Button("Withdraw Funds");
        withdrawButton.setStyle("-fx-font-size: 12; -fx-padding: 8 20; -fx-background-color: #f44336; -fx-text-fill: white;");
        
        buttonSection.getChildren().addAll(depositButton, withdrawButton);
        
        // Add all sections to main VBox
        this.getChildren().addAll(
            titleLabel,
            balanceSection,
            portfolioSection,
            profitLossSection,
            statusSection,
            buttonSection
        );
        
        this.setStyle("-fx-padding: 15;");
        this.setSpacing(15);
    }
    
    /**
     * Updates the displayed balance.
     */
    public void updateBalance(BigDecimal balance) {
        if (balance != null) {
            balanceLabel.setText(String.format("$%.2f", balance));
        }
    }
    
    /**
     * Updates the portfolio value display.
     */
    public void updatePortfolioValue(double value) {
        portfolioValueLabel.setText(String.format("$%.2f", value));
    }
    
    /**
     * Updates the profit/loss display with color coding.
     */
    public void updateProfitLoss(double profitLoss) {
        profitLossLabel.setText(String.format("$%.2f", profitLoss));
        
        if (profitLoss >= 0) {
            profitLossLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        } else {
            profitLossLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #c62828;");
        }
    }
    
    /**
     * Updates the account status display.
     */
    public void updateAccountStatus(String status) {
        accountStatusLabel.setText(status);
    }
    
    /**
     * Sets action handler for deposit button.
     */
    public void setDepositButtonAction(EventHandler<ActionEvent> handler) {
        depositButton.setOnAction(handler);
    }
    
    /**
     * Sets action handler for withdraw button.
     */
    public void setWithdrawButtonAction(EventHandler<ActionEvent> handler) {
        withdrawButton.setOnAction(handler);
    }
    
    /**
     * Gets the balance label for custom styling.
     */
    public Label getBalanceLabel() {
        return balanceLabel;
    }
    
    /**
     * Gets the profit/loss label for custom styling.
     */
    public Label getProfitLossLabel() {
        return profitLossLabel;
    }
}
