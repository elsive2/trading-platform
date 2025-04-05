CREATE TABLE user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    balance NUMERIC(15, 2) DEFAULT 0
);

CREATE TABLE portfolio_item (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user(id) ON DELETE CASCADE,
    ticker VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL
);

INSERT INTO user (name, email, balance) VALUES
    ('John Doe', 'john.doe@example.com', 10000.00),
    ('Jane Smith', 'jane.smith@example.com', 15000.50),
    ('Alice Johnson', 'alice.johnson@example.com', 5000.75);