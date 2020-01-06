val flyway_version: String by project

dependencies {
    implementation("org.flywaydb:flyway-core:$flyway_version")
    implementation(project(":ktor-sql"))
}