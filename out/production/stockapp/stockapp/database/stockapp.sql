-- Stock Trading Application Schema for Oracle 23ai


-- ===================================================================================
-- 2. CREATE TABLES
-- ===================================================================================


-- Table to store user information for the stock trading application
CREATE TABLE users (
    -- Use GENERATED ALWAYS AS IDENTITY for Oracle's auto-incrementing primary key
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username    VARCHAR2(50) UNIQUE NOT NULL,
    -- Increased size for potential password hashing (e.g., Argon2 or Bcrypt)
    password    VARCHAR2(255) NOT NULL, 
    -- Use Oracle's preferred NUMBER type for fixed-precision financial data
    balance     NUMBER(15, 2) DEFAULT 100000.00 NOT NULL,
    -- Use SYSTIMESTAMP for high-precision, timezone-aware creation time
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP
);

COMMENT ON TABLE users IS 'Table to store user login and initial balance information.';


-- Table to store stock information
CREATE TABLE stocks (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    symbol      VARCHAR2(10) UNIQUE NOT NULL,
    name        VARCHAR2(100) NOT NULL,
    -- Current price of the stock
    price       NUMBER(15, 2) NOT NULL, 
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP
);

COMMENT ON TABLE stocks IS 'Table to store static and current price data for tradable stocks.';


-- Table to store user portfolios (stock holdings)
CREATE TABLE portfolios (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id         NUMBER NOT NULL,
    stock_id        NUMBER NOT NULL,
    quantity        NUMBER(10) NOT NULL, -- Integer quantity of shares
    average_price   NUMBER(15, 4) NOT NULL, -- Using 4 decimal places for price precision
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP,
    
    -- Constraints to link to USERS and STOCKS tables
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (stock_id) REFERENCES stocks(id),
    
    -- Ensure a user can only have one portfolio entry per stock
    UNIQUE (user_id, stock_id) 
);

COMMENT ON TABLE portfolios IS 'Table to track the quantity and average cost of stocks held by each user.';


-- Table to store transaction history (BUY/SELL)
CREATE TABLE transactions (
    id                  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id             NUMBER NOT NULL,
    stock_id            NUMBER NOT NULL,
    quantity            NUMBER(10) NOT NULL,
    price               NUMBER(15, 2) NOT NULL,
    -- Oracle ENUM equivalent: VARCHAR2 with a CHECK constraint
    transaction_type    VARCHAR2(4) NOT NULL
                        CONSTRAINT chk_transaction_type 
                        CHECK (transaction_type IN ('BUY', 'SELL')),
    created_at          TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

COMMENT ON TABLE transactions IS 'Historical record of all buy and sell actions by users.';


-- Table to store stock price history for analysis
CREATE TABLE stock_price_history (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    stock_id    NUMBER NOT NULL,
    price       NUMBER(15, 2) NOT NULL,
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP,
    
    FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

COMMENT ON TABLE stock_price_history IS 'Detailed historical price points for technical analysis.';

-- ===================================================================================
-- 3. INITIAL DATA INSERT (Optional, but useful for testing)
-- ===================================================================================

-- Insert a starting stock
INSERT INTO stocks (symbol, name, price) 
VALUES ('GOOG', 'Google Alphabet Inc. Class C', 150.00);

-- Insert another starting stock
INSERT INTO stocks (symbol, name, price) 
VALUES ('AAPL', 'Apple Inc.', 180.50);

-- Commit the changes
COMMIT;
