plugins {
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components.getByName("kotlin"))
    }
}
