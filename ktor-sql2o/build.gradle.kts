val sql2o_version: String by project

dependencies {
    api("org.sql2o:sql2o:$sql2o_version")
    api(project(":ktor-sql"))
}