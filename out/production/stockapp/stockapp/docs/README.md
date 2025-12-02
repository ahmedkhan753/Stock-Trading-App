# Stock Trading App - Complete Documentation

## 📋 Project Overview
A JavaFX-based stock trading application that allows users to:
- Register and login with secure authentication
- View real-time stock market data
- Build and manage investment portfolios
- Execute buy/sell transactions
- Track wallet balance and profit/loss

---

## 📁 Project Structure

```
Stock-Trading-App/
├── src/                           (22 Java source files)
├── resources/                     (2 FXML UI layouts)
├── database/                      (MySQL schema)
├── docs/                          (Documentation files)
├── lib/                           (JavaFX SDK)
└── Configuration files
```

See `PROJECT_STRUCTURE.md` for complete file breakdown.

---

## 🔧 Core Components Explained

### **1. Database Layer (DAO Pattern)**

#### **DatabaseConnection.java**
- Manages MySQL database connections
- Uses JDBC driver
- Connection management for data operations

```java
Connection conn = DatabaseConnection.getConnection();
```

#### **UserDAO.java**
- **registerUser()** - Insert new user with initial balance
- **loginUser()** - Authenticate user by username/password
- **getUserById()** - Retrieve user information
- **updateUserBalance()** - Update account balance after transactions

#### **StockDAO.java**
- **getAllStocks()** - Fetch all available stocks from database
- Simulates market data with price change percentages

#### **PortfolioDAO.java**
- **addToPortfolio()** - Add stock to user's holdings
- **getPortfolioByUser()** - Get all stocks owned by user
- **updatePortfolio()** - Update quantity and average price
- **deletePortfolio()** - Remove stock when sold completely

#### **TransactionDAO.java**
- **recordTransaction()** - Log buy/sell transactions
- **getTransactionsByUser()** - Get user's transaction history
- **getAllTransactions()** - Get market-wide transaction history

---

### **2. Service Layer (Business Logic)**

#### **AuthService.java**
```java
AuthService authService = new AuthService();
User user = authService.loginUser("john", "password123");
User newUser = authService.registerUser("john", "password123", new BigDecimal("100000"));
```

#### **StockService.java**
```java
StockService stockService = new StockService();
List<Stock> allStocks = stockService.getAllStocks();
List<Stock> searched = stockService.searchStocksByTicker("AAPL");
Stock topGainers = stockService.getTopGainers(10);
boolean affordable = stockService.canAffordStock(5000, 50, 99.99);
```

#### **PortfolioService.java**
```java
PortfolioService portfolioService = new PortfolioService();
portfolioService.addStockToPortfolio(userID, stockID, quantity, price);
portfolioService.sellStockFromPortfolio(userID, stockID, quantityToSell);
double portfolioValue = portfolioService.calculatePortfolioValue(userID);
double profitLoss = portfolioService.calculatePortfolioProfitLoss(userID);
```

#### **WalletService.java**
```java
WalletService walletService = new WalletService();
BigDecimal balance = walletService.getUserBalance(userID);
walletService.deductBalance(userID, 5000);  // For buying stocks
walletService.addBalance(userID, 2500);     // For selling stocks
boolean hasEnough = walletService.hasSufficientBalance(userID, 5000);
```

---

### **3. UI Components (JavaFX)**

#### **Chartview.java**
- Line chart for stock price visualization
- Shows 30-day simulated price trends
- Methods:
  - `updateChart(Stock)` - Update chart for single stock
  - `updateChartWithMultipleStocks(List<Stock>)` - Compare multiple stocks
  - `clearChart()` - Reset chart

#### **Stocklistview.java**
- TableView displaying all stocks
- Columns: Ticker, Name, Price, Change %
- Color coding: Green (positive), Red (negative)
- Methods:
  - `updateStockList(List<Stock>)` - Load stocks
  - `getSelectedStock()` - Get user selection
  - `selectStock(Stock)` - Programmatically select

