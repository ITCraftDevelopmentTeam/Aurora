import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("maven-publish")
    application
}

group = "pixel.aurora"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.58.0")
    implementation("com.google.guava:guava:33.1.0-jre")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = java.targetCompatibility.toString()
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components.getByName("kotlin"))
    }
}
