INSERT INTO currencies (code) VALUES ('USD');
INSERT INTO currencies (code) VALUES ('EUR');
INSERT INTO currencies (code) VALUES ('GBP');

INSERT INTO clients (email, created_at) VALUES ('test1@example.com', CURRENT_TIMESTAMP());
INSERT INTO clients (email, created_at) VALUES ('test2@example.com', CURRENT_TIMESTAMP());

INSERT INTO balances (client_id, currency_id, amount) VALUES (1, 1, 10000.00); -- 10000 USD
INSERT INTO balances (client_id, currency_id, amount) VALUES (1, 2, 8000.00); -- 8000 EUR
INSERT INTO balances (client_id, currency_id, amount) VALUES (2, 3, 5000.00); -- 5000 GBP

INSERT INTO convert_transactions (timestamp, source_amount, source_currency_id, target_amount, target_currency_id, conversion_rate, new_source_balance, new_target_balance, client_id) VALUES (CURRENT_TIMESTAMP(), 100, 1, 200, 2, 2, 900, 700, 1)