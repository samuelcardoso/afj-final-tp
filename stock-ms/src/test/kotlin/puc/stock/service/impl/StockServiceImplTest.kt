package puc.stock.service.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import puc.stock.controller.request.StockUpdateRequest
import puc.stock.exception.NotEnoughStockException
import puc.stock.exception.ProductAlreadyExistsException
import puc.stock.exception.ProductNotFoundException
import puc.stock.model.Stock
import puc.stock.repository.StockRepository
import puc.stock.service.ProductService

@ExtendWith(SpringExtension::class)
class StockServiceImplTest {

    private val stockRepository: StockRepository = mockk()
    private val productService: ProductService = mockk()
    private val stockService = StockServiceImpl(stockRepository, productService)

    @Test
    fun `should update stock when sufficient quantity exists`() {
        // Given
        val productId = "1"
        val stockUpdateRequest = StockUpdateRequest(productId, 5)
        val existingStock = Stock(id = 1L, productId = productId, quantity = 10)

        every { productService.findProductById(productId) } returns mockk()
        every { stockRepository.findByProductId(productId) } returns existingStock
        every { stockRepository.save(any()) } returns existingStock.apply { quantity -= 5 }

        // When
        val response = stockService.writeDownStock(stockUpdateRequest)

        // Then
        assertEquals(0, response.quantity)
        verify { productService.findProductById(productId) }
        verify { stockRepository.findByProductId(productId) }
        verify { stockRepository.save(existingStock) }
    }

    @Test
    fun `should throw NotEnoughStockException when stock is insufficient`() {
        // Given
        val productId = "1"
        val stockUpdateRequest = StockUpdateRequest(productId, 15)
        val existingStock = Stock(id = 1L, productId = productId, quantity = 10)

        every { productService.findProductById(productId) } returns mockk()
        every { stockRepository.findByProductId(productId) } returns existingStock

        // When / Then
        val exception = assertThrows(NotEnoughStockException::class.java) {
            stockService.writeDownStock(stockUpdateRequest)
        }

        assertEquals("Produto com id [1] não tem estoque suficiente", exception.message)
        verify { productService.findProductById(productId) }
        verify { stockRepository.findByProductId(productId) }
    }

    @Test
    fun `should add product stock when stock does not exist`() {
        // Given
        val productId = "2"
        val stockUpdateRequest = StockUpdateRequest(productId, 20)

        every { productService.findProductById(productId) } returns mockk()
        every { stockRepository.findByProductId(productId) } returns null
        every { stockRepository.save(any()) } returns Stock(id = 2L, productId = productId, quantity = 20)

        // When
        val response = stockService.addProductStock(stockUpdateRequest)

        // Then
        assertEquals(20, response.quantity)
        verify { productService.findProductById(productId) }
        verify { stockRepository.findByProductId(productId) }
        verify { stockRepository.save(any()) }
    }

    @Test
    fun `should throw ProductAlreadyExistsException when stock already exists`() {
        // Given
        val productId = "2"
        val stockUpdateRequest = StockUpdateRequest(productId, 20)
        val existingStock = Stock(id = 2L, productId = productId, quantity = 20)

        every { productService.findProductById(productId) } returns mockk()
        every { stockRepository.findByProductId(productId) } returns existingStock

        // When / Then
        val exception = assertThrows(ProductAlreadyExistsException::class.java) {
            stockService.addProductStock(stockUpdateRequest)
        }

        assertEquals("Produto com id [2] já existente", exception.message)
        verify { productService.findProductById(productId) }
        verify { stockRepository.findByProductId(productId) }
    }

    @Test
    fun `should throw ProductNotFoundException when product is not found in stock repository`() {
        // Given
        val productId = "3"
        val stockUpdateRequest = StockUpdateRequest(productId, 10)

        every { productService.findProductById(productId) } returns mockk()
        every { stockRepository.findByProductId(productId) } returns null

        // When / Then
        val exception = assertThrows(ProductNotFoundException::class.java) {
            stockService.writeDownStock(stockUpdateRequest)
        }

        assertEquals("Produto com id [3] não encontrado na base de estoques", exception.message)
        verify { productService.findProductById(productId) }
        verify { stockRepository.findByProductId(productId) }
    }
}