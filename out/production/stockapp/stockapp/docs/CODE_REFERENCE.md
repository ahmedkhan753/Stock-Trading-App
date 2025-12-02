# 📚 Stock Trading App - Code Reference Guide

## Quick Method Index

### 🔐 Authentication (AuthService)
```java
User registerUser(String username, String password, BigDecimal initialBalance)
User loginUser(String username, String password)
User getUserByUsername(String username)
```

### 📊 Stock Operations (StockService)
```java
List<Stock> getAllStocks()
List<Stock> searchStocksByTicker(String ticker)
List<Stock> searchStocksByName(String name)
Stock getStockById(int stockID)
Stock getStockByTicker(String ticker)
List<Stock> getStocksSortedByPrice()
List<Stock> getStocksSortedByChange()
List<Stock> getTopGainers(int limit)
List<Stock> getTopLosers(int limit)
boolean canAffordStock(double walletBalance, int quantity, double stockPrice)
double calculateTransactionCost(int quantity, double stockPrice)
```

### 💼 Portfolio Management (PortfolioService)
```java
boolean addStockToPortfolio(int userID, int stockID, int quantity, double price)
boolean sellStockFromPortfolio(int userID, int stockID, int quantityToSell)
List<Portfolio> getUserPortfolio(int userID)
double calculatePortfolioValue(int userID)
double calculatePortfolioProfitLoss(int userID)
```

### 💰 Wallet Management (WalletService)
```java
BigDecimal getUserBalance(int userID)
boolean deductBalance(int userID, double amount)
boolean addBalance(int userID, double amount)
boolean hasSufficientBalance(int userID, double requiredAmount)
String getFormattedBalance(int userID)
boolean depositFunds(int userID, double amount)
boolean withdrawFunds(int userID, double amount)
```

### 📈 Financial Calculations (PriceCalculator)
```java
double calculateTransactionCost(int quantity, double pricePerShare)
double calculateProfitLoss(double buyPrice, double sellPrice, int quantity)
double calculateProfitLossPercentage(double buyPrice, double sellPrice)
double calculateAveragePrice(double existingPrice, int existingQuantity, 
                           double newPrice, int newQuantity)
double calculatePortfolioValue(double[] prices, int[] quantities)
double calculateUnrealizedProfitLoss(double costBasis, double currentValue)
double calculateUnrealizedProfitLossPercentage(double costBasis, double currentValue)
double roundPrice(double price)
String formatPrice(double price)
boolean isAffordable(double walletBalance, double transactionCost)
```

### 📅 Date Operations (DateUtil)
```java
String getCurrentDate()                          // yyyy-MM-dd
String getCurrentDateTime()                      // yyyy-MM-dd HH:mm:ss
String getCurrentTime()                          // HH:mm:ss
Timestamp getCurrentTimestamp()
String formatTimestamp(Timestamp timestamp)      // MMM dd, yyyy hh:mm a
String formatTimestampDate(Timestamp timestamp)  // yyyy-MM-dd
String formatTimestampTime(Timestamp timestamp)  // HH:mm:ss
String formatDate(Date date)                     // yyyy-MM-dd
LocalDate parseDate(String dateString)
boolean isValidDate(String dateString)
String getTimeAgoString(Timestamp timestamp)     // "2 hours ago"
```

### 🗄️ Database Operations (DAOs)

#### UserDAO
```java
boolean registerUser(User user)
User findUserByUsername(String username)
User loginUser(String username, String password)
User getUserById(int userID)
boolean updateUserBalance(int userID, BigDecimal newBalance)
```

#### StockDAO
```java
List<Stock> getAllStocks()
```

#### PortfolioDAO
```java
boolean addToPortfolio(int userID, int stockID, int quantity, double avgPrice)
List<Portfolio> getPortfolioByUser(int userID)
Portfolio getPortfolioByUserAndStock(int userID, int stockID)
boolean updatePortfolio(int portfolioID, int quantity, double avgPrice)
boolean deletePortfolio(int portfolioID)
```

#### TransactionDAO
```java
boolean recordTransaction(int userID, int stockID, int quantity, double price, String type)
List<Transaction> getTransactionsByUser(int userID)
List<Transaction> getAllTransactions()
```

---

## Data Models

### User
```java
int id
String username
String password
BigDecimal balance
```

