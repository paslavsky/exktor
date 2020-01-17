package net.paslavsky.ktor.jooq

import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.util.AttributeKey
import org.jooq.Configuration
import org.jooq.DSLContext
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

typealias DaoFactory<T> = (daoClass: KClass<T>, context: DSLContext, configuration: Configuration) -> T

val defaultDaoFactory: DaoFactory<*> = { daoClass, context, configuration ->
    daoClass.constructors.let { collection ->
        collection.firstOrNull {
            it.parameters.size == 1 && Configuration::class.isSuperclassOf(it.parameters[0].type.classifier as KClass<*>)
        }?.call(configuration) ?: collection.firstOrNull {
            it.parameters.size == 1 && DSLContext::class.isSuperclassOf(it.parameters[0].type.classifier as KClass<*>)
        }?.call(context) ?: collection.firstOrNull {
            it.parameters.isEmpty()
        }?.call() ?: throw InstantiationException("Could not create instance of the ${daoClass.qualifiedName}: no suitable constructors")
    }
}

val keys = ConcurrentHashMap<KClass<*>, AttributeKey<*>>()

inline fun <reified T : Any> Application.dao(): T {
    @Suppress("UNCHECKED_CAST")
    val key = keys.computeIfAbsent(T::class) {
        AttributeKey<T>("jooq::dao::${T::class.simpleName}")
    } as AttributeKey<T>

    return attributes.computeIfAbsent(key) { daoFactory(T::class, jooq, jooqConfig) as T }
}

inline fun <reified T : Any> Routing.dao(): T = application.dao()