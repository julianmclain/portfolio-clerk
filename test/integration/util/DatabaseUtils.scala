//package integration.util
//
//import play.api.db.Database
//import play.api.db.Databases
//import play.api.db.evolutions.Evolutions
//
//object DatabaseUtils {
//
//  def withTestApplicationDatabase[T](block: Database => T): T = {
//    Databases.withDatabase(
//      driver = "org.postgresql.Driver",
//      url =
//        "jdbc:postgresql://localhost:5432/portfolio_clerk?currentSchema=public"
//    ) { database =>
//      Evolutions.withEvolutions(database) {
//        block(database)
//      }
//    }
//  }
//}

// This approach probably isn't right
// https://www.playframework.com/documentation/2.8.x/PlaySlickFAQ#A-binding-to-play.api.db.DBApi-was-already-configured
// this was in build.sbt  libraryDependencies += jdbc % Test
