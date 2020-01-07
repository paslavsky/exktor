package net.paslavsky.ktor.flyway

import io.ktor.application.Application
import io.ktor.application.ApplicationEvents
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import net.paslavsky.ktor.sql.DBConnected
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.flywaydb.core.internal.configuration.ConfigUtils

class FlywayFeature private constructor(
    monitor: ApplicationEvents,
    config: ClassicConfiguration
) {
    init {
        monitor.subscribe(DBConnected) { dataSource ->
            Flyway.
                configure(config.classLoader).
                configuration(config).
                dataSource(dataSource).
                load().
                migrate()
        }
    }

    companion object Feature : ApplicationFeature<Application, ClassicConfiguration, FlywayFeature> {
        override val key = AttributeKey<FlywayFeature>("FlywayFeature")

        @KtorExperimentalAPI
        override fun install(pipeline: Application, configure: ClassicConfiguration.() -> Unit): FlywayFeature {
            fun value(name: String) =
                pipeline.environment.config.propertyOrNull(name)

            fun propToValue(name: String) =
                name to value(name)?.getString()

            fun placeholders() =
                value(ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX.dropLast(1))?.getList()?.mapNotNull {
                    it.split(':').let { list ->
                        if (list.size == 2)
                            "${ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX}${list[0]}" to list[1]
                        else
                            null
                    }
                }?.toTypedArray() ?: emptyArray()

            val configuration = ClassicConfiguration()
            configuration.configure(
                mapOf(
                    propToValue(ConfigUtils.CONNECT_RETRIES),
                    propToValue(ConfigUtils.INIT_SQL),
                    propToValue(ConfigUtils.LOCATIONS),
                    propToValue(ConfigUtils.PLACEHOLDER_REPLACEMENT),
                    propToValue(ConfigUtils.PLACEHOLDER_PREFIX),
                    propToValue(ConfigUtils.PLACEHOLDER_SUFFIX),
                    propToValue(ConfigUtils.SQL_MIGRATION_PREFIX),
                    propToValue(ConfigUtils.UNDO_SQL_MIGRATION_PREFIX),
                    propToValue(ConfigUtils.REPEATABLE_SQL_MIGRATION_PREFIX),
                    propToValue(ConfigUtils.SQL_MIGRATION_SEPARATOR),
                    propToValue(ConfigUtils.SQL_MIGRATION_SUFFIXES),
                    propToValue(ConfigUtils.ENCODING),
                    propToValue(ConfigUtils.SCHEMAS),
                    propToValue(ConfigUtils.TABLE),
                    propToValue(ConfigUtils.CLEAN_ON_VALIDATION_ERROR),
                    propToValue(ConfigUtils.CLEAN_ON_VALIDATION_ERROR),
                    propToValue(ConfigUtils.VALIDATE_ON_MIGRATE),
                    propToValue(ConfigUtils.BASELINE_VERSION),
                    propToValue(ConfigUtils.BASELINE_DESCRIPTION),
                    propToValue(ConfigUtils.BASELINE_ON_MIGRATE),
                    propToValue(ConfigUtils.IGNORE_MISSING_MIGRATIONS),
                    propToValue(ConfigUtils.IGNORE_IGNORED_MIGRATIONS),
                    propToValue(ConfigUtils.IGNORE_PENDING_MIGRATIONS),
                    propToValue(ConfigUtils.IGNORE_FUTURE_MIGRATIONS),
                    propToValue(ConfigUtils.TARGET),
                    propToValue(ConfigUtils.OUT_OF_ORDER),
                    propToValue(ConfigUtils.RESOLVERS),
                    propToValue(ConfigUtils.SKIP_DEFAULT_RESOLVERS),
                    propToValue(ConfigUtils.CALLBACKS),
                    propToValue(ConfigUtils.SKIP_DEFAULT_CALLBACKS),
                    propToValue(ConfigUtils.MIXED),
                    propToValue(ConfigUtils.GROUP),
                    propToValue(ConfigUtils.INSTALLED_BY),
                    propToValue(ConfigUtils.DRYRUN_OUTPUT),
                    propToValue(ConfigUtils.ERROR_OVERRIDES),
                    propToValue(ConfigUtils.STREAM),
                    propToValue(ConfigUtils.BATCH),
                    propToValue(ConfigUtils.ORACLE_SQLPLUS),
                    propToValue(ConfigUtils.LICENSE_KEY),
                    *placeholders()
                )
            )
            configuration.apply(configure)
            return FlywayFeature(pipeline.environment.monitor, configuration)
        }
    }
}