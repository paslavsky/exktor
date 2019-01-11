package net.paslavsky.ktor.consul

import com.orbitz.consul.model.health.ServiceHealth

typealias LoadBalancer = List<ServiceHealth>.() -> ServiceHealth?

fun takeFirst(): LoadBalancer = { getOrNull(0) }

fun roundRobin(): LoadBalancer {
    var index = 0
    return {
        getOrNull(index).also {
            index = (index + 1) % size
        }
    }
}