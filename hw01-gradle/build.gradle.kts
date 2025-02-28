import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "8.3.6"
    id("java-library")
}

repositories {
    mavenCentral()
}
dependencies {
    implementation ("com.google.guava:guava:33.4.0-jre")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("gradleHelloWorld")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.otus.java.HelloOtus"))
        }
    }
    build {
        dependsOn(shadowJar)
    }

}