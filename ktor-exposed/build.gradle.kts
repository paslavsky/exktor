val exposed_version: String by project

dependencies {
    api("org.jetbrains.exposed:exposed-core:$exposed_version")
    api("org.jetbrains.exposed:exposed-dao:$exposed_version")
    api("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    api(project(":ktor-sql"))
}