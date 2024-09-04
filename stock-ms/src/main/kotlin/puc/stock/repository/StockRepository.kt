package puc.stock.repository

import org.springframework.data.jpa.repository.JpaRepository
import puc.stock.model.Stock

interface StockRepository : JpaRepository<Stock, Long> {
    fun findByProductId(productId: String): Stock?
}
