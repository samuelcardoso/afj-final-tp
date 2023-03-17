package br.puc.tp_final.stock

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stock-ms/rest/stock")
class StockController(
    val stockService: StockService
) {
    @PostMapping("write_off")
    fun writeOff() {
        stockService.writeOff()
    }

    @GetMapping("/status/{id}")
    fun status(): String {
        return stockService.status();
    }
}