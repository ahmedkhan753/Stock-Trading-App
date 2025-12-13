package com.stockapp.auth;

import com.stockapp.dao.TransactionDAO;
import com.stockapp.models.Stock;
import com.stockapp.models.Transaction;
import com.stockapp.services.StockService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class ChartController {

    @FXML
    private LineChart<String, Number> priceChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private ComboBox<Stock> stockComboBox;

    private final StockService stockService = new StockService();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");

    @FXML
    public void initialize() {
        loadStockList();

        // Default to showing all stocks
        handleShowAll();

        stockComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showStockChart(newVal);
            }
        });
    }

    private void loadStockList() {
        List<Stock> stocks = stockService.getAllStocks();
        stockComboBox.setItems(FXCollections.observableArrayList(stocks));

        // Custom converter to show Ticker in ComboBox
        stockComboBox.setConverter(new StringConverter<Stock>() {
            @Override
            public String toString(Stock stock) {
                return stock == null ? "" : stock.getTicker() + " - " + stock.getName();
            }

            @Override
            public Stock fromString(String string) {
                return null; // Not needed
            }
        });
    }

    @FXML
    private void handleShowAll() {
        stockComboBox.getSelectionModel().clearSelection();
        priceChart.getData().clear();
        priceChart.setTitle("All Stocks Price History");

        List<Stock> stocks = stockService.getAllStocks();
        for (Stock stock : stocks) {
            addStockSeries(stock);
        }
    }

    private void showStockChart(Stock stock) {
        priceChart.getData().clear();
        priceChart.setTitle(stock.getName() + " (" + stock.getTicker() + ") Price History");
        addStockSeries(stock);
    }

    private void addStockSeries(Stock stock) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(stock.getTicker());

        List<Transaction> transactions = transactionDAO.getTransactionsByStockId(stock.getId());

        // Add start point (Reference 200)? Or just start with first transaction?
        // User said: "ups and downs in past".
        // If no transactions, we can plot the current price at least?

        if (transactions.isEmpty()) {
            // Plot reference point and current point
            series.getData().add(new XYChart.Data<>("Start", 200.0));
            series.getData().add(new XYChart.Data<>("Now", stock.getPrice()));
        } else {
            // Sort by date just in case
            transactions.sort(Comparator.comparing(Transaction::getTimestamp));

            // Plot initial reference if desired? User said "reference to 200 dollar default
            // starting price" for P&L.
            // Assume they all started at 200.
            series.getData().add(new XYChart.Data<>("Initial", 200.0));

            for (Transaction t : transactions) {
                String dateStr = dateFormat.format(t.getTimestamp());
                series.getData().add(new XYChart.Data<>(dateStr, t.getPrice()));
            }

            // Add current price as "Now"
            series.getData().add(new XYChart.Data<>("Now", stock.getPrice()));
        }

        priceChart.getData().add(series);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) priceChart.getScene().getWindow();
        stage.close();
    }
}