#### **Walletview.java**
- Displays user wallet information
- Shows: Available Balance, Portfolio Value, Profit/Loss, Status
- Methods:
  - `updateBalance(BigDecimal)` - Refresh balance
  - `updatePortfolioValue(double)` - Update portfolio value
  - `updateProfitLoss(double)` - Update P&L with color coding
  - `setDepositButtonAction()` - Set deposit handler
  - `setWithdrawButtonAction()` - Set withdraw handler

#### **TransactionForm.java**
- Buy/Sell transaction input form
- Displays: Transaction type, Stock info, Quantity, Price, Total cost
- Methods:
  - `setStock(ticker, name, price)` - Set selected stock
  - `getQuantity()`, `getPrice()`, `getTotalCost()` - Get inputs
  - `validateForm()` - Validate before execution
  - `showError()` - Display error messages

---

### **4. Controllers (JavaFX)**

#### **LoginController.java**
- Handles user login/registration
- Manages scene switching to dashboard
- Validates input before submission
- Passes authenticated user to dashboard

#### **DashboardController.java**
- Main application dashboard
- Displays stock market table
- Shows user wallet information
- Methods:
  - `initialize()` - Setup table columns and load data
  - `loadStockData()` - Fetch stocks from database
  - `refreshStockList()` - Reload market data
  - `handleBuyStock()` - Open buy dialog
  - `handleSellStock()` - Open sell dialog
  - `handleLogout()` - Return to login screen

---

### **5. Utility Classes**

#### **DateUtil.java**
```java
String today = DateUtil.getCurrentDate();           // yyyy-MM-dd
String now = DateUtil.getCurrentDateTime();         // yyyy-MM-dd HH:mm:ss
String formatted = DateUtil.formatTimestamp(ts);    // MMM dd, yyyy hh:mm a
String timeAgo = DateUtil.getTimeAgoString(ts);     // "2 hours ago"
```

#### **PriceCalculator.java**
```java
double cost = PriceCalculator.calculateTransactionCost(100, 99.99);     // 9999.00
double pnl = PriceCalculator.calculateProfitLoss(100, 110, 100);        // 1000.00
double pnlPct = PriceCalculator.calculateProfitLossPercentage(100, 110); // 10.00
double avgPrice = PriceCalculator.calculateAveragePrice(100, 50, 110, 50);
double portfolioVal = PriceCalculator.calculatePortfolioValue(prices, quantities);
String formatted = PriceCalculator.formatPrice(9999.999);               // $10,000.00
```

---

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  balance DECIMAL(15,2) NOT NULL
);
```

### Stocks Table
```sql
CREATE TABLE stocks (
  id INT PRIMARY KEY AUTO_INCREMENT,
  symbol VARCHAR(10) UNIQUE NOT NULL,
  name VARCHAR(255) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  change_percent DECIMAL(5,2)
);
```

### Portfolio Table
```sql
CREATE TABLE portfolio (
  id INT PRIMARY KEY AUTO_INCREMENT,
  userID INT NOT NULL,
  stockID INT NOT NULL,
  quantity INT NOT NULL,
  avgPrice DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (userID) REFERENCES users(id),
  FOREIGN KEY (stockID) REFERENCES stocks(id)
);
```

### Transactions Table
```sql
CREATE TABLE transactions (
  id INT PRIMARY KEY AUTO_INCREMENT,
  userID INT NOT NULL,
  stockID INT NOT NULL,
  quantity INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  type VARCHAR(10) NOT NULL,
  FOREIGN KEY (userID) REFERENCES users(id),
  FOREIGN KEY (stockID) REFERENCES stocks(id)
);
```

---

## 🚀 Quick Start

### Setup
1. **Install MySQL & Configure Database**
   ```bash
   mysql -u root -p < database/stockapp.sql
   ```

2. **Update DatabaseConnection.java** with your credentials
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/stockapp?serverTimezone=UTC";
   private static final String USER = "root";
   private static final String PASS = "your_password";
   ```

3. **Run the Application**
   ```bash
   javac src/Main.java
   java -cp src stockapp.src.Main
   ```

