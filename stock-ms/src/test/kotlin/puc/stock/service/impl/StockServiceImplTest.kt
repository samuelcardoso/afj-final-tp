package puc.stock.service.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import puc.stock.controller.request.StockUpdateRequest
import puc.stock.exception.NotEnoughStockException
import puc.stock.exception.ProductAlreadyExistsException
import puc.stock.exception.ProductNotFoundException
import puc.stock.model.Stock
import puc.stock.repository.StockRepository
import puc.stock.resources.ProductResource
import puc.stock.service.ProductService

@ExtendWith(SpringExtension::class)
class StockServiceImplTest {
    private val stockRepository: StockRepository = mockk()
    private val productService: ProductService = mockk()
    private val stockService = StockServiceImpl(stockRepository, productService)

    @Test
    fun `should write down stock of product with success`() {
        // Given
        val request = StockUpdateRequest("1", 1)
        val stock = Stock(1, "1", 1)

        every { stockRepository.findByProductId(request.productId!!) } returns stock
        every { stockRepository.save(stock) } returns stock

        // When
        val actualStock = stockService.writeDownStock(request)

        // Then
        assertNotNull(actualStock)
        assertEquals(0, actualStock.quantity)
        verify { stockRepository.findByProductId(request.productId!!) }
        verify { stockRepository.save(stock) }
    }

    @Test
    fun `should fail write down stock if product not found`() {
        // Given
        val request = StockUpdateRequest("1", 1)

        every { stockRepository.findByProductId(request.productId!!) } returns null

        // Then
        val exception = assertThrows(ProductNotFoundException::class.java) {
            stockService.writeDownStock(request)
        }

        assertEquals("Produto com id [1] não encontrado na base de estoques", exception.message)
        verify { stockRepository.findByProductId(request.productId!!) }
    }

    @Test
    fun `should fail write down stock if product not have enough quantity`() {
        // Given
        val request = StockUpdateRequest("1", 1)
        val stock = Stock(1, "1", 0)

        every { stockRepository.findByProductId(request.productId!!) } returns stock

        // Then
        val exception = assertThrows(NotEnoughStockException::class.java) {
            stockService.writeDownStock(request)
        }

        assertEquals("Produto com id [1] não tem estoque suficiente", exception.message)
        verify { stockRepository.findByProductId(request.productId!!) }
    }

    @Test
    fun `should add product stock when stock does not exist`() {
        // Given
        val productId = "2"
        val stockUpdateRequest = StockUpdateRequest(productId, 20)

        every { productService.findProductById(productId) } returns ProductResource("2", "test", 10.0)
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

        every { productService.findProductById(productId) } returns ProductResource("2", "test", 10.0)
        every { stockRepository.findByProductId(productId) } returns existingStock

        // When / Then
        val exception = assertThrows(ProductAlreadyExistsException::class.java) {
            stockService.addProductStock(stockUpdateRequest)
        }

        assertEquals("Estoque do produto com id [2] já está cadastrado", exception.message)
        verify { productService.findProductById(productId) }
        verify { stockRepository.findByProductId(productId) }
    }
}