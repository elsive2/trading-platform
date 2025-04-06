CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance NUMERIC(15, 2) DEFAULT 0
);

CREATE TABLE portfolio_item (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES account(id) ON DELETE CASCADE,
    ticker VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL
);
