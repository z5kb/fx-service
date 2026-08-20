INSERT INTO currencies (code) VALUES ('USD');
INSERT INTO currencies (code) VALUES ('EUR');
INSERT INTO currencies (code) VALUES ('GBP');

INSERT INTO clients (email, created_at) VALUES ('test1@example.com', CURRENT_TIMESTAMP());
INSERT INTO clients (email, created_at) VALUES ('test2@example.com', CURRENT_TIMESTAMP());

INSERT INTO balances (client_id, currency_id, amount) VALUES (1, 1, 10000.00); -- user 1, 10000 USD
INSERT INTO balances (client_id, currency_id, amount) VALUES (1, 2, 8000.00); -- user 1, 8000 EUR
INSERT INTO balances (client_id, currency_id, amount) VALUES (2, 3, 5000.00); -- user 2, 5000 GBP