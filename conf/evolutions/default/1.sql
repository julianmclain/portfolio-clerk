-- noinspection SqlNoDataSourceInspectionForFile

# --- !Ups

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(510) NOT NULL
);

CREATE TABLE portfolios (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    share_count NUMERIC,
    share_price VARCHAR(510),
    total_value VARCHAR(510)
);

CREATE TABLE deposits (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    total_amount VARCHAR(510) NOT NULL
);

CREATE TABLE portfolio_snapshots (
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
    date DATE
);

CREATE TABLE assets (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    asset_name VARCHAR(510),
    asset_symbol VARCHAR(510),
    asset_type VARCHAR(510),
    quantity NUMERIC,
    unit_price VARCHAR(510),
    total_value VARCHAR(510)
);

# --- !Downs

DROP TABLE IF EXISTS deposits;
DROP TABLE IF EXISTS assets;
DROP TABLE IF EXISTS portfolio_snapshots;
DROP TABLE IF EXISTS portfolios;
DROP TABLE IF EXISTS users;
