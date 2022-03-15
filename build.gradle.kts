import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.bundling.Jar

plugins {
    base
    kotlin("jvm") version "1.3.61" apply false
    id("com.github.ben-manes.versions") version "0.27.0"
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    `maven-publish`
}

val ktor_version: String by project
val _version = project.findProperty("version").let {
    if ((it as? String).isNullOrEmpty() || it == "unspecified") {
        val tag = System.getenv("TRAVIS_TAG")
        if (tag.isNullOrEmpty()) ktor_version else tag
    } else {
        it!!
    }
}

group = "net.paslavsky"
version = _version

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
    version = _version

    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    dependencies {
        fun implementation(dependencyNotation: Any) = this.add("implementation", dependencyNotation)

        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
        implementation("io.ktor:ktor-server-core:$ktor_version")
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
                version = project.version.toString()
            }
        }
    }
}