package puc.stock.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import puc.stock.dto.StockRequest
import puc.stock.model.Stock
import puc.stock.service.StockService

@RestController
@RequestMapping("/write-down")
class StockController(val stockService: StockService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun writeDown(@RequestBody request: StockRequest):ResponseEntity<Stock> {
        return ResponseEntity.ok().body(stockService.writeDownStock(request.productId, request.quantity));
    }

    @GetMapping("/{produtId}")
    fun busca(@PathVariable("produtId") produtId: String): Stock {
        return stockService.stockById(produtId);

    }

    @GetMapping()
    fun buscaAll(): List<Stock>{
        return stockService.stockAll();
    }

    @PutMapping("/{productId}/{quantidade}")
    @ResponseStatus(HttpStatus.OK)
    fun atualizaQuantidade(@PathVariable("productId") productId: String, @PathVariable("quantidade") quantidade: Int): ResponseEntity<Stock>{
        return ResponseEntity.ok().body(stockService.writeDownStockUpdate(productId,quantidade));
    }
}


