package net.paslavsky.ktor.sql2o

import io.ktor.application.Application
import org.sql2o.StatementRunnableWithResult
import org.sql2o.Connection

enum class TransactionIsolation(internal val value: Int) {
    TRANSACTION_NONE(java.sql.Connection.TRANSACTION_NONE),
    TRANSACTION_READ_UNCOMMITTED(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED),
    TRANSACTION_READ_COMMITTED(java.sql.Connection.TRANSACTION_READ_COMMITTED),
    TRANSACTION_REPEATABLE_READ(java.sql.Connection.TRANSACTION_REPEATABLE_READ),
    TRANSACTION_SERIALIZABLE(java.sql.Connection.TRANSACTION_SERIALIZABLE)
}

fun <R> Application.transaction(
    isolation: TransactionIsolation = TransactionIsolation.TRANSACTION_READ_COMMITTED,
    block: Connection.() -> R
) = sql2o.runInTransaction(
        StatementRunnableWithResult<R> { connection, _ -> connection.block() },
        null,
        isolation.value
    ) as R