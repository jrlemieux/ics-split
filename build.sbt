

name := "ics_split"

version := "1.0"

scalaVersion := "2.13.6"

Compile / mainClass := Some("cc.lemieux.ics_split.Main")

assembly / assemblyJarName := "ics_split.jar"

scalacOptions ++= Seq("-deprecation")