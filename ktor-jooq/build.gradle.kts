val jooq_version: String by project

dependencies {
    api("org.jooq:jooq:$jooq_version")
    api(project(":ktor-sql"))
}