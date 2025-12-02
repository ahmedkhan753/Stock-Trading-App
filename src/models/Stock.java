package stockapp.src.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Stock {

    private final SimpleStringProperty ticker;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty id;
    private final SimpleDoubleProperty changePercent;

    public Stock(int id, String symbol, String name, double price, double changePercent) {
        this.id = new SimpleIntegerProperty(id);
        this.ticker = new SimpleStringProperty(symbol);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.changePercent = new SimpleDoubleProperty(changePercent);
    }

    // Default constructor
    public Stock() {
        this.id = new SimpleIntegerProperty(0);
        this.ticker = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.price = new SimpleDoubleProperty(0.0);
        this.changePercent = new SimpleDoubleProperty(0.0);
    }

    // --- Getter Methods for Properties (CRITICAL for TableView) ---
    // The TableView's PropertyValueFactory looks for these 'propertyNameProperty()' methods.

    public SimpleStringProperty tickerProperty() {
        return ticker;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public SimpleDoubleProperty changePercentProperty() {
        return changePercent;
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    // --- Standard Getters/Setters ---

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getTicker() { return ticker.get(); }
    public void setTicker(String ticker) { this.ticker.set(ticker); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }

    public double getChangePercent() { return changePercent.get(); }
    public void setChangePercent(double changePercent) { this.changePercent.set(changePercent); }
}