plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JSON
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    // CSV
    implementation("com.jsoizo:kotlin-csv-jvm:1.10.0")
    // HTTP4K
    implementation("org.http4k:http4k-core:5.32.0.0")
    implementation("org.http4k:http4k-client-apache:5.32.0.0")
    implementation("org.http4k:http4k-server-netty:5.32.0.0")
    implementation("org.http4k:http4k-format-jackson:5.32.0.0")

    //JCommander
    implementation("org.jcommander:jcommander:2.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "org.example.MainKt"
}
