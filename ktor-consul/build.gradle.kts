val consul_version: String by project
val ktor_version: String by project

dependencies {
    api("com.orbitz.consul:consul-client:$consul_version")
    compileOnly("io.ktor:ktor-server-host-common:$ktor_version")
    compileOnly("io.ktor:ktor-client:$ktor_version")
}