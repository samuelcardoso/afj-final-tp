package puc.infrastructure.resilience

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.circuitbreaker.executeFunction
import io.github.resilience4j.kotlin.retry.executeFunction
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryRegistry
import io.mockk.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KFunction
import org.junit.jupiter.api.Assertions.assertEquals


class ResilienceStrategyTest {

    @Test
    fun `should succeed when executing nullable return`() {
        val retryRegistry = spyk(RetryRegistry.ofDefaults())
        val retry = mockk<Retry>()
        every { retryRegistry.retry("my-test-retry") } returns retry

        val publisher = mockk<Retry.EventPublisher>()
        every { publisher.onRetry(any()) } returns publisher
        every { retry.eventPublisher } returns publisher

        val circuitBreakerRegistry = spyk(CircuitBreakerRegistry.ofDefaults())
        val circuitBreaker = mockk<CircuitBreaker>()

        every { circuitBreakerRegistry.circuitBreaker("my-test-retry") } returns circuitBreaker

        val circuitBreakerExecuteFunction: KFunction<() -> String> = CircuitBreaker::executeFunction
        val retryExecuteFunction: KFunction<() -> String> = Retry::executeFunction

        mockkStatic(circuitBreakerExecuteFunction, retryExecuteFunction) {
            every {
                retry.executeFunction(captureLambda<() -> String>())
            } answers {
                every {
                    val lambda = captureLambda<() -> String>()
                    circuitBreaker.executeFunction(lambda)
                } answers {
                    lambda<() -> String>().captured.invoke()
                }
                lambda<() -> String>().captured.invoke()

            }

            val resilienceStrategy = ResilienceStrategy<String>(retryRegistry, circuitBreakerRegistry)

            val text = "Meu texto!"
            val actual = resilienceStrategy.executeWithRetryAndCircuitBreaker("my-test-retry") {
                text
            }

            assertEquals(text, actual)
            verify(exactly = 1) {
                retryRegistry.retry("my-test-retry")
            }
            verify(exactly = 1) {
                circuitBreakerRegistry.circuitBreaker("my-test-retry")
            }
        }
    }
}