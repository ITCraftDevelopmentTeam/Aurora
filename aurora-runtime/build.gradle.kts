plugins {
    kotlin("jvm") version "2.0.0"
    id("maven-publish")
}

dependencies {
    api(project(":aurora-common"))
    api(project(":aurora-compiler"))

    implementation("net.bytebuddy:byte-buddy:1.14.17")
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

