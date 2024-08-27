package puc.products.domain


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ProductTest{
    @Test
    fun `should create incoming product successfully`(){
        assertDoesNotThrow {
            val product = Product(name = "Testing product 01", price = 20.1)

            assertEquals(20.1, product.price)
            assertNotNull("Testing product 01", product.name)
        }
    }

    @Test
    fun `should fail on create incoming product as name is not given`(){
        assertThrows<IllegalArgumentException> {
            Product(name = "", price = 20.1)
        }
    }

    @Test
    fun `should fail on create incoming product as price is negative`(){
        assertThrows<IllegalArgumentException> {
            Product(name = "Testing product 01", price = -1.0)
        }
    }

    @Test
    fun `should fail on create incoming product as price is not a number`(){
        assertThrows<IllegalArgumentException> {
            Product(name = "Testing product 01", price = Double.NaN)
        }
    }
}