name := "Albatross"

version := "0.0.1"

scalaVersion := "2.10.3"

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/releases/",
  "jogamp" at "http://jogamp.org/deployment/maven"
)

libraryDependencies ++= Seq(
  "java3d" % "j3d-core-utils" % "1.3.1",
  "java3d" % "j3d-core" % "1.3.1",
  "java3d" % "vecmath" % "1.3.1",
  "org.scalanlp" % "breeze_2.10" % "0.7-SNAPSHOT",
  "org.scalanlp" % "breeze-natives_2.10" % "0.7-SNAPSHOT",
  "com.fifesoft" % "rsyntaxtextarea" % "2.5.0",
  "org.jogamp.jogl" % "jogl-all-main" % "2.1.4",
  "org.jogamp.gluegen" % "gluegen-rt-main" % "2.1.4"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.0" % "test"

unmanagedSourceDirectories in Compile <++= baseDirectory { base =>
  Seq(
    base / "src/main/org/albatrosscad/",
    base / "src/main/org/albatrosscad/devices"
  )
}

