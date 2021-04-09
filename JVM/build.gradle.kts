import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.32"
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("org.jetbrains.dokka") version "1.4.30"
}

group = "me.cyberdie22"
version = "2021.0"

dependencies {
    implementation(project(":IR"))
    implementation("com.diogonunes:JColor:5.0.1")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
    withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "13"
    }
    withType<ShadowJar>() {
        minimize()
        manifest {
            attributes(mapOf(
                "Main-Class" to "MainKt"
            ))
        }
    }
    register<Jar>("buildWithDocumentation") {
        group = "build"
        dependsOn(dokkaHtml, dokkaGfm, dokkaJekyll, shadowJar)
    }
}

application {
    mainClassName = "MainKt"
}