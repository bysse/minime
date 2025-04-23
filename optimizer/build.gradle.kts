import org.apache.tools.ant.filters.ReplaceTokens

plugins {
	id("com.github.johnrengelman.shadow") version "7.0.0"
	id("java-library")
	id("maven-publish")
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
	api(project(":minime-parser"))
	api(libs.jopt.simple)
	api(libs.antlr.runtime)

	testImplementation("org.jogamp.jogl:jogl-all:2.3.2")
	testImplementation("org.jogamp.gluegen:gluegen-rt-main:2.3.2")
	testImplementation(project(":minime-test-util"))
}

tasks.named<Jar>("jar") {
	manifest {
		attributes(
				"Implementation-Title" to "GLSL-Optimizer",
				"Implementation-Version" to project.version,
				"Main-Class" to "com.tazadum.glsl.cli.OptimizerMain"
		)
	}
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
	configurations = listOf(project.configurations.runtimeClasspath.get())
	archiveBaseName.set("minime")
	archiveClassifier.set("")
}


tasks.register<Copy>("dist") {
	dependsOn(tasks.named("shadowJar"))
	finalizedBy("addPayload", "distZip")
	from(tasks.named("shadowJar"))
	from("src/main/scripts") {
		filter<ReplaceTokens>("tokens" to mapOf("VERSION" to project.version.toString()))
	}
	into("${layout.buildDirectory.get()}/dist")
}

tasks.register<Copy>("copyInstallTemplate") {
	dependsOn(tasks.named("processResources"))
	from("src/main/build-scripts/") {
		filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
	}
	into("build")
}

tasks.named("jar") {
	dependsOn("copyInstallTemplate")
}

tasks.named("shadowJar") {
	dependsOn("copyInstallTemplate")
}


tasks.register<Exec>("addPayload") {
	dependsOn("dist", "copyInstallTemplate")
	commandLine("${layout.buildDirectory.get()}/add_payload.sh")
}

tasks.register<Zip>("distZip") {
	dependsOn("dist", "copyInstallTemplate")
	archiveFileName.set("minime-${project.version}.zip")
	destinationDirectory.set(project.layout.buildDirectory.get())
	from("${layout.buildDirectory.get()}/dist")
}
