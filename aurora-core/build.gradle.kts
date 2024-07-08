plugins {
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
}

dependencies {
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
}

dependencies {
    testApi(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components.getByName("kotlin"))
    }
}
