plugins {
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
}

dependencies {
    api(project(":aurora-common"))
    api("io.arrow-kt:arrow-core:1.2.4")
    api("com.google.guava:guava:33.1.0-jre")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
}

dependencies {
    testApi(kotlin("test"))
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components.getByName("kotlin"))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
