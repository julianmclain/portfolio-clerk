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
    amount VARCHAR(510) NOT NULL,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id)
)

# --- !Downs

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS deposits;
DROP TABLE IF EXISTS portfolios;