### Workflow
1. **Register** - Create new account with $100,000 initial balance
2. **Login** - Authenticate with username/password
3. **Browse Stocks** - View market data in dashboard
4. **Trade** - Buy/Sell stocks from portfolio
5. **Monitor** - Track wallet balance and profit/loss

---

## 🎯 Key Features

✅ **User Management**
- Secure login/registration
- Account balance tracking
- User authentication

✅ **Stock Trading**
- Browse available stocks
- Search by ticker/name
- Real-time price display
- Profit/loss calculation

✅ **Portfolio Management**
- Add/remove stocks
- Track holdings with average cost
- Calculate portfolio value
- View unrealized gains/losses

✅ **Transaction Tracking**
- Record all buy/sell activities
- Transaction history per user
- Timestamp tracking

✅ **Financial Calculations**
- Cost basis calculation
- Profit/loss percentage
- Average price calculation
- Portfolio valuation

---

## 📊 Data Flow

```
User Login → Authenticate (AuthService) → Dashboard
                                           ↓
                    ┌─────────────────────┼─────────────────────┐
                    ↓                     ↓                     ↓
              View Stocks          Manage Portfolio        Track Wallet
           (StockService)       (PortfolioService)      (WalletService)
                    ↓                     ↓                     ↓
              StockDAO          PortfolioDAO + TransactionDAO   UserDAO
                    ↓                     ↓                     ↓
                Database ←────────────────┴─────────────────────→ Database
```

---

## 🔐 Security Notes

⚠️ **Current Implementation (Development)**
- Plaintext password storage (NOT for production)
- No encryption implemented
- Hardcoded credentials

🛡️ **Recommendations for Production**
- Use password hashing (BCrypt/Argon2)
- Implement SSL/TLS for connections
- Use environment variables for credentials
- Add role-based access control (RBAC)
- Implement SQL injection prevention (parameterized queries already used)
- Add audit logging

---

## 📝 Documentation Files

- **README.md** - This file, complete overview
- **CODE_REFERENCE.md** - Method reference and quick lookup
- **PROJECT_STRUCTURE.md** - Complete file inventory and organization
- **DEPENDENCIES.md** - Dependency verification and testing

---

## 📦 Dependencies

### Core Libraries
- **JavaFX 21** - GUI framework
- **MySQL Connector/J** - Database driver
- **Java 25** - Runtime environment

### External Libraries (Bundled)
- JavaFX SDK (in `/lib/javafx-sdk`)

---

## 🐛 Common Issues & Solutions

### Issue: Database Connection Failed
**Solution:**
1. Verify MySQL is running
2. Check credentials in `DatabaseConnection.java`
3. Ensure `stockapp` database exists
4. Run `database/stockapp.sql` to create tables

### Issue: FXML File Not Found
**Solution:**
1. Verify FXML files in `/resources` folder
2. Check resource path in FXMLLoader
3. Ensure project classpath includes resources

### Issue: JavaFX not rendering
**Solution:**
1. Add JavaFX SDK to project libraries
2. Update JVM arguments: `--module-path /path/to/javafx-sdk --add-modules javafx.controls,javafx.fxml`

---

## 🔄 Development Workflow

### Adding New Feature Example: "Transfer Stocks to Friend"

1. **Add to TransactionDAO**
   ```java
   public boolean transferStock(int fromUserID, int toUserID, int stockID, int quantity)
   ```

2. **Add to PortfolioService**
   ```java
   public boolean transferStock(int fromUserID, int toUserID, int stockID, int quantity)
   ```

3. **Add Button to TransactionForm**
   ```java
   transferButton = new Button("Transfer");
   transferButton.setOnAction(e -> handleTransfer());
   ```

4. **Call from DashboardController**
   ```java
   @FXML
   private void handleTransferStock() { ... }
   ```

---

## 📄 License
This is a development/educational project.

---

## 👤 Author
Stock Trading App Development Team

**Last Updated:** November 30, 2025

---

## 📧 Support
For issues or questions, review the code comments and JavaDoc documentation in source files.
