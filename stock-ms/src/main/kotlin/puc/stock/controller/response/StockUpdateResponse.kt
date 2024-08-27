package puc.stock.controller.response

data class StockUpdateResponse(
    val id: Long,
    val productId: String,
    var quantity: Int
)
