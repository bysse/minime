plugins {
	id("antlr")
	id("java-library")
	id("maven-publish")
}

val antlrConfig = mapOf(
		"grammarpackage" to "com.tazadum.glsl.parser",
		"antlrSource" to "src/main/antlr",
		"destinationDir" to "src/generated-sources/java"
)

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
	api(project(":minime-preprocessor"))
	antlr(libs.antlr.buildtime)
	testImplementation(project(":minime-test-util"))
}

configurations {
	named("api") {
		// Do not export the antlr4 build time dependency to the API configuration
		// Work around from: https://github.com/gradle/gradle/issues/820
		setExtendsFrom(extendsFrom.filterNot { it.name == "antlr" })
	}
}

tasks.withType<AntlrTask>().configureEach {
	arguments.addAll(listOf("-package", antlrConfig["grammarpackage"].toString(), "-visitor"))
	outputDirectory = file("${projectDir}/generated-src/com/tazadum/glsl/parser")
}

val cleanAntlr by tasks.registering(Delete::class) {
	delete(file("${projectDir}/generated-src"))
}

tasks.named("clean") {
	dependsOn(cleanAntlr)
}

sourceSets {
	named("main") {
		java.srcDir(file("${projectDir}/generated-src"))
	}
}
