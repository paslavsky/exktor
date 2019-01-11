package net.paslavsky.ktor.sql2o

import io.ktor.application.Application
import io.ktor.util.AttributeKey
import org.sql2o.Sql2o

private val key = AttributeKey<Sql2o>("sql2o")

val Application.sql2o get() = attributes[key]

internal fun Application.attachSql2o(sql2o: Sql2o) {
    attributes.put(key, sql2o)
}

internal fun Application.detachSql2o() {
    attributes.remove(key)
}