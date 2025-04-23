rootProject.name = "minime"

include("preprocessor")
include("parser")
include("optimizer")
include("test-util")

rootProject.children.forEach { subProject ->
    subProject.name = "${rootProject.name}-${subProject.name}"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("antlr", "4.13.2")
            version("slf4j", "2.0.17")
            version("jopt", "5.0.4")

            library("slf4j", "org.slf4j", "slf4j-api").versionRef("slf4j")
            library("antlr-runtime", "org.antlr", "antlr4-runtime").versionRef("antlr")
            library("antlr-buildtime", "org.antlr", "antlr4").versionRef("antlr")
            library("jopt-simple", "net.sf.jopt-simple", "jopt-simple").versionRef("jopt")
        }
    }
}