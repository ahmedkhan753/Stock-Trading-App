package com.stockapp.auth;

import com.stockapp.dao.TransactionDAO;
import com.stockapp.models.Stock;
import com.stockapp.models.Transaction;
import com.stockapp.models.User;
import com.stockapp.services.StockService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class TransactionsController {

    @FXML
    private TableView<TransactionViewModel> transactionTable;
    @FXML
    private TableColumn<TransactionViewModel, Number> idColumn;
    @FXML
    private TableColumn<TransactionViewModel, String> typeColumn;
    @FXML
    private TableColumn<TransactionViewModel, String> tickerColumn;
    @FXML
    private TableColumn<TransactionViewModel, Number> quantityColumn;
    @FXML
    private TableColumn<TransactionViewModel, String> priceColumn;
    @FXML
    private TableColumn<TransactionViewModel, String> dateColumn;

    private User user;
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final StockService stockService = new StockService();
    private final DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void initData(User user) {
        this.user = user;
        loadTransactions();
    }

    private void loadTransactions() {
        ObservableList<TransactionViewModel> displayList = FXCollections.observableArrayList();

        for (Transaction t : transactionDAO.getTransactionsByUser(user.getId())) {
            Stock stock = stockService.getStockById(t.getStockID());
            String ticker = (stock != null) ? stock.getTicker() : "UNKNOWN";
            displayList.add(new TransactionViewModel(t, ticker));
        }

        transactionTable.setItems(displayList);
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        tickerColumn.setCellValueFactory(cellData -> cellData.getValue().tickerProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) transactionTable.getScene().getWindow();
        stage.close();
        if (user != null) {
            // In a real app we might want to refresh the dashboard
        }
    }

    // Helper class for TableView
    public class TransactionViewModel {
        private final IntegerProperty id;
        private final StringProperty type;
        private final StringProperty ticker;
        private final IntegerProperty quantity;
        private final StringProperty price;
        private final StringProperty date;

        public TransactionViewModel(Transaction t, String tickerSymbol) {
            this.id = new SimpleIntegerProperty(t.getId());
            this.type = new SimpleStringProperty(t.getType());
            this.ticker = new SimpleStringProperty(tickerSymbol);
            this.quantity = new SimpleIntegerProperty(t.getQuantity());
            this.price = new SimpleStringProperty(currencyFormat.format(t.getPrice()));
            this.date = new SimpleStringProperty(t.getTimestamp() != null ? t.getTimestamp().toString() : "");
        }

        public IntegerProperty idProperty() {
            return id;
        }

        public StringProperty typeProperty() {
            return type;
        }

        public StringProperty tickerProperty() {
            return ticker;
        }

        public IntegerProperty quantityProperty() {
            return quantity;
        }

        public StringProperty priceProperty() {
            return price;
        }

        public StringProperty dateProperty() {
            return date;
        }
    }
}
