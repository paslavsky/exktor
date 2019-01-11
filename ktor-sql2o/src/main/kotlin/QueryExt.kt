package net.paslavsky.ktor.sql2o

import org.sql2o.Query

inline fun <reified T> Query.executeScalar() = executeScalar(T::class.java)

inline fun <reified T> Query.executeAndFetch(): List<T> = executeAndFetch(T::class.java)

inline fun <reified T> Query.executeAndFetchFirst(): T? = executeAndFetchFirst(T::class.java)