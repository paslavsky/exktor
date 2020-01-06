# Extension modules for Ktor
[ ![Download](https://api.bintray.com/packages/paslavsky/maven/ktor-sql/images/download.svg) ](https://bintray.com/paslavsky/maven/ktor-sql/_latestVersion)
[![Build Status](https://travis-ci.org/paslavsky/exktor.svg?branch=master)](https://travis-ci.org/paslavsky/exktor)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

## Modules
* [ktor-consule](ktor-consule/README.md)
* [ktor-elastic](ktor-elastic/README.md)
* [ktor-exposed](ktor-exposed/README.md)
* [ktor-flyway](ktor-flyway/README.md)
* [ktor-sql](ktor-sql/README.md)
* [ktor-sql2o](ktor-sql2o/README.md)

## Quick start
This guide shows how to setup [ktor-elastic](ktor-elastic/README.md)

#### Maven
```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <name>jcenter</name>
      <url>http://jcenter.bintray.com</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>net.paslavsky</groupId>
        <artifactId>ktor-elastic</artifactId>
        <version>1.2.6</version>
    </dependency>
</dependencies>
```

#### Gradle
```groovy
repositories {
  jcenter()
}

dependencies {
  implementation 'net.paslavsky:ktor-elastic:1.2.6'
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
