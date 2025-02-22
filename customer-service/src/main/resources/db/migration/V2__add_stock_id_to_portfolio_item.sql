ALTER TABLE portfolio_item DROP COLUMN ticker;

ALTER TABLE portfolio_item ADD COLUMN stock_id BIGINT NOT NULL;