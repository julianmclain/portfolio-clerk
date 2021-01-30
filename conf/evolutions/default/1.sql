-- noinspection SqlNoDataSourceInspectionForFile

# --- !Ups

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE portfolios (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE deposits (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    amount VARCHAR(510) NOT NULL
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

# --- !Downs

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS deposits;
DROP TABLE IF EXISTS portfolios;
DROP TABLE IF EXISTS portfolio_snapshots;
