package puc.stock.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import puc.stock.controller.request.StockUpdateRequest
import puc.stock.controller.response.StockUpdateResponse
import puc.stock.service.StockService

@Validated
@RestController
@RequestMapping("/stock")
class StockController(val stockService: StockService) {

    private val logger = LoggerFactory.getLogger(javaClass)


    @PatchMapping("/write-down")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Da baixa no estoque de uma determinada quantidade de produtos")
    fun writeDown(@Valid @RequestBody stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockUpdateResponse> {
        logger.info("=== Atualizando estoque do produto [{}] com [{}] item(s)", stockUpdateRequest.productId, (stockUpdateRequest.quantity!! * -1))
        return ResponseEntity.ok(stockService.writeDownStock(stockUpdateRequest));
    }

    @PostMapping("/add-product")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Adiciona um novo produto ao estoque")
    fun addStock(@Valid @RequestBody stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockUpdateResponse> {
        logger.info("=== Adicionando produto [{}] ao estoque", stockUpdateRequest.productId)
        return ResponseEntity.ok(stockService.addProductStock(stockUpdateRequest))
    }
}
