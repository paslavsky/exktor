val flyway_version: String by project

dependencies {
    api("org.flywaydb:flyway-core:$flyway_version")
    api(project(":ktor-sql"))
}