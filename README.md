# Portfolio Clerk

## Calculating your portfolio's rate of return 

Calculating your portfolio's rate of return is easy unless there are cash inflows / outflows during the period. If 
that's the case, it's a lot trickier. You need to use a method that accounts for the amount of time that capital was 
working in the portfolio.

Brokerages show portfolio returns using an Internal Rate of Return calculation (Vanguard, for example, calls it out
[here](https://personal.vanguard.com/us/content/MyPortfolio/performance/LMperfSummaryInfoContent.jsp) under 
__Calculation method__). Unfortunately IRR isn't a metric that you can compare against the performance of other 
benchmarks. In order to user to make those comparisons, you need to use a method that accounts for the effect 
of cash flows. 

[Source](https://www.retailinvestor.org/PortfolioReturnCalc.pdf)

Solution - The goal is to replace spreadsheets like [this](https://docs.google.com/spreadsheets/d/13GF2Gzw_8LsAAUDosaONqqCxfBoNfEJY/edit#gid=1622810545)

## Functionality

// TODO:
- before going deeper on testing you should figure out DB transactions. You might need to restructure the repos. 
  Start with Deposit creation. See the bottom answer in here for context https://stackoverflow.
  com/questions/31192851/mocking-database-with-slick-in-scalatest-mockito-and-testing-update
- testing repository code:
  - (done) approach 1) need a TestApplicationInstance trait that provides an app instance loaded from a test config 
    file. 
    Shouldn't be too hard, but it's less than ideal because you're spinning up an entire app just to use the DB 
    component.
  - approach 2) figure out a way to initialize just the DAO with a test config. I think you could do this via 
    dependency injection. Your test suite be injected with a DatabaseConfigProvider for a named database profile. 
    Then manually construct your repo instance and pass in the db config provider. See https://github.com/dnvriend/slick-3.2.0-test/blob/f5bfe8c9e29ba122f852a6e069b168581ab58ef7/play-slick/src/main/scala/play/api/db/slick/DatabaseConfigProvider.scala#L6
  - need to clean the DB between tests. I think the application automatically runs ups on application create and 
    downs on application teardown. Might just need a before test / after test hook to truncate persistence.tables. Not sure

v1
- [ ] ability to register as a user
- [ ] ability to create a portfolio
- [ ] ability to make a deposit
- [ ] ability to make a withdrawal 
- [ ] ability to make a transaction
- [ ] calculate daily portfolio snapshots
  - for v1, portfolio / asset values will only be updated 1 time per day, after market close. In a future version it 
    would be good to fetch latest asset values / portfolio value on demand
 
v2
- [ ] define a few reference assets (thinking $SPY, BTC, and 1 other)
- [ ] calculate performance comparison against reference assets

v3
- [ ] ability to forward a transaction confirmation email and have it automatically parsed and added to 
  the user's portfolio. 
- [ ] ability to define a recurring deposit.
  
v4 
- visualizations: make it look nice

v5
- other integrations (maybe Discord)
- RSS feed type thing or webhook for your trades so they're easy to share with other people

## Notes

### Splitting up dev / prod application conf
- https://sysgears.com/articles/how-to-configure-your-application-for-different-environments-with-play-framework/

### Playing around in the sbt console

```scala
import play.api._
val env     = Environment(new java.io.File("."), this.getClass.getClassLoader, Mode.Dev)
val context = ApplicationLoader.Context.create(env)
val loader  = ApplicationLoader(context)
val app     = loader.load(context)
Play.start(app)
// get an instance of a class:
val fd = app.injector.instanceOf[SimpleFinancialDataClient]
```

Read more about guice here https://www.baeldung.com/guice

## Example projects for inspiration
- Linking all of the reference projects that I found useful. I found a bunch of them via Slick docs, Play docs, 
  Silhouette docs
- https://github.com/KadekM/play-slick-silhouette-auth-api
- https://github.com/setusoft/silhouette-play-react-seed (complexish example implementation with React)
- https://github.com/adamzareba/play-silhouette-rest-slick-reactjs-typescript (simpleish example implementation with 
  React)

## Future TODO

- research how to handle stock splits and dividends
- setup docker-compose for postgres
- setup `sbt test` to only run unit tests and `sbt it:test` to run integration tests http://craigthomas.ca/blog/2015/05/08/play-framework-custom-integration-test-configuration/
