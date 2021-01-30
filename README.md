# Portfolio Clerk

## Notes

The goal is to replace spreadsheets like this one: https://docs.google.
com/spreadsheets/d/13GF2Gzw_8LsAAUDosaONqqCxfBoNfEJY/edit#gid=1622810545

Money
- tips for dealing with money http://www.yacoset.com/how-to-handle-currency-conversions

## Functionality
v1
- [ ] ability to create a portfolio
- [ ] ability to make a deposit
- [ ] ability to make a transaction
- [ ] calculate daily portfolio snapshots

- add timestamps to all models, e.g.
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
 
v2
- [ ] define a few reference assets (thinking $SPY, BTC, and 1 other)
- [ ] calculate daily reference asset snapshots

v3
- integrations: make it easy to do deposits and transactions (e.g. forward a buy / sell confirmation email to an 
  address and scrape the email for the transaction info)
  
v4 
- visualizations: make it look nice

## TODO

- setup docker-compose for postgres 
