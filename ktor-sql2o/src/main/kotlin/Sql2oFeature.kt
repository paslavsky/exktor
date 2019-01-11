package net.paslavsky.ktor.sql2o

import io.ktor.application.Application
import io.ktor.application.ApplicationEvents
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import net.paslavsky.ktor.sql.DBClosed
import net.paslavsky.ktor.sql.DBConnected
import org.sql2o.Sql2o
import org.sql2o.quirks.Quirks

class Sql2oFeature private constructor(
    pipeline: Application,
    monitor: ApplicationEvents,
    config: Config
) {
    init {
        val (quirks, init) = config
        monitor.subscribe(DBConnected) { dataSource ->
            pipeline.attachSql2o(
                if (quirks != null) {
                    Sql2o(dataSource, quirks)
                } else {
                    Sql2o(dataSource)
                }
            )
            init()
        }
        monitor.subscribe(DBClosed) {
            pipeline.detachSql2o()
        }
    }

    class Config {
        private var init: () -> Unit = {}
        var quirks: Quirks? = null

        internal operator fun component1() = quirks
        internal operator fun component2() = init

        fun init(init: () -> Unit) {
            this.init = init
        }
    }

    companion object Feature : ApplicationFeature<Application, Config, Sql2oFeature> {
        override val key = AttributeKey<Sql2oFeature>("Sql2oFeature")

        override fun install(pipeline: Application, configure: Config.() -> Unit): Sql2oFeature {
            return Sql2oFeature(pipeline, pipeline.environment.monitor, Config().apply(configure))
        }
    }
}