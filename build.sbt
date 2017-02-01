name := "vue_stocks"

version := "1.0"

lazy val `vue_stocks` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  filters,
  cache,
  ws,
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "mysql" % "mysql-connector-java" % "5.1.23",
  "org.mindrot" % "jbcrypt" % "0.3m",
  specs2 % Test,
  "org.scalatest" % "scalatest_2.11" % "3.0.1" % "test",
  "org.mockito" % "mockito-core" % "2.2.25" % "test",
  "org.scalatestplus.play" % "scalatestplus-play_2.11" % "2.0.0-M1" % "test"
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

pipelineStages := Seq(digest)