### Stock
```java
int id
String ticker
String name
double price
double changePercent
```

### Portfolio
```java
int id
int userID
int stockID
int quantity
double avgPrice
```

### Transaction
```java
int id
int userID
int stockID
int quantity
double price
Timestamp timestamp
String type  // "buy" or "sell"
```

---

## UI Components

### Chartview
```java
void updateChart(Stock stock)
void updateChartWithMultipleStocks(List<Stock> stocks)
void clearChart()
LineChart<Number, Number> getChart()
```

### Stocklistview
```java
void updateStockList(List<Stock> stocks)
Stock getSelectedStock()
void selectStock(Stock stock)
TableView<Stock> getStockTable()
void clearTable()
void setTitle(String title)
int getStockCount()
```

### Walletview
```java
void updateBalance(BigDecimal balance)
void updatePortfolioValue(double value)
void updateProfitLoss(double profitLoss)
void updateAccountStatus(String status)
void setDepositButtonAction(EventHandler<ActionEvent> handler)
void setWithdrawButtonAction(EventHandler<ActionEvent> handler)
Label getBalanceLabel()
Label getProfitLossLabel()
```

### TransactionForm
```java
void setStock(String ticker, String name, double price)
String getTransactionType()
int getQuantity()
double getPrice()
double getTotalCost()
void clearForm()
void showError(String errorMessage)
void clearError()
void setExecuteButtonAction(EventHandler<ActionEvent> handler)
void setCancelButtonAction(EventHandler<ActionEvent> handler)
boolean validateForm()
```

---

## Controller Methods

### LoginController
```java
void initialize()
void handleLogin(ActionEvent event)
void handleRegister()
void switchToDashboard(User authenticatedUser)
```

### DashboardController
```java
void initialize()
void initData(User user)
void updateDashboardView()
void loadStockData()
void refreshStockList()
void handleBuyStock()
void handleSellStock()
void handleLogout(ActionEvent event)
void showPortfolio()
void showTransactions()
void showWallet()
```

---

## Database Queries

### INSERT Operations
```sql
-- Register user
INSERT INTO users (username, password, balance) VALUES (?, ?, ?)

-- Add to portfolio
INSERT INTO portfolio (userID, stockID, quantity, avgPrice) VALUES (?, ?, ?, ?)

-- Record transaction
INSERT INTO transactions (userID, stockID, quantity, price, timestamp, type) VALUES (?, ?, ?, ?, ?, ?)
```

### SELECT Operations
```sql
-- Get user by username
SELECT id, username, password, balance FROM users WHERE username = ?

-- Get user by ID
SELECT id, username, password, balance FROM users WHERE id = ?

-- Get user portfolio
SELECT id, userID, stockID, quantity, avgPrice FROM portfolio WHERE userID = ?

-- Get specific portfolio entry
SELECT id, userID, stockID, quantity, avgPrice FROM portfolio WHERE userID = ? AND stockID = ?

-- Get user transactions
SELECT id, userID, stockID, quantity, price, timestamp, type FROM transactions WHERE userID = ? ORDER BY timestamp DESC

-- Get all stocks
SELECT id, symbol, name, price, change_percent FROM stocks

-- Get all transactions
SELECT id, userID, stockID, quantity, price, timestamp, type FROM transactions ORDER BY timestamp DESC
```

### UPDATE Operations
```sql
-- Update portfolio
UPDATE portfolio SET quantity = ?, avgPrice = ? WHERE id = ?

-- Update user balance
UPDATE users SET balance = ? WHERE id = ?
```

### DELETE Operations
```sql
-- Delete portfolio entry
DELETE FROM portfolio WHERE id = ?
```

---

## Common Usage Examples

### Register & Login Flow
```java
AuthService authService = new AuthService();

// Register
User newUser = authService.registerUser("john_doe", "secure_pass", new BigDecimal("100000.00"));

// Login
User loggedInUser = authService.loginUser("john_doe", "secure_pass");
if (loggedInUser != null) {
    System.out.println("Welcome " + loggedInUser.getUsername());
}
```

