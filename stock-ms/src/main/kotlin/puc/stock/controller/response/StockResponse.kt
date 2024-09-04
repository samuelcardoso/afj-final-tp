package puc.stock.controller.response

import puc.stock.model.Stock

data class StockResponse(
    val id: Long,
    val productId: String,
    var quantity: Int
) {

    fun toEntity() : Stock {
        return Stock(this.id, this.productId, this.quantity)
    }
}
