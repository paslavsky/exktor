# HikariCP extension for Ktor
[ ![Version](https://img.shields.io/badge/Version-1.6.7-blue.svg?style=flat&logo=apachemaven) ](https://github.com/paslavsky/exktor/releases/tag/1.6.7)
[![Java CI with Gradle](https://github.com/paslavsky/exktor/actions/workflows/gradle.yml/badge.svg)](https://github.com/paslavsky/exktor/actions/workflows/gradle.yml)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

This module allows to get up HikariCP connection pool at application start.

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
        <artifactId>ktor-sql</artifactId>
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
  implementation "net.paslavsky:ktor-sql:$exktorVersion"
}
```

#### Connection pool configuration
```kotlin
fun Application.module() {
    install(SqlFeature) { // this: HikariConfig
        // ...
    }
}
```

Almost all configs could be configured at the configuration file:
* dataSource.dataSourceClassName
* dataSource.jdbcUrl
* dataSource.username
* dataSource.password
* dataSource.autoCommit
* dataSource.connectionTimeout
* dataSource.idleTimeout
* dataSource.maxLifetime
* dataSource.connectionTestQuery
* dataSource.minimumIdle
* dataSource.maximumPoolSize
* dataSource.poolName
* dataSource.initializationFailTimeout
* dataSource.isolateInternalQueries
* dataSource.allowPoolSuspension
* dataSource.readOnly
* dataSource.registerMbeans
* dataSource.catalog
* dataSource.connectionInitSql
* dataSource.driverClassName
* dataSource.transactionIsolation
* dataSource.validationTimeout
* dataSource.leakDetectionThreshold
* dataSource.schema

For more information please read [HikaryCP official documentation](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby).

**Note**: Programmatic configuration will override configs from configuration file

#### Access to the connection pool
Anywhere inside `Application` context you could use variable `dataSource` 
(`javax.sql.DataSource`) to access to the connection pool.

#### Events
`ktor-sql` module listen to standard ktor events to create/close connection poll and producing own events:
* _ApplicationStarted_
  * **DBConnecting** - before create connection pool
  * **DBConnected** - when connection pool was successfully created
* _ApplicationStopPreparing_
  * **DBClosing** - before closing connection pool
  * **DBClosed** - connection poll was closed
 