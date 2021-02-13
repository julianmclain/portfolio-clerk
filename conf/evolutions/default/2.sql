-- noinspection SqlNoDataSourceInspectionForFile
-- Seed data

# --- !Ups

INSERT INTO users (email) VALUES ('julio@gmail.com');

INSERT INTO portfolios (user_id, name, share_count, share_price, total_value) VALUES (1, 'Test portfolio', 1000, 'USD 10', 'USD 10000');

INSERT INTO deposits (portfolio_id, total_amount) VALUES (1, 'USD 5000');

-- INSERT INTO portfolio_snapshots (
--     portfolio_id,
--     opening_share_count,
--     opening_share_price,
--     opening_value,
--     net_cash_flow,
--     cash_flow_share_price,
--     num_share_change,
--     closing_share_count,
--     closing_share_price,
--     closing_value,
--     net_return,
--     date
-- )
-- VALUES (
--         1,
--         0,
--         'USD 0',
--         'USD 0',
--         'USD 9990',
--
--        );

INSERT INTO assets (portfolio_id, asset_name, asset_symbol, asset_type)
VALUES (1, 'Cash', '$', 'Cash');
INSERT INTO assets (portfolio_id, asset_name, asset_symbol, asset_type)
VALUES (1, 'Apple', 'AAPL', 'Stock');

INSERT INTO portfolio_assets (portfolio_id, asset_id, quantity)
VALUES (1, 1, 5000);
INSERT INTO portfolio_assets (portfolio_id, asset_id, quantity)
VALUES (1, 2, 100);
--
-- INSERT INTO transactions (portfolio_id, portfolio_asset_id, quantity, unit_price, total_value)
-- VALUES (1, 1, 5000, 'USD 1', 'USD 5000');
-- INSERT INTO transactions (portfolio_id, portfolio_asset_id, quantity, unit_price, total_value)
-- VALUES (1, 2, 100, 'USD 100', 'USD 10000');
--
-- # --- !Downs
