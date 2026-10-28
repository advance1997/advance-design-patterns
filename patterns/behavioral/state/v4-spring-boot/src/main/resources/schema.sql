CREATE TABLE settlement_plan (
    id BIGINT PRIMARY KEY,
    status VARCHAR(32) NOT NULL,
    version BIGINT NOT NULL
);

CREATE TABLE settlement_outbox (
    event_id VARCHAR(64) PRIMARY KEY,
    plan_id BIGINT NOT NULL,
    plan_version BIGINT NOT NULL,
    event VARCHAR(32) NOT NULL,
    from_status VARCHAR(32) NOT NULL,
    to_status VARCHAR(32) NOT NULL,
    delivery_status VARCHAR(8) NOT NULL,
    CONSTRAINT uk_plan_version UNIQUE (plan_id, plan_version),
    CONSTRAINT fk_outbox_plan FOREIGN KEY (plan_id) REFERENCES settlement_plan(id)
);
