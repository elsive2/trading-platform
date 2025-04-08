CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    stock_id INT NOT NULL,
    action VARCHAR(20) NOT NULL, -- BUY / SELL
    quantity INT NOT NULL,
    total_cost NUMERIC(15, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);