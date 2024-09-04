package puc.stock.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import puc.stock.controller.response.StockResponse
import puc.stock.controller.request.StockUpdateRequest

interface StockService {

    fun writeDownStock(stockUpdateRequest: StockUpdateRequest) : StockResponse

    fun addProductStock(stockUpdateRequest: StockUpdateRequest) : StockResponse

    fun addStock(stockUpdateRequest: StockUpdateRequest) : StockResponse

    fun findStockByProductId(productId: String) : StockResponse

    fun findStockAll(pageable: Pageable) : Page<StockResponse>
}