val sql2o_version: String by project

dependencies {
    implementation("org.sql2o:sql2o:$sql2o_version")
    implementation(project(":ktor-sql"))
}