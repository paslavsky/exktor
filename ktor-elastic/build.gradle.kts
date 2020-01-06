val elasticsearch_rest_client_version: String by project

dependencies {
    implementation("org.elasticsearch.client:elasticsearch-rest-client:$elasticsearch_rest_client_version")
}