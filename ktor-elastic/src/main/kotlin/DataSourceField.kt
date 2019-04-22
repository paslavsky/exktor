package net.paslavsky.ktor.sql

import io.ktor.application.Application
import io.ktor.util.AttributeKey
import org.elasticsearch.client.RestClient

private val elasticSearchClientKey = AttributeKey<RestClient>("ElasticSearchClient")

val Application.elasticSearchClient: RestClient
    get() = checkNotNull(attributes.getOrNull(elasticSearchClientKey)) {
        "DataSource not configured or still not initialized"
    }

internal fun Application.attachElasticSearchClient(value: RestClient) {
    attributes.put(elasticSearchClientKey, value)
}

internal fun Application.detachElasticSearchClient() = attributes.remove(elasticSearchClientKey)