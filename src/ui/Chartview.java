package stockapp.src.ui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import stockapp.src.models.Stock;
import java.util.List;

/**
 * UI component for displaying stock price charts.
 * Shows price trends and market performance visualization.
 */
public class Chartview extends VBox {
    
    private LineChart<Number, Number> priceChart;
    private Label chartTitleLabel;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    
    // Constructor
    public Chartview() {
        initializeChart();
    }
    
    /**
     * Initializes the chart components.
     */
    private void initializeChart() {
        // Create axes
        xAxis = new NumberAxis();
        xAxis.setLabel("Time Period (Days)");
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        
        yAxis = new NumberAxis();
        yAxis.setLabel("Price ($)");
        
        // Create line chart
        priceChart = new LineChart<>(xAxis, yAxis);
        priceChart.setTitle("Stock Price Trend");
        priceChart.setStyle("-fx-font-size: 12;");
        
        // Create title label
        chartTitleLabel = new Label("Stock Price Chart");
        chartTitleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        
        // Add components to VBox
        this.getChildren().addAll(chartTitleLabel, priceChart);
        this.setStyle("-fx-padding: 10;");
        this.setSpacing(10);
    }
    
    /**
     * Updates chart with simulated price data for a stock.
     */
    public void updateChart(Stock stock) {
        priceChart.getData().clear();
        
        if (stock == null) {
            chartTitleLabel.setText("No Stock Selected");
            return;
        }
        
        chartTitleLabel.setText("Price Chart: " + stock.getTicker());
        
        // Create a data series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(stock.getTicker() + " Price");
        
        // Generate simulated price data (last 30 days)
        double basePrice = stock.getPrice();
        double volatility = 2.0;
        
        for (int day = 1; day <= 30; day++) {
            // Simulate price with random variation
            double variation = (Math.random() - 0.5) * volatility;
            double price = basePrice + variation * (day / 10.0);
            
            series.getData().add(new XYChart.Data<>(day, Math.max(price, basePrice * 0.8)));
        }
        
        priceChart.getData().add(series);
    }
    
    /**
     * Updates chart with multiple stock comparisons.
     */
    public void updateChartWithMultipleStocks(List<Stock> stocks) {
        priceChart.getData().clear();
        
        if (stocks == null || stocks.isEmpty()) {
            chartTitleLabel.setText("No Stocks to Display");
            return;
        }
        
        chartTitleLabel.setText("Stock Price Comparison");
        
        // Add data series for each stock
        for (Stock stock : stocks) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(stock.getTicker());
            
            double basePrice = stock.getPrice();
            
            for (int day = 1; day <= 20; day++) {
                double variation = (Math.random() - 0.5) * 1.5;
                double price = basePrice + variation * (day / 10.0);
                series.getData().add(new XYChart.Data<>(day, Math.max(price, basePrice * 0.9)));
            }
            
            priceChart.getData().add(series);
        }
    }
    
    /**
     * Clears the chart.
     */
    public void clearChart() {
        priceChart.getData().clear();
        chartTitleLabel.setText("Stock Price Chart");
    }
    
    /**
     * Gets the underlying chart object.
     */
    public LineChart<Number, Number> getChart() {
        return priceChart;
    }
}
