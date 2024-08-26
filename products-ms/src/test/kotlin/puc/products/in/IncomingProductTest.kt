package puc.products.`in`

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class IncomingProductTest {
    @Test
    fun `should create incoming product successfully`(){
        assertDoesNotThrow {
            IncomingProduct("Testing product 01", 20.1)
        }
    }
}