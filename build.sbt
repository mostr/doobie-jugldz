import sbt.Keys.version

lazy val root = (project in file("."))
  .settings(commonSmlBuildSettings)
  .settings(Seq(
    name := "doobie-jug",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    libraryDependencies ++= Dependencies.all,
    scalafmtOnCompile := true,
    fork in Test := true,
    parallelExecution in Test := false
  ))

scalacOptions := Seq(
  "-encoding",
  "UTF-8", // source files are in UTF-8
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint", // enable handy linter warnings
  "-Ypartial-unification" // allow the compiler to unify type constructors of different arities
)
