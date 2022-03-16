val ktor_version: String by project

dependencies {
    api("com.orbitz.consul:consul-client:1.5.3")
    compileOnly("io.ktor:ktor-server-host-common:$ktor_version")
    compileOnly("io.ktor:ktor-client:$ktor_version")
}