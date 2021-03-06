name := """portfolio-clerk"""
organization := "com.julianmclain"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies += guice
libraryDependencies += evolutions
libraryDependencies += ws
// test stuff
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
libraryDependencies += "org.mockito" % "mockito-scala_2.13" % "1.16.29"
// db stuff
libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.0.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.18"
libraryDependencies += "com.github.tminglei" %% "slick-pg" % "0.19.4"
// libs
libraryDependencies += "org.joda" % "joda-money" % "1.0.1"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.1.1"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.julianmclain.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.julianmclain.binders._"
