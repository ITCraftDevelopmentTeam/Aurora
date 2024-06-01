import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
}

group = "pixel.aurora"
version = "1.0.0"

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlin {
            jvmToolchain(21)
        }
    }

    tasks.withType<Jar> {
        manifest {
            attributes("Implementation-Version" to project.version)
        }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components.getByName("kotlin"))
    }
}
