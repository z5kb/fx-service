ALTER TABLE convert_transactions ADD COLUMN idempotency_key VARCHAR(255);
ALTER TABLE convert_transactions ADD CONSTRAINT uk_idempotency_key UNIQUE (idempotency_key);