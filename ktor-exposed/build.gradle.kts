val exposed_version: String by project

dependencies {
    compile("org.jetbrains.exposed:exposed:$exposed_version")
    compile(project(":ktor-sql"))
}