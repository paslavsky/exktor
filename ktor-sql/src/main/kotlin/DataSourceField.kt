package net.paslavsky.ktor.sql

import io.ktor.application.Application
import io.ktor.util.AttributeKey
import javax.sql.DataSource

private val dataSourceKey = AttributeKey<DataSource>("DataSource")

val Application.dataSource: DataSource
    get() = checkNotNull(attributes.getOrNull(dataSourceKey)) {
        "DataSource not configured or still not initialized"
    }

internal fun Application.attachDataSource(value: DataSource) {
    attributes.put(dataSourceKey, value)
}

internal fun Application.detachDataSource() = attributes.remove(dataSourceKey)