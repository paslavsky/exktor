import io.ktor.application.Application
import io.ktor.application.ApplicationEvents
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import net.paslavsky.ktor.sql.DBConnected
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

/**
 * TODO Describe
 *
 * @author Andrey Paslavsky
 * @since 0.0.1
 */
class ExposedFeature private constructor(
    monitor: ApplicationEvents,
    config: Config
) {
    init {
        val (setupConnection, manager, init) = config
        val setupConnectionOrDefault = setupConnection ?: {}
        monitor.subscribe(DBConnected) {
            if (manager != null) {
                Database.connect(
                    datasource = it,
                    setupConnection = setupConnectionOrDefault,
                    manager = manager
                )
            } else {
                Database.connect(
                    datasource = it,
                    setupConnection = setupConnectionOrDefault
                )
            }
            init()
        }
    }

    class Config {
        internal operator fun component1() = setupConnection
        internal operator fun component2() = manager
        internal operator fun component3() = init

        var setupConnection: ((Connection) -> Unit)? = null
        var manager: ((Database) -> TransactionManager)? = null
        var init: () -> Unit = {}
    }

    companion object Feature : ApplicationFeature<Application, Config, ExposedFeature> {
        override val key: AttributeKey<ExposedFeature> = AttributeKey("Exposed")

        override fun install(pipeline: Application, configure: Config.() -> Unit): ExposedFeature {
            return ExposedFeature(pipeline.environment.monitor, Config().apply(configure))
        }
    }
}