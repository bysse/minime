allprojects {
    group = "com.tazadum.glsl"
    version = "1.4.3"
}

subprojects {
    repositories {
        mavenCentral()
    }
    /*
    // Use extra property for antlrVersion
    extra["antlrVersion"] = "4.7.2"

    tasks.withType<Javadoc> {
        options.addStringOption("Xdoclint:none", "-quiet")
    }

    publishing {
        publications {
            create<MavenPublication>("lib") {
                from(components["java"])
            }
        }
    }
    */

}
