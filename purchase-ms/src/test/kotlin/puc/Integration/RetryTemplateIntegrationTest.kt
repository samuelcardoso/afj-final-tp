import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.retry.support.RetryTemplate
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

@SpringBootTest
class RetryTemplateIntegrationTest {

    @Autowired
    lateinit var retryTemplate: RetryTemplate

    @Test
    fun `should retry operation and succeed`() {
        var attempt = 0
        val result = retryTemplate.execute {
            attempt++
            if (attempt < 3) {
                throw RuntimeException("Failing attempt")
            }
            "Success"
        }
        assertEquals("Success", result)
    }
}
