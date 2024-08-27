package puc.stock.service

import org.springframework.http.ResponseEntity
import puc.stock.controller.response.StockUpdateResponse
import puc.stock.controller.request.StockUpdateRequest

interface StockService {

    fun writeDownStock(stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockUpdateResponse>

    fun addProductStock(stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockUpdateResponse>
}