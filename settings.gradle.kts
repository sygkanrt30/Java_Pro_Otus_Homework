rootProject.name = "Homework_Otus_Pro"
include("hw01-gradle")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}
include("hw02-generic")
include("hw03-annotation")
include("hw04-GarbageCollector")
include("hw06-solid")
include("hw05-bytecode")
include("hw07-structuralPatterns")
include("hw09-jdbc")
include("hw07-io")
include("hw11-cache")
include("hw10-hibernate")
include("hw13-Ioc")
