plugins {
    // Apply the java plugin to add support for Java
    id("java")

    // Apply the application plugin to add support for building a CLI application
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    // Use Maven Central for resolving dependencies
    mavenCentral()
}

dependencies {
    // The star of our CLI: Picocli
    implementation("info.picocli:picocli:4.7.6")
    implementation("org.apache.maven:maven-model:3.9.6")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Use JUnit Jupiter for testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    // Define the main class for the application
    mainClass.set("com.ratan.guardian.GuardianCli")
}

tasks.withType<Test> {
    useJUnitPlatform()
}