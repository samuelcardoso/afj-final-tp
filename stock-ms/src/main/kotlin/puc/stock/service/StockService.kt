package puc.stock.service

import puc.stock.controller.response.StockResponse
import puc.stock.controller.request.StockUpdateRequest

interface StockService {

    fun writeDownStock(stockUpdateRequest: StockUpdateRequest) : StockResponse

    fun addProductStock(stockUpdateRequest: StockUpdateRequest) : StockResponse

    fun findStockByProductId(productId: String) : StockResponse

    fun findStockAll() : List<StockResponse>
}