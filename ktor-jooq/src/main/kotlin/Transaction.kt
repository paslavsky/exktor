package net.paslavsky.ktor.jooq

import io.ktor.application.Application
import io.ktor.routing.Route
import io.ktor.routing.application
import org.jooq.TransactionalRunnable

inline fun <T> Application.transaction(crossinline block: () -> T): T {
    var results: T? = null
    jooq.transaction(TransactionalRunnable { results = block() })
    @Suppress("UNCHECKED_CAST")
    return results as T
}

inline fun <T> Route.transaction(crossinline block: () -> T): T = application.transaction(block)
