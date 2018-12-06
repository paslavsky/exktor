package net.paslavsky.ktor.sql

import com.zaxxer.hikari.HikariConfig
import io.ktor.application.*
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import com.zaxxer.hikari.HikariDataSource

class SqlFeature private constructor(
    private val config: Config,
    private val monitor: ApplicationEvents
) {
    init {
        monitor.subscribe(ApplicationStarted, ::onStarted)
        monitor.subscribe(ApplicationStopPreparing, ::onStopPreparing)
    }

    private lateinit var dataSource: HikariDataSource

    class Config : HikariConfig()

    private fun onStarted(app: Application) {
        monitor.raise(DBConnecting, app)
        dataSource = HikariDataSource(config)
        dataSources[app] = dataSource
        monitor.raise(DBConnected, dataSource)
    }

    private fun onStopPreparing(environment: ApplicationEnvironment) {
        monitor.raise(DBClosing, dataSource)
        if (dataSource.isClosed) {
            dataSource.close()
        }
        monitor.raise(DBClosed, environment)
    }

    companion object Feature : ApplicationFeature<Application, Config, SqlFeature> {
        override val key: AttributeKey<SqlFeature> = AttributeKey("SQL")

        @KtorExperimentalAPI
        override fun install(pipeline: Application, configure: Config.() -> Unit): SqlFeature {
            val configuration = SqlFeature.Config().apply {
                fun prop(name: String) =
                    pipeline.environment.config.propertyOrNull("dataSource.$name")?.getString()

                fun set(name: String, body: (s: String) -> Unit) {
                    val value = prop(name)
                    if (!value.isNullOrBlank()) {
                        body(value)
                    }
                }

                fun set(name: String, body: (b: Boolean) -> Unit) {
                    val value = prop(name)
                    if (value != null) {
                        body(value.equals("true", true) || value.equals("on", true))
                    }
                }

                fun set(name: String, body: (l: Long) -> Unit) {
                    val value = prop(name)
                    if (value != null) {
                        body(value.toLong())
                    }
                }

                fun set(name: String, body: (i: Int) -> Unit) {
                    val value = prop(name)
                    if (value != null) {
                        body(value.toInt())
                    }
                }

                set("dataSourceClassName", this::setDataSourceClassName)
                set("jdbcUrl", this::setJdbcUrl)
                set("username", this::setUsername)
                set("password", this::setPassword)
                set("autoCommit", this::setAutoCommit)
                set("connectionTimeout", this::setConnectionTimeout)
                set("idleTimeout", this::setIdleTimeout)
                set("maxLifetime", this::setMaxLifetime)
                set("connectionTestQuery", this::setConnectionTestQuery)
                set("minimumIdle", this::setMinimumIdle)
                set("maximumPoolSize", this::setMaximumPoolSize)
                set("poolName", this::setPoolName)
                set("initializationFailTimeout", this::setInitializationFailTimeout)
                set("isolateInternalQueries", this::setIsolateInternalQueries)
                set("allowPoolSuspension", this::setAllowPoolSuspension)
                set("readOnly", this::setReadOnly)
                set("registerMbeans", this::setRegisterMbeans)
                set("catalog", this::setCatalog)
                set("connectionInitSql", this::setConnectionInitSql)
                set("driverClassName", this::setDriverClassName)
                set("transactionIsolation", this::setTransactionIsolation)
                set("validationTimeout", this::setValidationTimeout)
                set("leakDetectionThreshold", this::setLeakDetectionThreshold)
                set("schema", this::setSchema)
            }.apply(configure)
            return SqlFeature(configuration, pipeline.environment.monitor)
        }
    }
}