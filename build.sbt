name := """acm_revamp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.8.Final",
  "org.hibernate" % "hibernate-jpamodelgen" % "4.3.8.Final",
  cache,
  javaWs,
  "org.mindrot" % "jbcrypt" % "0.3m"
)

javacOptions ++= Seq("-s", "metamodel")

fork in run := true