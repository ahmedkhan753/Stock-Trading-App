package com.stockapp.auth;

import com.stockapp.models.Portfolio;
import com.stockapp.models.Stock;
import com.stockapp.models.User;
import com.stockapp.services.PortfolioService;
import com.stockapp.services.StockService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class PortfolioController {

    @FXML
    private TableView<PortfolioViewModel> portfolioTable;
    @FXML
    private TableColumn<PortfolioViewModel, String> tickerColumn;
    @FXML
    private TableColumn<PortfolioViewModel, Number> quantityColumn;
    @FXML
    private TableColumn<PortfolioViewModel, String> avgPriceColumn;
    @FXML
    private TableColumn<PortfolioViewModel, String> currentPriceColumn;
    @FXML
    private TableColumn<PortfolioViewModel, String> marketValueColumn;
    @FXML
    private TableColumn<PortfolioViewModel, String> profitLossColumn;

    private User user;
    private final PortfolioService portfolioService = new PortfolioService();
    private final StockService stockService = new StockService();
    private final DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    private final DecimalFormat percentFormat = new DecimalFormat("#,##0.00%");

    public void initData(User user) {
        this.user = user;
        loadPortfolio();
    }

    private void loadPortfolio() {
        ObservableList<PortfolioViewModel> displayList = FXCollections.observableArrayList();

        for (Portfolio p : portfolioService.getUserPortfolio(user.getId())) {
            Stock stock = stockService.getStockById(p.getStockID());
            if (stock != null) {
                displayList.add(new PortfolioViewModel(p, stock));
            }
        }

        portfolioTable.setItems(displayList);
    }

    @FXML
    public void initialize() {
        tickerColumn.setCellValueFactory(cellData -> cellData.getValue().tickerProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        avgPriceColumn.setCellValueFactory(cellData -> cellData.getValue().avgPriceProperty());
        currentPriceColumn.setCellValueFactory(cellData -> cellData.getValue().currentPriceProperty());
        marketValueColumn.setCellValueFactory(cellData -> cellData.getValue().marketValueProperty());
        profitLossColumn.setCellValueFactory(cellData -> cellData.getValue().profitLossProperty());
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) portfolioTable.getScene().getWindow();
        stage.close();
    }

    // Helper class for TableView
    public class PortfolioViewModel {
        private final StringProperty ticker;
        private final IntegerProperty quantity;
        private final StringProperty avgPrice;
        private final StringProperty currentPrice;
        private final StringProperty marketValue;
        private final StringProperty profitLoss;

        public PortfolioViewModel(Portfolio p, Stock s) {
            this.ticker = new SimpleStringProperty(s.getTicker());
            this.quantity = new SimpleIntegerProperty(p.getQuantity());

            double avg = p.getAvgPrice();
            double cur = s.getPrice();
            double value = cur * p.getQuantity();
            double cost = avg * p.getQuantity();
            double pl = value - cost;
            double plPercent = (cost > 0) ? (pl / cost) : 0;

            this.avgPrice = new SimpleStringProperty(currencyFormat.format(avg));
            this.currentPrice = new SimpleStringProperty(currencyFormat.format(cur));
            this.marketValue = new SimpleStringProperty(currencyFormat.format(value));

            String plString = currencyFormat.format(pl) + " (" + percentFormat.format(plPercent) + ")";
            this.profitLoss = new SimpleStringProperty(plString);
        }

        public StringProperty tickerProperty() {
            return ticker;
        }

        public IntegerProperty quantityProperty() {
            return quantity;
        }

        public StringProperty avgPriceProperty() {
            return avgPrice;
        }

        public StringProperty currentPriceProperty() {
            return currentPrice;
        }

        public StringProperty marketValueProperty() {
            return marketValue;
        }

        public StringProperty profitLossProperty() {
            return profitLoss;
        }
    }
}
