package net.paslavsky.ktor.elastic

import io.ktor.application.*
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import org.apache.http.Header
import org.apache.http.HttpHost
import org.elasticsearch.client.NodeSelector
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import kotlin.properties.Delegates

class ElasticFeature private constructor(
    private val config: Config,
    monitor: ApplicationEvents,
    private val pipeline: Application
) {
    init {
        monitor.subscribe(ApplicationStarted, ::onStarted)
        monitor.subscribe(ApplicationStopPreparing, ::onStopPreparing)
    }

    private lateinit var restClient: RestClient

    class Config {
        var hosts: Array<HttpHost> by Delegates.notNull()
        var defaultHeaders: Array<Header>? = null
        var failureListener: RestClient.FailureListener? = null
        var httpClientConfigCallback: RestClientBuilder.HttpClientConfigCallback? = null
        var requestConfigCallback: RestClientBuilder.RequestConfigCallback? = null
        var pathPrefix: String? = null
        var nodeSelector: NodeSelector? = null
        var strictDeprecationMode: Boolean? = null
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onStarted(app: Application) {
        restClient = RestClient.builder(*config.hosts).apply {
            if (config.defaultHeaders != null) {
                setDefaultHeaders(config.defaultHeaders)
            }
            if (config.failureListener != null) {
                setFailureListener(config.failureListener)
            }
            if (config.httpClientConfigCallback != null) {
                setHttpClientConfigCallback(config.httpClientConfigCallback)
            }
            if (config.requestConfigCallback != null) {
                setRequestConfigCallback(config.requestConfigCallback)
            }
            if (config.pathPrefix != null) {
                setPathPrefix(config.pathPrefix)
            }
            if (config.nodeSelector != null) {
                setNodeSelector(config.nodeSelector)
            }
            if (config.strictDeprecationMode != null) {
                setStrictDeprecationMode(config.strictDeprecationMode!!)
            }
        }.build()
        pipeline.attachElasticSearchClient(restClient)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onStopPreparing(environment: ApplicationEnvironment) {
        restClient.close()
        pipeline.detachElasticSearchClient()
    }

    companion object Feature : ApplicationFeature<Application, Config, ElasticFeature> {
        override val key: AttributeKey<ElasticFeature> = AttributeKey("ElasticSearchFeature")

        override fun install(pipeline: Application, configure: Config.() -> Unit): ElasticFeature {
            val configuration = Config().apply(configure)
            return ElasticFeature(configuration, pipeline.environment.monitor, pipeline)
        }
    }
}