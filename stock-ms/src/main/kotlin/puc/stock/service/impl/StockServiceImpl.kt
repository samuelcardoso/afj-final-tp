package puc.stock.service.impl

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
        val productStock = getCurrentProductStock(stockUpdateRequest.productId!!)

        if (isNotEligibleProductStock(stockUpdateRequest,productStock)) {
            val errorMessage = "Produto com id [${stockUpdateRequest.productId}.] não tem estoque suficiente"
            logger.error(errorMessage)
            throw NotEnoughStockException(errorMessage)
        }

        productStock.quantity -= stockUpdateRequest.quantity!!
        logger.info("=== Quantidade do produto atualizada [{}]", productStock.quantity)

        logger.info("=== Salvando atualização de estoque [{}]", productStock)
        val stockUpdated = stockRepository.save(productStock)

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

    @Transactional
    override fun findStockAll(pageable: Pageable) : Page<StockResponse> {
        val stockPage = stockRepository.findAll(pageable)
            ?: throw ProductNotFoundException(String.format("Erro ao buscar todos os produtos do estoque"))

        logger.info("=== Produtos encontrados no estoque [{}] ", stockPage.content)
        return stockPage.map { it.toResponse() }
    }

    private fun validateStockExistence(existingStock: Stock?, stockUpdateRequest: StockUpdateRequest) {
        if (nonNull(existingStock)) {
            logger.error("=== Erro, o produto [{}] já existe no estoque", stockUpdateRequest.productId)
            throw ProductAlreadyExistsException(String.format("Estoque do produto com id [%s] já está cadastrado", stockUpdateRequest.productId))
        }
    }

    private fun getCurrentProductStock(productId: String) : Stock  {
        return this.findStockByProductId(productId).toEntity()
    }
    private fun isNotEligibleProductStock(stockUpdateRequest: StockUpdateRequest, productStock: Stock): Boolean {
        logger.info("=== [isNotEligibleProductStock] Id do produto: [{}]  Quantidade disponivel [{}]. Quantidade Solicitada [{}]", productStock.productId, productStock.quantity, stockUpdateRequest.quantity)
        return productStock.quantity < stockUpdateRequest.quantity!!
    }
}
