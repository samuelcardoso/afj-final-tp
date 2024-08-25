package puc.stock.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import puc.stock.repository.StockRepository
import puc.stock.service.StockService

@Service
class StockServiceImpl(val stockRepository: StockRepository) : StockService {

    @Transactional
    override fun writeDownStock(productId: String, quantity: Int) {
        val stock = stockRepository.findByProductId(productId)
            ?: throw IllegalArgumentException("Product not found")

        if (stock.quantity < quantity) {
            throw IllegalArgumentException("Insufficient stock")
        }

        stock.quantity -= quantity
        stockRepository.save(stock)
    }
}
