package io.github.marioalvial.kealth

import io.github.marioalvial.kealth.core.HealthComponent
import io.github.marioalvial.kealth.core.HealthStatus
import io.github.marioalvial.kealth.core.HealthStatus.HEALTHY
import org.slf4j.LoggerFactory
import java.sql.SQLTimeoutException
import javax.sql.DataSource

class JdbcHealthComponent(
    override val name: String,
    private val datasource: DataSource,
    private val timeout: Int = 3
) : HealthComponent {

    private val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    override fun doHealthCheck(): HealthStatus {
        logger.info("Starting Database health check of component $name")

        return when (datasource.connection.isValid(timeout)) {
            true -> {
                logger.info("Evaluate connection with database successfully")

                HEALTHY
            }
            false -> throw SQLTimeoutException("Could not connect to database")
        }
    }

    override fun handleFailure(throwable: Throwable) {
        logger.error("Fail to validate database connection.", throwable)
    }
}