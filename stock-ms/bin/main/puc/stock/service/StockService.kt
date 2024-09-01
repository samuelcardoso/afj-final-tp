package puc.stock.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import puc.stock.excecoes.InsufficientStockException
import puc.stock.excecoes.StockAlreadyRegisteredException
import puc.stock.model.Stock
import puc.stock.repository.StockRepository
import puc.stock.excecoes.StockNotFoundException

@Service
class StockService(val stockRepository: StockRepository) {

    @Transactional
    fun writeDownStock(productId: String, quantity: Int): Stock {
        val stock = stockRepository.findByProductId(productId);
        if(stock.isPresent) throw StockAlreadyRegisteredException("Product já cadastrado")

        val stockEntity = Stock(null, productId,quantity);
        return stockRepository.save(stockEntity);
    }
    @Transactional
    fun writeDownStockUpdate(productId: String, quantity: Int): Stock {
        val stock = stockRepository.findByProductId(productId).get()
            ?: throw StockNotFoundException("Product not found")

        if (stock.quantity < quantity) {
            throw InsufficientStockException("Insufficient stock")
        }

        stock.quantity -= quantity

        return stockRepository.save(stock);
    }

    fun stockAll(): List<Stock>{
        return stockRepository.findAll();
    }

    fun stockById(produtoId:String): Stock {
        return stockRepository.findByProductId(produtoId).get()
                ?: throw StockAlreadyRegisteredException("Product not found")

    }

}
