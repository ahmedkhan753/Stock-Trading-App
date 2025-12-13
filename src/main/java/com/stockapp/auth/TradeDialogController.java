package com.stockapp.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.stockapp.models.Stock;
import com.stockapp.models.User;
import com.stockapp.services.PortfolioService;
import com.stockapp.services.StockService;
import com.stockapp.services.WalletService;
import com.stockapp.dao.TransactionDAO;

import java.text.DecimalFormat;

public class TradeDialogController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label stockInfoLabel;
    @FXML
    private TextField quantityField;
    @FXML
    private Label totalCostLabel;
    @FXML
    private Label errorLabel;

    private Stock stock;
    private User user;
    private boolean isBuy; // true for Buy, false for Sell
    private final PortfolioService portfolioService = new PortfolioService();
    private final WalletService walletService = new WalletService();
    private final StockService stockService = new StockService();
    private final TransactionDAO transactionDAO = new TransactionDAO(); // For recording transactions

    private final DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

    public void initData(Stock stock, User user, boolean isBuy) {
        this.stock = stock;
        this.user = user;
        this.isBuy = isBuy;

        titleLabel.setText(isBuy ? "Buy " + stock.getTicker() : "Sell " + stock.getTicker());
        stockInfoLabel
                .setText(stock.getName() + " (" + stock.getTicker() + ") - " + currencyFormat.format(stock.getPrice()));

        // Add listener to calculate total cost/value dynamically
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotal();
        });
    }

    private void calculateTotal() {
        try {
            int qty = Integer.parseInt(quantityField.getText());
            double total = qty * stock.getPrice();
            totalCostLabel.setText(currencyFormat.format(total));
            errorLabel.setText(""); // Clear errors
        } catch (NumberFormatException e) {
            totalCostLabel.setText("$0.00");
        }
    }

    @FXML
    private void handleConfirm() {
        try {
            int qty = Integer.parseInt(quantityField.getText());
            if (qty <= 0) {
                errorLabel.setText("Quantity must be positive.");
                return;
            }

            double price = stock.getPrice();
            double totalAmount = qty * price;

            if (isBuy) {
                // BUY LOGIC
                if (walletService.hasSufficientBalance(user.getId(), totalAmount)) {
                    // 1. Deduct Money
                    boolean fundsDeducted = walletService.deductBalance(user.getId(), totalAmount);
                    if (fundsDeducted) {
                        // 2. Add to Portfolio
                        portfolioService.addStockToPortfolio(user.getId(), stock.getId(), qty, price);

                        // 3. Record Transaction
                        transactionDAO.recordTransaction(user.getId(), stock.getId(), qty, price, "BUY");

                        // 4. Update Stock Price (Demand increases price)
                        stockService.updateStockPrice(stock.getId(), qty, true);

                        closeDialog();
                    } else {
                        errorLabel.setText("Transaction failed during fund deduction.");
                    }
                } else {
                    errorLabel.setText("Insufficient funds! Wallet: "
                            + currencyFormat.format(walletService.getUserBalance(user.getId())));
                }

            } else {
                // SELL LOGIC
                // 1. Check if user has enough shares
                // (PortfolioService.sellStockFromPortfolio handles the check, but good to check
                // here or catch error)

                boolean sellSuccess = portfolioService.sellStockFromPortfolio(user.getId(), stock.getId(), qty);
                if (sellSuccess) {
                    // 2. Add Funds
                    walletService.addBalance(user.getId(), totalAmount);

                    // 3. Record Transaction
                    transactionDAO.recordTransaction(user.getId(), stock.getId(), qty, price, "SELL");

                    // 4. Update Stock Price (Supply decreases price)
                    stockService.updateStockPrice(stock.getId(), qty, false);

                    closeDialog();
                } else {
                    errorLabel.setText("Failed to sell. Do you have enough shares?");
                }
            }

        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid quantity.");
        } catch (Exception e) {
            errorLabel.setText("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}
