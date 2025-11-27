-- Stock Trading Application Schema for MySQL

-- ===================================================================================
-- 1. DROP TABLES , useful for rebuilding the schema)
-- ===================================================================================
-- Ensure tables are dropped in the correct order to avoid foreign key issues
DROP TABLE IF EXISTS stock_price_history;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS portfolios;
DROP TABLE IF EXISTS stocks;
DROP TABLE IF EXISTS users;

-- ===================================================================================
-- 2. CREATE TABLES
-- ===================================================================================

-- Table to store user information for the stock trading application
CREATE TABLE users (
    id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    balance     DECIMAL(15, 2) DEFAULT 100000.00 NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table to store stock information
CREATE TABLE stocks (
    id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    symbol      VARCHAR(10) UNIQUE NOT NULL,
    name        VARCHAR(100) NOT NULL,
    -- Current price of the stock
    price       DECIMAL(15, 2) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- COMMENT='Table to store static and current price data for tradable stocks.';

-- Table to store user portfolios (stock holdings)
CREATE TABLE portfolios (
    id              INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- Data type consistent with users.id and stocks.id
    user_id         INT NOT NULL,
    stock_id        INT NOT NULL,
    quantity        INT NOT NULL, -- Integer quantity of shares
    average_price   DECIMAL(15, 4) NOT NULL, -- Using 4 decimal places for price precision
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints to link to USERS and STOCKS tables
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (stock_id) REFERENCES stocks(id),

    -- Ensure a user can only have one portfolio entry per stock
    UNIQUE KEY uc_user_stock (user_id, stock_id)
);

-- COMMENT='Table to track the quantity and average cost of stocks held by each user.';

-- Table to store transaction history (BUY/SELL)
CREATE TABLE transactions (
    id                  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id             INT NOT NULL,
    stock_id            INT NOT NULL,
    quantity            INT NOT NULL,
    price               DECIMAL(15, 2) NOT NULL,
    transaction_type    VARCHAR(4) NOT NULL
                        CONSTRAINT chk_transaction_type
                        CHECK (transaction_type IN ('BUY', 'SELL')),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

-- COMMENT='Historical record of all buy and sell actions by users.';

-- Table to store stock price history for analysis
CREATE TABLE stock_price_history (
    id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    stock_id    INT NOT NULL,
    price       DECIMAL(15, 2) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

-- COMMENT='Detailed historical price points for technical analysis.';


-- Insert a starting stock
INSERT INTO stocks (symbol, name, price)
VALUES ('UITU', 'UIT UNI STOCK', 150.00);

-- Insert another starting stock
INSERT INTO stocks (symbol, name, price)
VALUES ('AAPL', 'Apple Inc.', 180.50);

COMMIT;