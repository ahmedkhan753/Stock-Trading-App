package stockapp.ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * UI component for stock buy/sell transaction form.
 * Allows users to input transaction details and execute trades.
 */
public class TransactionForm extends VBox {
    
    private ComboBox<String> transactionTypeCombo;
    private Label stockInfoLabel;
    private TextField quantityField;
    private Label pricePerShareLabel;
    private Label totalCostLabel;
    private Button executeButton;
    private Button cancelButton;
    private Label formTitleLabel;
    private Label errorMessageLabel;
    
    // Constructor
    public TransactionForm() {
        initializeForm();
    }
    
    /**
     * Initializes the transaction form components.
     */
    private void initializeForm() {
        // Create title
        formTitleLabel = new Label("Stock Transaction");
        formTitleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        
        // Create error message label
        errorMessageLabel = new Label("");
        errorMessageLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 11;");
        
        // Transaction type selection
        VBox typeSection = new VBox();
        typeSection.setSpacing(5);
        Label typeLabel = new Label("Transaction Type");
        typeLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        transactionTypeCombo = new ComboBox<>();
        transactionTypeCombo.getItems().addAll("Buy", "Sell");
        transactionTypeCombo.setValue("Buy");
        transactionTypeCombo.setPrefWidth(200);
        
        typeSection.getChildren().addAll(typeLabel, transactionTypeCombo);
        
        // Stock information section
        VBox stockSection = new VBox();
        stockSection.setSpacing(5);
        Label stockLabel = new Label("Selected Stock");
        stockLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        stockInfoLabel = new Label("No stock selected");
        stockInfoLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666666;");
        
        stockSection.getChildren().addAll(stockLabel, stockInfoLabel);
        
        // Quantity input section
        VBox quantitySection = new VBox();
        quantitySection.setSpacing(5);
        Label quantityLabel = new Label("Quantity");
        quantityLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        quantityField = new TextField();
        quantityField.setPromptText("Enter number of shares");
        quantityField.setPrefWidth(200);
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            updateTotalCost();
        });
        
        quantitySection.getChildren().addAll(quantityLabel, quantityField);
        
        // Price per share section
        VBox priceSection = new VBox();
        priceSection.setSpacing(5);
        Label priceLabel = new Label("Price per Share");
        priceLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        pricePerShareLabel = new Label("$0.00");
        pricePerShareLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #1565c0;");
        
        priceSection.getChildren().addAll(priceLabel, pricePerShareLabel);
        
        // Total cost section
        VBox totalSection = new VBox();
        totalSection.setSpacing(5);
        Label totalLabel = new Label("Total Cost");
        totalLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
        
        totalCostLabel = new Label("$0.00");
        totalCostLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        
        totalSection.getChildren().addAll(totalLabel, totalCostLabel);
        
        // Button section
        HBox buttonSection = new HBox();
        buttonSection.setSpacing(10);
        buttonSection.setAlignment(Pos.CENTER);
        
        executeButton = new Button("Execute Transaction");
        executeButton.setStyle("-fx-font-size: 12; -fx-padding: 10 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 12; -fx-padding: 10 25; -fx-background-color: #999999; -fx-text-fill: white;");
        
        buttonSection.getChildren().addAll(executeButton, cancelButton);
        
        // Add all sections to main VBox
        this.getChildren().addAll(
            formTitleLabel,
            errorMessageLabel,
            typeSection,
            stockSection,
            quantitySection,
            priceSection,
            totalSection,
            new Separator(),
            buttonSection
        );
        
        this.setStyle("-fx-padding: 15; -fx-border-color: #cccccc; -fx-border-radius: 5;");
        this.setSpacing(12);
    }
    
    /**
     * Sets the selected stock information.
     */
    public void setStock(String ticker, String name, double price) {
        stockInfoLabel.setText(ticker + " - " + name);
        pricePerShareLabel.setText(String.format("$%.2f", price));
        updateTotalCost();
    }
    
    /**
     * Gets the selected transaction type (Buy or Sell).
     */
    public String getTransactionType() {
        return transactionTypeCombo.getValue();
    }
    
    /**
     * Gets the quantity entered by user.
     */
    public int getQuantity() {
        try {
            String qty = quantityField.getText().trim();
            return Integer.parseInt(qty);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Gets the price per share from label.
     */
    public double getPrice() {
        String priceText = pricePerShareLabel.getText().replace("$", "").trim();
        try {
            return Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Gets the total transaction cost.
     */
    public double getTotalCost() {
        String totalText = totalCostLabel.getText().replace("$", "").trim();
        try {
            return Double.parseDouble(totalText);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Updates the total cost based on quantity and price.
     */
    private void updateTotalCost() {
        int quantity = getQuantity();
        double price = getPrice();
        double total = quantity * price;
        totalCostLabel.setText(String.format("$%.2f", total));
    }
    
    /**
     * Clears all form fields.
     */
    public void clearForm() {
        transactionTypeCombo.setValue("Buy");
        quantityField.clear();
        stockInfoLabel.setText("No stock selected");
        pricePerShareLabel.setText("$0.00");
        totalCostLabel.setText("$0.00");
        errorMessageLabel.setText("");
    }
    
    /**
     * Displays error message.
     */
    public void showError(String errorMessage) {
        errorMessageLabel.setText("Error: " + errorMessage);
    }
    
    /**
     * Clears error message.
     */
    public void clearError() {
        errorMessageLabel.setText("");
    }
    
    /**
     * Sets action handler for execute button.
     */
    public void setExecuteButtonAction(EventHandler<ActionEvent> handler) {
        executeButton.setOnAction(handler);
    }
    
    /**
     * Sets action handler for cancel button.
     */
    public void setCancelButtonAction(EventHandler<ActionEvent> handler) {
        cancelButton.setOnAction(handler);
    }
    
    /**
     * Validates form inputs.
     */
    public boolean validateForm() {
        int quantity = getQuantity();
        if (quantity <= 0) {
            showError("Quantity must be greater than 0");
            return false;
        }
        
        if (stockInfoLabel.getText().equals("No stock selected")) {
            showError("Please select a stock first");
            return false;
        }
        
        return true;
    }
}
