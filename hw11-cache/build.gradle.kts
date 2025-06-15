plugins {
    id("io.freefair.lombok") version "8.13.1"
}

dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("org.ehcache:ehcache")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("com.zaxxer:HikariCP")
}
