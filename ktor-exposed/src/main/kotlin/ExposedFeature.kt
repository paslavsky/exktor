package net.paslavsky.ktor.exposed

import io.ktor.application.Application
import io.ktor.application.ApplicationEvents
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import net.paslavsky.ktor.sql.DBConnected
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

class ExposedFeature private constructor(
    monitor: ApplicationEvents,
    config: Config
) {
    init {
        val (setupConnection, manager, init) = config
        monitor.subscribe(DBConnected) {
            if (manager != null) {
                Database.connect(
                    datasource = it,
                    setupConnection = setupConnection,
                    manager = manager
                )
            } else {
                Database.connect(
                    datasource = it,
                    setupConnection = setupConnection
                )
            }
            init()
        }
    }

    class Config {
        private var init: () -> Unit = {}
        private var setupConnection: ((Connection) -> Unit) = {}
        private var manager: ((Database) -> TransactionManager)? = null

        internal operator fun component1() = setupConnection
        internal operator fun component2() = manager
        internal operator fun component3() = init

        fun setupConnection(setupConnection: (Connection) -> Unit) {
            this.setupConnection = setupConnection
        }

        fun manager(manager: (Database) -> TransactionManager) {
            this.manager = manager
        }

        fun init(init: () -> Unit) {
            this.init = init
        }
    }

    companion object Feature : ApplicationFeature<Application, Config, ExposedFeature> {
        override val key: AttributeKey<ExposedFeature> = AttributeKey("Exposed")

        override fun install(pipeline: Application, configure: Config.() -> Unit): ExposedFeature {
            return ExposedFeature(pipeline.environment.monitor, Config().apply(configure))
        }
    }
}