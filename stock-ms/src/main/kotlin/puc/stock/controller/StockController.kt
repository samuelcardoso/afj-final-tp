package puc.stock.controller

import org.springframework.web.bind.annotation.*
import puc.stock.service.StockService

@RestController
@RequestMapping("/write-down")
class StockController(val stockService: StockService) {

    @PostMapping
    fun writeDown(@RequestBody request: StockRequest) {
        stockService.writeDownStock(request.productId, request.quantity)
    }
}

data class StockRequest(val productId: String, val quantity: Int)
