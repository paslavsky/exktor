package net.paslavsky.ktor.sql

import io.ktor.application.Application
import javax.sql.DataSource

internal val dataSources = HashMap<Application, DataSource>()

val Application.dataSource: DataSource
    get() = checkNotNull(dataSources[this]) {
        "DataSource not configured or still not initialized"
    }