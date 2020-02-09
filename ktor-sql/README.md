# HikariCP extension for Ktor
[ ![Download](https://api.bintray.com/packages/paslavsky/maven/ktor-sql/images/download.svg) ](https://bintray.com/paslavsky/maven/ktor-sql/_latestVersion)
[![Build Status](https://travis-ci.org/paslavsky/exktor.svg?branch=master)](https://travis-ci.org/paslavsky/exktor)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

This module allows to get up HikariCP connection pool at application start.

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
        <artifactId>ktor-sql</artifactId>
        <version>1.3.1</version>
    </dependency>
</dependencies>
```

#### Gradle
```groovy
repositories {
  jcenter()
}

dependencies {
  implementation 'net.paslavsky:ktor-sql:1.3.1'
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
 