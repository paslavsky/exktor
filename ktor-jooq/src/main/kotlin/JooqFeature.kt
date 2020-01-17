package net.paslavsky.ktor.jooq

import io.ktor.application.Application
import io.ktor.application.ApplicationEvents
import io.ktor.application.ApplicationFeature
import io.ktor.application.ApplicationStopPreparing
import io.ktor.util.AttributeKey
import net.paslavsky.ktor.sql.DBClosed
import net.paslavsky.ktor.sql.DBClosing
import net.paslavsky.ktor.sql.DBConnected
import org.jooq.*
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import java.time.Clock
import java.util.concurrent.Executor

class JooqFeature private constructor(
    pipeline: Application,
    monitor: ApplicationEvents,
    config: Config
) {
    init {
        pipeline.attachDaoFactory(config.daoFactory)
        monitor.subscribe(DBConnected) { dataSource ->
            pipeline.attachJooqContext(
                config.configuration.set(dataSource).let {
                    pipeline.attachJooqConfig(it)
                    DSL.using(it)
                }
            )
        }
        monitor.subscribe(DBClosing) {
            try {
                pipeline.jooq.close()
            } catch (ignore: Exception) {
            }
        }
        monitor.subscribe(DBClosed) {
            pipeline.detachJooqConfig()
            pipeline.detachJooqContext()
        }
        monitor.subscribe(ApplicationStopPreparing) {
            pipeline.detachDaoFactory()
        }
    }

    class Config(internal val configuration: DefaultConfiguration = DefaultConfiguration()) {
        var metaProvider: MetaProvider
            get() = configuration.metaProvider()
            set(value) {
                configuration.set(value)
            }

        var executor: Executor
            get() = configuration.executorProvider().provide()
            set(value) {
                configuration.set(value)
            }

        var executorProvider: ExecutorProvider
            get() = configuration.executorProvider()
            set(value) {
                configuration.set(value)
            }

        var transactionProvider: TransactionProvider
            get() = configuration.transactionProvider()
            set(value) {
                configuration.set(value)
            }

        var recordMapper: RecordMapper<*, *>
            get() = throw NotImplementedError()
            set(value) {
                configuration.set(value)
            }

        var recordMapperProvider: RecordMapperProvider
            get() = configuration.recordMapperProvider()
            set(value) {
                configuration.set(value)
            }

        var recordUnmapper: RecordUnmapper<*, *>
            get() = throw NotImplementedError()
            set(value) {
                configuration.set(value)
            }

        var recordUnmapperProvider: RecordUnmapperProvider
            get() = configuration.recordUnmapperProvider()
            set(value) {
                configuration.set(value)
            }

        var recordListeners: Array<RecordListener>
            get() = configuration.recordListenerProviders().map { it.provide() }.toTypedArray()
            set(value) {
                configuration.set(*value)
            }

        var recordListenerProviders: Array<RecordListenerProvider>
            get() = configuration.recordListenerProviders()
            set(value) {
                configuration.set(*value)
            }

        var executeListeners: Array<ExecuteListener>
            get() = configuration.executeListenerProviders().map { it.provide() }.toTypedArray()
            set(value) {
                configuration.set(*value)
            }

        var executeListenerProviders: Array<ExecuteListenerProvider>
            get() = configuration.executeListenerProviders()
            set(value) {
                configuration.set(*value)
            }

        var visitListeners: Array<VisitListener>
            get() = configuration.visitListenerProviders().map { it.provide() }.toTypedArray()
            set(value) {
                configuration.set(*value)
            }

        var visitListenerProvider: Array<VisitListenerProvider>
            get() = configuration.visitListenerProviders()
            set(value) {
                configuration.set(*value)
            }

        var transactionListeners: Array<TransactionListener>
            get() = configuration.transactionListenerProviders().map { it.provide() }.toTypedArray()
            set(value) {
                configuration.set(*value)
            }

        var transactionListenerProviders: Array<TransactionListenerProvider>
            get() = configuration.transactionListenerProviders()
            set(value) {
                configuration.set(*value)
            }

        var diagnosticsListeners: Array<DiagnosticsListener>
            get() = configuration.diagnosticsListenerProviders().map { it.provide() }.toTypedArray()
            set(value) {
                configuration.set(*value)
            }

        var diagnosticsListenerProviders: Array<DiagnosticsListenerProvider>
            get() = configuration.diagnosticsListenerProviders()
            set(value) {
                configuration.set(*value)
            }

        var unwrapper: Unwrapper
            get() = configuration.unwrapperProvider().provide()
            set(value) {
                configuration.set(value)
            }

        var unwrapperProvider: UnwrapperProvider
            get() = configuration.unwrapperProvider()
            set(value) {
                configuration.set(value)
            }

        var clock: Clock
            get() = configuration.clock()
            set(value) {
                configuration.set(value)
            }

        var dialect: SQLDialect
            get() = configuration.dialect()
            set(value) {
                configuration.set(value)
            }

        var settings: Settings
            get() = configuration.settings()
            set(value) {
                configuration.set(value)
            }

        var daoFactory: DaoFactory<*> = defaultDaoFactory
    }

    companion object Feature : ApplicationFeature<Application, Config, JooqFeature> {
        override val key = AttributeKey<JooqFeature>("JooqFeature")

        override fun install(pipeline: Application, configure: Config.() -> Unit): JooqFeature {
            return JooqFeature(pipeline, pipeline.environment.monitor, Config().apply(configure))
        }
    }
}
