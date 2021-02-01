# Portfolio Clerk

## Calculating your portfolio's rate of return 

Calculating your portfolio's rate of return is easy unless there are cash inflows / outflows during the period. If 
that's the case, you have to use a method that accounts for the time value of money.

Brokerages show portfolio returns using an Internal Rate of Return calculation (Vanguard for example calls it out
[here](https://personal.vanguard.com/us/content/MyPortfolio/performance/LMperfSummaryInfoContent.jsp) under __Calculation method__. This isn't a metric 
that you can compare against the performance of other benchmarks. You need to use a method that removes the effect 
of cash flows. 

[Source](https://www.retailinvestor.org/PortfolioReturnCalc.pdf)

The solution:

The goal is to replace spreadsheets like [this](https://docs.google.com/spreadsheets/d/13GF2Gzw_8LsAAUDosaONqqCxfBoNfEJY/edit#gid=1622810545)

## Functionality
v1
- [ ] ability to create a portfolio
- [ ] ability to make a deposit
- [ ] ability to make a transaction
- [ ] calculate daily portfolio snapshots

- for v1, portfolio / asset values will only be updated 1 time per day, after market close. In a future version it 
  would be good to fetch latest asset values / portfolio value on demand
- need to figure out how to handle cash. Thinking it's just an asset and it's auto-deducted with transactions and 
  auto-incremented with deposits
- need to add timestamps to all models, e.g.
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
  
- akka scheduler https://github.com/enragedginger/akka-quartz-scheduler
 
v2
- [ ] define a few reference assets (thinking $SPY, BTC, and 1 other)
- [ ] calculate performance comparison against reference assets

v3
- integrations: make it easy to do deposits and transactions (e.g. forward a buy / sell confirmation email to an 
  address and scrape the email for the transaction info)
  
v4 
- visualizations: make it look nice

## TODO

- setup docker-compose for postgres 
