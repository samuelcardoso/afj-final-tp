package puc.infrastructure.resilience

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.retry.RetryRegistry
import io.github.resilience4j.kotlin.circuitbreaker.executeFunction
import io.github.resilience4j.kotlin.retry.executeFunction
import io.github.resilience4j.retry.Retry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ResilienceStrategy<T>(
    private val retryRegistry: RetryRegistry,
    private val circuitBreakerRegistry: CircuitBreakerRegistry
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)!!

    fun executeWithRetryAndCircuitBreaker(
        resilienceServiceName: String,
        functionBlock: () -> T?
    ): T? {
        val retry = retryRegistry.retry(resilienceServiceName)
        retry.eventEnable()
        val circuitBreakerRegistry = circuitBreakerRegistry.circuitBreaker(resilienceServiceName)

        return retry.executeFunction {
            circuitBreakerRegistry.executeFunction {
                functionBlock()
            }
        }
    }

    private fun Retry.eventEnable() {
        this.eventPublisher.onRetry { retryEvent ->
            logger.info("Triggered retry > name: ${retryEvent.name}")
            logger.info("Number of retries: ${retryEvent.numberOfRetryAttempts}")
            logger.info("waiting: ${retryEvent.waitInterval}")
            logger.info("Exception ${retryEvent.lastThrowable.cause}")
        }
    }
}