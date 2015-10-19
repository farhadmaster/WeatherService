name := """weather_dump"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "javax.inject" % "javax.inject" % "1",
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.9.Final"
 
)


EclipseKeys.withSource := true
routesGenerator := InjectedRoutesGenerator

libraryDependencies += "org.elasticsearch" % "elasticsearch" % "1.7.2"
libraryDependencies += "org.apache.commons" % "commons-csv" % "1.1"
libraryDependencies += "commons-net" % "commons-net" % "3.3"
libraryDependencies += "com.google.code.gson" % "gson" % "2.3.1"
libraryDependencies += "org.apache.commons" % "commons-compress" % "1.10"
libraryDependencies += "junit" % "junit" % "4.11"
libraryDependencies += "org.avaje" % "ebean" % "2.7.3"
libraryDependencies += "javax.persistence" % "persistence-api" % "1.0.2"
libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"



// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
PlayKeys.externalizeResources := false
