organization := "com.azaptree"

name := "azaptree-commons"

version := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.10.2"

autoCompilerPlugins := true

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test" 

libraryDependencies += "com.typesafe" % "config" % "1.0.1"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.12"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "commons-codec" % "commons-codec" % "1.8"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.2"

libraryDependencies += compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")

scalacOptions ++= Seq("-P:continuations:enable",
					  "-optimise",
					  "-feature",
					  "-language:postfixOps",
					  "-language:higherKinds",
					  "-deprecation")

scalariformSettings

net.virtualvoid.sbt.graph.Plugin.graphSettings


