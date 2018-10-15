import sbt._

object Dependencies {

  private lazy val database: Seq[ModuleID] = {
    val doobieVersion = "0.5.3"
    Seq(
      "org.tpolecat" %% "doobie-core"      % doobieVersion,
      "org.tpolecat" %% "doobie-hikari"    % doobieVersion,
      "org.tpolecat" %% "doobie-postgres"  % doobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test,
      "org.flywaydb" % "flyway-core"       % "5.1.4"
    )
  }

  private lazy val logging: Seq[ModuleID] = {
    val logbackVersion = "1.2.3"
    Seq(
      "ch.qos.logback"             % "logback-classic"  % logbackVersion,
      "ch.qos.logback"             % "logback-core"     % logbackVersion,
      "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.0",
      "org.slf4j"                  % "log4j-over-slf4j" % "1.7.25"
    )
  }

  private lazy val miscellaneous: Seq[ModuleID] = {
    Seq(
      "io.monix"      %% "monix"     % "3.0.0-RC1",
      "org.typelevel" %% "cats-core" % "1.4.0"
    )
  }

  private lazy val testing: Seq[ModuleID] = {
    val scalaTestVersion = "3.0.5"
    Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion
    ).map(_ % Test)
  }

  lazy val all: Seq[ModuleID] = database ++ logging ++ miscellaneous ++ testing

}
