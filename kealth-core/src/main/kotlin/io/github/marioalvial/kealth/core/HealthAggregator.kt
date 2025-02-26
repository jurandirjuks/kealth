package io.github.marioalvial.kealth.core

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * An aggregator of health jdbc.
 *
 * This class holds a list of health jdbc.
 *
 * @property components List<HealthComponent> - list of health jdbc.
 * @constructor Creates a health aggregator for the component list passed as constructor parameter.
 */
class HealthAggregator(
    private val components: List<HealthComponent>
) {

    /**
     * Execute the health method of each health component and builds a map with component name and health information.
     * @return Map<String, HealthStatus>
     */
    fun aggregate(): Map<String, HealthInfo> = executeAggregation(components)

    /**
     * Execute the health method of each component that matched the given predicate and builds a map with component's
     * name and health information.
     * @param filterBlock - Function that receive component's name and criticalLevel as parameter
     * @return Map<String, HealthStatus>
     */
    fun aggregateWithFilter(filterBlock: (name: String, criticalLevel: String) -> Boolean) =
        components
            .filter { filterBlock(it.name, it.criticalLevel) }
            .let { executeAggregation(it) }

    private fun executeAggregation(componentList: List<HealthComponent>) = runBlocking {
        componentList
            .associate { it.name to async { it.health() } }
            .mapValues { it.value.await() }
    }
}
