package puc.products.application.exception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import puc.application.exception.ProductsExceptionHandler

class ProductsExceptionHandlerTest{

    @Test
    fun `should handle illegalArgumentException successfully`(){
        val illegal = IllegalArgumentException("The precondition failed. Fill the fields correctly.")

        val result = ProductsExceptionHandler().handleIllegalArgumentException(illegal)

        assertEquals("Fill the fields correctly", result.title)
        assertEquals(illegal.message, result.detail)
    }
}