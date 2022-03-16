# Extension modules for Ktor
[ ![Version](https://img.shields.io/badge/Version-1.6.7-blue.svg?style=flat&logo=apachemaven) ](https://github.com/paslavsky/exktor/releases/tag/1.6.7)
[![Java CI with Gradle](https://github.com/paslavsky/exktor/actions/workflows/gradle.yml/badge.svg)](https://github.com/paslavsky/exktor/actions/workflows/gradle.yml)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

ExKtor - it's set of extension libraries for Ktor that simplify integration with 3rd party applications.

## Modules
* [ktor-consul](ktor-consul/README.md)
* [ktor-elastic](ktor-elastic/README.md)
* [ktor-exposed](ktor-exposed/README.md)
* [ktor-flyway](ktor-flyway/README.md)
* [ktor-flyway](ktor-flyway/README.md)
* [ktor-sql](ktor-sql/README.md)
* [ktor-sql2o](ktor-sql2o/README.md)

## Quick start
This guide shows how to setup [ktor-elastic](ktor-elastic/README.md)

#### Maven
```xml
<repositories>
    <repository>
        <id>exktor</id>
        <url>https://maven.pkg.github.com/paslavsky/exktor</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>net.paslavsky</groupId>
        <artifactId>ktor-elastic</artifactId>
        <version>${exktor.version}</version>
    </dependency>
</dependencies>
```

#### Gradle
```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/paslavsky/exktor")
    }
}

dependencies {
  implementation "net.paslavsky:ktor-elastic:$exktorVersion"
}
```

#### Configuring Elastic Rest Client
```kotlin

fun Application.module() {
    install(ElasticFeature) {
        hosts = arrayOf("http://my-elastic-host.com")
    }
    // ...
}
```
For more details please see [module documentation](ktor-elastic/README.md)

## Contributing

Please use [Issues](https://github.com/paslavsky/exktor/issues) to propose new ideas or report the bug. 
PR are welcome if you know how to fix or/and improve something :smile_cat:
