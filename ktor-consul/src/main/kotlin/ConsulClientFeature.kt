package net.paslavsky.ktor.consul

import com.orbitz.consul.Consul
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.AttributeKey
import org.slf4j.LoggerFactory
import kotlin.properties.Delegates

@Suppress("MemberVisibilityCanBePrivate")
class ConsulClientFeature(private val config: Config) {
    class Config {
        private var loadBalancer: LoadBalancer = takeFirst()
        private var config: Consul.Builder.() -> Unit = {}

        var consulUrl by Delegates.notNull<String>()
        var serviceName: String? = null

        fun loadBalancer(loadBalancer: LoadBalancer) {
            this.loadBalancer = loadBalancer
        }

        fun config(config: Consul.Builder.() -> Unit) {
            this.config = config
        }

        internal operator fun component1() = loadBalancer
        internal operator fun component2() = config
        internal operator fun component3() = consulUrl
        internal operator fun component4() = serviceName
    }

    companion object Feature : HttpClientFeature<Config, ConsulClientFeature> {
        private val logger = LoggerFactory.getLogger(ConsulClientFeature::class.java)

        override val key = AttributeKey<ConsulClientFeature>("ConsulClient")

        override fun prepare(block: Config.() -> Unit) = ConsulClientFeature(Config().apply(block))

        override fun install(feature: ConsulClientFeature, scope: HttpClient) {
            val (loadBalancer, consulConfig, consulUrl, possibleServiceName) = feature.config

            scope.requestPipeline.intercept(HttpRequestPipeline.Render) {
                val serviceName = possibleServiceName ?: context.url.host

                val consulClient = Consul.builder().withUrl(consulUrl).apply(consulConfig).build()
                val nodes = consulClient.healthClient().getHealthyServiceInstances(serviceName).response

                val selectedNode = checkNotNull(nodes.loadBalancer()) {
                    "Impossible to find available nodes of the $serviceName"
                }

                context.url.host = selectedNode.service.address
                context.url.port = selectedNode.service.port

                logger.trace("Calling ${selectedNode.service.id}: ${context.url.buildString()}")
            }
        }
    }
}