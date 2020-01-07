package net.paslavsky.ktor.jooq

import io.ktor.application.Application
import io.ktor.util.AttributeKey
import org.jooq.Configuration
import org.jooq.DSLContext

private val contextKey = AttributeKey<DSLContext>("jooq::context")
val Application.jooq get() = attributes[contextKey]
internal fun Application.attachJooqContext(dslContext: DSLContext) = attributes.put(contextKey, dslContext)
internal fun Application.detachJooqContext() = attributes.remove(contextKey)

private val configKey = AttributeKey<Configuration>("jooq::configuration")
val Application.jooqConfig get() = attributes[configKey]
internal fun Application.attachJooqConfig(configuration: Configuration) = attributes.put(configKey, configuration)
internal fun Application.detachJooqConfig() = attributes.remove(configKey)
