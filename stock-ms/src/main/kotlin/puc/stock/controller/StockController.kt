package puc.stock.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import puc.stock.controller.request.StockUpdateRequest
import puc.stock.controller.response.StockResponse
import puc.stock.service.StockService

@Validated
@RestController
@RequestMapping("/stock")
class StockController(val stockService: StockService) {

    private val logger = LoggerFactory.getLogger(javaClass)


    @PatchMapping("/write-down")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Da baixa no estoque de uma determinada quantidade de produtos")
    fun writeDown(@Valid @RequestBody stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockResponse> {
        logger.info("=== Atualizando estoque do produto [{}] com [{}] item(s)", stockUpdateRequest.productId, (stockUpdateRequest.quantity!! * -1))
        return ResponseEntity.ok(stockService.writeDownStock(stockUpdateRequest));
    }

    @PostMapping("/add-product")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Adiciona um novo produto ao estoque")
    fun addProductStock(@Valid @RequestBody stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockResponse> {
        logger.info("=== Adicionando produto [{}] ao estoque", stockUpdateRequest.productId)
        return ResponseEntity.ok(stockService.addProductStock(stockUpdateRequest))
    }

    @PatchMapping("/add-stock")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Adiciona estoque a um produto")
    fun addStock(@Valid @RequestBody stockUpdateRequest: StockUpdateRequest) : ResponseEntity<StockResponse> {
        logger.info("=== Adicionando stoque ao produto [{}]", stockUpdateRequest.productId)
        return ResponseEntity.ok(stockService.addStock(stockUpdateRequest))
    }

    @GetMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Verifica se um produto existe no estoque")
    fun getStockById(@PathVariable(name = "productId") productId : String) : ResponseEntity<StockResponse> {
        logger.info("=== Buscando estoque do produto [{}]", productId)
        return ResponseEntity.ok(stockService.findStockByProductId(productId))
    }

    @GetMapping("/product")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Busca todos produtos do estoque paginados")  // Update summary
    fun getStockAll(pageable: Pageable): ResponseEntity<Page<StockResponse>> {
        logger.info("=== Buscando estoque de todos os produtos paginados")

        val stockPage = stockService.findStockAll(pageable)

        return ResponseEntity.ok(stockPage)
    }
}
