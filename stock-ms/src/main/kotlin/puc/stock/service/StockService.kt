package puc.stock.service

interface StockService {

    fun writeDownStock(productId: String, quantity: Int);
}