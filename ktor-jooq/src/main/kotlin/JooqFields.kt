package net.paslavsky.ktor.jooq

import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.util.AttributeKey
import org.jooq.Configuration
import org.jooq.DSLContext

private val contextKey = AttributeKey<DSLContext>("jooq::context")
val Application.jooq get() = attributes[contextKey]
inline val Routing.jooq get() = application.jooq
internal fun Application.attachJooqContext(dslContext: DSLContext) = attributes.put(contextKey, dslContext)
internal fun Application.detachJooqContext() = attributes.remove(contextKey)

private val configKey = AttributeKey<Configuration>("jooq::configuration")
val Application.jooqConfig get() = attributes[configKey]
inline val Routing.jooqConfig get() = application.jooqConfig
internal fun Application.attachJooqConfig(configuration: Configuration) = attributes.put(configKey, configuration)
internal fun Application.detachJooqConfig() = attributes.remove(configKey)

private val daoFactoryKey = AttributeKey<DaoFactory<*>>("jooq::daoFactory")
val Application.daoFactory get() = attributes[daoFactoryKey]
inline val Routing.daoFactory get() = application.daoFactory
internal fun Application.attachDaoFactory(daoFactory: DaoFactory<*>) = attributes.put(daoFactoryKey, daoFactory)
internal fun Application.detachDaoFactory() = attributes.remove(daoFactoryKey)
