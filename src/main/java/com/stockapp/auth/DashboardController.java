package com.stockapp.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.stockapp.models.User;
import com.stockapp.models.Stock;
import com.stockapp.dao.StockDAO;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Manages portfolio display, stock list interaction, and navigation.
 */
public class DashboardController {

    // FXML Injected Components
    @FXML
    private Label userWelcomeLabel;
    @FXML
    private Label cashBalanceLabel;
    @FXML
    private Label totalValueLabel;
    @FXML
    private Label profitLossLabel;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Stock> stockTableView;
    @FXML
    private TableColumn<Stock, String> tickerColumn;
    @FXML
    private TableColumn<Stock, String> nameColumn;
    @FXML
    private TableColumn<Stock, Double> priceColumn;
    @FXML
    private TableColumn<Stock, Double> changeColumn;
    @FXML
    private Button buyButton;
    @FXML
    private Button sellButton;
    @FXML
    private Label selectedStockLabel;
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button logoutButton;

    // Instance Variables and Services
    private User currentUser;
    private final com.stockapp.services.StockService stockService = new com.stockapp.services.StockService();
    // Add PortfolioService for P/L calculations
    private final com.stockapp.services.PortfolioService portfolioService = new com.stockapp.services.PortfolioService();
    private ObservableList<Stock> marketData;

    /**
     * Initializes the controller. This is called immediately after the FXML is
     * loaded.
     */
    @FXML
    public void initialize() {
        tickerColumn.setCellValueFactory(new PropertyValueFactory<>("ticker"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Setup price column formatting
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(column -> new PriceCell());

        // Setup change column formatting and color
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("changePercent"));
        changeColumn.setCellFactory(column -> new ChangeCell());

        // 3. Setup listener for table row selection
        stockTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // When a stock is selected, enable trade buttons and display ticker/price
                selectedStockLabel
                        .setText(newSelection.getTicker() + " - $" + String.format("%.2f", newSelection.getPrice()));
                buyButton.setDisable(false);
                sellButton.setDisable(false);
            } else {
                // If nothing is selected, disable buttons
                selectedStockLabel.setText("N/A");
                buyButton.setDisable(true);
                sellButton.setDisable(true);
            }
        });
    }

    /**
     * Custom TableCell implementation for price formatting (e.g., $100.00).
     */
    private class PriceCell extends TableCell<Stock, Double> {
        private final DecimalFormat formatter = new DecimalFormat("$#,##0.00");

        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setStyle("");
            } else {
                setText(formatter.format(item));
                setStyle("-fx-text-fill: #FFFFFF;");
            }
        }
    }

    /**
     * Custom TableCell implementation for change percent formatting and coloring.
     */
    private class ChangeCell extends TableCell<Stock, Double> {
        private final DecimalFormat formatter = new DecimalFormat("0.00%");

        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setStyle("");
            } else {
                setText(formatter.format(item));
                // Conditional coloring for positive/negative change
                if (item > 0) {
                    setStyle("-fx-text-fill: #22c55e; -fx-font-weight: bold;"); // Green
                } else if (item < 0) {
                    setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;"); // Red
                } else {
                    setStyle("-fx-text-fill: #B0B0B0;"); // Neutral grey
                }
            }
        }
    }

    /**
     * Called by the LoginController after successful authentication to pass data.
     * 
     * @param user The User object of the currently logged-in user.
     */
    public void initData(User user) {
        this.currentUser = user;
        updateDashboardView();
        // Load stock data AFTER user is set, though it doesn't depend on user, it's
        // good practice
        loadStockData();
    }

    /**
     * Updates all dynamic labels on the dashboard with the current user's data.
     */
    private void updateDashboardView() {
        if (currentUser == null)
            return;

        userWelcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

        // Use proper number formatting for currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // 1. Cash Balance
        // NOTE: Assuming User.getBalance() returns a double or BigDecimal. Using
        // doubleValue() if it's BigDecimal.
        double cashBalance = currentUser.getBalance().doubleValue();
        cashBalanceLabel.setText(currencyFormat.format(cashBalance));

        // 2. Portfolio Value
        double portfolioValue = portfolioService.calculatePortfolioValue(currentUser.getId());
        double totalAssets = cashBalance + portfolioValue;

        totalValueLabel.setText(currencyFormat.format(totalAssets));

        // 3. Profit / Loss
        // User specified a default starting value of 100,000
        double initialInvestment = 100000.0;
        double profitLoss = totalAssets - initialInvestment;
        double profitLossPercent = (profitLoss / initialInvestment);

        String plText = currencyFormat.format(profitLoss) + " (" + new DecimalFormat("0.00%").format(profitLossPercent)
                + ")";
        profitLossLabel.setText(plText);

        // Color coding
        if (profitLoss >= 0) {
            profitLossLabel.setStyle("-fx-text-fill: #22c55e;"); // Green
        } else {
            profitLossLabel.setStyle("-fx-text-fill: #ef4444;"); // Red
        }
    }

    /**
     * Loads stock data from the Service (Database) into the TableView.
     */
    private void loadStockData() {
        try {
            // Fetch sorted data via StockService (uses MergeSort)
            marketData = FXCollections.observableArrayList(stockService.getAllStocksSorted());

            if (marketData.isEmpty()) {
                System.out.println("Thinking: Market data is empty. Attempting to seed or warn.");
                // Show an alert if data is empty even after auto-seeding attempt
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Data");
                alert.setHeaderText("No Stocks Found");
                alert.setContentText(
                        "The database returned no stocks. Please check your database connection or try restarting the app to trigger auto-seeding.");
                alert.showAndWait();
            }

            stockTableView.setItems(marketData);
            System.out.println("Market data loaded: " + marketData.size() + " stocks found.");
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Failed to load stock data.");
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Load Stocks");
            alert.setContentText("An error occurred while loading stocks:\n" + e.getMessage());
            alert.showAndWait();
        }
    }

    // --- FXML Event Handlers ---

    @FXML
    private void refreshStockList() {
        loadStockData();
        updateDashboardView();
        System.out.println("Stock list refreshed.");
    }

    // Trading actions
    @FXML
    private void handleBuyStock() {
        openTradeDialog(true);
    }

    @FXML
    private void handleSellStock() {
        openTradeDialog(false);
    }

    private void openTradeDialog(boolean isBuy) {
        Stock selected = stockTableView.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/trade_dialog.fxml"));
            Parent root = loader.load();

            TradeDialogController controller = loader.getController();
            controller.initData(selected, currentUser, isBuy);

            Stage stage = new Stage();
            stage.setTitle(isBuy ? "Buy Stock" : "Sell Stock");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Wait for dialog to close before refreshing

            // Refresh data after trade
            loadStockData();
            updateDashboardView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        currentUser = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login_register.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Stock Trader: Login & Register");
        stage.setScene(new Scene(root, 400, 450));
        stage.show();
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        handleLogout(event);
    }

    @FXML
    private void showPortfolio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfolio_view.fxml"));
            Parent root = loader.load();
            PortfolioController controller = loader.getController();
            controller.initData(currentUser);
            Stage stage = new Stage();
            stage.setTitle("My Portfolio");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transactions_view.fxml"));
            Parent root = loader.load();
            TransactionsController controller = loader.getController();
            controller.initData(currentUser);
            Stage stage = new Stage();
            stage.setTitle("Transaction History");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showChartView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chart_view.fxml"));
            Parent root = loader.load();

            loader.getController();
            // controller.initData() if needed, but initialize() handles it

            Stage stage = new Stage();
            stage.setTitle("Stock Price Charts");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}