import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.bundling.Jar

plugins {
    base
    kotlin("jvm") version "1.3.30" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
    id("com.github.ben-manes.versions") version "0.20.0"
    `maven-publish`
}

group = "net.paslavsky"
version = "1.1.4"

val ktor_version: String by project

val Project.sourceSets: SourceSetContainer get() =
    (this as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer

subprojects {
    apply {
        plugin("kotlin")
        plugin("com.jfrog.bintray")
        plugin("org.gradle.maven-publish")
    }

    group = "net.paslavsky"
    version = "1.1.4"

    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    dependencies {
        fun compile(dependencyNotation: Any) = this.add("compile", dependencyNotation)

        compile(kotlin("stdlib-jdk8"))
        compile(kotlin("reflect"))
        compile("io.ktor:ktor-server-core:$ktor_version")
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    fun bintray(configure: BintrayExtension.() -> Unit) = (this as ExtensionAware).extensions.configure("bintray", configure)

    bintray {
        user = System.getenv("BINTRAY_USER")
        key = System.getenv("BINTRAY_KEY")
        setPublications("mavenJava")

        pkg(closureOf<BintrayExtension.PackageConfig> {
            repo = "maven"
            name = project.name
            vcsUrl = "https://github.com/paslavsky/exktor"
            setLicenses("Apache-2.0")
        })
    }

    val project = this
    publishing {
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])
                artifact(sourcesJar.get())
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
            }
        }
    }
}