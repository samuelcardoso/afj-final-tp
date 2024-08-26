package puc.products.`in`

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class IncomingProductTest {
    @Test
    fun `should create incoming product successfully`(){
        assertDoesNotThrow {
            IncomingProduct("Testing product 01", 20.1)
        }
    }

    @Test
    fun `should fail on create incoming product as name is not given`(){
        assertThrows<IllegalArgumentException> {
            IncomingProduct("", 20.1)
        }
    }

    @Test
    fun `should fail on create incoming product as price is negative`(){
        assertThrows<IllegalArgumentException> {
            IncomingProduct("Testing product 01", -1.0)
        }
    }

    @Test
    fun `should fail on create incoming product as price is not a number`(){
        assertThrows<IllegalArgumentException> {
            IncomingProduct("Testing product 01", Double.NaN)
        }
    }
}