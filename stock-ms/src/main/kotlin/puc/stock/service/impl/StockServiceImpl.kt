package puc.stock.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import puc.stock.controller.request.StockUpdateRequest
import puc.stock.controller.response.StockResponse
import puc.stock.exception.NotEnoughStockException
import puc.stock.exception.ProductAlreadyExistsException
import puc.stock.exception.ProductNotFoundException
import puc.stock.model.Stock
import puc.stock.repository.StockRepository
import puc.stock.service.ProductService
import puc.stock.service.StockService
import java.util.Objects.nonNull

@Service
class StockServiceImpl(val stockRepository: StockRepository, val productService : ProductService) : StockService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun writeDownStock(stockUpdateRequest: StockUpdateRequest) : StockResponse {
        val stock = this.findStockByProductId(stockUpdateRequest.productId!!).toEntity()

        if (stock.quantity < stockUpdateRequest.quantity!!) {
            logger.error("=== Erro, produto [{}] com estoque insuficiente", stockUpdateRequest.productId)
            throw NotEnoughStockException(String.format("Produto com id [%s] não tem estoque suficiente", stockUpdateRequest.productId))
        }

        stock.quantity -= stockUpdateRequest.quantity

        logger.info("=== Salvando atualização de estoque [{}]", stock.toString())
        val stockUpdated = stockRepository.save(stock)

        logger.info("=== Estoque atualizado com sucesso, quantidade atual [{}]", stockUpdated.quantity)
        return stockUpdated.toResponse()
    }

    @Transactional
    override fun addProductStock(stockUpdateRequest: StockUpdateRequest) : StockResponse {
        val product = productService.findProductById(stockUpdateRequest.productId!!)
        val existingStock = stockRepository.findByProductId(product.id)

        validateStockExistence(existingStock, stockUpdateRequest)

        val stock = Stock(
            productId = stockUpdateRequest.productId,
            quantity = stockUpdateRequest.quantity!!
        )

        logger.info("=== Salvando estoque [{}]", stock.toString())
        val stockSaved = stockRepository.save(stock)

        logger.info("=== Estoque [{}] salvo", stockSaved.toString())
        return stockSaved.toResponse()
    }

    @Transactional
    override fun findStockByProductId(productId: String) : StockResponse {
        val stock = stockRepository.findByProductId(productId)
            ?: throw ProductNotFoundException(String.format("Produto com id [%s] não encontrado na base de estoques", productId))

        logger.info("=== Produto [{}] encontrado no estoque", stock)
        return stock.toResponse()
    }

    private fun validateStockExistence(existingStock: Stock?, stockUpdateRequest: StockUpdateRequest) {
        if (nonNull(existingStock)) {
            logger.error("=== Erro, o produto [{}] já existe no estoque", stockUpdateRequest.productId)
            throw ProductAlreadyExistsException(String.format("Estoque do produto com id [%s] já está cadastrado", stockUpdateRequest.productId))
        }
    }
}
