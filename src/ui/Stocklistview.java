package stockapp.src.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import stockapp.src.models.Stock;
import java.util.List;

/**
 * UI component for displaying a list of stocks in a table format.
 * Shows stock ticker, name, price, and price change information.
 */
public class Stocklistview extends VBox {
    
    private TableView<Stock> stockTable;
    private TableColumn<Stock, String> tickerColumn;
    private TableColumn<Stock, String> nameColumn;
    private TableColumn<Stock, Double> priceColumn;
    private TableColumn<Stock, Double> changeColumn;
    private Label titleLabel;
    private ObservableList<Stock> stockData;
    
    // Constructor
    public Stocklistview() {
        initializeTable();
    }
    
    /**
     * Initializes the stock table with columns.
     */
    private void initializeTable() {
        // Create title label
        titleLabel = new Label("Market Stocks");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        
        // Create table
        stockTable = new TableView<>();
        stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Create columns
        tickerColumn = new TableColumn<>("Ticker");
        tickerColumn.setCellValueFactory(new PropertyValueFactory<>("ticker"));
        tickerColumn.setPrefWidth(100);
        
        nameColumn = new TableColumn<>("Company Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(250);
        
        priceColumn = new TableColumn<>("Price ($)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(100);
        
        // Format price column
        priceColumn.setCellFactory(column -> new TableCell<Stock, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        
        changeColumn = new TableColumn<>("Change %");
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("changePercent"));
        changeColumn.setPrefWidth(100);
        
        // Format change column with color coding
        changeColumn.setCellFactory(column -> new TableCell<Stock, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(String.format("%.2f%%", item * 100));
                    if (item >= 0) {
                        setTextFill(Color.GREEN);
                    } else {
                        setTextFill(Color.RED);
                    }
                }
            }
        });
        
        // Add columns to table
        stockTable.getColumns().addAll(tickerColumn, nameColumn, priceColumn, changeColumn);
        
        // Add components to VBox
        this.getChildren().addAll(titleLabel, stockTable);
        this.setStyle("-fx-padding: 10;");
        this.setSpacing(10);
        
        // Initialize empty data
        stockData = FXCollections.observableArrayList();
        stockTable.setItems(stockData);
    }
    
    /**
     * Updates the table with stock data.
     */
    public void updateStockList(List<Stock> stocks) {
        if (stocks != null) {
            stockData.clear();
            stockData.addAll(stocks);
        }
    }
    
    /**
     * Returns the currently selected stock.
     */
    public Stock getSelectedStock() {
        return stockTable.getSelectionModel().getSelectedItem();
    }
    
    /**
     * Selects a stock in the table.
     */
    public void selectStock(Stock stock) {
        stockTable.getSelectionModel().select(stock);
    }
    
    /**
     * Gets the stock table for direct manipulation.
     */
    public TableView<Stock> getStockTable() {
        return stockTable;
    }
    
    /**
     * Clears all stocks from the table.
     */
    public void clearTable() {
        stockData.clear();
    }
    
    /**
     * Sets custom title for the table.
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    /**
     * Gets the number of stocks displayed.
     */
    public int getStockCount() {
        return stockData.size();
    }
}