### Buy Stock Flow
```java
StockService stockService = new StockService();
PortfolioService portfolioService = new PortfolioService();
WalletService walletService = new WalletService();

Stock stock = stockService.getStockByTicker("AAPL");
int userID = 1;

// Check affordability
double cost = stockService.calculateTransactionCost(100, stock.getPrice());
if (walletService.hasSufficientBalance(userID, cost)) {
    // Deduct from wallet
    walletService.deductBalance(userID, cost);
    
    // Add to portfolio
    portfolioService.addStockToPortfolio(userID, stock.getId(), 100, stock.getPrice());
    
    System.out.println("Purchase successful!");
}
```

### Sell Stock Flow
```java
double sellPrice = 105.50;
int quantityToSell = 50;

// Remove from portfolio
if (portfolioService.sellStockFromPortfolio(userID, stockID, quantityToSell)) {
    // Add proceeds to wallet
    double proceeds = quantityToSell * sellPrice;
    walletService.addBalance(userID, proceeds);
    
    System.out.println("Sale successful!");
}
```

### Portfolio Analysis
```java
double portfolioValue = portfolioService.calculatePortfolioValue(userID);
double profitLoss = portfolioService.calculatePortfolioProfitLoss(userID);
double profitLossPercent = PriceCalculator.calculateUnrealizedProfitLossPercentage(
    calculateCostBasis(...), portfolioValue
);

System.out.println("Portfolio Value: " + PriceCalculator.formatPrice(portfolioValue));
System.out.println("Profit/Loss: " + PriceCalculator.formatPrice(profitLoss));
System.out.println("Return: " + profitLossPercent + "%");
```

---

## Error Handling Patterns

### Service Layer
```java
// Validate input
if (quantity <= 0 || price <= 0) {
    System.err.println("ERROR: Quantity and price must be positive");
    return false;
}

// Check existence
if (user == null) {
    System.err.println("ERROR: User not found");
    return null;
}

// Handle database errors
try {
    // database operation
} catch (SQLException e) {
    System.err.println("Database error: " + e.getMessage());
    e.printStackTrace();
    return null;
}
```

### Controller Layer
```java
@FXML
private void handleLogin(ActionEvent event) {
    if (username.isEmpty() || password.isEmpty()) {
        messageLabel.setTextFill(Color.RED);
        messageLabel.setText("Username and password required");
        return;
    }
    
    User user = authService.loginUser(username, password);
    if (user != null) {
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setText("Login successful!");
        switchToDashboard(user);
    } else {
        messageLabel.setTextFill(Color.RED);
        messageLabel.setText("Invalid credentials");
    }
}
```

---

## File Dependencies

### Main.java depends on:
- LoginController
- FXML resources
- JavaFX libraries

### LoginController depends on:
- UserDAO
- User model
- DashboardController
- AuthService (implicit through UserDAO)

### DashboardController depends on:
- StockDAO
- Stock model
- User model

### Services depend on:
- DAOs
- Models

### DAOs depend on:
- DatabaseConnection
- Models

---

## Important Configuration

### DatabaseConnection.java
```java
private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
private static final String URL = "jdbc:mysql://localhost:3306/stockapp?serverTimezone=UTC";
private static final String USER = "root";
private static final String PASS = "strongpassword";
```

### Application Configuration
- **JavaFX Version:** 21
- **Java Version:** 25 LTS
- **MySQL Version:** 8.0+
- **Build Tool:** Maven/Gradle (optional)
- **IDE:** IntelliJ IDEA (recommended)

---

## Testing Strategy

### Unit Testing Areas
1. **AuthService** - Registration, login, validation
2. **PriceCalculator** - All financial calculations
3. **PortfolioService** - Buy, sell, valuation logic
4. **DateUtil** - Date formatting and parsing

### Integration Testing
1. **Login → Dashboard** flow
2. **Buy transaction** flow
3. **Sell transaction** flow
4. **Portfolio update** after transactions

### UI Testing
1. Form validation
2. Table updates
3. Chart rendering
4. Balance display accuracy

---

## Performance Considerations

### Database
- Use connection pooling for production
- Index commonly queried columns (username, userID, stockID)
- Archive old transactions

### Caching
- Cache stock market data (refresh every minute)
- Cache user portfolio (refresh on transaction)

### UI
- Lazy load transaction history
- Paginate large result sets
- Use TableView virtualization

---

**Reference Guide Complete** ✅

For more details, see README.md and PROJECT_STRUCTURE.md
