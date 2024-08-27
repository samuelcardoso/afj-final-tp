package puc.stock.service.impl

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import puc.stock.controller.response.StockUpdateResponse
import puc.stock.controller.request.StockUpdateRequest
import puc.stock.exception.NotEnoughStockException
import puc.stock.exception.ProductFoundException
import puc.stock.exception.ProductNotFoundException
import puc.stock.repository.StockRepository
import puc.stock.service.StockService
import puc.stock.model.Stock

@Service
class StockServiceImpl(val stockRepository: StockRepository) : StockService {

    @Transactional
    override fun writeDownStock(stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockUpdateResponse> {
        val stock = stockRepository.findByProductId(stockUpdateRequest.productId!!)
            ?: throw ProductNotFoundException(String.format("Produto com id [%s] não encontrado", stockUpdateRequest.productId))

        if (stock.quantity < stockUpdateRequest.quantity!!) {
            throw NotEnoughStockException(String.format("Produto com id [%s] não tem estoque suficiente", stockUpdateRequest.productId))
        }

        stock.quantity -= stockUpdateRequest.quantity

        val stockUpdated = stockRepository.save(stock)

        return ResponseEntity.ok(StockUpdateResponse(stockUpdated.id!!, stockUpdated.productId, stockUpdated.quantity))
    }

    @Transactional
    override fun addProductStock(stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockUpdateResponse> {
        val existingStock = stockRepository.findByProductId(stockUpdateRequest.productId!!)

        if (existingStock != null) {
            throw ProductFoundException(String.format("Produto com id [%s] já existente", stockUpdateRequest.productId))
        }

        val stock = Stock(
            productId = stockUpdateRequest.productId,
            quantity = stockUpdateRequest.quantity!!
        )

        val stockSave = stockRepository.save(stock)

        return ResponseEntity.ok(StockUpdateResponse(stockSave.id!!, stockSave.productId, stockSave.quantity))
    }
}
