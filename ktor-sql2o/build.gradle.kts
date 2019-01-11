val sql2o_version: String by project

dependencies {
    compile("org.sql2o:sql2o:$sql2o_version")
    compile(project(":ktor-sql"))
}