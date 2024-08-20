package puc.stock

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockService(val stockRepository: StockRepository) {

    @Transactional
    fun writeDownStock(productId: String, quantity: Int) {
        val stock = stockRepository.findByProductId(productId)
            ?: throw IllegalArgumentException("Product not found")

        if (stock.quantity < quantity) {
            throw IllegalArgumentException("Insufficient stock")
        }

        stock.quantity -= quantity
        stockRepository.save(stock)
    }
}
