plugins {
    kotlin("jvm") version "2.0.21"
    `java-library`
    `maven-publish`
}

group = "dev.terrorify"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
    maven("https://repo.lunarclient.dev")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:1.21.4-f17a9aa3ea")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
