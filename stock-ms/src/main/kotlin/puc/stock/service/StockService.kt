package puc.stock.service

import puc.stock.controller.response.StockUpdateResponse
import puc.stock.controller.request.StockUpdateRequest

interface StockService {

    fun writeDownStock(stockUpdateRequest: StockUpdateRequest) : StockUpdateResponse

    fun addProductStock(stockUpdateRequest: StockUpdateRequest) : StockUpdateResponse
}