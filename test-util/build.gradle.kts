plugins {
    id("java-library")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.antlr.runtime)

    api("org.mockito:mockito-core:2.9.0")

    api("org.junit.jupiter:junit-jupiter-api:5.5.0")
    api("org.junit.jupiter:junit-jupiter-params:5.5.0")
    api("org.junit.jupiter:junit-jupiter-engine:5.5.0")
}
