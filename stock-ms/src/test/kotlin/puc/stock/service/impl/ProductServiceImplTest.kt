package puc.stock.service.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import puc.stock.client.ProductClient
import puc.stock.exception.ProductNotFoundException
import puc.stock.resources.ProductResource
import java.util.*

@ExtendWith(SpringExtension::class)
class ProductServiceImplTest {

    private val client: ProductClient = mockk()
    private val productService = ProductServiceImpl(client)

    @Test
    fun `should return ProductResource when client returns a product`() {
        // Given
        val productId = "1"
        val expectedProduct = ProductResource(id = productId, name = "Test Product", price = 10.0)

        every { client.getProductById(productId) } returns Optional.of(expectedProduct)

        // When
        val actualProduct = productService.findProductById(productId)

        // Then
        assertEquals(expectedProduct, actualProduct)
        verify { client.getProductById(productId) }
    }

    @Test
    fun `should throw ProductNotFoundException when client returns empty`() {
        // Given
        val productId = "2"

        every { client.getProductById(productId) } returns Optional.empty()

        // When / Then
        val exception = assertThrows(ProductNotFoundException::class.java) {
            productService.findProductById(productId)
        }

        assertEquals("Produto com id [$productId] não encontrado!", exception.message)
        verify { client.getProductById(productId) }
    }
}
