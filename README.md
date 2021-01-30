# Portfolio Clerk

## Notes

https://docs.google.com/spreadsheets/d/13GF2Gzw_8LsAAUDosaONqqCxfBoNfEJY/edit#gid=1622810545

## Functionality
v1
- [ ] ability to create a portfolio
- [ ] ability to make a deposit
- [ ] ability to make a transaction
- [ ] calculate daily portfolio snapshots
 
v2
- [ ] define a few reference assets (thinking $SPY, BTC, and 1 other)
- [ ] calculate daily reference asset snapshots

probably need a join table like 'portfolio_reference_comparison' with records that store things like the alpha, 
excess return amount, excess return percent for each reference asset

v3
- integrations: make it easy to do deposits and transactions (e.g. forward a buy / sell confirmation email to an 
  address and scrape the email for the transaction info)
  
v4 
- visualizations: make it look nice

## TODO

- research how to handle floating point values (e.g. prices, share quantities, etc...)