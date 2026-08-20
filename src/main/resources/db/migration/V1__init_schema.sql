-- 1. Currencies Table
CREATE TABLE currencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE
);

-- 2. Clients Table
CREATE TABLE clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Balances Table (Foreign Keys to Clients and Currencies)
CREATE TABLE balances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    amount DECIMAL(19, 6) NOT NULL DEFAULT 0.000000,
    CONSTRAINT fk_balances_client FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_balances_currency FOREIGN KEY (currency_id) REFERENCES currencies(id),
    CONSTRAINT uk_client_currency UNIQUE (client_id, currency_id) -- only 1 currency account per client
);

-- 4. ConvertTransactions Table
CREATE TABLE convert_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    source_amount DECIMAL(19, 6) NOT NULL,
    source_currency_id BIGINT NOT NULL,
    target_amount DECIMAL(19, 6) NOT NULL,
    target_currency_id BIGINT NOT NULL,
    conversion_rate DECIMAL(19, 6) NOT NULL,
    new_source_balance DECIMAL(19, 6) NOT NULL,
    new_target_balance DECIMAL(19, 6) NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT fk_trans_source_curr FOREIGN KEY (source_currency_id) REFERENCES currencies(id),
    CONSTRAINT fk_trans_target_curr FOREIGN KEY (target_currency_id) REFERENCES currencies(id),
    CONSTRAINT fk_trans_client FOREIGN KEY (client_id) REFERENCES clients(id)
);