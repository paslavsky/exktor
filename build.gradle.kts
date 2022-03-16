import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.bundling.Jar

plugins {
    base
    kotlin("jvm") version "1.6.10" apply false
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    `maven-publish`
}

buildscript {
    extra["ktor_version"] = "1.6.7"
}

val ktorVersion = "1.6.7"

val exKtorVersion = project.findProperty("version").let {
    if (it is String && it != "unspecified") {
        it
    } else {
        val tag = System.getenv("RELEASE_VERSION")
        if (tag.isNullOrEmpty()) ktorVersion else tag
    }
}

group = "net.paslavsky"
version = exKtorVersion

val username = project.findProperty("gpr.user") ?: env.fetchOrNull("GH_USERNAME")
val password = project.findProperty("gpr.key") ?: env.fetchOrNull("GH_TOKEN")

val Project.sourceSets: SourceSetContainer get() =
    (this as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer

subprojects {
    apply {
        plugin("kotlin")
        plugin("org.gradle.maven-publish")
    }

    group = "net.paslavsky"
    version = exKtorVersion

    repositories {
        mavenCentral()
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    dependencies {
        fun implementation(dependencyNotation: Any) = this.add("implementation", dependencyNotation)

        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
        implementation("io.ktor:ktor-server-core:$ktorVersion")
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    val project = this
    publishing {
        repositories {
            maven {
                name = "besttera-core"
                url = uri("https://maven.pkg.github.com/paslavsky/exktor")
                credentials {
                    username = username
                    password = password
                }
            }
        }
        publications {
            create<MavenPublication>("gpr") {
                from(components["kotlin"])
                artifact(sourcesJar.get())
                groupId = project.group.toString()
                artifactId = project.name
                version = exKtorVersion
            }
        }
    }
}