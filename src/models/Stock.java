package stockapp.src.models;

public class Stock {
    
    // Attributes for the stock
    private String symbol;
    private String name;
    private double price;
    private int id;

    // Constructor for the stock
    public Stock(String symbol, String name, double price, int id) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.id = id;
    }

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
