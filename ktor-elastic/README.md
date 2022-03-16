# Elastic Client extension for Ktor
[ ![Version](https://img.shields.io/badge/Version-1.6.7-blue.svg?style=flat&logo=apachemaven) ](https://github.com/paslavsky/exktor/releases/tag/1.6.7)
[![Java CI with Gradle](https://github.com/paslavsky/exktor/actions/workflows/gradle.yml/badge.svg)](https://github.com/paslavsky/exktor/actions/workflows/gradle.yml)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

This module allows to connect to the [Elastic Search](https://www.elastic.co/) on application start and use created 
client instance anywhere inside the `Application` context.

## Quick start
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

#### Configure client
```kotlin
fun Application.module() {
    install(ElasticFeature) {
        hosts = arrayOf(HttpHost.create("http://192.168.99.100:9300"))
        defaultHeaders = arrayOf(BasicHeader("MyHeader", "MyValue"))
        failureListener = {
            // Implementation of the RestClient.FailureListener
        } 
        httpClientConfigCallback = {
            // Implementation of the RestClientBuilder.HttpClientConfigCallback
        } 
        requestConfigCallback = {
            // Implementation of the RestClientBuilder.RequestConfigCallback
        }
        pathPrefix = "..."
        nodeSelector = NodeSelector.ANY
        strictDeprecationMode = true
    }
}
```

#### Elastic client
Anywhere inside `Application` context you could use variable `elasticSearchClient` 
(`org.elasticsearch.client.RestClient`) to access to the Elastic Search cluster.