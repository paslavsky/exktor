val flyway_version: String by project

dependencies {
    compile("org.flywaydb:flyway-core:$flyway_version")
    compile(project(":ktor-sql"))
}