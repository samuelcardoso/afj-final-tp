package puc.infrastructure.resilience

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.retry.RetryRegistry
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseResilienceStrategyTest {

    @Autowired
    lateinit var circuitBreakerRegistry: CircuitBreakerRegistry


    @Autowired
    lateinit var retryRegistry: RetryRegistry

    @Autowired
    lateinit var integrationTestResilience: ResilienceStrategy<String>

    val retry by lazy {
        retryRegistry.retry("integration-tests")
    }

    val circuitBreaker by lazy {
        circuitBreakerRegistry.circuitBreaker("integration-tests")
    }

    @BeforeEach
    fun setUp() {
        circuitBreaker.reset()
    }

    @AfterAll
    fun tearDown() {
        circuitBreaker.reset()
    }


    @Test
    fun `should execute function successfully`() {
        val expected = "olá do circuit breaker"

        assertEquals(
            expected,
            integrationTestResilience.executeWithRetryAndCircuitBreaker("integration-tests") { expected }
        )

        with(circuitBreaker.metrics) {
            assertEquals(1, numberOfSuccessfulCalls)
            assertEquals(0, numberOfFailedCalls)
        }
    }

    @Test
    fun `should retry once and then return successfully`() {
        val expected = "olá do circuit breaker"

        var mustFail = true
        val actual = integrationTestResilience.executeWithRetryAndCircuitBreaker("integration-tests") {
            if (mustFail) {
                mustFail = false
                throw RuntimeException()
            }

            expected
        }

        assertEquals(expected, actual)


        with(circuitBreaker.metrics) {
            assertEquals(1, numberOfSuccessfulCalls)
            assertEquals(1, numberOfFailedCalls)
        }

        with(retry.metrics) {
            assertEquals(1, numberOfSuccessfulCallsWithRetryAttempt)
        }
    }

    @Test
    fun `should fail when the max attempts are exceeded`() {

      assertThrows<RuntimeException>  {
            integrationTestResilience.executeWithRetryAndCircuitBreaker("integration-tests") {
                throw RuntimeException()
            }
        }

        with(circuitBreaker.metrics) {
            assertEquals(0, numberOfSuccessfulCalls)
            assertEquals(3, numberOfFailedCalls)
        }
    }


}