@file:Suppress("EXPERIMENTAL_API_USAGE")
package net.paslavsky.ktor.sql

import io.ktor.application.Application
import io.ktor.application.ApplicationEnvironment
import io.ktor.application.EventDefinition
import javax.sql.DataSource

val DBConnecting = EventDefinition<Application>()
val DBConnected = EventDefinition<DataSource>()
val DBClosing = EventDefinition<DataSource>()
val DBClosed = EventDefinition<ApplicationEnvironment>()