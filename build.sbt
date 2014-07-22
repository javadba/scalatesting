name := "general"

version := "0.1.0"

scalaVersion := "2.10.4"

organization := "com.blazedb"

libraryDependencies ++= {
  Seq(
    "org.scalatest" % "scalatest_2.10" % "2.0"
    ,"log4j" % "log4j" % "1.2.17"
  )
}

// if you have more than one main method, you can specify which is used when typing 'run' in sbt
mainClass := Some("com.blazedb.scalatest.MultiThreadedTest")

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"        at "http://oss.sonatype.org/content/repositories/releases"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest,"-oDF")
