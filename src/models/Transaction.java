package stockapp.src.models;
import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int userID;
    private int stockID;
    private int quantity;
    private double price;         //The price at the time of transaction
    private Timestamp timestamp;
    private String type;          //buy or sell

    // Constructor for the transaction ( When reading from the database)
    public Transaction(int id, int userID, int stockID, int quantity, double price
            , Timestamp timestamp, String type) {
        this.id = id;
        this.userID = userID;
        this.stockID = stockID;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
        this.type = type;
    }

    // Constructor for the transaction ( When creating a new transaction)
    public Transaction(int userID, int stockID, int quantity, double price, String type) {
        this.userID = userID;
        this.stockID = stockID;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }
    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getStockID() {
        return stockID;
    }
    public void setStockID(int stockID) {
        this.stockID = stockID;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
}
