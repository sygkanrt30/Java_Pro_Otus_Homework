plugins {
    id("io.freefair.lombok") version "8.13.1"
}

dependencies {
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("com.zaxxer:HikariCP")
}