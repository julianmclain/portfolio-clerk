-- noinspection SqlNoDataSourceInspectionForFile
-- Initial tables

# --- !Ups

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(510) NOT NULL,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

CREATE TABLE IF NOT EXISTS portfolios (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    name VARCHAR(510),
    share_count NUMERIC,
    share_price VARCHAR(510),
    total_value VARCHAR(510),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

CREATE TABLE IF NOT EXISTS deposits (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    total_amount VARCHAR(510) NOT NULL,
    deposit_datetime timestamp with time zone
--     created_at timestamp with time zone not null default now(),
--     updated_at timestamp with time zone not null default now()
);

CREATE TABLE IF NOT EXISTS portfolio_snapshots (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    opening_share_count NUMERIC,
    opening_share_price VARCHAR(510),
    opening_value VARCHAR(510),
    net_cash_flow VARCHAR(510),
    cash_flow_share_price VARCHAR(510),
    num_share_change NUMERIC,
    closing_share_count NUMERIC,
    closing_share_price VARCHAR(510),
    closing_value VARCHAR(510),
    net_return NUMERIC,
    date DATE,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

CREATE TABLE IF NOT EXISTS assets (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    asset_name VARCHAR(510),
    asset_symbol VARCHAR(510),
    asset_type VARCHAR(510),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

CREATE TABLE IF NOT EXISTS portfolio_assets (
    id BIGSERIAL not null,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    asset_id BIGINT NOT NULL REFERENCES assets(id),
    quantity NUMERIC,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    PRIMARY KEY(portfolio_id, asset_id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    asset_id BIGINT NOT NULL REFERENCES assets(id),
    quantity NUMERIC,
    unit_price VARCHAR(510),
    total_value VARCHAR(510),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

# --- !Downs

DROP TABLE IF EXISTS deposits;
DROP TABLE IF EXISTS portfolio_snapshots;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS portfolio_assets;
DROP TABLE IF EXISTS assets;
DROP TABLE IF EXISTS portfolios;
DROP TABLE IF EXISTS users;
