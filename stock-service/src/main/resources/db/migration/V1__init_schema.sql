CREATE TABLE stock (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price NUMERIC(15, 2) DEFAULT 0
);

