# Elastic Client extension for Ktor
[ ![Download](https://api.bintray.com/packages/paslavsky/maven/ktor-elastic/images/download.svg) ](https://bintray.com/paslavsky/maven/ktor-elastic/_latestVersion)
[![Build Status](https://travis-ci.org/paslavsky/exktor.svg?branch=master)](https://travis-ci.org/paslavsky/exktor)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

This module allows to connect to the [Elastic Search](https://www.elastic.co/) on application start and use created 
client instance anywhere inside the `Application` context.

## Quick start
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
        <version>${exktor.version}</version>
    </dependency>
</dependencies>
```

#### Gradle
```groovy
repositories {
  jcenter()
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