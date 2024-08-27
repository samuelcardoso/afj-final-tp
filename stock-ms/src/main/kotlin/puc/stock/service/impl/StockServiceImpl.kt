package puc.stock.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import puc.stock.controller.response.StockUpdateResponse
import puc.stock.controller.request.StockUpdateRequest
import puc.stock.exception.NotEnoughStockException
import puc.stock.exception.ProductAlreadyExistsException
import puc.stock.exception.ProductNotFoundException
import puc.stock.repository.StockRepository
import puc.stock.service.StockService
import puc.stock.model.Stock
import puc.stock.service.ProductService
import java.util.Objects.nonNull

@Service
class StockServiceImpl(val stockRepository: StockRepository, val productService : ProductService) : StockService {

    @Transactional
    override fun writeDownStock(stockUpdateRequest: StockUpdateRequest) : StockUpdateResponse {
        productService.findProductById(stockUpdateRequest.productId!!)
        val stock = stockRepository.findByProductId(stockUpdateRequest.productId)
            ?: throw ProductNotFoundException(String.format("Produto com id [%s] não encontrado na base de estoques", stockUpdateRequest.productId))

        if (stock.quantity < stockUpdateRequest.quantity!!) {
            throw NotEnoughStockException(String.format("Produto com id [%s] não tem estoque suficiente", stockUpdateRequest.productId))
        }

        stock.quantity -= stockUpdateRequest.quantity

        val stockUpdated = stockRepository.save(stock)

        return StockUpdateResponse(stockUpdated.id!!, stockUpdated.productId, stockUpdated.quantity)
    }

    @Transactional
    override fun addProductStock(stockUpdateRequest: StockUpdateRequest) : StockUpdateResponse {
        productService.findProductById(stockUpdateRequest.productId!!)
        val existingStock = stockRepository.findByProductId(stockUpdateRequest.productId)

        validateStockExistence(existingStock, stockUpdateRequest)

        val stock = Stock(
            productId = stockUpdateRequest.productId,
            quantity = stockUpdateRequest.quantity!!
        )

        val stockSave = stockRepository.save(stock)

        return StockUpdateResponse(stockSave.id!!, stockSave.productId, stockSave.quantity)
    }

    private fun validateStockExistence(existingStock: Stock?, stockUpdateRequest: StockUpdateRequest) {
        if (nonNull(existingStock)) {
            throw ProductAlreadyExistsException(String.format("Produto com id [%s] já existente", stockUpdateRequest.productId))
        }
    }
}
