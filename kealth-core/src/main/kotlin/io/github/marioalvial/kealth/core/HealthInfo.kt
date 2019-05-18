package io.github.marioalvial.kealth.core

/**
 * Class that aggregates information about the execution of component's health check
 * @property status Status of health component
 * @property duration Duration of component's health check execution
 */
data class HealthInfo(
    val status: HealthStatus,
    val duration: Long
)