INSERT INTO currencies (code) VALUES ('USD');
INSERT INTO currencies (code) VALUES ('EUR');
INSERT INTO currencies (code) VALUES ('GBP');

INSERT INTO clients (email, created_at) VALUES ('test1@example.com', CURRENT_TIMESTAMP());
INSERT INTO clients (email, created_at) VALUES ('test2@example.com', CURRENT_TIMESTAMP());

INSERT INTO balances (client_id, currency_id, amount) VALUES (1, 1, 1000.00); -- 1000 USD
INSERT INTO balances (client_id, currency_id, amount) VALUES (1, 2, 500.00);  -- 500 EUR

INSERT INTO convert_transactions (timestamp, source_amount, source_currency_id, target_amount, target_currency_id, conversion_rate, new_source_balance, new_target_balance, client_id) VALUES (CURRENT_TIMESTAMP(), 100, 1, 200, 2, 2, 900, 700, 1)