package stockapp.src.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import stockapp.src.models.User;
import stockapp.src.models.Stock;
import stockapp.src.dao.StockDAO;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Manages portfolio display, stock list interaction, and navigation.
 */
public class DashboardController {

    // FXML Injected Components
    @FXML private Label userWelcomeLabel;
    @FXML private Label cashBalanceLabel;
    @FXML private Label totalValueLabel;
    @FXML private Label profitLossLabel;
    @FXML private TextField searchField;
    @FXML private TableView<Stock> stockTableView;
    @FXML private TableColumn<Stock, String> tickerColumn;
    @FXML private TableColumn<Stock, String> nameColumn;
    @FXML private TableColumn<Stock, Double> priceColumn;
    @FXML private TableColumn<Stock, Double> changeColumn;
    @FXML private Button buyButton;
    @FXML private Button sellButton;
    @FXML private Label selectedStockLabel;
    @FXML private BorderPane mainPane;
    @FXML private Button logoutButton;

    // Instance Variables and Services
    private User currentUser;
    // Initialize the DAO to access database data
    private final StockDAO stockDAO = new StockDAO();
    private ObservableList<Stock> marketData;

    /**
     * Initializes the controller. This is called immediately after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        tickerColumn.setCellValueFactory(new PropertyValueFactory<>("ticker"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Setup price column formatting
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(column -> new  PriceCell());

        // Setup change column formatting and color
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("changePercent"));
        changeColumn.setCellFactory(column -> new ChangeCell());

        // 2. Load initial stock data
        loadStockData();

        // 3. Setup listener for table row selection
        stockTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // When a stock is selected, enable trade buttons and display ticker/price
                selectedStockLabel.setText(newSelection.getTicker() + " - $" + String.format("%.2f", newSelection.getPrice()));
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
            } else {
                setText(formatter.format(item));
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
                    setStyle("-fx-text-fill: #0f172a;"); // Neutral
                }
            }
        }
    }

    /**
     * Called by the LoginController after successful authentication to pass data.
     * @param user The User object of the currently logged-in user.
     */
    public void initData(User user) {
        this.currentUser = user;
        updateDashboardView();
    }

    /**
     * Updates all dynamic labels on the dashboard with the current user's data.
     */
    private void updateDashboardView() {
        if (currentUser == null) return;

        userWelcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

        // Use proper number formatting for currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // Format and display the cash balance using BigDecimal
        // NOTE: Assuming User.getBalance() returns a double or BigDecimal
        cashBalanceLabel.setText(currencyFormat.format(currentUser.getBalance()));

        // Placeholder: Total Value (will need PortfolioService later)
        totalValueLabel.setText(currencyFormat.format(currentUser.getBalance()));

        // Placeholder for P/L
        profitLossLabel.setText("+0.00%");
    }

    /**
     * Loads stock data from the DAO (Database) into the TableView.
     */
    private void loadStockData() {
        try {
            // Fetch data from the database via the DAO
            marketData = FXCollections.observableArrayList(stockDAO.getAllStocks());
            stockTableView.setItems(marketData);
            System.out.println("Market data loaded: " + marketData.size() + " stocks found.");
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Failed to load stock data from DAO.");
            e.printStackTrace();
            // Optionally show an error dialog to the user
        }
    }

    // --- FXML Event Handlers ---

    @FXML
    private void refreshStockList() {
        loadStockData();
        System.out.println("Stock list refreshed from database.");
    }

    // Trading actions (currently placeholders)
    @FXML private void handleBuyStock() {
        Stock selected = stockTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Open BUY modal for: " + selected.getTicker());
            // TODO: Next step: Open the Trade Modal
        }
    }

    @FXML private void handleSellStock() {
        Stock selected = stockTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Open SELL modal for: " + selected.getTicker());
            // TODO: Next step: Open the Trade Modal
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        System.out.println("Logging out user: " + (currentUser != null ? currentUser.getUsername() : "Unknown"));
        currentUser = null;

        // Load the login view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login_register.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Stock Trader: Login & Register");
        stage.setScene(new Scene(root, 400, 450));
        stage.show();
    }

    // Placeholder navigation methods (to be implemented later)
    @FXML private void showPortfolio() { System.out.println("Navigating to Portfolio."); }
    @FXML private void showTransactions() { System.out.println("Navigating to Transactions."); }
    @FXML private void showWallet() { System.out.println("Opening Deposit/Withdrawal modal."); }
}